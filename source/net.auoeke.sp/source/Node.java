package net.auoeke.sp.source;

import java.util.Objects;
import java.util.stream.Stream;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.lexeme.Position;
import net.auoeke.sp.source.tree.Tree;

public abstract sealed class Node implements CharSequence permits Lexeme, Tree {
	public Node previous;
	public Node next;
	protected Tree parent;

	public abstract Position start();

	public abstract void accept(NodeVisitor visitor);

	public abstract <T> T accept(NodeTransformer<T> transformer);

	@Override public abstract Node clone();

	@Override public abstract String toString();

	public abstract Type type();

	public abstract Stream<Node> stream();

	public abstract Stream<Node> family();

	public abstract Stream<Node> deepStream();

	public abstract Stream<Node> deepFamily();

	public final boolean is(Type type) {
		return type == this.type();
	}

	public boolean isNewline() {
		return this.is(Type.NEWLINE);
	}

	public boolean isWhitespace() {
		return this.is(Type.WHITESPACE) || this.is(Type.NEWLINE);
	}

	public boolean isStrictlyWhitespace() {
		return this.is(Type.WHITESPACE);
	}

	public boolean isComment() {
		return this.is(Type.LINE_COMMENT) || this.is(Type.BLOCK_COMMENT_START) || this.is(Type.BLOCK_COMMENT_END) || this.is(Type.BLOCK_COMMENT);
	}

	public boolean isSemantic() {
		return !this.isStrictlyWhitespace() && !this.isComment();
	}

	public boolean isSemanticVisible() {
		return !this.isWhitespace() && !this.isComment();
	}

	public boolean isMapping() {
		return this.is(Type.EQUALS);
	}

	public boolean isComma() {
		return this.is(Type.COMMA);
	}

	public boolean isStringDelimiter() {
		return this.is(Type.STRING_DELIMITER);
	}

	public boolean isBlockCommentStart() {
		return this.is(Type.BLOCK_COMMENT_START);
	}

	public boolean isBlockCommentEnd() {
		return this.is(Type.BLOCK_COMMENT_END);
	}

	public Tree parent() {
		return this.parent;
	}

	public Node previous() {
		return this.previous;
	}

	public Node next() {
		return this.next;
	}

	public boolean hasPrevious() {
		return this.previous != null;
	}

	public boolean hasNext() {
		return this.next != null;
	}

	public Node previous(int distance) {
		return this.iteratePrevious().limit(distance + 1).reduce(this, (a, b) -> b);
	}

	public Node next(int distance) {
		return this.iterateNext().limit(distance + 1).reduce(this, (a, b) -> b);
	}

	public Node root() {
		return this.ancestors().reduce(this, (a, b) -> b);
	}

	public Stream<Node> ancestors() {
		return Stream.iterate(this.parent, Objects::nonNull, Node::parent);
	}

	public Stream<Node> iteratePrevious() {
		return Stream.iterate(this, Objects::nonNull, Node::previous);
	}

	public Stream<Node> iterateStrictlyPrevious() {
		return Stream.iterate(this.previous, Objects::nonNull, Node::previous);
	}

	public Stream<Node> iterateNext() {
		return Stream.iterate(this, Objects::nonNull, Node::next);
	}

	public Stream<Node> iterateStrictlyNext() {
		return Stream.iterate(this.next, Objects::nonNull, Node::next);
	}

	public int index() {
		return (int) this.iterateStrictlyPrevious().count();
	}

	public void parent(Tree parent) {
		if (this.parent != parent) {
			if (this.parent != null) {
				this.parent.remove(this);
			}

			this.parent = parent;
		}
	}

	public void set(Node replacement) {
		replacement.link(this.previous, this.next);
		replacement.parent(this.parent);
		this.parent(null);
	}

	public void previous(Node previous) {
		this.previous = previous;
	}

	public void next(Node next) {
		this.next = next;
	}

	public void linkPrevious(Node previous) {
		if (this.previous != null) {
			this.previous.next(null);
		}

		if (previous != null) {
			if (previous.next != null) {
				previous.next.previous(null);
			}

			previous.next(this);
		}

		this.previous(previous);
	}

	public void linkNext(Node next) {
		if (this.next != null) {
			this.next.previous(null);
		}

		if (next != null) {
			if (next.previous != null) {
				next.previous.next(null);
			}

			next.previous(this);
		}

		this.next(next);
	}

	public void link(Node previous, Node next) {
		this.linkPrevious(previous);
		this.linkNext(next);
	}

	public void insertPrevious(Node previous) {
		previous.link(this.previous(), this);
		previous.parent(this.parent);
	}

	public void insertNext(Node next) {
		next.link(this, this.next());
		next.parent(this.parent);
	}

	public void remove() {
		this.parent = null;
		this.link(null, null);
	}

	public Node cloneRoot() {
		var breadcrumbs = Stream.iterate(this, node -> node.parent != null, Node::parent).mapToInt(Node::index).toArray();
		var clone = this.root().clone();

		if (clone instanceof Tree tree) {
			for (var index = breadcrumbs.length - 1; index >= 0; index--) {
				clone = tree.first.next(breadcrumbs[index]);

				if (index != 0) {
					tree = (Tree) clone;
				}
			}
		}

		return clone;
	}

	public String stringValue() {
		return this.toString();
	}

	public boolean isValue() {
		return false;
	}

	public boolean isPrimitive() {
		return false;
	}

	@Override public int length() {
		return this.toString().length();
	}

	@Override public char charAt(int index) {
		return this.toString().charAt(index);
	}

	@Override public CharSequence subSequence(int start, int end) {
		return this.toString().substring(start, end);
	}

	public enum Type {
		NEWLINE('\n'),
		WHITESPACE,
		LINE_COMMENT,
		BLOCK_COMMENT,
		BLOCK_COMMENT_STRING,
		BLOCK_COMMENT_START,
		BLOCK_COMMENT_END,
		STRING,
		STRING_DELIMITER,
		BOOLEAN,
		INTEGER,
		FLOAT,
		NULL,
		ESCAPE,
		COMMA(','),
		EQUALS('='),
		LBRACKET('['),
		RBRACKET(']'),
		LBRACE('{'),
		RBRACE('}'),
		STRING_TREE,
		PAIR,
		ARRAY,
		MAP,
		FILE,
		ERROR;

		private final char character;

		Type(char character) {
			this.character = character;
		}

		Type() {
			this(Character.MAX_VALUE);
		}

		public static Type delimiter(char character) {
			return switch (character) {
				case '[' -> LBRACKET;
				case ']' -> RBRACKET;
				case '{' -> LBRACE;
				case '}' -> RBRACE;
				default -> throw new IllegalArgumentException(String.valueOf(character));
			};
		}

		public char character() {
			if (this.character == Character.MAX_VALUE) {
				throw new UnsupportedOperationException();
			}

			return this.character;
		}

		public boolean newline() {
			return this == NEWLINE;
		}

		public boolean begin() {
			return this == LBRACKET || this == LBRACE;
		}

		public boolean end() {
			return this == RBRACKET || this == RBRACE;
		}

		public boolean separator() {
			return this == COMMA || this == NEWLINE;
		}
	}
}

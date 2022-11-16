package net.auoeke.sp.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.error.Error;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.tree.ArrayTree;
import net.auoeke.sp.source.tree.BlockCommentTree;
import net.auoeke.sp.source.tree.MapTree;
import net.auoeke.sp.source.tree.PairTree;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.StringTree;
import net.auoeke.sp.source.tree.Tree;

@SuppressWarnings("DuplicatedCode")
public class Parser {
	private final List<Error> errors = new ArrayList<>();
	private final CharSequence sp;
	private Context context = Context.TOP;
	private Node node;
	private Node lookahead;
	private boolean going;

	Parser(CharSequence source, Lexeme first) {
		this.sp = source;
		this.lookahead = this.node = first;
		this.going = this.node != null;
	}

	public static ParseResult parse(String location, String source, StructuredProperties.Option... options) {
		return Lexer.lex(location, source, options).parse();
	}

	public static ParseResult parse(String source, StructuredProperties.Option... options) {
		return parse(null, source, options);
	}

	ParseResult parse0(String location) {
		var tree = new SourceUnit(location, this.sp);

		if (this.going) {
			this.begin(tree);

			if (this.ensureCode()) {
				do {
					var element = this.nextValue();

					if (element != null) {
						if (element instanceof PairTree pair) {
							var map = new MapTree();
							this.begin(map, pair);
							map.entry(pair);
							this.endMap(map, false);
							this.end(map);
							element = map;
						} else if (this.consumeSeparator(false) && (!this.node.isNewline() || this.lookAheadCode())) {
							var array = new ArrayTree();
							this.begin(array, element);
							array.element(element);
							this.endArray(array, false);
							this.end(array);
							element = array;
						}

						tree.element = element;
					} else {
						this.error(Error.Key.ILLEGAL_TOKEN);
					}
				} while (this.advanceCode());
			}

			if (tree.first.parent != null) {
				tree.first = tree.first.parent;
			}

			this.end(tree);
		}

		return new ParseResult(tree, Collections.unmodifiableList(this.errors));
	}

	private Node nextValue() {
		var element = this.value();

		if (element != null && this.going) {
			var primitive = element.isPrimitive();

			if (this.node.type().begin()) {
				if (!primitive) {
					this.error(Error.Key.COMPOUND_STRUCTURE_KEY);
				}

				return this.pair(null, element, this.nextValue());
			}

			if (this.node.isPrimitive()) {
				if (primitive) {
					this.error(Error.Key.PRIMITIVE_RIGHT_NO_MAPPING, Error.Offset.BEFORE);
				} else {
					this.error(element, Error.Key.COMPOUND_KEY);
				}

				return this.pair(null, element, this.nextValue());
			}

			if (this.node.isMapping() || this.lookAheadCode() && this.lookahead.isMapping() && this.advanceCode()) {
				if (this.context != Context.ARRAY && !primitive) {
					this.error(element, Error.Key.COMPOUND_KEY);
				}

				return this.pair(this.node, element, this.advanceCode() ? this.nextValue() : null);
			}
		}

		return element;
	}

	private PairTree pair(Node lastFallback, Node a, Node b) {
		var pair = new PairTree();
		this.begin(pair, a);
		this.end(pair, Objects.requireNonNullElse(b, lastFallback));

		if (b == null) {
			this.error(pair, Error.Key.NO_VALUE);
		}

		return pair;
	}

	private void begin(Tree tree, Node first) {
		tree.last = tree.first = first;

		if (first.previous != null) {
			tree.previous = first.previous;
			first.previous.next = tree;
			first.previous = null;
		}
	}

	private void begin(Tree tree) {
		this.begin(tree, this.node);
	}

	private void end(Tree tree, Node last) {
		tree.last = last;
		tree.forEach(child -> child.parent = tree);

		if (last.next != null) {
			if (last == this.node && !this.advance()) {
				this.node = tree;
			}

			tree.next = last.next;
			last.next.previous = tree;
			last.next = null;
		}
	}

	private void end(Tree tree) {
		this.end(tree, this.node);
	}

	private Node value() {
		switch (this.node.type()) {
			case STRING, BOOLEAN, INTEGER, FLOAT, NULL -> {
				var lexeme = this.node;
				this.advance();

				return lexeme;
			}
			case STRING_DELIMITER -> {
				var tree = new StringTree();
				this.begin(tree);

				do {
					if (!this.advance()) {
						this.error(tree, Error.Key.OPEN_STRING);
						break;
					}
				} while (!this.node.isStringDelimiter());

				this.end(tree);
				return tree;
			}
			default -> {
				var context = this.context;

				switch (this.node.type()) {
					case LBRACKET -> {
						this.context = Context.ARRAY;
						var array = new ArrayTree();
						this.begin(array);
						this.endArray(array, true);
						this.end(array);
						this.context = context;

						return array;
					}
					case LBRACE -> {
						this.context = Context.MAP;
						var map = new MapTree();
						this.begin(map);
						this.endMap(map, true);
						this.end(map);
						this.context = context;

						return map;
					}
					default -> {
						return null;
					}
				}
			}
		}
	}

	private void endArray(ArrayTree array, boolean close) {
		while (this.advanceCode()) {
			if (close && this.node.is(Node.Type.RBRACKET)) {
				return;
			}

			var element = this.nextValue();

			if (element == null) {
				this.error(Error.Key.ILLEGAL_TOKEN);
			} else {
				array.element(element);
			}

			if (close && this.going && this.node.is(Node.Type.RBRACKET)) {
				return;
			}

			this.consumeSeparator(true);
		}

		if (close) {
			this.error(array, Error.Key.OPEN_ARRAY);
		}
	}

	private void endMap(MapTree map, boolean close) {
		while (this.advanceCode()) {
			if (close && this.node.is(Node.Type.RBRACE)) {
				return;
			}

			var element = this.nextValue();

			if (element instanceof PairTree pair) {
				if (map.entry(pair) != null) {
					this.error(pair.a(), Error.Key.DUPLICATE_KEY);
				}
			} else if (element == null) {
				this.error(Error.Key.ILLEGAL_TOKEN);
			} else {
				this.error(Error.Key.NO_MAP_VALUE);
			}

			if (close && this.going && this.node.is(Node.Type.RBRACE)) {
				return;
			}

			this.consumeSeparator(true);
		}

		if (close) {
			this.error(map, Error.Key.OPEN_MAP);
		}
	}

	private boolean consumeSeparator(boolean require) {
		if (this.going) {
			if (this.node.isNewline()) {
				return true;
			}

			if (this.node.isComma()) {
				while (this.lookAheadCode() && this.lookahead.isComma()) {
					this.advanceCode();
					this.error(Error.Key.CONSECUTIVE_COMMA);
				}

				return true;
			}

			if (require) {
				this.error(Error.Key.NO_SEPARATOR, Error.Offset.BEFORE);
			}
		}

		return false;
	}

	private boolean ensureCode() {
		return this.going && this.node.isSemanticVisible() || this.advanceCode();
	}

	private boolean advanceCode() {
		return this.advance(Node::isSemanticVisible);
	}

	private boolean advance() {
		return this.advance(Node::isSemantic);
	}

	private boolean advanceAny() {
		return this.advance(lexeme -> true);
	}

	private boolean advance(Predicate<Node> predicate) {
		if (!this.going) {
			return false;
		}

		while (true) {
			var next = this.next(this.node);

			if (next == null) {
				this.going = false;
				break;
			}

			this.node = next;

			if (predicate.test(next)) {
				if (next.type().end() && !next.is(this.context.end())) {
					this.error(next, Error.Key.END_OUT_OF_CONTEXT);
				}

				break;
			}
		}

		this.lookahead = this.node;
		return this.going;
	}

	private boolean lookAheadCode() {
		return this.lookAhead(Node::isSemanticVisible);
	}

	private boolean lookAhead(Predicate<Node> predicate) {
		return this.peek(predicate) != null;
	}

	private Node peekCode() {
		return this.peek(Node::isSemanticVisible);
	}

	private Node peek(Predicate<Node> predicate) {
		this.lookahead = this.node;

		while (this.lookahead != null) {
			this.lookahead = this.next(this.lookahead);

			if (this.lookahead == null) {
				break;
			}

			if (predicate.test(this.lookahead)) {
				break;
			}
		}

		return this.lookahead;
	}

	private Node next(Node node) {
		if (node.isBlockCommentStart()) {
			var tree = new BlockCommentTree();
			this.begin(tree, node);

			do {
				if (node.next == null) {
					this.error(tree, Error.Key.OPEN_BLOCK_COMMENT);
					break;
				}

				node = node.next;
			} while (!node.isBlockCommentEnd());

			this.end(tree, node);

			if (this.node == tree.first) {
				this.node = tree;
			}

			return tree;
		}

		return node.next;
	}

	private void error(Error.Key error, Object... arguments) {
		this.error(this.node, error, Error.Offset.NONE, arguments);
	}

	private void error(Error.Key error, Error.Offset offset, Object... arguments) {
		this.error(this.node, error, offset, arguments);
	}

	private void error(Node node, Error.Key error, Object... arguments) {
		this.error(node, error, Error.Offset.NONE, arguments);
	}

	private void error(Node node, Error.Key error, Error.Offset offset, Object... arguments) {
		this.errors.add(new Error(node, offset, error, arguments));
	}
}

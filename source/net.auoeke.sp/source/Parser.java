package net.auoeke.sp.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.error.Error;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.lexeme.StringLexeme;
import net.auoeke.sp.source.lexeme.Token;
import net.auoeke.sp.source.tree.ArrayTree;
import net.auoeke.sp.source.tree.MapTree;
import net.auoeke.sp.source.tree.PairTree;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.StringTree;
import net.auoeke.sp.source.tree.Tree;

@SuppressWarnings("DuplicatedCode")
public class Parser {
	private final List<Error> errors = new ArrayList<>();
	private final Lexeme[] stack = new Lexeme[2];
	private final String sp;
	private Context context = Context.TOP;
	private int stackIndex = -1;
	private Lexeme lexeme;
	private Lexeme lookahead;

	private Parser(String sp) {
		this.sp = sp;
		this.lookahead = this.lexeme = new Lexer(sp, StructuredProperties.Option.RETAIN_WHITESPACE, StructuredProperties.Option.RETAIN_COMMENTS).first();
	}

	public static ParseResult parse(String location, String source) {
		return new Parser(source).parse0(location);
	}

	public static ParseResult parse(String source) {
		return parse(null, source);
	}

	private ParseResult parse0(String location) {
		var tree = new SourceUnit(location, this.sp);

		if (this.lexeme != null && this.ensureCode()) {
			do {
				var element = this.nextValue();

				if (element != null) {
					if (element instanceof PairTree pair) {
						var map = new MapTree();
						map.first(element);
						map.entry(pair);
						this.endMap(map, false);
						map.last(this.lexeme);
						tree.addLast(map);
					} else if (this.consumeSeparator(false)) {
						if (this.lexeme.isNewline()) {
							this.pushNew();

							if (this.advanceCode()) {
								this.pop();
							} else {
								tree.addLast(element);
								break;
							}
						}

						var array = new ArrayTree();
						array.first(element);
						array.addLast(element);
						this.endArray(array, false);
						array.last(this.lexeme);
						tree.addLast(array);
					} else {
						tree.addLast(element);
					}
				} else {
					this.error(Error.Key.ILLEGAL_TOKEN);
				}
			} while (this.advanceCode());
		}

		tree.accept(new NodeVisitor() {
			static void fix(Tree node) {
				node.link(node.first.previous, node.last == null ? null : node.last.next);
				node.first.previous(null);
				node.last.next(null);
			}

			@Override public void visit(StringTree node) {
				fix(node);
			}

			@Override public void visit(PairTree node) {
				node.a().accept(this);

				if (node.b() != null) {
					node.b().accept(this);
				}

				fix(node);
			}

			@Override public void visit(ArrayTree node) {
				node.elements.forEach(element -> element.accept(this));
				fix(node);
			}

			@Override public void visit(MapTree node) {
				node.pairs.forEach((key, pair) -> pair.accept(this));
				fix(node);
			}
		});

		return new ParseResult(tree, Collections.unmodifiableList(this.errors));
	}

	private Node nextValue() {
		var element = this.value();

		if (element != null) {
			this.pushNew();

			if (this.advance()) {
				var primitive = element.isPrimitive();

				if (this.lexeme.token().begin()) {
					if (!primitive) {
						this.error(Error.Key.COMPOUND_STRUCTURE_KEY);
					}

					return this.pair(null, element, this.nextValue());
				}

				if (this.lexeme.isPrimitive()) {
					if (primitive) {
						this.error(Error.Key.PRIMITIVE_RIGHT_NO_MAPPING, Error.Offset.BEFORE);
					} else {
						this.error(element, Error.Key.COMPOUND_KEY);
					}

					return this.pair(null, element, this.nextValue());
				}

				this.top();

				if (this.advanceCode() && this.lexeme.isMapping()) {
					if (this.context != Context.ARRAY && !primitive) {
						this.error(element, Error.Key.COMPOUND_KEY);
					}

					return this.pair(this.lexeme, element, this.advanceCode() ? this.nextValue() : null);
				}
			}

			this.pop();
		}

		return element;
	}

	private PairTree pair(Node lastFallback, Node a, Node b) {
		var pair = new PairTree();
		pair.first(a);
		pair.last(b);

		if (b == null) {
			pair.last(lastFallback);
			this.error(pair, Error.Key.NO_VALUE);
		}

		return pair;
	}

	private Node value() {
		for (;;) {
			switch (this.lexeme.token()) {
				case BOOLEAN, INTEGER, FLOAT, NULL -> {
					return this.lexeme;
				}
				case STRING_DELIMITER -> {
					var tree = new StringTree();
					tree.first(this.lexeme);

					for (;;) {
						if (!this.advance()) {
							this.error(tree, Error.Key.OPEN_STRING);
							break;
						}

						if (this.lexeme.isStringDelimiter()) {
							break;
						}
					}

					tree.last(this.lexeme);

					return tree;
				}
				case STRING -> {
					var string = (StringLexeme) this.lexeme;

					if (string == string) {
						var tree = new StringTree();
						tree.first(string);
						tree.last(string);

						return tree;
					}
				}
				default -> {
					var context = this.context;

					switch (this.lexeme.token()) {
						case ARRAY_BEGIN -> {
							this.context = Context.ARRAY;
							var array = new ArrayTree();
							array.first(this.lexeme);
							this.endArray(array, true);
							array.last(this.lexeme);
							this.context = context;

							return array;
						}
						case MAP_BEGIN -> {
							this.context = Context.MAP;
							var map = new MapTree();
							map.first(this.lexeme);
							this.endMap(map, true);
							map.last(this.lexeme);
							this.context = context;

							return map;
						}
						default -> {
							// this.error(Error.Key.ILLEGAL_TOKEN);
							//
							// if (!this.advanceCode()) {
							// 	return null;
							// }

							return null;
						}
					}
				}
			}
		}
	}

	private void endArray(ArrayTree array, boolean close) {
		while (this.advanceCode()) {
			if (this.lexeme.is(Token.ARRAY_END)) {
				return;
			}

			array.element(this.nextValue());
			this.consumeSeparator(true);
		}

		if (close) {
			this.error(array, Error.Key.OPEN_ARRAY);
		}
	}

	private void endMap(MapTree map, boolean close) {
		while (this.advanceCode()) {
			if (this.lexeme.is(Token.MAP_END)) {
				return;
			}

			var element = this.nextValue();

			if (element instanceof PairTree pair) {
				if (map.entry(pair) != null) {
					this.error(pair.a(), Error.Key.DUPLICATE_KEY);
				}

				if (pair.b() == null) {
					this.lexeme = (Lexeme) pair.last;
				}
			} else if (element == null) {
				this.error(Error.Key.ILLEGAL_TOKEN);
			} else {
				this.error(Error.Key.NO_MAP_VALUE);
			}

			this.consumeSeparator(true);
		}

		if (close) {
			this.error(map, Error.Key.OPEN_MAP);
		}
	}

	private boolean consumeSeparator(boolean require) {
		this.pushNew();

		if (this.advance()) {
			if (this.lexeme.isNewline()) {
				return true;
			}

			if (this.lexeme.isComma()) {
				while (this.lookAheadCode() && this.lookahead.isComma()) {
					this.advanceCode();
					this.error(Error.Key.CONSECUTIVE_COMMA);
				}

				return true;
			}

			if (this.lexeme.is(this.context.end())) {
				this.pop();
				return true;
			}

			if (require) {
				this.error(Error.Key.NO_SEPARATOR, Error.Offset.BEFORE);
			}
		}

		return false;
	}

	private boolean ensureCode() {
		return this.lexeme.isSemanticVisible() || this.advanceCode();
	}

	private boolean advanceCode() {
		return this.advance(Lexeme::isSemanticVisible);
	}

	private boolean advance() {
		return this.advance(Lexeme::isSemantic);
	}

	private boolean advanceAny() {
		return this.advance(lexeme -> true);
	}

	private boolean advance(Predicate<Lexeme> predicate) {
		this.lookahead = this.lexeme;

		if (this.lookAhead(predicate)) {
			this.lexeme = this.lookahead;
			return true;
		}

		return false;
	}

	private boolean lookAheadCode() {
		return this.lookAhead(Lexeme::isSemanticVisible);
	}

	private boolean lookAhead(Predicate<Lexeme> predicate) {
		return this.peek(predicate) != null;
	}

	private Lexeme peekCode() {
		return this.peek(Lexeme::isSemanticVisible);
	}

	private Lexeme peek(Predicate<Lexeme> predicate) {
		while (this.lookahead.next != null) {
			this.lookahead = (Lexeme) this.lookahead.next;

			if (!this.lookahead.token().end() || this.lookahead.is(this.context.end())) {
				if (predicate.test(this.lookahead)) {
					return this.lookahead;
				}
			} else {
				this.error(this.lookahead, Error.Key.END_OUT_OF_CONTEXT);
			}
		}

		return this.lookahead = null;
	}

	private void push() {
		this.stack[++this.stackIndex] = this.lexeme;
	}

	private void pushNew() {
		this.stackIndex = -1;
		this.push();
	}

	private Lexeme top() {
		return this.lexeme = this.stack[this.stackIndex];
	}

	private Lexeme pop() {
		return this.lexeme = this.stack[this.stackIndex--];
	}

	private void error(Error.Key error, Object... arguments) {
		this.error(this.lexeme, error, Error.Offset.NONE, arguments);
	}

	private void error(Error.Key error, Error.Offset offset, Object... arguments) {
		this.error(this.lexeme, error, offset, arguments);
	}

	private void error(Node node, Error.Key error, Object... arguments) {
		this.error(node, error, Error.Offset.NONE, arguments);
	}

	private void error(Node node, Error.Key error, Error.Offset offset, Object... arguments) {
		this.errors.add(new Error(node, offset, error, arguments));
	}
}

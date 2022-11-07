package net.auoeke.sp.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
	private final String sp;
	private Context context = Context.TOP;
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
						map.linkFirst(pair);
						map.entry(pair);
						this.endMap(map, false);
						this.linkLast(map);
						tree.addLast(map);
					} else if (this.consumeSeparator(false)) {
						if (this.lexeme.isNewline() && !this.lookAheadCode()) {
							tree.addLast(element);
						} else {
							var array = new ArrayTree();
							array.linkFirst(element);
							array.element(element);
							this.endArray(array, false);
							this.linkLast(array);
							tree.addLast(array);
						}
					} else {
						tree.addLast(element);
					}
				} else {
					this.error(Error.Key.ILLEGAL_TOKEN);
				}
			} while (this.advanceCode());
		}

		return new ParseResult(tree, Collections.unmodifiableList(this.errors));
	}

	private Node nextValue() {
		var element = this.value();

		if (element != null && this.lexeme != null) {
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

			if (this.lexeme.isMapping() || this.lookAheadCode() && this.lookahead.isMapping() && this.advanceCode()) {
				if (this.context != Context.ARRAY && !primitive) {
					this.error(element, Error.Key.COMPOUND_KEY);
				}

				return this.pair(this.lexeme, element, this.advanceCode() ? this.nextValue() : null);
			}
		}

		return element;
	}

	private PairTree pair(Node lastFallback, Node a, Node b) {
		var pair = new PairTree();
		pair.linkFirst(a);
		this.linkLast(pair, Objects.requireNonNullElse(b, lastFallback));

		if (b == null) {
			this.error(pair, Error.Key.NO_VALUE);
		}

		return pair;
	}

	private void linkLast(Tree tree, Node last) {
		if (last == this.lexeme) {
			this.linkLast(tree);
		} else {
			tree.linkLast(last);
		}
	}

	private void linkLast(Tree tree) {
		var last = this.lexeme;
		this.advance();
		tree.linkLast(last);
	}

	private Node value() {
		for (;;) {
			switch (this.lexeme.token()) {
				case BOOLEAN, INTEGER, FLOAT, NULL -> {
					var lexeme = this.lexeme;
					this.advance();

					return lexeme;
				}
				case STRING_DELIMITER -> {
					var tree = new StringTree();
					tree.linkFirst(this.lexeme);

					for (;;) {
						if (!this.advance()) {
							this.error(tree, Error.Key.OPEN_STRING);
							break;
						}

						if (this.lexeme.isStringDelimiter()) {
							break;
						}
					}

					this.linkLast(tree);

					return tree;
				}
				case STRING -> {
					var tree = new StringTree();
					tree.linkFirst(this.lexeme);
					this.linkLast(tree);

					return tree;
				}
				default -> {
					var context = this.context;

					switch (this.lexeme.token()) {
						case ARRAY_BEGIN -> {
							this.context = Context.ARRAY;
							var array = new ArrayTree();
							array.linkFirst(this.lexeme);
							this.endArray(array, true);
							this.linkLast(array);
							this.context = context;

							return array;
						}
						case MAP_BEGIN -> {
							this.context = Context.MAP;
							var map = new MapTree();
							map.linkFirst(this.lexeme);
							this.endMap(map, true);
							this.linkLast(map);
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
	}

	private void endArray(ArrayTree array, boolean close) {
		while (this.advanceCode()) {
			if (close && this.lexeme.is(Token.ARRAY_END)) {
				return;
			}

			var element = this.nextValue();

			if (element == null) {
				this.error(Error.Key.ILLEGAL_TOKEN);
			} else {
				array.element(element);
			}

			if (close && this.lexeme != null && this.lexeme.is(Token.ARRAY_END)) {
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
			if (close && this.lexeme.is(Token.MAP_END)) {
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

			if (close && this.lexeme != null && this.lexeme.is(Token.MAP_END)) {
				return;
			}

			this.consumeSeparator(true);
		}

		if (close) {
			this.error(map, Error.Key.OPEN_MAP);
		}
	}

	private boolean consumeSeparator(boolean require) {
		if (this.lexeme != null) {
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

		while (this.lexeme != null) {
			this.lexeme = (Lexeme) this.lexeme.next;

			if (this.lexeme != null && predicate.test(this.lexeme)) {
				if (this.lexeme.token().end() && !this.lexeme.is(this.context.end())) {
					this.error(this.lexeme, Error.Key.END_OUT_OF_CONTEXT);
				}

				break;
			}
		}

		return this.lexeme != null;
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
		this.lookahead = this.lexeme;

		while (this.lookahead != null) {
			this.lookahead = (Lexeme) this.lookahead.next;

			if (this.lookahead == null || predicate.test(this.lookahead)) {
				break;
			}
		}

		return this.lookahead;
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

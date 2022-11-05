package net.auoeke.sp.source;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.lexeme.BooleanLexeme;
import net.auoeke.sp.source.lexeme.CharacterLexeme;
import net.auoeke.sp.source.lexeme.CommentLexeme;
import net.auoeke.sp.source.lexeme.EscapedLexeme;
import net.auoeke.sp.source.lexeme.FloatLexeme;
import net.auoeke.sp.source.lexeme.IntegerLexeme;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.lexeme.NullLexeme;
import net.auoeke.sp.source.lexeme.Position;
import net.auoeke.sp.source.lexeme.StringDelimiterLexeme;
import net.auoeke.sp.source.lexeme.StringLexeme;
import net.auoeke.sp.source.lexeme.Token;
import net.auoeke.sp.source.lexeme.WhitespaceLexeme;

public class Lexer {
	private static final String STRING_DELIMITERS = "\"'`";
	private static final String LINE_COMMENT = "##";
	private static final String BLOCK_COMMENT = "/*";
	private static final String BLOCK_COMMENT_END = "*/";
	private static final String RAW_STRING_TERMINATORS = "\n,={}[]";

	private final String source;
	private final boolean retainWhitespace;
	private final boolean retainComments;

	private int nextIndex;
	private int line;
	private int column = -1;
	private Position savedPosition = new Position(-1, -1, -1);
	private char current;
	private Lexeme first;
	private Lexeme last;

	public Lexer(String source, StructuredProperties.Option... options) {
		this.source = source;

		var optionSet = EnumSet.copyOf(List.of(options));
		this.retainWhitespace = optionSet.contains(StructuredProperties.Option.RETAIN_WHITESPACE);
		this.retainComments = optionSet.contains(StructuredProperties.Option.RETAIN_COMMENTS);

		while (this.advance()) {
			this.process();
		}
	}

	public Lexeme first() {
		return this.first;
	}

	public Lexeme last() {
		return this.last;
	}

	public Stream<Lexeme> lexemes() {
		return Stream.iterate(this.first, Objects::nonNull, lexeme -> (Lexeme) lexeme.next());
	}

	private Position position() {
		return new Position(this.previousIndex(), this.line, this.column);
	}

	private int previousIndex() {
		return this.nextIndex - 1;
	}

	private boolean hasNext() {
		return this.nextIndex < this.source.length();
	}

	private char next() {
		if (this.current == '\n') {
			++this.line;
			this.column = -1;
		}

		++this.column;
		return this.current = this.source.charAt(this.nextIndex++);
	}

	private boolean advance() {
		if (this.nextIndex < this.source.length()) {
			this.next();
			return true;
		}

		return false;
	}

	private char peek() {
		return this.source.charAt(this.nextIndex);
	}

	private boolean match(int lookahead, String expected) {
		return this.source.regionMatches(this.previousIndex() + lookahead, expected, 0, expected.length());
	}

	private boolean peek(char expected) {
		return this.hasNext() && this.source.charAt(this.nextIndex) == expected;
	}

	private void savePosition() {
		this.savedPosition = this.position();
	}

	private boolean whitespaceOrComment() {
		if (this.current == '\n') {
			this.add(new CharacterLexeme(this.position(), Token.NEWLINE));
		} else if (Character.isWhitespace(this.current)) {
			this.whitespace();
		} else {
			return this.comment();
		}

		return true;
	}

	private boolean comma() {
		if (this.current == ',') {
			this.add(new CharacterLexeme(this.position(), Token.COMMA));
			return true;
		}

		return false;
	}

	private boolean mapping() {
		if (this.current == '=') {
			this.add(new CharacterLexeme(this.position(), Token.MAPPING));
			return true;
		}

		return false;
	}

	private void process() {
		if (!this.whitespaceOrComment() && !this.comma() && !this.mapping()) {
			this.savePosition();

			if (contains("{}[]", this.current)) {
				this.structure();
			} else if (contains(STRING_DELIMITERS, this.current)) {
				this.string();
			} else {
				this.rawString();
			}

			if (this.hasNext() && !this.peek('\n') && Character.isWhitespace(this.peek())) {
				this.next();
				this.whitespace();
			}
		}
	}

	private void add(Lexeme lexeme) {
		if (lexeme != null && (lexeme.isComment() ? this.retainComments : this.retainWhitespace || !lexeme.isStrictlyWhitespace())) {
			if (this.first == null) this.first = lexeme;
			if (this.last != null) lexeme.linkPrevious(this.last);

			this.last = lexeme;
		}
	}

	private void whitespace() {
		this.add(new WhitespaceLexeme(this.position(), buildString(builder -> {
			builder.append(this.current);

			while (this.hasNext()) {
				if (!Character.isWhitespace(this.peek()) || this.peek('\n')) {
					return;
				}

				builder.append(this.next());
			}
		})));
	}

	private boolean comment() {
		if (this.match(0, BLOCK_COMMENT)) {
			this.add(new CommentLexeme(this.position(), Token.BLOCK_COMMENT, buildString(builder -> {
				this.next();
				var depth = 1;

				while (this.advance()) {
					if (this.match(0, BLOCK_COMMENT)) {
						this.next();
						++depth;
						builder.append(BLOCK_COMMENT);
					} else if (this.match(0, BLOCK_COMMENT_END)) {
						this.next();

						if (--depth == 0) {
							return;
						}

						builder.append(BLOCK_COMMENT_END);
					} else {
						builder.append(this.current);
					}
				}
			})));
		} else if (this.match(0, LINE_COMMENT)) {
			this.add(new CommentLexeme(this.position(), Token.LINE_COMMENT, buildString(builder -> {
				this.next();

				while (this.hasNext()) {
					if (this.peek('\n')) {
						return;
					}

					builder.append(this.next());
				}
			})));
		} else {
			return false;
		}

		return true;
	}

	private void structure() {
		var token = Token.delimiter(this.current);
		this.add(new CharacterLexeme(this.position(), token));

		if (!token.end()) {
			var end = switch (token) {
				case ARRAY_BEGIN -> Token.ARRAY_END;
				case MAP_BEGIN -> Token.MAP_END;
				default -> throw null;
			};

			while (this.advance()) {
				if (this.current == end.character()) {
					this.structure();
					return;
				}

				this.process();
			}
		}
	}

	private int delimiterLength(char delimiter) {
		var length = 1;

		while (this.peek(delimiter)) {
			++length;
			this.next();
		}

		return length;
	}

	private void string() {
		var delimiter = this.current;
		var length = this.delimiterLength(delimiter);

		if (length == 2) {
			this.add(new StringDelimiterLexeme(this.savedPosition, String.valueOf(delimiter)));
			this.add(new StringLexeme(this.position(), ""));
			this.add(new StringDelimiterLexeme(this.position(), String.valueOf(delimiter)));
		} else {
			this.add(new StringDelimiterLexeme(this.savedPosition, String.valueOf(delimiter).repeat(length)));

			this.savePosition();
			var builder = new StringBuilder();
			Runnable flush = () -> {
				if (!builder.isEmpty()) {
					this.add(new StringLexeme(this.savedPosition, builder.toString()));
				}
			};

			while (this.advance()) {
				if (builder.isEmpty()) {
					this.savePosition();
				}

				if (this.current == delimiter) {
					var position = this.position();
					var count = 1;

					while (this.peek(delimiter) && count < length) {
						this.next();
						++count;
					}

					if (count == length) {
						flush.run();
						this.add(new StringDelimiterLexeme(position, String.valueOf(delimiter).repeat(length)));

						return;
					}

					builder.append(this.source, this.previousIndex() - count, this.nextIndex);
				} else if (this.current == '\\') {
					var position = this.position();

					if (this.advance()) {
						flush.run();
						builder = new StringBuilder();
						this.add(new EscapedLexeme(position, this.current));
					} else {
						builder.append(this.current);
						flush.run();

						return;
					}
				} else {
					builder.append(this.current);
				}
			}

			flush.run();
		}
	}

	private void rawString() {
		var character = this.current;
		var additionalWhitespace = new ArrayList<Lexeme>();

		var string = buildString(builder -> {
			builder.append(character);
			Position whitespace = null;

			while (this.hasNext()) {
				if (contains(RAW_STRING_TERMINATORS, this.peek()) || this.match(1, LINE_COMMENT) || this.match(1, BLOCK_COMMENT)) {
					if (whitespace != null) {
						additionalWhitespace.add(new WhitespaceLexeme(whitespace, this.source.substring(whitespace.index(), this.nextIndex)));
					}

					return;
				}

				this.next();

				if (Character.isWhitespace(this.current)) {
					if (whitespace == null) {
						whitespace = this.position();
					}
				} else if (whitespace == null) {
					builder.append(this.current);
				} else {
					builder.append(this.source, whitespace.index(), this.nextIndex);
					whitespace = null;
				}
			}
		});

		this.add(switch (string) {
			case "false" -> new BooleanLexeme(this.savedPosition, false);
			case "true" -> new BooleanLexeme(this.savedPosition, true);
			case "null" -> new NullLexeme(this.savedPosition);
			default -> {
				try {
					yield new IntegerLexeme(this.savedPosition, string, new BigInteger(string));
				} catch (NumberFormatException exception) {
					try {
						yield new FloatLexeme(this.savedPosition, string, new BigDecimal(string));
					} catch (NumberFormatException e) {
						yield new StringLexeme(this.savedPosition, string);
					}
				}
			}
		});
		additionalWhitespace.forEach(this::add);
	}

	private static String buildString(Consumer<StringBuilder> builder) {
		var b = new StringBuilder();
		builder.accept(b);
		return b.toString();
	}

	private static boolean contains(String string, int character) {
		return string.indexOf(character) >= 0;
	}
}

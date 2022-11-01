package net.auoeke.sp.source;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.lexeme.CharacterLexeme;
import net.auoeke.sp.source.lexeme.CommentLexeme;
import net.auoeke.sp.source.lexeme.EscapedLexeme;
import net.auoeke.sp.source.lexeme.Lexeme;
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

	private Position position() {
		return new Position(this.previousIndex(), this.line, this.column);
	}

	private int previousIndex() {
		return this.nextIndex - 1;
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

	private char previous() {
		var previous = this.source.charAt(--this.nextIndex);

		if (previous == '\n') {
			--this.line;
		}

		--this.column;

		return this.current = previous;
	}

	private boolean peek(String expected) {
		return this.source.regionMatches(this.previousIndex(), expected, 0, expected.length());
	}

	private boolean peek(char expected) {
		return this.source.length() > this.nextIndex && this.source.charAt(this.nextIndex) == expected;
	}

	private void index(int index) {
		this.nextIndex = index + 1;
		this.current = this.source.charAt(index);
	}

	private void savePosition() {
		this.savedPosition = this.position();
	}

	private boolean whitespaceOrComment() {
		if (this.current == '\n') {
			this.add(new CharacterLexeme(this.savedPosition, Token.NEWLINE));
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

	private void scanExpression() {
		if (contains("{}[]", this.current)) {
			this.structure();
		} else if (contains(STRING_DELIMITERS, this.current)) {
			this.string();
		} else {
			this.rawString();
		}
	}

	private void process() {
		if (!this.whitespaceOrComment() && !this.comma() && !this.mapping()) {
			this.savePosition();
			this.scanExpression();

			while (this.advance()) {
				if (!this.shouldTerminateExpression() && Character.isWhitespace(this.current)) {
					this.whitespace();
					return;
				}

				this.previous();
				return;
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

			while (this.advance()) {
				if (!Character.isWhitespace(this.current) || this.current == '\n') {
					this.previous();
					return;
				}

				builder.append(this.current);
			}
		})));
	}

	private boolean comment() {
		if (this.peek(BLOCK_COMMENT)) {
			this.add(new CommentLexeme(this.position(), Token.BLOCK_COMMENT, buildString(builder -> {
				var depth = 1;

				while (this.advance()) {
					if (this.peek(BLOCK_COMMENT)) {
						++this.nextIndex;
						++depth;
						builder.append(BLOCK_COMMENT);
					} else if (this.peek(BLOCK_COMMENT_END)) {
						++this.nextIndex;

						if (--depth == 0) {
							return;
						}

						builder.append(BLOCK_COMMENT_END);
					} else {
						builder.append(this.current);
					}
				}
			})));
		} else if (this.peek(LINE_COMMENT)) {
			this.add(new CommentLexeme(this.position(), Token.LINE_COMMENT, buildString(builder -> {
				++this.nextIndex;

				while (this.advance()) {
					if (this.current == '\n') {
						this.previous();
						return;
					}

					builder.append(this.current);
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
			this.advance();
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
				if (this.current == delimiter) {
					var position = this.position();
					var count = 1;

					while (this.peek(delimiter) && count < length) {
						this.advance();
						++count;
					}

					if (count == length) {
						flush.run();
						this.add(new StringDelimiterLexeme(position, String.valueOf(delimiter).repeat(length)));

						return;
					}

					builder.append(this.source, this.previousIndex() - count, this.nextIndex);
				} else if (this.current == '\\') {
					this.savePosition();

					if (this.advance()) {
						flush.run();
						builder = new StringBuilder();
						this.savePosition();
						this.add(new EscapedLexeme(this.savedPosition, this.current));
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
			var whitespace = -1;

			while (this.advance()) {
				if (this.shouldTerminateExpression()) {
					if (whitespace != -1) {
						additionalWhitespace.add(new WhitespaceLexeme(this.savedPosition, this.source.substring(whitespace, this.previousIndex())));
					}

					this.previous();
					return;
				}

				if (Character.isWhitespace(this.current)) {
					if (whitespace == -1) {
						whitespace = this.previousIndex();
					}
				} else if (whitespace == -1) {
					builder.append(this.current);
				} else {
					builder.append(this.source, whitespace, this.nextIndex);
					whitespace = -1;
				}
			}
		});

		this.add(new StringLexeme(this.savedPosition, string));
		additionalWhitespace.forEach(this::add);
	}

	private boolean shouldTerminateExpression() {
		return contains("\n,={}[]", this.current) || this.peek(LINE_COMMENT) || this.peek(BLOCK_COMMENT);
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

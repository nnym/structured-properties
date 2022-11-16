package net.auoeke.sp.source;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.lexeme.BooleanLexeme;
import net.auoeke.sp.source.lexeme.CharacterLexeme;
import net.auoeke.sp.source.lexeme.LineCommentLexeme;
import net.auoeke.sp.source.lexeme.EscapedLexeme;
import net.auoeke.sp.source.lexeme.FloatLexeme;
import net.auoeke.sp.source.lexeme.IntegerLexeme;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.lexeme.NullLexeme;
import net.auoeke.sp.source.lexeme.Position;
import net.auoeke.sp.source.lexeme.DelimiterLexeme;
import net.auoeke.sp.source.lexeme.StringLexeme;
import net.auoeke.sp.source.lexeme.WhitespaceLexeme;

public class Lexer {
	private static final String STRING_DELIMITERS = "\"'`";
	private static final String LINE_COMMENT = "##";
	private static final String BLOCK_COMMENT = "/*";
	private static final String BLOCK_COMMENT_END = "*/";
	private static final String RAW_STRING_TERMINATORS = "\n,={}[]";

	final CharSequence source;

	private final boolean retainWhitespace;
	private final boolean retainComments;

	private int nextIndex;
	private int line;
	private int column = -1;
	private char current;
	private Lexeme first;
	private Lexeme last;

	public Lexer(CharSequence source, StructuredProperties.Option... options) {
		this.source = source;

		var optionList = List.of(options);
		this.retainWhitespace = !optionList.contains(StructuredProperties.Option.SKIP_WHITESPACE);
		this.retainComments = !optionList.contains(StructuredProperties.Option.SKIP_COMMENTS);
	}

	public static LexResult lex(String location, CharSequence source, StructuredProperties.Option... options) {
		var lexer = new Lexer(source, options);
		while (lexer.advance()) {}

		return new LexResult(location, source, lexer.first, lexer.last);
	}

	public static LexResult lex(CharSequence source, StructuredProperties.Option... options) {
		return lex(null, source, options);
	}

	public boolean advance() {
		if (this.tryNext()) {
			switch (this.current) {
				case '\n' -> this.add(new CharacterLexeme(this.position(), Node.Type.NEWLINE));
				case ',' -> this.add(new CharacterLexeme(this.position(), Node.Type.COMMA));
				case '=' -> this.add(new CharacterLexeme(this.position(), Node.Type.EQUALS));
				default -> {
					if (Character.isWhitespace(this.current)) {
						this.whitespace();
					} else if (this.match(0, LINE_COMMENT)) {
						this.lineComment();
					} else if (this.match(0, BLOCK_COMMENT)) {
						this.blockComment();
					} else {
						if (contains("{[", this.current)) {
							this.structure();
						} else if (contains("}]", this.current)) {
							this.structureDelimiter();
						} else if (contains(STRING_DELIMITERS, this.current)) {
							this.string();
						} else {
							this.rawString();
							return true;
						}

						if (this.hasNext() && !this.peek('\n') && Character.isWhitespace(this.peek())) {
							this.next();
							this.whitespace();
						}
					}
				}
			}

			return true;
		}

		return false;
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

	private Position nextPosition() {
		return new Position(this.nextIndex, this.line, this.column);
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

	private boolean tryNext() {
		if (this.hasNext()) {
			this.next();
			return true;
		}

		return false;
	}

	private char peek() {
		return this.source.charAt(this.nextIndex);
	}

	private boolean match(int lookahead, String expected) {
		var offset = this.previousIndex() + lookahead;
		var length = expected.length();

		if (offset + length > this.source.length()) {
			return false;
		}

		for (var index = 0; index < length; index++) {
			if (this.source.charAt(offset + index) != expected.charAt(index)) {
				return false;
			}
		}

		return true;
	}

	private boolean peek(char expected) {
		return this.hasNext() && this.source.charAt(this.nextIndex) == expected;
	}

	private void add(Lexeme lexeme) {
		if (lexeme != null && (lexeme.isComment() ? this.retainComments : this.retainWhitespace || !lexeme.isStrictlyWhitespace())) {
			if (this.first == null) this.first = lexeme;
			if (this.last != null) lexeme.linkPrevious(this.last);

			this.last = lexeme;
		}
	}

	private void addString(Node.Type type, Position start, int end) {
		if (end != start.index()) {
			this.add(new StringLexeme(start, type, this.source.subSequence(start.index(), end)));
		}
	}

	private void whitespace() {
		var position = this.position();

		while (this.hasNext() && Character.isWhitespace(this.peek()) && !this.peek('\n')) {
			this.next();
		}

		this.add(new WhitespaceLexeme(position, this.source.subSequence(position.index(), this.nextIndex)));
	}

	private void lineComment() {
		var position = this.position();
		this.next();

		while (this.hasNext() && !this.peek('\n')) {
			this.next();
		}

		this.add(new LineCommentLexeme(position, this.source.subSequence(position.index() + 2, this.nextIndex)));
	}

	private void structureDelimiter() {
		this.add(new CharacterLexeme(this.position(), Node.Type.delimiter(this.current)));
	}

	private void structure() {
		var type = Node.Type.delimiter(this.current);
		this.add(new CharacterLexeme(this.position(), type));

		var end = switch (type) {
			case LBRACKET -> Node.Type.RBRACKET.character();
			case LBRACE -> Node.Type.RBRACE.character();
			default -> throw null;
		};

		while (this.advance() && this.current != end) {}
	}

	private int delimiterLength(char delimiter) {
		var length = 1;

		while (this.peek(delimiter)) {
			++length;
			this.next();
		}

		return length;
	}

	private Position escaped(Node.Type type, Position start) {
		if (this.current == '\\') {
			var position = this.position();

			if (this.tryNext()) {
				this.addString(type, start, position.index());
				this.add(new EscapedLexeme(position, this.current));

				return this.nextPosition();
			}

			this.addString(type, start, this.nextIndex);
			return null;
		}

		return start;
	}

	private void blockComment() {
		this.add(new DelimiterLexeme(this.position(), Node.Type.BLOCK_COMMENT_START, BLOCK_COMMENT));
		this.next();

		var start = this.nextPosition();
		var depth = 1;

		while (this.tryNext() && start != null) {
			if (this.match(0, BLOCK_COMMENT)) {
				++depth;
				this.next();
			} else if (this.match(0, BLOCK_COMMENT_END)) {
				var position = this.position();
				this.next();

				if (--depth == 0) {
					this.addString(Node.Type.BLOCK_COMMENT_STRING, start, position.index());
					this.add(new DelimiterLexeme(position, Node.Type.BLOCK_COMMENT_END, BLOCK_COMMENT_END));

					return;
				}
			} else {
				start = this.escaped(Node.Type.BLOCK_COMMENT_STRING, start);
			}
		}

		this.addString(Node.Type.BLOCK_COMMENT_STRING, start, this.nextIndex);
	}

	private void string() {
		var start = this.position();
		var delimiter = this.current;
		var length = this.delimiterLength(delimiter);

		if (length == 2) {
			var delimiterString = String.valueOf(delimiter);
			this.add(new DelimiterLexeme(start, Node.Type.STRING_DELIMITER, delimiterString));
			this.add(new DelimiterLexeme(this.position(), Node.Type.STRING_DELIMITER, delimiterString));
		} else {
			this.add(new DelimiterLexeme(start, Node.Type.STRING_DELIMITER, String.valueOf(delimiter).repeat(length)));
			start = this.nextPosition();

			while (this.tryNext() && start != null) {
				if (this.current == delimiter) {
					var position = this.position();
					var count = 1;

					while (this.peek(delimiter) && count < length) {
						++count;
						this.next();
					}

					if (count == length) {
						this.addString(Node.Type.STRING, start, position.index());
						this.add(new DelimiterLexeme(position, Node.Type.STRING_DELIMITER, String.valueOf(delimiter).repeat(length)));

						return;
					}
				} else {
					start = this.escaped(Node.Type.STRING, start);
				}
			}

			this.addString(Node.Type.STRING, start, this.nextIndex);
		}
	}

	private void rawString() {
		var start = this.position();
		Position whitespace = null;

		while (this.hasNext() && !contains(RAW_STRING_TERMINATORS, this.peek()) && !this.match(1, LINE_COMMENT) && !this.match(1, BLOCK_COMMENT)) {
			this.next();

			if (Character.isWhitespace(this.current)) {
				if (whitespace == null) {
					whitespace = this.position();
				}
			} else {
				whitespace = null;
			}
		}

		var string = this.source.subSequence(start.index(), whitespace == null ? this.nextIndex : whitespace.index()).toString();

		this.add(switch (string) {
			case "false" -> new BooleanLexeme(start, false);
			case "true" -> new BooleanLexeme(start, true);
			case "null" -> new NullLexeme(start);
			default -> {
				try {
					yield new IntegerLexeme(start, string, new BigInteger(string));
				} catch (NumberFormatException exception) {
					try {
						yield new FloatLexeme(start, string, new BigDecimal(string));
					} catch (NumberFormatException e) {
						yield new StringLexeme(start, Node.Type.STRING, string);
					}
				}
			}
		});

		if (whitespace != null) {
			this.add(new WhitespaceLexeme(whitespace, this.source.subSequence(whitespace.index(), this.nextIndex)));
		}
	}

	private static boolean contains(String string, int character) {
		return string.indexOf(character) >= 0;
	}
}

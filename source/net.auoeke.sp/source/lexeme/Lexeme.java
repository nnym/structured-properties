package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.Node;

/**
 A terminal symbol.
 */
public abstract sealed class Lexeme extends Node permits BooleanLexeme, CharacterLexeme, CommentLexeme, EscapedLexeme, FloatLexeme, IntegerLexeme, NullLexeme, StringDelimiterLexeme, StringLexeme, WhitespaceLexeme {
	public final Position position;

	public Lexeme(Position position) {
		this.position = position;
	}

	public abstract Token token();

	public boolean is(Token token) {
		return token == this.token();
	}

	public boolean isNewline() {
		return this.is(Token.NEWLINE);
	}

	public boolean isWhitespace() {
		return this.is(Token.WHITESPACE) || this.is(Token.NEWLINE);
	}

	public boolean isStrictlyWhitespace() {
		return this.is(Token.WHITESPACE);
	}

	public boolean isComment() {
		return this.is(Token.LINE_COMMENT) || this.is(Token.BLOCK_COMMENT);
	}

	public boolean isSemantic() {
		return !this.isStrictlyWhitespace() && !this.isComment();
	}

	public boolean isSemanticVisible() {
		return !this.isWhitespace() && !this.isComment();
	}

	public boolean isMapping() {
		return this.is(Token.MAPPING);
	}

	public boolean isComma() {
		return this.is(Token.COMMA);
	}

	public boolean isStringDelimiter() {
		return this.is(Token.STRING_DELIMITER);
	}

	@Override public Position start() {
		return this.position;
	}
}

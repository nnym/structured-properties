package net.auoeke.eson.parser.lexer.lexeme;

import net.auoeke.eson.parser.lexer.error.SyntaxError;

public final class WhitespaceLexeme extends Lexeme {
    public final String value;

    public WhitespaceLexeme(int line, int column, String value, SyntaxError error) {
        super(line, column, error);

        this.value = value;
    }

    public WhitespaceLexeme(int line, int column, String value) {
        this(line, column, value, null);
    }

    @Override public Token token() {
        return Token.WHITESPACE;
    }

    @Override public String toString() {
        return this.value;
    }
}

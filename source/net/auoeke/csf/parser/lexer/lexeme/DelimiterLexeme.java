package net.auoeke.csf.parser.lexer.lexeme;

import net.auoeke.csf.parser.lexer.error.SyntaxError;

public final class DelimiterLexeme extends Lexeme {
    public final Token token;

    public DelimiterLexeme(int line, int column, Token token, SyntaxError error) {
        super(line, column, error);

        this.token = token;
    }

    public DelimiterLexeme(int line, int column, Token token) {
        this(line, column, token, null);
    }

    @Override public Token token() {
        return this.token;
    }

    @Override public String toString() {
        return String.valueOf(this.token().character());
    }
}

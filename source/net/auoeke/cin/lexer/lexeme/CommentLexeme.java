package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class CommentLexeme extends Lexeme {
    public final String value;
    public final Token token;

    public CommentLexeme(int line, int column, Token token, String value, SyntaxError error) {
        super(line, column, error);

        this.token = token;
        this.value = value;
    }

    public CommentLexeme(int line, int column, Token token, String value) {
        this(line, column, token, value, null);
    }

    @Override public Token token() {
        return this.token;
    }

    @Override public String toString() {
        return switch (this.token()) {
            case LINE_COMMENT -> "##" + this.value;
            case BLOCK_COMMENT -> "/*" + this.value + "*/";
            default -> throw new IllegalStateException(this.token.toString());
        };
    }
}

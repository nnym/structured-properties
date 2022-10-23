package net.auoeke.sp.parser.lexer.lexeme;

public final class CommentLexeme extends Lexeme {
    public final String value;
    public final Token token;

    public CommentLexeme(int line, int column, Token token, String value) {
        super(line, column, null);

        this.token = token;
        this.value = value;
    }

    @Override public Token token() {
        return this.token;
    }

    @Override public String toString() {
        return switch (this.token) {
            case LINE_COMMENT -> "##" + this.value;
            case BLOCK_COMMENT -> "/*" + this.value + "*/";
            default -> throw new IllegalStateException(this.token.toString());
        };
    }
}

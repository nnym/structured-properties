package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class CommentLexeme extends Lexeme {
    public final String value;
    public final Type type;

    public CommentLexeme(int line, int column, Type type, String value, SyntaxError error) {
        super(line, column, error);

        this.type = type;
        this.value = value;
    }

    public CommentLexeme(int line, int column, Type type, String value) {
        this(line, column, type, value, null);
    }

    @Override public Type type() {
        return this.type;
    }

    @Override public String toString() {
        return switch (this.type()) {
            case LINE_COMMENT -> "##$value";
            case BLOCK_COMMENT -> "/*$value*/";
            default -> throw new IllegalStateException(this.type.toString());
        };
    }
}

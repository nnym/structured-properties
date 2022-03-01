package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class IntegerLexeme extends Lexeme {
    public final long value;

    public IntegerLexeme(int line, int column, long value, SyntaxError error) {
        super(line, column, error);

        this.value = value;
    }

    public IntegerLexeme(int line, int column, long value) {
        this(line, column, value, null);
    }

    @Override public Type type() {
        return Type.INTEGER;
    }

    @Override public String toString() {
        return Long.toString(this.value);
    }
}

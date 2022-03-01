package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class BooleanLexeme extends Lexeme {
    public final boolean value;

    public BooleanLexeme(int line, int column, boolean value, SyntaxError error) {
        super(line, column, error);

        this.value = value;
    }

    public BooleanLexeme(int line, int column, boolean value) {
        this(line, column, value, null);
    }

    @Override public Type type() {
        return Type.BOOLEAN;
    }

    @Override public String toString() {
        return Boolean.toString(this.value);
    }
}

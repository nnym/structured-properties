package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class FloatLexeme extends Lexeme {
    public final String string;
    public final double value;

    public FloatLexeme(int line, int column, double value, String string, SyntaxError error) {
        super(line, column, error);

        this.value = value;
        this.string = string;
    }

    public FloatLexeme(int line, int column, double value, String string) {
        this(line, column, value, string, null);
    }

    @Override public Type type() {
        return Type.FLOAT;
    }

    @Override public String toString() {
        return this.string;
    }
}

package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class WhitespaceLexeme extends Lexeme {
    public final String value;

    public WhitespaceLexeme(int line, int column, String value, SyntaxError error) {
        super(line, column, error);

        this.value = value;
    }

    public WhitespaceLexeme(int line, int column, String value) {
        this(line, column, value, null);
    }

    @Override public Type type() {
        return Type.WHITESPACE;
    }

    @Override public String toString() {
        return this.value;
    }
}

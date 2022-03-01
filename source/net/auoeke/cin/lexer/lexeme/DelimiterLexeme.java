package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class DelimiterLexeme extends Lexeme {
    public final char value;

    public DelimiterLexeme(int line, int column, char value, SyntaxError error) {
        super(line, column, error);

        this.value = value;
    }

    public DelimiterLexeme(int line, int column, char value) {
        this(line, column, value, null);
    }

    @Override public Type type() {
        return Type.DELIMITER;
    }

    @Override public String toString() {
        return String.valueOf(this.value);
    }
}

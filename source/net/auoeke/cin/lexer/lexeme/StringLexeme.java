package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class StringLexeme extends Lexeme {
    public final String value;
    public final String delimiter;

    public StringLexeme(int line, int column, String delimiter, String value, SyntaxError error) {
        super(line, column, error);

        this.delimiter = delimiter;
        this.value = value;
    }

    public StringLexeme(int line, int column, String delimiter, String value) {
        this(line, column, delimiter, value, null);
    }

    @Override public Type type() {
        return Type.STRING;
    }

    @Override public String toString() {
        return this.delimiter == null ? this.value : this.delimiter + this.value + this.delimiter;
    }
}

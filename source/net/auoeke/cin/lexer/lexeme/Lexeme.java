package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public abstract class Lexeme implements CharSequence {
    public final int column;
    public final int line;
    public SyntaxError error;

    public Lexeme(int line, int column, SyntaxError error) {
        this.column = column;
        this.error = error;
        this.line = line;
    }

    public Lexeme(int line, int column) {
        this(line, column, null);
    }

    public abstract Token token();

    @Override public abstract String toString();

    @Override public int length() {
        return this.toString().length();
    }

    @Override public char charAt(int index) {
        return this.toString().charAt(index);
    }

    @Override public CharSequence subSequence(int start, int end) {
        return this.toString().substring(start, end);
    }
}

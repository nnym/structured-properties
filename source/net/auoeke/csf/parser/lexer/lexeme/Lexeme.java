package net.auoeke.csf.parser.lexer.lexeme;

import net.auoeke.csf.parser.lexer.error.SyntaxError;

public abstract class Lexeme implements CharSequence {
    public final int line;
    public final int column;
    public SyntaxError error;

    public Lexeme(int line, int column, SyntaxError error) {
        this.line = line;
        this.column = column;
        this.error = error;
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

    public String position() {
        return this.line + ":" + this.column;
    }
}

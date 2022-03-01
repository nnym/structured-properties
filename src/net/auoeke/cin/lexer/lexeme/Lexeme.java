package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public abstract class Lexeme {
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

    public abstract Type type();

    @Override public abstract String toString();

    public enum Type {
        WHITESPACE,
        NEWLINE,
        LINE_COMMENT,
        BLOCK_COMMENT,
        COMMA,
        DELIMITER,
        EQUALS,
        BOOLEAN,
        NULL,
        INTEGER,
        FLOAT,
        STRING;

        public final boolean isMapping() {
            return this == EQUALS;
        }

        public final boolean isWhitespace() {
            return this == WHITESPACE || this == NEWLINE;
        }

        public final boolean isComment() {
            return this == LINE_COMMENT || this == BLOCK_COMMENT;
        }

        public final boolean isSourceOnly() {
            return this == WHITESPACE || this.isComment();
        }
    }
}

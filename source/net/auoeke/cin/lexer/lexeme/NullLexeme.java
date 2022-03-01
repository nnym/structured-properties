package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class NullLexeme extends Lexeme {
    public NullLexeme(int line, int column, SyntaxError error) {
        super(line, column, error);
    }

    public NullLexeme(int line, int column) {
        super(line, column);
    }

    @Override public Type type() {
        return Type.NULL;
    }

    @Override public String toString() {
        return "null";
    }
}

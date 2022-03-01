package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class MappingLexeme extends Lexeme {
    public MappingLexeme(int line, int column, SyntaxError error) {
        super(line, column, error);
    }

    public MappingLexeme(int line, int column) {
        super(line, column);
    }

    @Override public Type type() {
        return Type.EQUALS;
    }

    @Override public String toString() {
        return "=";
    }
}

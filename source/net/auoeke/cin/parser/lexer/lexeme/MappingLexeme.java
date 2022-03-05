package net.auoeke.cin.parser.lexer.lexeme;

import net.auoeke.cin.parser.lexer.error.SyntaxError;

public final class MappingLexeme extends Lexeme {
    public MappingLexeme(int line, int column, SyntaxError error) {
        super(line, column, error);
    }

    public MappingLexeme(int line, int column) {
        super(line, column);
    }

    @Override public Token token() {
        return Token.MAPPING;
    }

    @Override public String toString() {
        return "=";
    }
}

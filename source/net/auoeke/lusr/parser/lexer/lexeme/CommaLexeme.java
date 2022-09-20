package net.auoeke.lusr.parser.lexer.lexeme;

import net.auoeke.lusr.parser.lexer.error.SyntaxError;

public final class CommaLexeme extends Lexeme {
    public CommaLexeme(int line, int column, SyntaxError error) {
        super(line, column, error);
    }

    public CommaLexeme(int line, int column) {
        super(line, column);
    }

    @Override public Token token() {
        return Token.COMMA;
    }

    @Override public String toString() {
        return ",";
    }
}

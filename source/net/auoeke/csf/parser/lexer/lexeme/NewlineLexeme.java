package net.auoeke.csf.parser.lexer.lexeme;

import net.auoeke.csf.parser.lexer.error.SyntaxError;

public final class NewlineLexeme extends Lexeme {
    public NewlineLexeme(int line, int column, SyntaxError error) {
        super(line, column, error);
    }

    public NewlineLexeme(int line, int column) {
        super(line, column);
    }

    @Override public Token token() {
        return Token.NEWLINE;
    }

    @Override public String toString() {
        return "\n";
    }
}

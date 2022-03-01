package net.auoeke.cin.lexer.lexeme;

import net.auoeke.cin.lexer.error.SyntaxError;

public final class NewlineLexeme extends Lexeme {
    public NewlineLexeme(int line, int column, SyntaxError error) {
        super(line, column, error);
    }

    public NewlineLexeme(int line, int column) {
        super(line, column);
    }

    @Override public Type type() {
        return Type.NEWLINE;
    }

    @Override public String toString() {
        return "\n";
    }
}

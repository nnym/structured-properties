package net.auoeke.cin.lexer;

import java.util.ListIterator;
import net.auoeke.cin.lexer.lexeme.Lexeme;

class TokenIterator implements ListIterator<Lexeme> {
    private final int size;
    private final Lexeme[] lexemes;

    TokenIterator(int size, Lexeme[] lexemes) {
        this.size = size;
        this.lexemes = lexemes;
    }

    private int cursor = 0;

    @Override public boolean hasNext() {
        return this.cursor < this.size;
    }
    @Override public boolean hasPrevious() {
        return this.cursor > 0;
    }
    @Override public Lexeme next() {
        return this.lexemes[this.cursor++];
    }
    @Override public int nextIndex() {
        return this.cursor;
    }
    @Override public Lexeme previous() {
        return this.lexemes[--this.cursor];
    }
    @Override public int previousIndex() {
        return this.cursor - 1;
    }

    @Override public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override public void set(Lexeme lexeme) {
        throw new UnsupportedOperationException();
    }

    @Override public void add(Lexeme lexeme) {
        throw new UnsupportedOperationException();
    }
}

package net.auoeke.csf.parser.lexer;

import java.util.ListIterator;
import net.auoeke.csf.parser.lexer.lexeme.Lexeme;

public class LexemeIterator implements ListIterator<Lexeme> {
    private final int size;
    private final Lexeme[] lexemes;
    private int cursor = 0;

    public LexemeIterator(int size, Lexeme[] lexemes) {
        this.size = size;
        this.lexemes = lexemes;
    }

    public void cursor(int cursor) {
        if (cursor < 0 || cursor > this.size) {
            throw new IllegalArgumentException(Integer.toString(cursor));
        }

        this.cursor = cursor;
    }

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

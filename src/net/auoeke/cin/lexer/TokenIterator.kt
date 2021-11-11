package net.auoeke.cin.lexer

import net.auoeke.cin.lexer.lexeme.Lexeme

internal class TokenIterator(private val size: Int, private val lexemes: Array<Lexeme>) : ListIterator<Lexeme> {
    private var cursor: Int = 0

    override fun hasNext(): Boolean = cursor < size
    override fun hasPrevious(): Boolean = cursor > 0
    override fun next(): Lexeme = lexemes[cursor++]
    override fun nextIndex(): Int = cursor
    override fun previous(): Lexeme = lexemes[--cursor]
    override fun previousIndex(): Int = cursor - 1
}

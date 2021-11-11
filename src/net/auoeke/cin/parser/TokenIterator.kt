package net.auoeke.cin.parser

import net.auoeke.cin.parser.token.Token

internal class TokenIterator(private val size: Int, private val tokens: Array<Token>) : ListIterator<Token> {
    private var cursor: Int = 0

    override fun hasNext(): Boolean = cursor < size
    override fun hasPrevious(): Boolean = cursor > 0
    override fun next(): Token = tokens[cursor++]
    override fun nextIndex(): Int = cursor
    override fun previous(): Token = tokens[--cursor]
    override fun previousIndex(): Int = cursor - 1
}

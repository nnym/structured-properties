package net.auoeke.cin.lexer.lexeme

import net.auoeke.extensions.string

class DelimiterLexeme(line: Int, column: Int, val value: Char) : Lexeme(line, column) {
    override val type: Type get() = Type.DELIMITER

    override fun toString(): String = value.string
}

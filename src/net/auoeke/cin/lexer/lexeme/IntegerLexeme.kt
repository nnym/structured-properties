package net.auoeke.cin.lexer.lexeme

import net.auoeke.extensions.string

class IntegerLexeme(line: Int, column: Int, val value: Long) : Lexeme(line, column) {
    override val type: Type get() = Type.INTEGER

    override fun toString(): String = value.string
}

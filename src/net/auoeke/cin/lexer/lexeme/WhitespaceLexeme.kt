package net.auoeke.cin.lexer.lexeme

import net.auoeke.extensions.string

class WhitespaceLexeme(line: Int, column: Int, val value: String) : Lexeme(line, column) {
    override val type: Type get() = Type.WHITESPACE

    override fun toString(): String = value.string
}

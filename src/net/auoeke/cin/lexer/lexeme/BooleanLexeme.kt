package net.auoeke.cin.lexer.lexeme

import net.auoeke.cin.lexer.SyntaxError
import net.auoeke.extensions.string

class BooleanLexeme(line: Int, column: Int, val value: Boolean, error: SyntaxError? = null) : Lexeme(line, column, error) {
    override val type: Type get() = Type.BOOLEAN

    override fun toString(): String = value.string
}

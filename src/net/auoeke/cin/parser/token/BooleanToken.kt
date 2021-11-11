package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.SyntaxError
import net.auoeke.cin.parser.token.type.TokenType
import net.auoeke.extensions.string

class BooleanToken(line: Int, column: Int, val value: Boolean, error: SyntaxError? = null) : Token(line, column, error) {
    override val type: TokenType get() = TokenType.BOOLEAN

    override fun toString(): String = value.string
}

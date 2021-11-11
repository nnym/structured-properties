package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType
import net.auoeke.extensions.string

class IntegerToken(line: Int, column: Int, val value: Long) : Token(line, column) {
    override val type: TokenType get() = TokenType.INTEGER

    override fun toString(): String = value.string
}

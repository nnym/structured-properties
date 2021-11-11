package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType

class NullToken(line: Int, column: Int) : Token(line, column) {
    override val type: TokenType get() = TokenType.NULL

    override fun toString(): String = "null"
}

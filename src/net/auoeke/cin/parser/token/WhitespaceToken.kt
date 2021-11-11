package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType
import net.auoeke.extensions.string

class WhitespaceToken(line: Int, column: Int, val value: String) : Token(line, column) {
    override val type: TokenType get() = TokenType.WHITESPACE

    override fun toString(): String = value.string
}

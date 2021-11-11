package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType
import net.auoeke.extensions.string

class DelimiterToken(line: Int, column: Int, val value: Char) : Token(line, column) {
    override val type: TokenType get() = TokenType.DELIMITER

    override fun toString(): String = value.string
}

package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType

class StringToken(line: Int, column: Int, val delimiter: String?, val value: String) : Token(line, column) {
    override val type: TokenType get() = TokenType.STRING

    override fun toString(): String = when (delimiter) {
        null -> value
        else -> delimiter + value + delimiter
    }
}

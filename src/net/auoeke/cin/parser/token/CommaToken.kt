package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType

class CommaToken(line: Int, column: Int) : Token(line, column) {
    override val type: TokenType get() = TokenType.COMMA

    override fun toString(): String = ","
}

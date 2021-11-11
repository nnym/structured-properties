package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType

class NewlineToken(line: Int, column: Int) : Token(line, column) {
    override val type: TokenType get() = TokenType.NEWLINE

    override fun toString(): String = "\n"
}

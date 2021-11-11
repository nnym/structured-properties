package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.SyntaxError
import net.auoeke.cin.parser.token.type.TokenType

abstract class Token(val line: Int, val column: Int, var error: SyntaxError? = null) {
    abstract val type: TokenType

    abstract override fun toString(): String
}

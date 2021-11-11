package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.TokenType
import net.auoeke.extensions.*

class FloatToken(line: Int, column: Int, val value: Double, val integral: Boolean) : Token(line, column) {
    override val type: TokenType get() = TokenType.FLOAT

    override fun toString(): String = value.string.mapIf(integral) {it.substring(0, it.indexOf('.') + 1)}
}

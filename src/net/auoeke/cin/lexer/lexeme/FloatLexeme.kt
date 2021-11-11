package net.auoeke.cin.lexer.lexeme

import net.auoeke.extensions.*

class FloatLexeme(line: Int, column: Int, val value: Double, val integral: Boolean) : Lexeme(line, column) {
    override val type: Type get() = Type.FLOAT

    override fun toString(): String = value.string.mapIf(integral) {it.substring(0, it.indexOf('.') + 1)}
}

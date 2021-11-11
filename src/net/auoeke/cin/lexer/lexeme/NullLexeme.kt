package net.auoeke.cin.lexer.lexeme

class NullLexeme(line: Int, column: Int) : Lexeme(line, column) {
    override val type: Type get() = Type.NULL

    override fun toString(): String = "null"
}

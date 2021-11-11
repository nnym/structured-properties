package net.auoeke.cin.lexer.lexeme

class NewlineLexeme(line: Int, column: Int) : Lexeme(line, column) {
    override val type: Type get() = Type.NEWLINE

    override fun toString(): String = "\n"
}

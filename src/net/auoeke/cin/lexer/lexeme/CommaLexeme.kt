package net.auoeke.cin.lexer.lexeme

class CommaLexeme(line: Int, column: Int) : Lexeme(line, column) {
    override val type: Type get() = Type.COMMA

    override fun toString(): String = ","
}

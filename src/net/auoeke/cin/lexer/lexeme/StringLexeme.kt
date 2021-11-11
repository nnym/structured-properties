package net.auoeke.cin.lexer.lexeme

class StringLexeme(line: Int, column: Int, val delimiter: String?, val value: String) : Lexeme(line, column) {
    override val type: Type get() = Type.STRING

    override fun toString(): String = when (delimiter) {
        null -> value
        else -> delimiter + value + delimiter
    }
}

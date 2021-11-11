package net.auoeke.cin.lexer.lexeme

class MappingLexeme(line: Int, column: Int, override val type: Type) : Lexeme(line, column) {
    override fun toString(): String = when (type) {
        Type.EQUALS -> "="
        Type.COLON -> ":"
        else -> throw IllegalStateException()
    }
}

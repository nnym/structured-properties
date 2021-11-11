package net.auoeke.cin.lexer.lexeme

import net.auoeke.extensions.*

class CommentLexeme(line: Int, column: Int, override val type: Type, val value: String) : Lexeme(line, column) {
    override fun toString(): String = when (type) {
        Type.LINE_COMMENT -> "//$value"
        Type.BLOCK_COMMENT -> "///$value///"
        else -> throw IllegalStateException(type.string)
    }
}

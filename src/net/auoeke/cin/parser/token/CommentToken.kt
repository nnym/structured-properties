package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.CommentType
import net.auoeke.cin.parser.token.type.TokenType

class CommentToken(line: Int, column: Int, val delimiter: CommentType, val value: String) : Token(line, column) {
    override val type: TokenType get() = TokenType.COMMENT

    override fun toString(): String = when (delimiter) {
        CommentType.LINE -> "//$value"
        CommentType.BLOCK -> "///$value///"
    }
}

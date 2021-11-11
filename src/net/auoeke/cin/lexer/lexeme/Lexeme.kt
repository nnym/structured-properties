package net.auoeke.cin.lexer.lexeme

import net.auoeke.cin.lexer.SyntaxError

abstract class Lexeme(val line: Int, val column: Int, var error: SyntaxError? = null) {
    abstract val type: Type

    abstract override fun toString(): String

    enum class Type {
        WHITESPACE,
        NEWLINE,
        LINE_COMMENT,
        BLOCK_COMMENT,
        COMMA,
        DELIMITER,
        COLON,
        EQUALS,
        BOOLEAN,
        NULL,
        INTEGER,
        FLOAT,
        STRING;

        inline val isWhitespace: Boolean get() = this == WHITESPACE || this == NEWLINE
        inline val isComment: Boolean get() = this == LINE_COMMENT || this == BLOCK_COMMENT
        inline val isMapping: Boolean get() = this == COLON || this == EQUALS
    }
}

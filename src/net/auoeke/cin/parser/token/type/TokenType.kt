package net.auoeke.cin.parser.token.type

enum class TokenType {
    WHITESPACE,
    NEWLINE,
    COMMENT,
    COMMA,
    DELIMITER,
    MAPPING,
    BOOLEAN,
    NULL,
    INTEGER,
    FLOAT,
    STRING;

    inline val isWhitespace: Boolean get() = this == WHITESPACE || this == NEWLINE
}

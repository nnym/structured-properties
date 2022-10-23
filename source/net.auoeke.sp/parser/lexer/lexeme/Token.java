package net.auoeke.sp.parser.lexer.lexeme;

public enum Token {
    NEWLINE('\n'),
    WHITESPACE(Character.MAX_VALUE),
    LINE_COMMENT(Character.MAX_VALUE),
    BLOCK_COMMENT(Character.MAX_VALUE),
    STRING(Character.MAX_VALUE),
    COMMA(','),
    MAPPING('='),
    ARRAY_BEGIN('['),
    ARRAY_END(']'),
    MAP_BEGIN('{'),
    MAP_END('}');

    private final char character;

    Token(char character) {
        this.character = character;
    }

    public static Token delimiter(char character) {
        return switch (character) {
            case '[' -> ARRAY_BEGIN;
            case ']' -> ARRAY_END;
            case '{' -> MAP_BEGIN;
            case '}' -> MAP_END;
            default -> throw new IllegalArgumentException(String.valueOf(character));
        };
    }

    public char character() {
        if (this.character == Character.MAX_VALUE) {
            throw new UnsupportedOperationException();
        }

        return this.character;
    }

    public boolean mapping() {
        return this == MAPPING;
    }

    public boolean newline() {
        return this == NEWLINE;
    }

    public boolean whitespace() {
        return this == WHITESPACE || this == NEWLINE;
    }

    public boolean comment() {
        return this == LINE_COMMENT || this == BLOCK_COMMENT;
    }

    public boolean sourceOnly() {
        return this == WHITESPACE || this.comment();
    }

    public boolean begin() {
        return this == ARRAY_BEGIN || this == MAP_BEGIN;
    }

    public boolean end() {
        return this == ARRAY_END || this == MAP_END;
    }

    public boolean separator() {
        return this == COMMA || this == NEWLINE;
    }

    public boolean delimiter() {
        return switch (this) {
            case ARRAY_BEGIN, ARRAY_END, MAP_BEGIN, MAP_END -> true;
            default -> false;
        };
    }

    public boolean primitive() {
        return switch (this) {
            case STRING -> true;
            default -> false;
        };
    }

    public boolean expression() {
        return switch (this) {
            case STRING, ARRAY_BEGIN, MAP_BEGIN -> true;
            default -> false;
        };
    }
}

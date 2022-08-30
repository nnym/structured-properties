package net.auoeke.csf.parser;

import net.auoeke.csf.parser.lexer.lexeme.Token;

public enum Context {
    FILE,
    ARRAY,
    MAP;

    public Token begin() {
        return switch (this) {
            case ARRAY -> Token.ARRAY_BEGIN;
            case MAP -> Token.MAP_BEGIN;
            case FILE -> null;
        };
    }

    public Token end() {
        return switch (this) {
            case ARRAY -> Token.ARRAY_END;
            case MAP -> Token.MAP_END;
            case FILE -> null;
        };
    }

    public String description() {
        return switch (this) {
            case ARRAY -> "an array";
            case MAP -> "a map";
            case FILE -> "a file";
        };
    }

    public static Context of(char character) {
        return switch (character) {
            case '[' -> ARRAY;
            case '{' -> MAP;
            default -> throw new Error();
        };
    }
}

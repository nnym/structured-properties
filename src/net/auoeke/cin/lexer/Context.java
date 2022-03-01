package net.auoeke.cin.lexer;

public enum Context {
    FILE,
    ARRAY,
    MAP;

    public char start() {
        return switch (this) {
            case ARRAY -> '[';
            case MAP -> '{';
            case FILE -> Character.MIN_VALUE;
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

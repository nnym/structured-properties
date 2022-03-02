package net.auoeke.cin.element;

public interface Element {
    Type type();

    enum Type {
        EMPTY,
        NULL,
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING,
        ARRAY,
        PAIR,
        MAP;

        public boolean primitive() {
            return switch (this) {
                case NULL, BOOLEAN, INTEGER, FLOAT, STRING -> true;
                default -> false;
            };
        }
    }
}

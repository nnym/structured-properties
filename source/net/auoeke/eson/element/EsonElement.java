package net.auoeke.eson.element;

public sealed interface EsonElement permits EsonPrimitive, EsonPair, EsonArray, EsonMap {
    Type type();

    enum Type {
        EMPTY,
        NULL,
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING,
        PAIR,
        ARRAY,
        MAP;

        public boolean primitive() {
            return switch (this) {
                case NULL, BOOLEAN, INTEGER, FLOAT, STRING -> true;
                default -> false;
            };
        }

        public boolean structure() {
            return this == ARRAY || this == MAP;
        }
    }
}

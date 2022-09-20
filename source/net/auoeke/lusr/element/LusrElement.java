package net.auoeke.lusr.element;

public sealed interface LusrElement permits LusrPrimitive, LusrPair, LusrArray, LusrMap {
    Type type();

    enum Type {
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

package net.auoeke.csf.element;

public sealed interface CsfElement permits CsfPrimitive, CsfPair, CsfArray, CsfMap {
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

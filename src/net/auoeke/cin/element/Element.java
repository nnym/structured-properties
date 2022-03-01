package net.auoeke.cin.element;

public interface Element {
    Type type();

    enum Type {
        NULL,
        BOOLEAN,
        INTEGER,
        FLOAT,
        STRING,
        ARRAY,
        MAP
    }
}

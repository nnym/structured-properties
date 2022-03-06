package net.auoeke.eson.element;

public final class EsonEmpty implements EsonElement {
    public static final EsonEmpty instance = new EsonEmpty();

    private EsonEmpty() {}

    @Override public Type type() {
        return Type.EMPTY;
    }

    @Override public int hashCode() {
        return 1;
    }

    @Override public boolean equals(Object object) {
        return this == object;
    }

    @Override public String toString() {
        return "";
    }
}

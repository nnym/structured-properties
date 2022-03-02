package net.auoeke.cin.element;

public final class EmptyElement implements Element {
    public static final EmptyElement instance = new EmptyElement();

    private EmptyElement() {}

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

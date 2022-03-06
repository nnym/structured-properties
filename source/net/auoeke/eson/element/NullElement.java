package net.auoeke.eson.element;

public final class NullElement implements Element {
    public static final NullElement instance = new NullElement();

    private NullElement() {}

    @Override public Type type() {
        return Element.Type.NULL;
    }

    @Override public String toString() {
        return "null";
    }
}

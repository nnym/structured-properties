package net.auoeke.cin.element;

public class NullElement implements Element {
    public static final NullElement instance = new NullElement();

    private NullElement() {}

    @Override public Type type() {
        return Element.Type.NULL;
    }

    @Override public String toString() {
        return "null";
    }
}

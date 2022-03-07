package net.auoeke.eson.element;

public final class EsonNull implements EsonElement {
    public static final EsonNull instance = new EsonNull();

    private EsonNull() {}

    @Override public Type type() {
        return Type.NULL;
    }

    @Override public String toString() {
        return "null";
    }
}

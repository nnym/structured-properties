package net.auoeke.eson.element;

public final class EsonNull implements EsonPrimitive {
    public static final EsonNull instance = new EsonNull();

    private EsonNull() {}

    @Override public String stringValue() {
        return "null";
    }

    @Override public Type type() {
        return Type.NULL;
    }

    @Override public String toString() {
        return this.stringValue();
    }
}

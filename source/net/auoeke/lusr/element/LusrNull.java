package net.auoeke.lusr.element;

public final class LusrNull implements LusrPrimitive {
    public static final LusrNull instance = new LusrNull();

    private LusrNull() {}

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

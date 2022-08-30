package net.auoeke.csf.element;

public final class CsfNull implements CsfPrimitive {
    public static final CsfNull instance = new CsfNull();

    private CsfNull() {}

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

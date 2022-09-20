package net.auoeke.lusr.element;

public final class LusrBoolean implements LusrPrimitive {
    private static final LusrBoolean tru = new LusrBoolean(true);
    private static final LusrBoolean fals = new LusrBoolean(false);

    public boolean value;

    private LusrBoolean(boolean value) {
        this.value = value;
    }

    public static LusrBoolean of(boolean value) {
        return value ? tru : fals;
    }

    @Override public String stringValue() {
        return Boolean.toString(this.value);
    }

    @Override public Type type() {
        return Type.BOOLEAN;
    }

    @Override public boolean equals(Object other) {
        return other instanceof LusrBoolean bool && this.value == bool.value;
    }

    @Override public int hashCode() {
        return Boolean.hashCode(this.value);
    }

    @Override public String toString() {
        return this.stringValue();
    }
}

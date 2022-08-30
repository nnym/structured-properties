package net.auoeke.csf.element;

public final class CsfBoolean implements CsfPrimitive {
    private static final CsfBoolean tru = new CsfBoolean(true);
    private static final CsfBoolean fals = new CsfBoolean(false);

    public boolean value;

    private CsfBoolean(boolean value) {
        this.value = value;
    }

    public static CsfBoolean of(boolean value) {
        return value ? tru : fals;
    }

    @Override public String stringValue() {
        return Boolean.toString(this.value);
    }

    @Override public Type type() {
        return Type.BOOLEAN;
    }

    @Override public boolean equals(Object other) {
        return other instanceof CsfBoolean bool && this.value == bool.value;
    }

    @Override public int hashCode() {
        return Boolean.hashCode(this.value);
    }

    @Override public String toString() {
        return this.stringValue();
    }
}

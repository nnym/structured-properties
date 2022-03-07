package net.auoeke.eson.element;

public class EsonBoolean implements EsonElement {
    private static final EsonBoolean tru = new EsonBoolean(true);
    private static final EsonBoolean fals = new EsonBoolean(false);

    public boolean value;

    private EsonBoolean(boolean value) {
        this.value = value;
    }

    public static EsonBoolean of(boolean value) {
        return value ? tru : fals;
    }

    @Override public Type type() {
        return Type.BOOLEAN;
    }

    @Override public boolean equals(Object other) {
        return other instanceof EsonBoolean bool && this.value == bool.value;
    }

    @Override public int hashCode() {
        return Boolean.hashCode(this.value);
    }

    @Override public String toString() {
        return Boolean.toString(this.value);
    }
}

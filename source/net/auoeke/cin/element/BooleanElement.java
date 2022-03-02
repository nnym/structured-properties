package net.auoeke.cin.element;

public class BooleanElement implements Element {
    private static final BooleanElement tru = new BooleanElement(true);
    private static final BooleanElement fals = new BooleanElement(false);

    public boolean value;

    private BooleanElement(boolean value) {
        this.value = value;
    }

    public static BooleanElement of(boolean value) {
        return value ? tru : fals;
    }

    @Override public Type type() {
        return Element.Type.BOOLEAN;
    }

    @Override public boolean equals(Object other) {
        return other instanceof BooleanElement bool && this.value == bool.value;
    }

    @Override public int hashCode() {
        return Boolean.hashCode(this.value);
    }

    @Override public String toString() {
        return Boolean.toString(this.value);
    }
}

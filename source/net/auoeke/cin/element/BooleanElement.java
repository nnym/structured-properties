package net.auoeke.cin.element;

public class BooleanElement implements Element {
    public boolean value;

    public BooleanElement(boolean value) {
        this.value = value;
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

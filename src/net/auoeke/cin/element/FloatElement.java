package net.auoeke.cin.element;

public class FloatElement extends NumberElement {
    public double value;

    public FloatElement(double value) {
        this.value = value;
    }

    @Override public Type type() {
        return Type.FLOAT;
    }

    @Override public int intValue() {
        return (int) this.value;
    }

    @Override public long longValue() {
        return (long) this.value;
    }

    @Override public float floatValue() {
        return (float) this.value;
    }

    @Override public double doubleValue() {
        return this.value;
    }

    @Override public byte byteValue() {
        return (byte) this.value;
    }

    @Override public short shortValue() {
        return (short) this.value;
    }

    @Override public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override public boolean equals(Object other) {
        return other instanceof FloatElement flote && this.value == flote.value;
    }

    @Override public String toString() {
        return Double.toString(this.value);
    }
}

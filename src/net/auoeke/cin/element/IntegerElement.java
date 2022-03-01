package net.auoeke.cin.element;

public class IntegerElement extends NumberElement {
    public long value;

    public IntegerElement(long value) {
        this.value = value;
    }

    @Override public Type type() {
        return Type.INTEGER;
    }

    @Override public int intValue() {
        return (int) this.value;
    }

    @Override public long longValue() {
        return this.value;
    }

    @Override public float floatValue() {
        return this.value;
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
        return other instanceof IntegerElement integer && this.value == integer.value;
    }

    @Override public String toString() {
        return Long.toString(this.value);
    }
}

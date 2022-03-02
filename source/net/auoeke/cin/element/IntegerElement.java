package net.auoeke.cin.element;

public class IntegerElement extends NumberElement {
    private long value;
    private boolean raw;

    public IntegerElement(String source, long value) {
        super(source);

        this.value = value;
    }

    public IntegerElement(long value) {
        super(Long.toString(value));

        this.value = value;
    }

    public IntegerElement(String value) {
        super(value);

        this.raw = true;
    }

    public double value() {
        return this.raw ? this.value = Long.parseLong(this.source) : this.value;
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
        return Long.hashCode(this.value);
    }

    @Override public boolean equals(Object other) {
        return other instanceof IntegerElement integer && this.value == integer.value;
    }
}

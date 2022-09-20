package net.auoeke.lusr.element;

import java.math.BigInteger;

public final class LusrInteger extends LusrNumber {
    private BigInteger value;
    private boolean raw;

    public LusrInteger(String source, BigInteger value) {
        super(source);

        this.value = value;
    }

    public LusrInteger(BigInteger value) {
        super(value.toString());

        this.value = value;
    }

    public LusrInteger(long value) {
        this(BigInteger.valueOf(value));
    }

    public LusrInteger(String value) {
        super(value);

        this.raw = true;
    }

    public BigInteger value() {
        return this.raw ? this.value = new BigInteger(this.source) : this.value;
    }

    @Override public Type type() {
        return Type.INTEGER;
    }

    @Override public int intValue() {
        return this.value().intValue();
    }

    @Override public long longValue() {
        return this.value().longValue();
    }

    @Override public float floatValue() {
        return this.value().floatValue();
    }

    @Override public double doubleValue() {
        return this.value().doubleValue();
    }

    @Override public byte byteValue() {
        return this.value().byteValue();
    }

    @Override public short shortValue() {
        return this.value().shortValue();
    }

    @Override public int hashCode() {
        return this.value.hashCode();
    }

    @Override public boolean equals(Object other) {
        return other instanceof LusrInteger integer && this.value().equals(integer.value());
    }
}

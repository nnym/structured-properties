package net.auoeke.sp.element;

import java.math.BigInteger;

public final class SpInteger extends SpNumber {
    private BigInteger value;
    private boolean raw;

    public SpInteger(String source, BigInteger value) {
        super(source);

        this.value = value;
    }

    public SpInteger(BigInteger value) {
        super(value.toString());

        this.value = value;
    }

    public SpInteger(long value) {
        this(BigInteger.valueOf(value));
    }

    public SpInteger(String value) {
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
        return other instanceof SpInteger integer && this.value().equals(integer.value());
    }
}

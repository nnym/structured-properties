package net.auoeke.sp.element;

import java.math.BigDecimal;

public final class SpFloat extends SpNumber {
    private BigDecimal value;
    private boolean raw;

    public SpFloat(String source, BigDecimal value) {
        super(source);

        this.value = value;
    }

    public SpFloat(String value) {
        super(value);

        this.raw = true;
    }

    public SpFloat(BigDecimal value) {
        super(value.toString());

        this.value = value;
    }

    public SpFloat(double value) {
        this(BigDecimal.valueOf(value));
    }

    public BigDecimal value() {
        if (this.raw) {
            this.value = new BigDecimal(this.source);
            this.raw = false;
        }

        return this.value;
    }

    @Override public Type type() {
        return Type.FLOAT;
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
        return this.value().hashCode();
    }

    @Override public boolean equals(Object other) {
        return other instanceof SpFloat flote && this.value().equals(flote.value());
    }
}

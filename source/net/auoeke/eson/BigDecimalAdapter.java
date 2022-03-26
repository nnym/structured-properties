package net.auoeke.eson;

import java.math.BigDecimal;
import net.auoeke.eson.element.EsonFloat;

final class BigDecimalAdapter implements EsonAdapter<BigDecimal, EsonFloat> {
    static final BigDecimalAdapter instance = new BigDecimalAdapter();

    @Override public EsonFloat toEson(BigDecimal integer, Eson serializer) {
        return new EsonFloat(integer);
    }

    @Override public BigDecimal fromEson(EsonFloat eson, Eson serializer) {
        return eson.value();
    }
}

package net.auoeke.eson.serialization;

import java.math.BigDecimal;
import net.auoeke.eson.element.EsonFloat;

final class BigDecimalSerializer implements EsonTypeSerializer<BigDecimal, EsonFloat> {
    static final BigDecimalSerializer instance = new BigDecimalSerializer();

    @Override public EsonFloat toEson(BigDecimal integer, EsonSerializer serializer) {
        return new EsonFloat(integer);
    }

    @Override public BigDecimal fromEson(EsonFloat eson, EsonSerializer serializer) {
        return eson.value();
    }
}

package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonFloat;

final class DoubleSerializer implements EsonTypeSerializer<Double, EsonFloat> {
    static final DoubleSerializer instance = new DoubleSerializer();

    @Override public EsonFloat toEson(Double integer, EsonSerializer serializer) {
        return new EsonFloat(integer);
    }

    @Override public Double fromEson(EsonFloat eson, EsonSerializer serializer) {
        return eson.value().doubleValue();
    }
}

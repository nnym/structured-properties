package net.auoeke.eson;

import net.auoeke.eson.element.EsonFloat;

final class DoubleAdapter implements EsonAdapter<Double, EsonFloat> {
    static final DoubleAdapter instance = new DoubleAdapter();

    @Override public EsonFloat toEson(Double integer, Eson serializer) {
        return new EsonFloat(integer);
    }

    @Override public Double fromEson(EsonFloat eson, Eson serializer) {
        return eson.value().doubleValue();
    }
}

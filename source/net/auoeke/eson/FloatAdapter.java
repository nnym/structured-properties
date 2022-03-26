package net.auoeke.eson;

import net.auoeke.eson.element.EsonFloat;

final class FloatAdapter implements EsonAdapter<Float, EsonFloat> {
    static final FloatAdapter instance = new FloatAdapter();

    @Override public EsonFloat toEson(Float floa, Eson serializer) {
        return new EsonFloat(floa);
    }

    @Override public Float fromEson(EsonFloat eson, Eson serializer) {
        return eson.value().floatValue();
    }
}

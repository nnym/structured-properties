package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonFloat;

final class FloatSerializer implements EsonTypeSerializer<Float, EsonFloat> {
    static final FloatSerializer instance = new FloatSerializer();

    @Override public EsonFloat toEson(Float floa, EsonSerializer serializer) {
        return new EsonFloat(floa);
    }

    @Override public Float fromEson(EsonFloat eson, EsonSerializer serializer) {
        return eson.value().floatValue();
    }
}

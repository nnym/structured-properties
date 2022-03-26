package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonElement;

final class EsonElementSerializer implements EsonTypeSerializer<EsonElement, EsonElement> {
    static final EsonElementSerializer instance = new EsonElementSerializer();

    @Override public EsonElement toEson(EsonElement eson, EsonSerializer serializer) {
        return eson;
    }

    @Override public EsonElement fromEson(EsonElement eson, EsonSerializer serializer) {
        return eson;
    }
}

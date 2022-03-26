package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonInteger;

final class LongSerializer implements EsonTypeSerializer<Long, EsonInteger> {
    static final LongSerializer instance = new LongSerializer();

    @Override public EsonInteger toEson(Long lon, EsonSerializer serializer) {
        return new EsonInteger(lon);
    }

    @Override public Long fromEson(EsonInteger eson, EsonSerializer serializer) {
        return eson.value().longValueExact();
    }
}

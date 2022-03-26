package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonInteger;

final class ShortSerializer implements EsonTypeSerializer<Short, EsonInteger> {
    static final ShortSerializer instance = new ShortSerializer();

    @Override public EsonInteger toEson(Short shor, EsonSerializer serializer) {
        return new EsonInteger(shor);
    }

    @Override public Short fromEson(EsonInteger eson, EsonSerializer serializer) {
        return eson.value().shortValueExact();
    }
}

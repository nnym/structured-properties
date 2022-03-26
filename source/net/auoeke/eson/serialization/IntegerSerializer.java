package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonInteger;

final class IntegerSerializer implements EsonTypeSerializer<Integer, EsonInteger> {
    static final IntegerSerializer instance = new IntegerSerializer();

    @Override public EsonInteger toEson(Integer integer, EsonSerializer serializer) {
        return new EsonInteger(integer);
    }

    @Override public Integer fromEson(EsonInteger eson, EsonSerializer serializer) {
        return eson.value().intValueExact();
    }
}

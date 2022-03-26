package net.auoeke.eson;

import net.auoeke.eson.element.EsonInteger;

final class LongAdapter implements EsonAdapter<Long, EsonInteger> {
    static final LongAdapter instance = new LongAdapter();

    @Override public EsonInteger toEson(Long lon, Eson serializer) {
        return new EsonInteger(lon);
    }

    @Override public Long fromEson(EsonInteger eson, Eson serializer) {
        return eson.value().longValueExact();
    }
}

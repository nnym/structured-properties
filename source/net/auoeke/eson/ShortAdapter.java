package net.auoeke.eson;

import net.auoeke.eson.element.EsonInteger;

final class ShortAdapter implements EsonAdapter<Short, EsonInteger> {
    static final ShortAdapter instance = new ShortAdapter();

    @Override public EsonInteger toEson(Short shor, Eson serializer) {
        return new EsonInteger(shor);
    }

    @Override public Short fromEson(EsonInteger eson, Eson serializer) {
        return eson.value().shortValueExact();
    }
}

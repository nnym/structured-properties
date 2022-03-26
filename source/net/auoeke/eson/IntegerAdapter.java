package net.auoeke.eson;

import net.auoeke.eson.element.EsonInteger;

final class IntegerAdapter implements EsonAdapter<Integer, EsonInteger> {
    static final IntegerAdapter instance = new IntegerAdapter();

    @Override public EsonInteger toEson(Integer integer, Eson serializer) {
        return new EsonInteger(integer);
    }

    @Override public Integer fromEson(EsonInteger eson, Eson serializer) {
        return eson.value().intValueExact();
    }
}

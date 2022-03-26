package net.auoeke.eson;

import net.auoeke.eson.element.EsonBoolean;

final class BooleanAdapter implements EsonAdapter<Boolean, EsonBoolean> {
    static final BooleanAdapter instance = new BooleanAdapter();

    @Override public EsonBoolean toEson(Boolean boolea, Eson serializer) {
        return EsonBoolean.of(boolea);
    }

    @Override public Boolean fromEson(EsonBoolean eson, Eson serializer) {
        return eson.value;
    }
}

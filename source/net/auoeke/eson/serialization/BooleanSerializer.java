package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonBoolean;

final class BooleanSerializer implements EsonTypeSerializer<Boolean, EsonBoolean> {
    static final BooleanSerializer instance = new BooleanSerializer();

    @Override public EsonBoolean toEson(Boolean boolea, EsonSerializer serializer) {
        return EsonBoolean.of(boolea);
    }

    @Override public Boolean fromEson(EsonBoolean eson, EsonSerializer serializer) {
        return eson.value;
    }
}

package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonInteger;

final class ByteSerializer implements EsonTypeSerializer<Byte, EsonInteger> {
    static final ByteSerializer instance = new ByteSerializer();

    @Override public EsonInteger toEson(Byte byt, EsonSerializer serializer) {
        return new EsonInteger(byt);
    }

    @Override public Byte fromEson(EsonInteger eson, EsonSerializer serializer) {
        return eson.value().byteValueExact();
    }
}

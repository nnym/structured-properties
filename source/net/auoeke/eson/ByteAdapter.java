package net.auoeke.eson;

import net.auoeke.eson.element.EsonInteger;

final class ByteAdapter implements EsonAdapter<Byte, EsonInteger> {
    static final ByteAdapter instance = new ByteAdapter();

    @Override public EsonInteger toEson(Byte byt, Eson serializer) {
        return new EsonInteger(byt);
    }

    @Override public Byte fromEson(EsonInteger eson, Eson serializer) {
        return eson.value().byteValueExact();
    }
}

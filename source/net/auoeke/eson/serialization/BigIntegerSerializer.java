package net.auoeke.eson.serialization;

import java.math.BigInteger;
import net.auoeke.eson.element.EsonInteger;

final class BigIntegerSerializer implements EsonTypeSerializer<BigInteger, EsonInteger> {
    static final BigIntegerSerializer instance = new BigIntegerSerializer();

    @Override public EsonInteger toEson(BigInteger integer, EsonSerializer serializer) {
        return new EsonInteger(integer);
    }

    @Override public BigInteger fromEson(EsonInteger eson, EsonSerializer serializer) {
        return eson.value();
    }
}

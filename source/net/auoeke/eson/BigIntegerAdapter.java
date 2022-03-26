package net.auoeke.eson;

import java.math.BigInteger;
import net.auoeke.eson.element.EsonInteger;

final class BigIntegerAdapter implements EsonAdapter<BigInteger, EsonInteger> {
    static final BigIntegerAdapter instance = new BigIntegerAdapter();

    @Override public EsonInteger toEson(BigInteger integer, Eson serializer) {
        return new EsonInteger(integer);
    }

    @Override public BigInteger fromEson(EsonInteger eson, Eson serializer) {
        return eson.value();
    }
}

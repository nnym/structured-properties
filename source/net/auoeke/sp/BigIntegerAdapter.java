package net.auoeke.sp;

import java.math.BigInteger;
import net.auoeke.sp.element.SpInteger;

final class BigIntegerAdapter implements SpAdapter<BigInteger, SpInteger> {
    static final BigIntegerAdapter instance = new BigIntegerAdapter();

    @Override public SpInteger toSp(BigInteger integer, StructuredProperties serializer) {
        return new SpInteger(integer);
    }

    @Override public BigInteger fromSp(SpInteger sp, StructuredProperties serializer) {
        return sp.value();
    }
}

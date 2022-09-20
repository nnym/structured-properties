package net.auoeke.lusr;

import java.math.BigInteger;
import net.auoeke.lusr.element.LusrInteger;

final class BigIntegerAdapter implements LusrAdapter<BigInteger, LusrInteger> {
    static final BigIntegerAdapter instance = new BigIntegerAdapter();

    @Override public LusrInteger toLusr(BigInteger integer, Lusr serializer) {
        return new LusrInteger(integer);
    }

    @Override public BigInteger fromLusr(LusrInteger lusr, Lusr serializer) {
        return lusr.value();
    }
}

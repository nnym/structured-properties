package net.auoeke.csf;

import java.math.BigInteger;
import net.auoeke.csf.element.CsfInteger;

final class BigIntegerAdapter implements CsfAdapter<BigInteger, CsfInteger> {
    static final BigIntegerAdapter instance = new BigIntegerAdapter();

    @Override public CsfInteger toCsf(BigInteger integer, Csf serializer) {
        return new CsfInteger(integer);
    }

    @Override public BigInteger fromCsf(CsfInteger csf, Csf serializer) {
        return csf.value();
    }
}

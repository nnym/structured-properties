package net.auoeke.csf;

import net.auoeke.csf.element.CsfInteger;

final class IntegerAdapter implements CsfAdapter<Integer, CsfInteger> {
    static final IntegerAdapter instance = new IntegerAdapter();

    @Override public CsfInteger toCsf(Integer integer, Csf serializer) {
        return new CsfInteger(integer);
    }

    @Override public Integer fromCsf(CsfInteger csf, Csf serializer) {
        return csf.value().intValueExact();
    }
}

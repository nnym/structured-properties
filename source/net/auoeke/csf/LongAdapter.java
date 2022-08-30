package net.auoeke.csf;

import net.auoeke.csf.element.CsfInteger;

final class LongAdapter implements CsfAdapter<Long, CsfInteger> {
    static final LongAdapter instance = new LongAdapter();

    @Override public CsfInteger toCsf(Long lon, Csf serializer) {
        return new CsfInteger(lon);
    }

    @Override public Long fromCsf(CsfInteger csf, Csf serializer) {
        return csf.value().longValueExact();
    }
}

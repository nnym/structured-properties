package net.auoeke.csf;

import net.auoeke.csf.element.CsfInteger;

final class ShortAdapter implements CsfAdapter<Short, CsfInteger> {
    static final ShortAdapter instance = new ShortAdapter();

    @Override public CsfInteger toCsf(Short shor, Csf serializer) {
        return new CsfInteger(shor);
    }

    @Override public Short fromCsf(CsfInteger csf, Csf serializer) {
        return csf.value().shortValueExact();
    }
}

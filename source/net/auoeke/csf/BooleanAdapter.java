package net.auoeke.csf;

import net.auoeke.csf.element.CsfBoolean;

final class BooleanAdapter implements CsfAdapter<Boolean, CsfBoolean> {
    static final BooleanAdapter instance = new BooleanAdapter();

    @Override public CsfBoolean toCsf(Boolean boolea, Csf serializer) {
        return CsfBoolean.of(boolea);
    }

    @Override public Boolean fromCsf(CsfBoolean csf, Csf serializer) {
        return csf.value;
    }
}

package net.auoeke.csf;

import net.auoeke.csf.element.CsfElement;

final class CsfElementAdapter implements CsfAdapter<CsfElement, CsfElement> {
    static final CsfElementAdapter instance = new CsfElementAdapter();

    @Override public CsfElement toCsf(CsfElement csf, Csf serializer) {
        return csf;
    }

    @Override public CsfElement fromCsf(CsfElement csf, Csf serializer) {
        return csf;
    }
}

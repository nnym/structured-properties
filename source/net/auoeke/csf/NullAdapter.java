package net.auoeke.csf;

import net.auoeke.csf.element.CsfNull;

final class NullAdapter implements CsfAdapter<Object, CsfNull> {
    static final NullAdapter instance = new NullAdapter();
    
    @Override public CsfNull toCsf(Object nul, Csf serializer) {
        return CsfNull.instance;
    }

    @Override public Object fromCsf(CsfNull csf, Csf serializer) {
        return null;
    }
}

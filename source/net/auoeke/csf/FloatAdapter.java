package net.auoeke.csf;

import net.auoeke.csf.element.CsfFloat;

final class FloatAdapter implements CsfAdapter<Float, CsfFloat> {
    static final FloatAdapter instance = new FloatAdapter();

    @Override public CsfFloat toCsf(Float floa, Csf serializer) {
        return new CsfFloat(floa);
    }

    @Override public Float fromCsf(CsfFloat csf, Csf serializer) {
        return csf.value().floatValue();
    }
}

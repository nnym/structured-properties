package net.auoeke.csf;

import net.auoeke.csf.element.CsfFloat;

final class DoubleAdapter implements CsfAdapter<Double, CsfFloat> {
    static final DoubleAdapter instance = new DoubleAdapter();

    @Override public CsfFloat toCsf(Double integer, Csf serializer) {
        return new CsfFloat(integer);
    }

    @Override public Double fromCsf(CsfFloat csf, Csf serializer) {
        return csf.value().doubleValue();
    }
}

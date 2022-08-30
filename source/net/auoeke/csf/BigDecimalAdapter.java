package net.auoeke.csf;

import java.math.BigDecimal;
import net.auoeke.csf.element.CsfFloat;

final class BigDecimalAdapter implements CsfAdapter<BigDecimal, CsfFloat> {
    static final BigDecimalAdapter instance = new BigDecimalAdapter();

    @Override public CsfFloat toCsf(BigDecimal integer, Csf serializer) {
        return new CsfFloat(integer);
    }

    @Override public BigDecimal fromCsf(CsfFloat csf, Csf serializer) {
        return csf.value();
    }
}

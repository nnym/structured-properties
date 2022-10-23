package net.auoeke.sp;

import java.math.BigDecimal;
import net.auoeke.sp.element.SpFloat;

final class BigDecimalAdapter implements SpAdapter<BigDecimal, SpFloat> {
    static final BigDecimalAdapter instance = new BigDecimalAdapter();

    @Override public SpFloat toSp(BigDecimal integer, StructuredProperties serializer) {
        return new SpFloat(integer);
    }

    @Override public BigDecimal fromSp(SpFloat sp, StructuredProperties serializer) {
        return sp.value();
    }
}

package net.auoeke.lusr;

import java.math.BigDecimal;
import net.auoeke.lusr.element.LusrFloat;

final class BigDecimalAdapter implements LusrAdapter<BigDecimal, LusrFloat> {
    static final BigDecimalAdapter instance = new BigDecimalAdapter();

    @Override public LusrFloat toLusr(BigDecimal integer, Lusr serializer) {
        return new LusrFloat(integer);
    }

    @Override public BigDecimal fromLusr(LusrFloat lusr, Lusr serializer) {
        return lusr.value();
    }
}

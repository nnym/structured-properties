package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrFloat;

final class DoubleAdapter implements LusrAdapter<Double, LusrFloat> {
    static final DoubleAdapter instance = new DoubleAdapter();

    @Override public LusrFloat toLusr(Double integer, Lusr serializer) {
        return new LusrFloat(integer);
    }

    @Override public Double fromLusr(LusrFloat lusr, Lusr serializer) {
        return lusr.value().doubleValue();
    }
}

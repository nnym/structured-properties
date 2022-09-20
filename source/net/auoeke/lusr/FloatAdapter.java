package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrFloat;

final class FloatAdapter implements LusrAdapter<Float, LusrFloat> {
    static final FloatAdapter instance = new FloatAdapter();

    @Override public LusrFloat toLusr(Float floa, Lusr serializer) {
        return new LusrFloat(floa);
    }

    @Override public Float fromLusr(LusrFloat lusr, Lusr serializer) {
        return lusr.value().floatValue();
    }
}

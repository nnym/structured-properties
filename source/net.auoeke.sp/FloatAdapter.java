package net.auoeke.sp;

import net.auoeke.sp.element.SpFloat;

final class FloatAdapter implements SpAdapter<Float, SpFloat> {
    static final FloatAdapter instance = new FloatAdapter();

    @Override public SpFloat toSp(Float floa, StructuredProperties serializer) {
        return new SpFloat(floa);
    }

    @Override public Float fromSp(SpFloat sp, StructuredProperties serializer) {
        return sp.value().floatValue();
    }
}

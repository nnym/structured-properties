package net.auoeke.sp;

import net.auoeke.sp.element.SpInteger;

final class ShortAdapter implements SpAdapter<Short, SpInteger> {
    static final ShortAdapter instance = new ShortAdapter();

    @Override public SpInteger toSp(Short shor, StructuredProperties serializer) {
        return new SpInteger(shor);
    }

    @Override public Short fromSp(SpInteger sp, StructuredProperties serializer) {
        return sp.value().shortValueExact();
    }
}

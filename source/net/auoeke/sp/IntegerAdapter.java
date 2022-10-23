package net.auoeke.sp;

import net.auoeke.sp.element.SpInteger;

final class IntegerAdapter implements SpAdapter<Integer, SpInteger> {
    static final IntegerAdapter instance = new IntegerAdapter();

    @Override public SpInteger toSp(Integer integer, StructuredProperties serializer) {
        return new SpInteger(integer);
    }

    @Override public Integer fromSp(SpInteger sp, StructuredProperties serializer) {
        return sp.value().intValueExact();
    }
}

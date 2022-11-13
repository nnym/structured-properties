package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpInteger;

public final class IntegerAdapter implements SpAdapter<Integer, SpInteger> {
	public static final IntegerAdapter instance = new IntegerAdapter();

	@Override public SpInteger toSp(Integer integer, StructuredProperties serializer) {
		return new SpInteger(integer);
	}

	@Override public Integer fromSp(SpInteger sp, StructuredProperties serializer) {
		return sp.value().intValueExact();
	}
}

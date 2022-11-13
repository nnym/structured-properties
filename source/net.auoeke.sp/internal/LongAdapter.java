package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpInteger;

public final class LongAdapter implements SpAdapter<Long, SpInteger> {
	public static final LongAdapter instance = new LongAdapter();

	@Override public SpInteger toSp(Long lon, StructuredProperties serializer) {
		return new SpInteger(lon);
	}

	@Override public Long fromSp(SpInteger sp, StructuredProperties serializer) {
		return sp.value().longValueExact();
	}
}

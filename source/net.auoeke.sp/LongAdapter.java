package net.auoeke.sp;

import net.auoeke.sp.element.SpInteger;

final class LongAdapter implements SpAdapter<Long, SpInteger> {
	static final LongAdapter instance = new LongAdapter();

	@Override public SpInteger toSp(Long lon, StructuredProperties serializer) {
		return new SpInteger(lon);
	}

	@Override public Long fromSp(SpInteger sp, StructuredProperties serializer) {
		return sp.value().longValueExact();
	}
}

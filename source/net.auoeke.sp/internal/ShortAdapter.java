package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpInteger;

public final class ShortAdapter implements SpAdapter<Short, SpInteger> {
	public static final ShortAdapter instance = new ShortAdapter();

	@Override public SpInteger toSp(Short shor, StructuredProperties serializer) {
		return new SpInteger(shor);
	}

	@Override public Short fromSp(SpInteger sp, StructuredProperties serializer) {
		return sp.value().shortValueExact();
	}
}

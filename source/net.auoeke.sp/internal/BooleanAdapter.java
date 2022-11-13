package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpBoolean;

public final class BooleanAdapter implements SpAdapter<Boolean, SpBoolean> {
	public static final BooleanAdapter instance = new BooleanAdapter();

	@Override public SpBoolean toSp(Boolean boolea, StructuredProperties serializer) {
		return SpBoolean.of(boolea);
	}

	@Override public Boolean fromSp(SpBoolean sp, StructuredProperties serializer) {
		return sp.value;
	}
}

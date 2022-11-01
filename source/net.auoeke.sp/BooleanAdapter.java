package net.auoeke.sp;

import net.auoeke.sp.element.SpBoolean;

final class BooleanAdapter implements SpAdapter<Boolean, SpBoolean> {
	static final BooleanAdapter instance = new BooleanAdapter();

	@Override public SpBoolean toSp(Boolean boolea, StructuredProperties serializer) {
		return SpBoolean.of(boolea);
	}

	@Override public Boolean fromSp(SpBoolean sp, StructuredProperties serializer) {
		return sp.value;
	}
}

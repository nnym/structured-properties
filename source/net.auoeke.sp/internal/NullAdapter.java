package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpNull;

final class NullAdapter implements SpAdapter<Object, SpNull> {
	static final NullAdapter instance = new NullAdapter();

	@Override public SpNull toSp(Object nul, StructuredProperties serializer) {
		return SpNull.instance;
	}

	@Override public Object fromSp(SpNull sp, StructuredProperties serializer) {
		return null;
	}
}

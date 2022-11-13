package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpFloat;

public final class FloatAdapter implements SpAdapter<Float, SpFloat> {
	public static final FloatAdapter instance = new FloatAdapter();

	@Override public SpFloat toSp(Float floa, StructuredProperties serializer) {
		return new SpFloat(floa);
	}

	@Override public Float fromSp(SpFloat sp, StructuredProperties serializer) {
		return sp.value().floatValue();
	}
}

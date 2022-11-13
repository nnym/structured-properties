package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpFloat;

public final class DoubleAdapter implements SpAdapter<Double, SpFloat> {
	public static final DoubleAdapter instance = new DoubleAdapter();

	@Override public SpFloat toSp(Double integer, StructuredProperties serializer) {
		return new SpFloat(integer);
	}

	@Override public Double fromSp(SpFloat sp, StructuredProperties serializer) {
		return sp.value().doubleValue();
	}
}

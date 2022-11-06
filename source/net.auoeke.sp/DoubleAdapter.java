package net.auoeke.sp;

import net.auoeke.sp.element.SpFloat;

final class DoubleAdapter implements SpAdapter<Double, SpFloat> {
	static final DoubleAdapter instance = new DoubleAdapter();

	@Override public SpFloat toSp(Double integer, StructuredProperties serializer) {
		return new SpFloat(integer);
	}

	@Override public Double fromSp(SpFloat sp, StructuredProperties serializer) {
		return sp.value().doubleValue();
	}
}

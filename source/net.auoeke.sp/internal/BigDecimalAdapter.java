package net.auoeke.sp.internal;

import java.math.BigDecimal;
import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpFloat;

public final class BigDecimalAdapter implements SpAdapter<BigDecimal, SpFloat> {
	public static final BigDecimalAdapter instance = new BigDecimalAdapter();

	@Override public SpFloat toSp(BigDecimal integer, StructuredProperties serializer) {
		return new SpFloat(integer);
	}

	@Override public BigDecimal fromSp(SpFloat sp, StructuredProperties serializer) {
		return sp.value();
	}
}

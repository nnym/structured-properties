package net.auoeke.sp.internal;

import java.math.BigInteger;
import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpInteger;

public final class BigIntegerAdapter implements SpAdapter<BigInteger, SpInteger> {
	public static final BigIntegerAdapter instance = new BigIntegerAdapter();

	@Override public SpInteger toSp(BigInteger integer, StructuredProperties serializer) {
		return new SpInteger(integer);
	}

	@Override public BigInteger fromSp(SpInteger sp, StructuredProperties serializer) {
		return sp.value();
	}
}

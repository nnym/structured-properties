package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpInteger;

public final class ByteAdapter implements SpAdapter<Byte, SpInteger> {
	public static final ByteAdapter instance = new ByteAdapter();

	@Override public SpInteger toSp(Byte byt, StructuredProperties serializer) {
		return new SpInteger(byt);
	}

	@Override public Byte fromSp(SpInteger sp, StructuredProperties serializer) {
		return sp.value().byteValueExact();
	}
}

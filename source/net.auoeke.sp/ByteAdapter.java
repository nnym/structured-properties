package net.auoeke.sp;

import net.auoeke.sp.element.SpInteger;

final class ByteAdapter implements SpAdapter<Byte, SpInteger> {
	static final ByteAdapter instance = new ByteAdapter();

	@Override public SpInteger toSp(Byte byt, StructuredProperties serializer) {
		return new SpInteger(byt);
	}

	@Override public Byte fromSp(SpInteger sp, StructuredProperties serializer) {
		return sp.value().byteValueExact();
	}
}

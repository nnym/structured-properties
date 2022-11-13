package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpElement;

public final class SpElementAdapter implements SpAdapter<SpElement, SpElement> {
	public static final SpElementAdapter instance = new SpElementAdapter();

	@Override public SpElement toSp(SpElement sp, StructuredProperties serializer) {
		return sp;
	}

	@Override public SpElement fromSp(SpElement sp, StructuredProperties serializer) {
		return sp;
	}
}

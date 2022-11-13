package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpString;

public final class CharSequenceAdapter implements SpAdapter<CharSequence, SpString> {
	public static final CharSequenceAdapter instance = new CharSequenceAdapter();

	@Override public SpString toSp(CharSequence charSequence, StructuredProperties serializer) {
		return new SpString(charSequence);
	}

	@Override public CharSequence fromSp(SpString sp, StructuredProperties serializer) {
		return sp.value;
	}
}

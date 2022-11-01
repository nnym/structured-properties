package net.auoeke.sp;

import net.auoeke.sp.element.SpString;

final class CharSequenceAdapter implements SpAdapter<CharSequence, SpString> {
	static final CharSequenceAdapter instance = new CharSequenceAdapter();

	@Override public SpString toSp(CharSequence charSequence, StructuredProperties serializer) {
		return new SpString(charSequence);
	}

	@Override public CharSequence fromSp(SpString sp, StructuredProperties serializer) {
		return sp.value;
	}
}

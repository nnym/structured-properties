package net.auoeke.sp.internal;

import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpString;

public final class CharacterAdapter implements SpAdapter<Character, SpString> {
	public static final CharacterAdapter instance = new CharacterAdapter();

	@Override public SpString toSp(Character character, StructuredProperties serializer) {
		return new SpString(String.valueOf(character));
	}

	@Override public Character fromSp(SpString string, StructuredProperties serializer) {
		if (string.value.length() != 1) {
			throw new ClassCastException("string \"%s\" is not convertible to char".formatted(string));
		}

		return string.value.charAt(0);
	}
}

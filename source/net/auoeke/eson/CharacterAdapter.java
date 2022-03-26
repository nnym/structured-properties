package net.auoeke.eson;

import net.auoeke.eson.element.EsonString;

final class CharacterAdapter implements EsonAdapter<Character, EsonString> {
    static final CharacterAdapter instance = new CharacterAdapter();

    @Override public EsonString toEson(Character character, Eson serializer) {
        return new EsonString(String.valueOf(character));
    }

    @Override public Character fromEson(EsonString string, Eson serializer) {
        if (string.value.length() != 1) {
            throw new ClassCastException("string \"%s\" is not convertible to char".formatted(string));
        }

        return string.value.charAt(0);
    }
}

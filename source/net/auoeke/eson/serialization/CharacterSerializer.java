package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonString;

final class CharacterSerializer implements EsonTypeSerializer<Character, EsonString> {
    static final CharacterSerializer instance = new CharacterSerializer();

    @Override public EsonString toEson(Character character, EsonSerializer serializer) {
        return new EsonString(String.valueOf(character));
    }

    @Override public Character fromEson(EsonString string, EsonSerializer serializer) {
        if (string.value.length() != 1) {
            throw new ClassCastException("string \"%s\" is not convertible to char".formatted(string));
        }

        return string.value.charAt(0);
    }
}

package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrString;

final class CharacterAdapter implements LusrAdapter<Character, LusrString> {
    static final CharacterAdapter instance = new CharacterAdapter();

    @Override public LusrString toLusr(Character character, Lusr serializer) {
        return new LusrString(String.valueOf(character));
    }

    @Override public Character fromLusr(LusrString string, Lusr serializer) {
        if (string.value.length() != 1) {
            throw new ClassCastException("string \"%s\" is not convertible to char".formatted(string));
        }

        return string.value.charAt(0);
    }
}

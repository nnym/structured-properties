package net.auoeke.csf;

import net.auoeke.csf.element.CsfString;

final class CharacterAdapter implements CsfAdapter<Character, CsfString> {
    static final CharacterAdapter instance = new CharacterAdapter();

    @Override public CsfString toCsf(Character character, Csf serializer) {
        return new CsfString(String.valueOf(character));
    }

    @Override public Character fromCsf(CsfString string, Csf serializer) {
        if (string.value.length() != 1) {
            throw new ClassCastException("string \"%s\" is not convertible to char".formatted(string));
        }

        return string.value.charAt(0);
    }
}

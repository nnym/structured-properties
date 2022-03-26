package net.auoeke.eson;

import net.auoeke.eson.element.EsonString;

final class CharSequenceAdapter implements EsonAdapter<CharSequence, EsonString> {
    static final CharSequenceAdapter instance = new CharSequenceAdapter();

    @Override public EsonString toEson(CharSequence charSequence, Eson serializer) {
        return new EsonString(charSequence);
    }

    @Override public CharSequence fromEson(EsonString eson, Eson serializer) {
        return eson.value;
    }
}

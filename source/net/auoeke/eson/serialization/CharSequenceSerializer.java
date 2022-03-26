package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonString;

final class CharSequenceSerializer implements EsonTypeSerializer<CharSequence, EsonString> {
    static final CharSequenceSerializer instance = new CharSequenceSerializer();

    @Override public EsonString toEson(CharSequence charSequence, EsonSerializer serializer) {
        return new EsonString(charSequence);
    }

    @Override public CharSequence fromEson(EsonString eson, EsonSerializer serializer) {
        return eson.value;
    }
}

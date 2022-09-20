package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrString;

final class CharSequenceAdapter implements LusrAdapter<CharSequence, LusrString> {
    static final CharSequenceAdapter instance = new CharSequenceAdapter();

    @Override public LusrString toLusr(CharSequence charSequence, Lusr serializer) {
        return new LusrString(charSequence);
    }

    @Override public CharSequence fromLusr(LusrString lusr, Lusr serializer) {
        return lusr.value;
    }
}

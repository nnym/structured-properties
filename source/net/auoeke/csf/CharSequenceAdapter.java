package net.auoeke.csf;

import net.auoeke.csf.element.CsfString;

final class CharSequenceAdapter implements CsfAdapter<CharSequence, CsfString> {
    static final CharSequenceAdapter instance = new CharSequenceAdapter();

    @Override public CsfString toCsf(CharSequence charSequence, Csf serializer) {
        return new CsfString(charSequence);
    }

    @Override public CharSequence fromCsf(CsfString csf, Csf serializer) {
        return csf.value;
    }
}

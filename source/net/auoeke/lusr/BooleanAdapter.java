package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrBoolean;

final class BooleanAdapter implements LusrAdapter<Boolean, LusrBoolean> {
    static final BooleanAdapter instance = new BooleanAdapter();

    @Override public LusrBoolean toLusr(Boolean boolea, Lusr serializer) {
        return LusrBoolean.of(boolea);
    }

    @Override public Boolean fromLusr(LusrBoolean lusr, Lusr serializer) {
        return lusr.value;
    }
}

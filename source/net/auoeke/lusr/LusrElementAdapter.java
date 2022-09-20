package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrElement;

final class LusrElementAdapter implements LusrAdapter<LusrElement, LusrElement> {
    static final LusrElementAdapter instance = new LusrElementAdapter();

    @Override public LusrElement toLusr(LusrElement lusr, Lusr serializer) {
        return lusr;
    }

    @Override public LusrElement fromLusr(LusrElement lusr, Lusr serializer) {
        return lusr;
    }
}

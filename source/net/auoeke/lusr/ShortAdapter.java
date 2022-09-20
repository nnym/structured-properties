package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrInteger;

final class ShortAdapter implements LusrAdapter<Short, LusrInteger> {
    static final ShortAdapter instance = new ShortAdapter();

    @Override public LusrInteger toLusr(Short shor, Lusr serializer) {
        return new LusrInteger(shor);
    }

    @Override public Short fromLusr(LusrInteger lusr, Lusr serializer) {
        return lusr.value().shortValueExact();
    }
}

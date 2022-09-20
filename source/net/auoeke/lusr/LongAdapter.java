package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrInteger;

final class LongAdapter implements LusrAdapter<Long, LusrInteger> {
    static final LongAdapter instance = new LongAdapter();

    @Override public LusrInteger toLusr(Long lon, Lusr serializer) {
        return new LusrInteger(lon);
    }

    @Override public Long fromLusr(LusrInteger lusr, Lusr serializer) {
        return lusr.value().longValueExact();
    }
}

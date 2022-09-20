package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrInteger;

final class IntegerAdapter implements LusrAdapter<Integer, LusrInteger> {
    static final IntegerAdapter instance = new IntegerAdapter();

    @Override public LusrInteger toLusr(Integer integer, Lusr serializer) {
        return new LusrInteger(integer);
    }

    @Override public Integer fromLusr(LusrInteger lusr, Lusr serializer) {
        return lusr.value().intValueExact();
    }
}

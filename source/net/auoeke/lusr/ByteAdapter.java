package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrInteger;

final class ByteAdapter implements LusrAdapter<Byte, LusrInteger> {
    static final ByteAdapter instance = new ByteAdapter();

    @Override public LusrInteger toLusr(Byte byt, Lusr serializer) {
        return new LusrInteger(byt);
    }

    @Override public Byte fromLusr(LusrInteger lusr, Lusr serializer) {
        return lusr.value().byteValueExact();
    }
}

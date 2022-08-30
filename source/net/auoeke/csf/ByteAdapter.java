package net.auoeke.csf;

import net.auoeke.csf.element.CsfInteger;

final class ByteAdapter implements CsfAdapter<Byte, CsfInteger> {
    static final ByteAdapter instance = new ByteAdapter();

    @Override public CsfInteger toCsf(Byte byt, Csf serializer) {
        return new CsfInteger(byt);
    }

    @Override public Byte fromCsf(CsfInteger csf, Csf serializer) {
        return csf.value().byteValueExact();
    }
}

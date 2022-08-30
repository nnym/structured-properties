package net.auoeke.csf;

import net.auoeke.csf.element.CsfString;
import net.auoeke.reflect.Classes;

final class EnumAdapter implements PolymorphicCsfAdapter<Enum<?>, CsfString> {
    static final EnumAdapter instance = new EnumAdapter();

    @Override public boolean accept(Class<?> type) {
        return type.isEnum();
    }

    @Override public CsfString toCsf(Enum<?> enu, Csf serializer) {
        return new CsfString(enu.name());
    }

    @Override public Enum<?> fromCsf(Class<Enum<?>> type, CsfString csf, Csf serializer) {
        return Enum.valueOf(Classes.cast(type), csf.value);
    }
}

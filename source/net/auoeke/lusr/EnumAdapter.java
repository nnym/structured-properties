package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrString;
import net.auoeke.reflect.Classes;

final class EnumAdapter implements PolymorphicLusrAdapter<Enum<?>, LusrString> {
    static final EnumAdapter instance = new EnumAdapter();

    @Override public boolean accept(Class<?> type) {
        return type.isEnum();
    }

    @Override public LusrString toLusr(Enum<?> enu, Lusr serializer) {
        return new LusrString(enu.name());
    }

    @Override public Enum<?> fromLusr(Class<Enum<?>> type, LusrString lusr, Lusr serializer) {
        return Enum.valueOf(Classes.cast(type), lusr.value);
    }
}

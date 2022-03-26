package net.auoeke.eson;

import net.auoeke.eson.element.EsonString;
import net.auoeke.reflect.Classes;

final class EnumAdapter implements PolymorphicEsonAdapter<Enum<?>, EsonString> {
    static final EnumAdapter instance = new EnumAdapter();

    @Override public boolean accept(Class<?> type) {
        return type.isEnum();
    }

    @Override public EsonString toEson(Enum<?> enu, Eson serializer) {
        return new EsonString(enu.name());
    }

    @Override public Enum<?> fromEson(Class<Enum<?>> type, EsonString eson, Eson serializer) {
        return Enum.valueOf(Classes.cast(type), eson.value);
    }
}

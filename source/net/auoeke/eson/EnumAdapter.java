package net.auoeke.eson;

import net.auoeke.eson.element.EsonString;
import net.auoeke.reflect.Classes;

final class EnumAdapter implements EsonAdapter<Enum<?>, EsonString>{
    private final Class<?> type;

    EnumAdapter(Class<?> type) {
        this.type = type;
    }

    @Override public EsonString toEson(Enum<?> enu, Eson serializer) {
        return new EsonString(enu.name());
    }

    @Override public Enum<?> fromEson(EsonString eson, Eson serializer) {
        return Enum.valueOf(Classes.cast(this.type), eson.value);
    }
}

package net.auoeke.lusr;

import java.util.Objects;
import net.auoeke.lusr.element.LusrMap;
import net.auoeke.reflect.Accessor;
import net.auoeke.reflect.Constructors;
import net.auoeke.reflect.Fields;
import net.auoeke.reflect.Flags;

final class ObjectAdapter implements PolymorphicLusrAdapter<Object, LusrMap> {
    static final ObjectAdapter instance = new ObjectAdapter();

    @Override public boolean accept(Class<?> type) {
        return !type.isPrimitive();
    }

    @Override public Object fromLusr(Class<Object> type, LusrMap lusr, Lusr serializer) {
        var object = Constructors.instantiate(type);
        lusr.forEach((key, value) -> {
            var field = Objects.requireNonNull(Fields.of(object, key), () -> "%s does not have a field named \"%s\"".formatted(type, key));
            Accessor.put(object, field, serializer.fromLusr(field.getType(), value));
        });

        return object;
    }

    @Override public LusrMap toLusr(Object object, Lusr serializer) {
        var map = new LusrMap();
        Fields.all(object)
            .filter(field -> !Flags.any(field, Flags.STATIC | Flags.SYNTHETIC | Flags.TRANSIENT))
            .forEach(field -> map.put(field.getName(), serializer.toLusr(Accessor.get(object, field))));

        return map;
    }
}

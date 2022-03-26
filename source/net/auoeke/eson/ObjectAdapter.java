package net.auoeke.eson;

import java.util.Objects;
import net.auoeke.eson.element.EsonMap;
import net.auoeke.reflect.Accessor;
import net.auoeke.reflect.Constructors;
import net.auoeke.reflect.Fields;
import net.auoeke.reflect.Flags;

final class ObjectAdapter implements PolymorphicEsonAdapter<Object, EsonMap> {
    static final ObjectAdapter instance = new ObjectAdapter();

    @Override public boolean accept(Class<?> type) {
        return !type.isPrimitive();
    }

    @Override public Object fromEson(Class<Object> type, EsonMap eson, Eson serializer) {
        var object = Constructors.instantiate(type);
        eson.forEach((key, value) -> {
            var field = Objects.requireNonNull(Fields.of(object, key), () -> "%s does not have a field named \"%s\"".formatted(type, key));
            Accessor.put(object, field, serializer.fromEson(field.getType(), value));
        });

        return object;
    }

    @Override public EsonMap toEson(Object object, Eson serializer) {
        var map = new EsonMap();
        Fields.all(object)
            .filter(field -> !Flags.any(field, Flags.STATIC | Flags.SYNTHETIC | Flags.TRANSIENT))
            .forEach(field -> map.put(field.getName(), serializer.toEson(Accessor.get(object, field))));

        return map;
    }
}

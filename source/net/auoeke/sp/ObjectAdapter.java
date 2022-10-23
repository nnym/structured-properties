package net.auoeke.sp;

import java.util.Objects;
import net.auoeke.sp.element.SpMap;
import net.auoeke.reflect.Accessor;
import net.auoeke.reflect.Constructors;
import net.auoeke.reflect.Fields;
import net.auoeke.reflect.Flags;

final class ObjectAdapter implements PolymorphicSpAdapter<Object, SpMap> {
    static final ObjectAdapter instance = new ObjectAdapter();

    @Override public boolean accept(Class<?> type) {
        return !type.isPrimitive();
    }

    @Override public Object fromSp(Class<Object> type, SpMap sp, StructuredProperties serializer) {
        var object = Constructors.instantiate(type);
        sp.forEach((key, value) -> {
            var field = Objects.requireNonNull(Fields.of(object, key), () -> "%s does not have a field named \"%s\"".formatted(type, key));
            Accessor.put(object, field, serializer.fromSp(field.getType(), value));
        });

        return object;
    }

    @Override public SpMap toSp(Object object, StructuredProperties serializer) {
        var map = new SpMap();
        Fields.all(object)
            .filter(field -> !Flags.any(field, Flags.STATIC | Flags.SYNTHETIC | Flags.TRANSIENT))
            .forEach(field -> map.put(field.getName(), serializer.toSp(Accessor.get(object, field))));

        return map;
    }
}

package net.auoeke.csf;

import java.util.Objects;
import net.auoeke.csf.element.CsfMap;
import net.auoeke.reflect.Accessor;
import net.auoeke.reflect.Constructors;
import net.auoeke.reflect.Fields;
import net.auoeke.reflect.Flags;

final class ObjectAdapter implements PolymorphicCsfAdapter<Object, CsfMap> {
    static final ObjectAdapter instance = new ObjectAdapter();

    @Override public boolean accept(Class<?> type) {
        return !type.isPrimitive();
    }

    @Override public Object fromCsf(Class<Object> type, CsfMap csf, Csf serializer) {
        var object = Constructors.instantiate(type);
        csf.forEach((key, value) -> {
            var field = Objects.requireNonNull(Fields.of(object, key), () -> "%s does not have a field named \"%s\"".formatted(type, key));
            Accessor.put(object, field, serializer.fromCsf(field.getType(), value));
        });

        return object;
    }

    @Override public CsfMap toCsf(Object object, Csf serializer) {
        var map = new CsfMap();
        Fields.all(object)
            .filter(field -> !Flags.any(field, Flags.STATIC | Flags.SYNTHETIC | Flags.TRANSIENT))
            .forEach(field -> map.put(field.getName(), serializer.toCsf(Accessor.get(object, field))));

        return map;
    }
}

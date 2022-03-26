package net.auoeke.eson;

import java.lang.reflect.Array;
import java.util.Arrays;
import net.auoeke.eson.element.EsonArray;
import net.auoeke.reflect.Types;

final class ArrayAdapter implements PolymorphicEsonAdapter<Object, EsonArray> {
    static final ArrayAdapter instance = new ArrayAdapter();

    @Override public boolean accept(Class<?> type) {
        return type.isArray();
    }

    @Override public EsonArray toEson(Object array, Eson serializer) {
        return (EsonArray) serializer.toEson(Arrays.asList(Types.box(array)));
    }

    @Override public Object fromEson(Class<Object> type, EsonArray eson, Eson serializer) {
        var componentType = type.componentType();

        return Types.convert(
            eson.stream()
                .map(element -> serializer.fromEson(componentType, element))
                .toArray(componentType.isPrimitive() ? Object[]::new : length -> (Object[]) Array.newInstance(componentType, length)),
            componentType
        );
    }
}

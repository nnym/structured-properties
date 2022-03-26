package net.auoeke.eson;

import java.lang.reflect.Array;
import java.util.Arrays;
import net.auoeke.eson.element.EsonArray;
import net.auoeke.reflect.Types;

final class ArrayAdapter implements EsonAdapter<Object, EsonArray> {
    private final Class<?> componentType;

    ArrayAdapter(Class<?> componentType) {
        this.componentType = componentType;
    }

    @Override public EsonArray toEson(Object array, Eson serializer) {
        return (EsonArray) serializer.toEson(Arrays.asList(Types.box(array)));
    }

    @Override public Object fromEson(EsonArray eson, Eson serializer) {
        return Types.convert(
            eson.stream()
                .map(element -> serializer.fromEson(this.componentType, element))
                .toArray(this.componentType.isPrimitive() ? Object[]::new : length -> (Object[]) Array.newInstance(this.componentType, length)),
            this.componentType
        );
    }
}

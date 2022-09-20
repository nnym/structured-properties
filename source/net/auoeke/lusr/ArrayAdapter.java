package net.auoeke.lusr;

import java.lang.reflect.Array;
import java.util.Arrays;
import net.auoeke.lusr.element.LusrArray;
import net.auoeke.reflect.Types;

final class ArrayAdapter implements PolymorphicLusrAdapter<Object, LusrArray> {
    static final ArrayAdapter instance = new ArrayAdapter();

    @Override public boolean accept(Class<?> type) {
        return type.isArray();
    }

    @Override public LusrArray toLusr(Object array, Lusr serializer) {
        return (LusrArray) serializer.toLusr(Arrays.asList(Types.box(array)));
    }

    @Override public Object fromLusr(Class<Object> type, LusrArray lusr, Lusr serializer) {
        var componentType = type.componentType();

        return Types.convert(
            lusr.stream()
                .map(element -> serializer.fromLusr(componentType, element))
                .toArray(componentType.isPrimitive() ? Object[]::new : length -> (Object[]) Array.newInstance(componentType, length)),
            componentType
        );
    }
}

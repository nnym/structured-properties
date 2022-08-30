package net.auoeke.csf;

import java.lang.reflect.Array;
import java.util.Arrays;
import net.auoeke.csf.element.CsfArray;
import net.auoeke.reflect.Types;

final class ArrayAdapter implements PolymorphicCsfAdapter<Object, CsfArray> {
    static final ArrayAdapter instance = new ArrayAdapter();

    @Override public boolean accept(Class<?> type) {
        return type.isArray();
    }

    @Override public CsfArray toCsf(Object array, Csf serializer) {
        return (CsfArray) serializer.toCsf(Arrays.asList(Types.box(array)));
    }

    @Override public Object fromCsf(Class<Object> type, CsfArray csf, Csf serializer) {
        var componentType = type.componentType();

        return Types.convert(
            csf.stream()
                .map(element -> serializer.fromCsf(componentType, element))
                .toArray(componentType.isPrimitive() ? Object[]::new : length -> (Object[]) Array.newInstance(componentType, length)),
            componentType
        );
    }
}

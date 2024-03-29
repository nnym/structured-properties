package net.auoeke.sp.internal;

import java.lang.reflect.Array;
import java.util.Arrays;
import net.auoeke.reflect.Types;
import net.auoeke.sp.PolymorphicSpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpArray;

public final class ArrayAdapter implements PolymorphicSpAdapter<Object, SpArray> {
	public static final ArrayAdapter instance = new ArrayAdapter();

	@Override public boolean accept(Class<?> type) {
		return type.isArray();
	}

	@Override public SpArray toSp(Object array, StructuredProperties serializer) {
		return (SpArray) serializer.toSp(Arrays.asList(Types.box(array)));
	}

	@Override public Object fromSp(Class<Object> type, SpArray sp, StructuredProperties serializer) {
		var componentType = type.componentType();

		return Types.convert(
			sp.stream()
				.map(element -> serializer.fromSp(componentType, element))
				.toArray(componentType.isPrimitive() ? Object[]::new : length -> (Object[]) Array.newInstance(componentType, length)),
			componentType
		);
	}
}

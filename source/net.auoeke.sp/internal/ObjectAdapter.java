package net.auoeke.sp.internal;

import java.util.Objects;
import net.auoeke.reflect.Accessor;
import net.auoeke.reflect.Constructors;
import net.auoeke.reflect.Fields;
import net.auoeke.reflect.Flags;
import net.auoeke.sp.PolymorphicSpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpMap;

public final class ObjectAdapter implements PolymorphicSpAdapter<Object, SpMap> {
	public static final ObjectAdapter instance = new ObjectAdapter();

	@Override public boolean accept(Class<?> type) {
		return !type.isPrimitive();
	}

	@Override public Object fromSp(Class<Object> type, SpMap sp, StructuredProperties serializer) {
		var object = Constructors.instantiate(type);
		sp.forEach((key, value) -> {
			var field = Objects.requireNonNull(Fields.of(object, key), () -> "%s does not have a field named \"%s\"".formatted(type, key));
			var fieldValue = Accessor.get(object, field);
			Accessor.put(object, field, serializer.fromSp((Class<?>) (fieldValue == null ? field.getType() : fieldValue.getClass()), value));
		});

		return object;
	}

	@Override public SpMap toSp(Object object, StructuredProperties serializer) {
		var map = new SpMap();
		Fields.allInstance(object.getClass())
			.filter(field -> !Flags.any(field, Flags.STATIC | Flags.SYNTHETIC | Flags.TRANSIENT))
			.forEach(field -> map.put(field.getName(), serializer.toSp(Accessor.get(object, field))));

		return map;
	}
}

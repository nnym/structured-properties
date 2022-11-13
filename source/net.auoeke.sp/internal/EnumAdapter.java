package net.auoeke.sp.internal;

import net.auoeke.reflect.Classes;
import net.auoeke.sp.PolymorphicSpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpString;

public final class EnumAdapter implements PolymorphicSpAdapter<Enum<?>, SpString> {
	public static final EnumAdapter instance = new EnumAdapter();

	@Override public boolean accept(Class<?> type) {
		return type.isEnum();
	}

	@Override public SpString toSp(Enum<?> enu, StructuredProperties serializer) {
		return new SpString(enu.name());
	}

	@Override public Enum<?> fromSp(Class<Enum<?>> type, SpString sp, StructuredProperties serializer) {
		return Enum.valueOf(Classes.cast(type), sp.value);
	}
}

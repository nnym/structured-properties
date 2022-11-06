package net.auoeke.sp;

import net.auoeke.reflect.Classes;
import net.auoeke.sp.element.SpString;

final class EnumAdapter implements PolymorphicSpAdapter<Enum<?>, SpString> {
	static final EnumAdapter instance = new EnumAdapter();

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

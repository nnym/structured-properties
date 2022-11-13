package net.auoeke.sp.internal;

import java.util.Collection;
import java.util.stream.Collectors;
import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpArray;

public final class CollectionAdapter implements SpAdapter<Collection<?>, SpArray> {
	public static final CollectionAdapter instance = new CollectionAdapter();

	@Override public SpArray toSp(Collection<?> collection, StructuredProperties serializer) {
		return collection.stream().map(serializer::toSp).collect(Collectors.toCollection(SpArray::new));
	}

	@Override public Collection<?> fromSp(SpArray sp, StructuredProperties serializer) {
		return sp.stream().map(serializer::fromSp).collect(Collectors.toList());
	}
}

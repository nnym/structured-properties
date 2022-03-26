package net.auoeke.eson;

import java.util.Collection;
import java.util.stream.Collectors;
import net.auoeke.eson.element.EsonArray;

final class CollectionAdapter implements EsonAdapter<Collection<?>, EsonArray> {
    static final CollectionAdapter instance = new CollectionAdapter();

    @Override public EsonArray toEson(Collection<?> collection, Eson serializer) {
        return collection.stream().map(serializer::toEson).collect(Collectors.toCollection(EsonArray::new));
    }

    @Override public Collection<?> fromEson(EsonArray eson, Eson serializer) {
        return eson.stream().map(serializer::fromEson).collect(Collectors.toList());
    }
}

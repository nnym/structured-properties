package net.auoeke.eson.serialization;

import java.util.Collection;
import java.util.stream.Collectors;
import net.auoeke.eson.element.EsonArray;

final class CollectionSerializer implements EsonTypeSerializer<Collection<?>, EsonArray> {
    static final CollectionSerializer instance = new CollectionSerializer();

    @Override public EsonArray toEson(Collection<?> collection, EsonSerializer serializer) {
        return collection.stream().map(serializer::toEson).collect(Collectors.toCollection(EsonArray::new));
    }

    @Override public Collection<?> fromEson(EsonArray eson, EsonSerializer serializer) {
        return eson.stream().map(serializer::fromEson).collect(Collectors.toList());
    }
}

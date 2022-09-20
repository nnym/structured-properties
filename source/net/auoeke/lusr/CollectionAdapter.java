package net.auoeke.lusr;

import java.util.Collection;
import java.util.stream.Collectors;
import net.auoeke.lusr.element.LusrArray;

final class CollectionAdapter implements LusrAdapter<Collection<?>, LusrArray> {
    static final CollectionAdapter instance = new CollectionAdapter();

    @Override public LusrArray toLusr(Collection<?> collection, Lusr serializer) {
        return collection.stream().map(serializer::toLusr).collect(Collectors.toCollection(LusrArray::new));
    }

    @Override public Collection<?> fromLusr(LusrArray lusr, Lusr serializer) {
        return lusr.stream().map(serializer::fromLusr).collect(Collectors.toList());
    }
}

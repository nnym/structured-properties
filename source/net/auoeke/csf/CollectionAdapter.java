package net.auoeke.csf;

import java.util.Collection;
import java.util.stream.Collectors;
import net.auoeke.csf.element.CsfArray;

final class CollectionAdapter implements CsfAdapter<Collection<?>, CsfArray> {
    static final CollectionAdapter instance = new CollectionAdapter();

    @Override public CsfArray toCsf(Collection<?> collection, Csf serializer) {
        return collection.stream().map(serializer::toCsf).collect(Collectors.toCollection(CsfArray::new));
    }

    @Override public Collection<?> fromCsf(CsfArray csf, Csf serializer) {
        return csf.stream().map(serializer::fromCsf).collect(Collectors.toList());
    }
}

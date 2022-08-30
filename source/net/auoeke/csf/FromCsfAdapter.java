package net.auoeke.csf;

import net.auoeke.csf.element.CsfElement;

public interface FromCsfAdapter<A, B extends CsfElement> extends PolymorphicFromCsfAdapter<A, B> {
    A fromCsf(B csf, Csf serializer);

    @Override default boolean accept(Class<?> type) {
        return false;
    }

    @Override default A fromCsf(Class<A> type, B csf, Csf serializer) {
        return this.fromCsf(csf, serializer);
    }
}

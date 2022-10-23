package net.auoeke.sp;

import net.auoeke.sp.element.SpElement;

public interface FromSpAdapter<A, B extends SpElement> extends PolymorphicFromSpAdapter<A, B> {
    A fromSp(B sp, StructuredProperties serializer);

    @Override default boolean accept(Class<?> type) {
        return false;
    }

    @Override default A fromSp(Class<A> type, B sp, StructuredProperties serializer) {
        return this.fromSp(sp, serializer);
    }
}

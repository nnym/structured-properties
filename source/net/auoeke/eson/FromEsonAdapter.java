package net.auoeke.eson;

import net.auoeke.eson.element.EsonElement;

public interface FromEsonAdapter<A, B extends EsonElement> extends PolymorphicFromEsonAdapter<A, B> {
    A fromEson(B eson, Eson serializer);

    @Override default boolean accept(Class<?> type) {
        return false;
    }

    @Override default A fromEson(Class<A> type, B eson, Eson serializer) {
        return this.fromEson(eson, serializer);
    }
}

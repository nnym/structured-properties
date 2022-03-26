package net.auoeke.eson;

import net.auoeke.eson.element.EsonElement;

public interface PolymorphicFromEsonAdapter<A, B extends EsonElement> {
    /**
     Return whether this adapter can adapt a given type from eson.
     <b>This method is pure.</b> If this method returns {@code true}, then it will become permanently associated with the type.

     @param type a type
     @return whether this adapter can adapt {@code type} to eson
     */
    boolean accept(Class<?> type);

    A fromEson(Class<A> type, B eson, Eson serializer);
}

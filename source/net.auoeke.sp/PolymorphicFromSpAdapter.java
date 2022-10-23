package net.auoeke.sp;

import net.auoeke.sp.element.SpElement;

public interface PolymorphicFromSpAdapter<A, B extends SpElement> {
    /**
     Return whether this adapter can adapt a given type from sp.
     <b>This method is pure.</b> If it returns {@code true}, then this adapter will become permanently associated with the type.

     @param type a type
     @return whether this adapter can adapt {@code type} to sp
     */
    boolean accept(Class<?> type);

    A fromSp(Class<A> type, B sp, StructuredProperties serializer);
}

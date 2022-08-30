package net.auoeke.csf;

import net.auoeke.csf.element.CsfElement;

public interface PolymorphicToCsfAdapter<A, B extends CsfElement> {
    /**
     Return whether this adapter can adapt a given type to csf.
     <b>This method is pure.</b> If it returns {@code true}, then this adapter will become permanently associated with the type.

     @param type a type
     @return whether this adapter can adapt {@code type} to csf
     */
    boolean accept(Class<?> type);

    B toCsf(A a, Csf serializer);
}

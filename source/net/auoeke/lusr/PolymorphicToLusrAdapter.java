package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrElement;

public interface PolymorphicToLusrAdapter<A, B extends LusrElement> {
    /**
     Return whether this adapter can adapt a given type to lusr.
     <b>This method is pure.</b> If it returns {@code true}, then this adapter will become permanently associated with the type.

     @param type a type
     @return whether this adapter can adapt {@code type} to lusr
     */
    boolean accept(Class<?> type);

    B toLusr(A a, Lusr serializer);
}

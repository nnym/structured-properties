package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrElement;

public interface PolymorphicFromLusrAdapter<A, B extends LusrElement> {
    /**
     Return whether this adapter can adapt a given type from lusr.
     <b>This method is pure.</b> If it returns {@code true}, then this adapter will become permanently associated with the type.

     @param type a type
     @return whether this adapter can adapt {@code type} to lusr
     */
    boolean accept(Class<?> type);

    A fromLusr(Class<A> type, B lusr, Lusr serializer);
}

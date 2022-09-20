package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrElement;

public interface FromLusrAdapter<A, B extends LusrElement> extends PolymorphicFromLusrAdapter<A, B> {
    A fromLusr(B lusr, Lusr serializer);

    @Override default boolean accept(Class<?> type) {
        return false;
    }

    @Override default A fromLusr(Class<A> type, B lusr, Lusr serializer) {
        return this.fromLusr(lusr, serializer);
    }
}

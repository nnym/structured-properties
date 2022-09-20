package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrElement;

public interface ToLusrAdapter<A, B extends LusrElement> extends PolymorphicToLusrAdapter<A, B> {
    @Override default boolean accept(Class<?> type) {
        return false;
    }
}

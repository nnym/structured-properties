package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrElement;

public interface LusrAdapter<A, B extends LusrElement> extends ToLusrAdapter<A, B>, FromLusrAdapter<A, B> {
    @Override default boolean accept(Class<?> type) {
        return false;
    }
}

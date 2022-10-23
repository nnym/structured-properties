package net.auoeke.sp;

import net.auoeke.sp.element.SpElement;

public interface SpAdapter<A, B extends SpElement> extends ToSpAdapter<A, B>, FromSpAdapter<A, B> {
    @Override default boolean accept(Class<?> type) {
        return false;
    }
}

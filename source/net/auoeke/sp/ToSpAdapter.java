package net.auoeke.sp;

import net.auoeke.sp.element.SpElement;

public interface ToSpAdapter<A, B extends SpElement> extends PolymorphicToSpAdapter<A, B> {
    @Override default boolean accept(Class<?> type) {
        return false;
    }
}

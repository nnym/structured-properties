package net.auoeke.eson;

import net.auoeke.eson.element.EsonElement;

public interface ToEsonAdapter<A, B extends EsonElement> extends PolymorphicToEsonAdapter<A, B> {
    @Override default boolean accept(Class<?> type) {
        return false;
    }
}

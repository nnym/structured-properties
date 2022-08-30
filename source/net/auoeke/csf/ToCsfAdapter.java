package net.auoeke.csf;

import net.auoeke.csf.element.CsfElement;

public interface ToCsfAdapter<A, B extends CsfElement> extends PolymorphicToCsfAdapter<A, B> {
    @Override default boolean accept(Class<?> type) {
        return false;
    }
}

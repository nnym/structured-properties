package net.auoeke.csf;

import net.auoeke.csf.element.CsfElement;

public interface CsfAdapter<A, B extends CsfElement> extends ToCsfAdapter<A, B>, FromCsfAdapter<A, B> {
    @Override default boolean accept(Class<?> type) {
        return false;
    }
}

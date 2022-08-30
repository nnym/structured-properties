package net.auoeke.csf.element;

import java.util.ArrayList;
import java.util.Collection;

public final class CsfArray extends ArrayList<CsfElement> implements CsfElement {
    public CsfArray() {}

    public CsfArray(int initialCapacity) {
        super(initialCapacity);
    }

    public CsfArray(Collection<? extends CsfElement> source) {
        super(source);
    }

    @Override public Type type() {
        return Type.ARRAY;
    }

    @Override public CsfArray clone() {
        return new CsfArray(this);
    }
}

package net.auoeke.eson.element;

import java.util.ArrayList;
import java.util.Collection;

public final class EsonArray extends ArrayList<EsonElement> implements EsonElement {
    public EsonArray() {}

    public EsonArray(int initialCapacity) {
        super(initialCapacity);
    }

    public EsonArray(Collection<? extends EsonElement> source) {
        super(source);
    }

    @Override public Type type() {
        return Type.ARRAY;
    }

    @Override public EsonArray clone() {
        return new EsonArray(this);
    }
}

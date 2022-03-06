package net.auoeke.eson.element;

import java.util.ArrayList;
import java.util.Collection;

public class EsonArray extends ArrayList<EsonElement> implements EsonElement {
    public EsonArray() {}

    public EsonArray(int initialCapacity) {
        super(initialCapacity);
    }

    public EsonArray(Collection<? extends EsonElement> source) {
        super(source);
    }

    @Override public Type type() {
        return EsonElement.Type.ARRAY;
    }

    @Override public EsonArray clone() {
        return new EsonArray(this);
    }
}

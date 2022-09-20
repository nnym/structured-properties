package net.auoeke.lusr.element;

import java.util.ArrayList;
import java.util.Collection;

public final class LusrArray extends ArrayList<LusrElement> implements LusrElement {
    public LusrArray() {}

    public LusrArray(int initialCapacity) {
        super(initialCapacity);
    }

    public LusrArray(Collection<? extends LusrElement> source) {
        super(source);
    }

    @Override public Type type() {
        return Type.ARRAY;
    }

    @Override public LusrArray clone() {
        return new LusrArray(this);
    }
}

package net.auoeke.cin.element;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayElement extends ArrayList<Element> implements Element {
    public ArrayElement() {}

    public ArrayElement(int initialCapacity) {
        super(initialCapacity);
    }

    public ArrayElement(Collection<? extends Element> source) {
        super(source);
    }

    @Override public Type type() {
        return Element.Type.ARRAY;
    }

    @Override public ArrayElement clone() {
        return new ArrayElement(this);
    }
}

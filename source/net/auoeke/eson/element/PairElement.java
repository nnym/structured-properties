package net.auoeke.eson.element;

import java.util.Objects;

public class PairElement implements Element {
    public Element a;
    public Element b;

    public PairElement(Element a, Element b) {
        this.a = a;
        this.b = b;
    }

    public MapElement map() {
        if (this.a instanceof PrimitiveElement primitive) {
            var map = new MapElement();
            map.put(primitive.stringValue(), this.b);

            return map;
        }

        throw new UnsupportedOperationException("key (%s) is not primitive".formatted(this.a));
    }

    @Override public Type type() {
        return Type.PAIR;
    }

    @Override public int hashCode() {
        return Objects.hash(this.a, this.b);
    }

    @Override public boolean equals(Object object) {
        return object instanceof PairElement pair && Objects.equals(this.a, pair.a) && Objects.equals(this.b, pair.b);
    }

    @Override public String toString() {
        return this.a instanceof PrimitiveElement && this.b.type().structure() ? this.a + " " + this.b : this.a + " = " + this.b;
    }
}

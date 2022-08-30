package net.auoeke.csf.element;

import java.util.Objects;

public final class CsfPair implements CsfElement {
    public CsfElement a;
    public CsfElement b;

    public CsfPair(CsfElement a, CsfElement b) {
        this.a = a;
        this.b = b;
    }

    public CsfMap map() {
        if (this.a instanceof CsfPrimitive primitive) {
            var map = new CsfMap();
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
        return object instanceof CsfPair pair && Objects.equals(this.a, pair.a) && Objects.equals(this.b, pair.b);
    }

    @Override public String toString() {
        return this.a instanceof CsfPrimitive && this.b.type().structure() ? this.a + " " + this.b : this.a + " = " + this.b;
    }
}

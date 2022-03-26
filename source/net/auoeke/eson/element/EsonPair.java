package net.auoeke.eson.element;

import java.util.Objects;

public final class EsonPair implements EsonElement {
    public EsonElement a;
    public EsonElement b;

    public EsonPair(EsonElement a, EsonElement b) {
        this.a = a;
        this.b = b;
    }

    public EsonMap map() {
        if (this.a instanceof EsonPrimitive primitive) {
            var map = new EsonMap();
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
        return object instanceof EsonPair pair && Objects.equals(this.a, pair.a) && Objects.equals(this.b, pair.b);
    }

    @Override public String toString() {
        return this.a instanceof EsonPrimitive && this.b.type().structure() ? this.a + " " + this.b : this.a + " = " + this.b;
    }
}

package net.auoeke.sp.element;

import java.util.Objects;

public final class SpPair implements SpElement {
    public SpElement a;
    public SpElement b;

    public SpPair(SpElement a, SpElement b) {
        this.a = a;
        this.b = b;
    }

    public SpMap map() {
        if (this.a instanceof SpPrimitive primitive) {
            var map = new SpMap();
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
        return object instanceof SpPair pair && Objects.equals(this.a, pair.a) && Objects.equals(this.b, pair.b);
    }

    @Override public String toString() {
        return this.a instanceof SpPrimitive && this.b.type().structure() ? this.a + " " + this.b : this.a + " = " + this.b;
    }
}

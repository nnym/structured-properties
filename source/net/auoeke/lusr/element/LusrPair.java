package net.auoeke.lusr.element;

import java.util.Objects;

public final class LusrPair implements LusrElement {
    public LusrElement a;
    public LusrElement b;

    public LusrPair(LusrElement a, LusrElement b) {
        this.a = a;
        this.b = b;
    }

    public LusrMap map() {
        if (this.a instanceof LusrPrimitive primitive) {
            var map = new LusrMap();
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
        return object instanceof LusrPair pair && Objects.equals(this.a, pair.a) && Objects.equals(this.b, pair.b);
    }

    @Override public String toString() {
        return this.a instanceof LusrPrimitive && this.b.type().structure() ? this.a + " " + this.b : this.a + " = " + this.b;
    }
}

package net.auoeke.cin.element;

import java.util.Objects;

public class PairElement implements Element {
    public Element a;
    public Element b;

    public PairElement(Element a, Element b) {
        this.a = a;
        this.b = b;
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
        return this.a + " = " + this.b;
    }
}

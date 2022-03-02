package net.auoeke.cin.element;

public abstract class NumberElement extends Number implements PrimitiveElement {
    protected final String source;

    public NumberElement(String source) {
        this.source = source;
    }

    @Override public String stringValue() {
        return this.source;
    }

    @Override public String toString() {
        return this.source;
    }
}

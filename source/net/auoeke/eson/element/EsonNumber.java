package net.auoeke.eson.element;

public abstract class EsonNumber extends Number implements EsonPrimitive {
    protected final String source;

    public EsonNumber(String source) {
        this.source = source;
    }

    @Override public String stringValue() {
        return this.source;
    }

    @Override public String toString() {
        return this.source;
    }
}

package net.auoeke.sp.element;

public abstract sealed class SpNumber extends Number implements SpPrimitive permits SpInteger, SpFloat {
    protected final String source;

    public SpNumber(String source) {
        this.source = source;
    }

    @Override public String stringValue() {
        return this.source;
    }

    @Override public String toString() {
        return this.source;
    }
}

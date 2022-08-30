package net.auoeke.csf.element;

public abstract sealed class CsfNumber extends Number implements CsfPrimitive permits CsfInteger, CsfFloat {
    protected final String source;

    public CsfNumber(String source) {
        this.source = source;
    }

    @Override public String stringValue() {
        return this.source;
    }

    @Override public String toString() {
        return this.source;
    }
}

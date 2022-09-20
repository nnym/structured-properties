package net.auoeke.lusr.element;

public abstract sealed class LusrNumber extends Number implements LusrPrimitive permits LusrInteger, LusrFloat {
    protected final String source;

    public LusrNumber(String source) {
        this.source = source;
    }

    @Override public String stringValue() {
        return this.source;
    }

    @Override public String toString() {
        return this.source;
    }
}

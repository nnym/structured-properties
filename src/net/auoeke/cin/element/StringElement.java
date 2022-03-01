package net.auoeke.cin.element;

public class StringElement implements Element {
    public String value;

    public StringElement(String value) {
        this.value = value;
    }

    @Override public Type type() {
        return Element.Type.STRING;
    }

    @Override public int hashCode() {
        return this.value.hashCode();
    }

    @Override public boolean equals(Object other) {
        return other instanceof StringElement string && this.value.equals(string.value);
    }

    @Override public String toString() {
        return '"' + this.value + '"';
    }
}

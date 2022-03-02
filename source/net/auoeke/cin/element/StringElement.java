package net.auoeke.cin.element;

public class StringElement implements PrimitiveElement {
    public final String value;

    private String delimiter;

    public StringElement(String value, String delimiter) {
        this.value = value;
        this.delimiter = delimiter;
    }

    public StringElement(String value) {
        this(value, null);
    }

    public String delimiter() {
        if (this.delimiter == null) {
            var delimiter = this.delimiter(1);

            if (delimiter == null) {
                for (var length = 3; delimiter == null; length++) {
                    delimiter = this.delimiter(length);
                }
            }

            this.delimiter = delimiter;
        }

        return this.delimiter;
    }

    @Override public String stringValue() {
        return this.value;
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
        return this.delimiter() + this.value + this.delimiter();
    }

    private String delimiter(int length) {
        var delimiter = "\"".repeat(length);

        if (!this.value.contains(delimiter)) {
            return delimiter;
        }

        if (!this.value.contains(delimiter = "'".repeat(length))) {
            return delimiter;
        }

        return null;
    }
}

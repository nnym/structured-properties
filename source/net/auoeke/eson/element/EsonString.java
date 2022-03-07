package net.auoeke.eson.element;

import java.util.Objects;
import java.util.regex.Pattern;

public class EsonString implements EsonPrimitive {
    private static final Pattern terminator = Pattern.compile("[]\\[{}\n,=]|##|/*");

    public final String value;

    private String delimiter;
    private boolean raw;

    public EsonString(String value, String delimiter) {
        this.value = Objects.requireNonNull(value);
        this.delimiter = delimiter;
    }

    public EsonString(String value) {
        this(value, null);

        this.raw = true;
    }

    public String delimiter() {
        if (this.raw && terminator.matcher(this.value).find()) {
            var delimiter = this.delimiter(1);

            if (delimiter == null) {
                for (var length = 3; delimiter == null; length++) {
                    delimiter = this.delimiter(length);
                }
            }

            this.delimiter = delimiter;
            this.raw = false;
        }

        return this.delimiter;
    }

    @Override public String stringValue() {
        return this.value;
    }

    @Override public Type type() {
        return Type.STRING;
    }

    @Override public int hashCode() {
        return this.value.hashCode();
    }

    @Override public boolean equals(Object other) {
        return other instanceof EsonString string && this.value.equals(string.value) && Objects.equals(this.delimiter(), string.delimiter());
    }

    @Override public String toString() {
        var delimiter = this.delimiter();
        return delimiter == null ? this.value : delimiter + this.value + delimiter;
    }

    private String delimiter(int length) {
        var delimiter = "\"".repeat(length);
        return this.value.startsWith(delimiter)
               && this.value.startsWith(delimiter = "'".repeat(length))
               && this.value.startsWith(delimiter = "`".repeat(length))
            ? null
            : delimiter;
    }
}

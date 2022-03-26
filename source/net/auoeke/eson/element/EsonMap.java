package net.auoeke.eson.element;

import java.util.LinkedHashMap;
import java.util.Map;

public final class EsonMap extends LinkedHashMap<String, EsonElement> implements EsonElement {
    public EsonMap() {}

    public EsonMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public EsonMap(int initialCapacity) {
        super(initialCapacity);
    }

    public EsonMap(Map<? extends String, ? extends EsonElement> source) {
        super(source);
    }

    @Override public Type type() {
        return Type.MAP;
    }

    @Override public EsonMap clone() {
        return new EsonMap(this);
    }

    @Override public String toString() {
        return switch (this.size()) {
            case 0 -> "{}";
            default -> {
                var builder = new StringBuilder("{").append(System.lineSeparator());
                this.forEach((key, value) -> builder.append("    ").append(this.format(new EsonString(key).toString(), value).replaceAll("\n", "\n    ")).append('\n'));

                yield builder.append('}').toString();
            }
        };
    }

    private String format(String key, EsonElement value) {
        return switch (value.type()) {
            case ARRAY, MAP -> key + ' ' + value;
            default -> key + " = " + value;
        };
    }
}

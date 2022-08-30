package net.auoeke.csf.element;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CsfMap extends LinkedHashMap<String, CsfElement> implements CsfElement {
    public CsfMap() {}

    public CsfMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public CsfMap(int initialCapacity) {
        super(initialCapacity);
    }

    public CsfMap(Map<? extends String, ? extends CsfElement> source) {
        super(source);
    }

    @Override public Type type() {
        return Type.MAP;
    }

    @Override public CsfMap clone() {
        return new CsfMap(this);
    }

    @Override public String toString() {
        return switch (this.size()) {
            case 0 -> "{}";
            default -> {
                var builder = new StringBuilder("{").append(System.lineSeparator());
                this.forEach((key, value) -> builder.append("    ").append(this.format(new CsfString(key).toString(), value).replaceAll("\n", "\n    ")).append('\n'));

                yield builder.append('}').toString();
            }
        };
    }

    private String format(String key, CsfElement value) {
        return switch (value.type()) {
            case ARRAY, MAP -> key + ' ' + value;
            default -> key + " = " + value;
        };
    }
}

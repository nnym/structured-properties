package net.auoeke.lusr.element;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LusrMap extends LinkedHashMap<String, LusrElement> implements LusrElement {
    public LusrMap() {}

    public LusrMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public LusrMap(int initialCapacity) {
        super(initialCapacity);
    }

    public LusrMap(Map<? extends String, ? extends LusrElement> source) {
        super(source);
    }

    @Override public Type type() {
        return Type.MAP;
    }

    @Override public LusrMap clone() {
        return new LusrMap(this);
    }

    @Override public String toString() {
        return switch (this.size()) {
            case 0 -> "{}";
            default -> {
                var builder = new StringBuilder("{").append(System.lineSeparator());
                this.forEach((key, value) -> builder.append("    ").append(this.format(new LusrString(key).toString(), value).replaceAll("\n", "\n    ")).append('\n'));

                yield builder.append('}').toString();
            }
        };
    }

    private String format(String key, LusrElement value) {
        return switch (value.type()) {
            case ARRAY, MAP -> key + ' ' + value;
            default -> key + " = " + value;
        };
    }
}

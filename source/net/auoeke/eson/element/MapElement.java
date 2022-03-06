package net.auoeke.eson.element;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapElement extends LinkedHashMap<String, Element> implements Element {
    public MapElement() {}

    public MapElement(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public MapElement(int initialCapacity) {
        super(initialCapacity);
    }

    public MapElement(Map<? extends String, ? extends Element> source) {
        super(source);
    }

    @Override public Type type() {
        return Type.MAP;
    }

    @Override public MapElement clone() {
        return new MapElement(this);
    }

    @Override public String toString() {
        return switch (this.size()) {
            case 0 -> "{}";
            default -> {
                var builder = new StringBuilder("{").append(System.lineSeparator());
                this.forEach((key, value) -> builder.append("    ").append(this.format(key, value).replaceAll("\n", "\n    ")).append('\n'));

                yield builder.append('}').toString();
            }
        };
    }

    private String format(String key, Element value) {
        return switch (value.type()) {
            case ARRAY, MAP -> key + ' ' + value;
            default -> key + " = " + value;
        };
    }
}

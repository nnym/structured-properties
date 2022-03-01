package net.auoeke.cin.element;

import java.util.HashMap;
import java.util.Map;

public class MapElement extends HashMap<String, Element> implements Element {
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
            case 1 -> '{' + this.format(this.entrySet().iterator().next().getValue()) + '}';
            default -> {
                var builder = new StringBuilder("{\n");
                this.forEach((key, value) -> builder.append("    ").append(this.format(value).replaceAll("\n", "\n    ")).append('\n'));

                yield builder.append('}').toString();
            }
        };
    }

    private String format(Element value) {
        return switch (value.type()) {
            case ARRAY, MAP -> "$key $value";
            default -> "$key = $value";
        };
    }
}

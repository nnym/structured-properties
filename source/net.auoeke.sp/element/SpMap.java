package net.auoeke.sp.element;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SpMap extends LinkedHashMap<String, SpElement> implements SpElement {
	public SpMap() {}

	public SpMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public SpMap(int initialCapacity) {
		super(initialCapacity);
	}

	public SpMap(Map<? extends String, ? extends SpElement> source) {
		super(source);
	}

	@Override public Type type() {
		return Type.MAP;
	}

	@Override public SpMap clone() {
		return new SpMap(this);
	}

	@Override public String toString() {
		return switch (this.size()) {
			case 0 -> "{}";
			default -> {
				var builder = new StringBuilder("{").append(System.lineSeparator());
				this.forEach((key, value) -> builder.append("    ").append(this.format(new SpString(key).toString(), value).replaceAll("\n", "\n    ")).append('\n'));

				yield builder.append('}').toString();
			}
		};
	}

	private String format(String key, SpElement value) {
		return switch (value.type()) {
			case ARRAY, MAP -> key + ' ' + value;
			default -> key + " = " + value;
		};
	}
}

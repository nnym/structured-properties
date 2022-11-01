package net.auoeke.sp.element;

public final class SpBoolean implements SpPrimitive {
	private static final SpBoolean tru = new SpBoolean(true);
	private static final SpBoolean fals = new SpBoolean(false);

	public boolean value;

	private SpBoolean(boolean value) {
		this.value = value;
	}

	public static SpBoolean of(boolean value) {
		return value ? tru : fals;
	}

	@Override public String stringValue() {
		return Boolean.toString(this.value);
	}

	@Override public Type type() {
		return Type.BOOLEAN;
	}

	@Override public boolean equals(Object other) {
		return other instanceof SpBoolean bool && this.value == bool.value;
	}

	@Override public int hashCode() {
		return Boolean.hashCode(this.value);
	}

	@Override public String toString() {
		return this.stringValue();
	}
}

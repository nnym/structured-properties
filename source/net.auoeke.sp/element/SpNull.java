package net.auoeke.sp.element;

public final class SpNull implements SpPrimitive {
	public static final SpNull instance = new SpNull();

	private SpNull() {}

	@Override public String stringValue() {
		return "null";
	}

	@Override public Type type() {
		return Type.NULL;
	}

	@Override public String toString() {
		return this.stringValue();
	}
}

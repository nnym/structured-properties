package net.auoeke.sp.element;

import java.util.ArrayList;
import java.util.Collection;

public final class SpArray extends ArrayList<SpElement> implements SpElement {
	public SpArray() {}

	public SpArray(int initialCapacity) {
		super(initialCapacity);
	}

	public SpArray(Collection<? extends SpElement> source) {
		super(source);
	}

	@Override public Type type() {
		return Type.ARRAY;
	}

	@Override public SpArray clone() {
		return new SpArray(this);
	}
}

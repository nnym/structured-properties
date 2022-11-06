package net.auoeke.sp.element;

public sealed interface SpPrimitive extends SpElement permits SpBoolean, SpNull, SpNumber, SpString {
	String stringValue();
}

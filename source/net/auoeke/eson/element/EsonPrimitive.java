package net.auoeke.eson.element;

public sealed interface EsonPrimitive extends EsonElement permits EsonBoolean, EsonNull, EsonNumber, EsonString {
    String stringValue();
}

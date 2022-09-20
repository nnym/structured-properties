package net.auoeke.lusr.element;

public sealed interface LusrPrimitive extends LusrElement permits LusrBoolean, LusrNull, LusrNumber, LusrString {
    String stringValue();
}

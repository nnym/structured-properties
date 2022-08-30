package net.auoeke.csf.element;

public sealed interface CsfPrimitive extends CsfElement permits CsfBoolean, CsfNull, CsfNumber, CsfString {
    String stringValue();
}

package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrElement;

public interface PolymorphicLusrAdapter<A, B extends LusrElement> extends PolymorphicToLusrAdapter<A, B>, PolymorphicFromLusrAdapter<A, B> {}

package net.auoeke.sp;

import net.auoeke.sp.element.SpElement;

public interface PolymorphicSpAdapter<A, B extends SpElement> extends PolymorphicToSpAdapter<A, B>, PolymorphicFromSpAdapter<A, B> {}

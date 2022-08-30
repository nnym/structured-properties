package net.auoeke.csf;

import net.auoeke.csf.element.CsfElement;

public interface PolymorphicCsfAdapter<A, B extends CsfElement> extends PolymorphicToCsfAdapter<A, B>, PolymorphicFromCsfAdapter<A, B> {}

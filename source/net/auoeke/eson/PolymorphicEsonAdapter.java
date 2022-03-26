package net.auoeke.eson;

import net.auoeke.eson.element.EsonElement;

public interface PolymorphicEsonAdapter<A, B extends EsonElement> extends PolymorphicToEsonAdapter<A, B>, PolymorphicFromEsonAdapter<A, B> {}

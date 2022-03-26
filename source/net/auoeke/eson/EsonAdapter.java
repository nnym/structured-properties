package net.auoeke.eson;

import net.auoeke.eson.element.EsonElement;

public interface EsonAdapter<A, B extends EsonElement> {
    B toEson(A a, Eson serializer);

    A fromEson(B eson, Eson serializer);
}

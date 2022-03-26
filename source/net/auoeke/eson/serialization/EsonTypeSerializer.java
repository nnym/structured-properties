package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonElement;
import net.auoeke.eson.element.EsonMap;

public interface EsonTypeSerializer<A, B extends EsonElement> {
    B toEson(A t);

    A fromEson(B eson);
}

package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonElement;

public interface EsonTypeSerializer<A, B extends EsonElement> {
    B toEson(A t, EsonSerializer serializer);

    A fromEson(B eson, EsonSerializer serializer);
}

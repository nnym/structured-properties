package net.auoeke.eson;

import net.auoeke.eson.element.EsonElement;

final class EsonElementAdapter implements EsonAdapter<EsonElement, EsonElement> {
    static final EsonElementAdapter instance = new EsonElementAdapter();

    @Override public EsonElement toEson(EsonElement eson, Eson serializer) {
        return eson;
    }

    @Override public EsonElement fromEson(EsonElement eson, Eson serializer) {
        return eson;
    }
}

package net.auoeke.eson;

import net.auoeke.eson.element.EsonNull;

final class NullAdapter implements EsonAdapter<Object, EsonNull> {
    static final NullAdapter instance = new NullAdapter();
    
    @Override public EsonNull toEson(Object nul, Eson serializer) {
        return EsonNull.instance;
    }

    @Override public Object fromEson(EsonNull eson, Eson serializer) {
        return null;
    }
}

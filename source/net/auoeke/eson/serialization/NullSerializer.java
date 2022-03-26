package net.auoeke.eson.serialization;

import net.auoeke.eson.element.EsonNull;

final class NullSerializer implements EsonTypeSerializer<Object, EsonNull> {
    static final NullSerializer instance = new NullSerializer();
    
    @Override public EsonNull toEson(Object nul, EsonSerializer serializer) {
        return EsonNull.instance;
    }

    @Override public Object fromEson(EsonNull eson, EsonSerializer serializer) {
        return null;
    }
}

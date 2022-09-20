package net.auoeke.lusr;

import net.auoeke.lusr.element.LusrNull;

final class NullAdapter implements LusrAdapter<Object, LusrNull> {
    static final NullAdapter instance = new NullAdapter();
    
    @Override public LusrNull toLusr(Object nul, Lusr serializer) {
        return LusrNull.instance;
    }

    @Override public Object fromLusr(LusrNull lusr, Lusr serializer) {
        return null;
    }
}

package net.auoeke.sp;

import net.auoeke.sp.element.SpNull;

final class NullAdapter implements SpAdapter<Object, SpNull> {
    static final NullAdapter instance = new NullAdapter();
    
    @Override public SpNull toSp(Object nul, StructuredProperties serializer) {
        return SpNull.instance;
    }

    @Override public Object fromSp(SpNull sp, StructuredProperties serializer) {
        return null;
    }
}

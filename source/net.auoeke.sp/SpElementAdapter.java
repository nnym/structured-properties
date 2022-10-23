package net.auoeke.sp;

import net.auoeke.sp.element.SpElement;

final class SpElementAdapter implements SpAdapter<SpElement, SpElement> {
    static final SpElementAdapter instance = new SpElementAdapter();

    @Override public SpElement toSp(SpElement sp, StructuredProperties serializer) {
        return sp;
    }

    @Override public SpElement fromSp(SpElement sp, StructuredProperties serializer) {
        return sp;
    }
}

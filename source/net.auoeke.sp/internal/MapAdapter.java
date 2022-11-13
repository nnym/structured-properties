package net.auoeke.sp.internal;

import java.util.Map;
import net.auoeke.sp.SpAdapter;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpMap;

public final class MapAdapter implements SpAdapter<Map<?, ?>, SpMap> {
	public static final MapAdapter instance = new MapAdapter();

	@Override public SpMap toSp(Map<?, ?> map, StructuredProperties serializer) {
		var spMap = new SpMap();
		map.forEach((key, value) -> spMap.put(String.valueOf(key), serializer.toSp(value)));

		return spMap;
	}

	@Override public Map<?, ?> fromSp(SpMap sp, StructuredProperties serializer) {
		return serializer.fromSp(sp);
	}
}

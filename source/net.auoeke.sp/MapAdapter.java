package net.auoeke.sp;

import java.util.Map;
import net.auoeke.sp.element.SpMap;

final class MapAdapter implements SpAdapter<Map<?, ?>, SpMap> {
	static final MapAdapter instance = new MapAdapter();

	@Override public SpMap toSp(Map<?, ?> map, StructuredProperties serializer) {
		var spMap = new SpMap();
		map.forEach((key, value) -> spMap.put(String.valueOf(key), serializer.toSp(value)));

		return spMap;
	}

	@Override public Map<?, ?> fromSp(SpMap sp, StructuredProperties serializer) {
		return serializer.fromSp(sp);
	}
}

package net.auoeke.eson;

import java.util.Map;
import net.auoeke.eson.element.EsonMap;

final class MapAdapter implements EsonAdapter<Map<?, ?>, EsonMap> {
    static final MapAdapter instance = new MapAdapter();

    @Override public EsonMap toEson(Map<?, ?> map, Eson serializer) {
        var esonMap = new EsonMap();
        map.forEach((key, value) -> esonMap.put(String.valueOf(key), serializer.toEson(value)));

        return esonMap;
    }

    @Override public Map<?, ?> fromEson(EsonMap eson, Eson serializer) {
        return serializer.fromEson(eson);
    }
}

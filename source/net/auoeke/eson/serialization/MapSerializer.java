package net.auoeke.eson.serialization;

import java.util.Map;
import net.auoeke.eson.element.EsonMap;

final class MapSerializer implements EsonTypeSerializer<Map<?, ?>, EsonMap> {
    static final MapSerializer instance = new MapSerializer();

    @Override public EsonMap toEson(Map<?, ?> map, EsonSerializer serializer) {
        var esonMap = new EsonMap();
        map.forEach((key, value) -> esonMap.put(String.valueOf(key), serializer.toEson(value)));

        return esonMap;
    }

    @Override public Map<?, ?> fromEson(EsonMap eson, EsonSerializer serializer) {
        return serializer.fromEson(eson);
    }
}

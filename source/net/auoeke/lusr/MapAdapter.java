package net.auoeke.lusr;

import java.util.Map;
import net.auoeke.lusr.element.LusrMap;

final class MapAdapter implements LusrAdapter<Map<?, ?>, LusrMap> {
    static final MapAdapter instance = new MapAdapter();

    @Override public LusrMap toLusr(Map<?, ?> map, Lusr serializer) {
        var lusrMap = new LusrMap();
        map.forEach((key, value) -> lusrMap.put(String.valueOf(key), serializer.toLusr(value)));

        return lusrMap;
    }

    @Override public Map<?, ?> fromLusr(LusrMap lusr, Lusr serializer) {
        return serializer.fromLusr(lusr);
    }
}

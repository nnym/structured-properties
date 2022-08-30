package net.auoeke.csf;

import java.util.Map;
import net.auoeke.csf.element.CsfMap;

final class MapAdapter implements CsfAdapter<Map<?, ?>, CsfMap> {
    static final MapAdapter instance = new MapAdapter();

    @Override public CsfMap toCsf(Map<?, ?> map, Csf serializer) {
        var csfMap = new CsfMap();
        map.forEach((key, value) -> csfMap.put(String.valueOf(key), serializer.toCsf(value)));

        return csfMap;
    }

    @Override public Map<?, ?> fromCsf(CsfMap csf, Csf serializer) {
        return serializer.fromCsf(csf);
    }
}

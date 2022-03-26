package net.auoeke.eson.serialization;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.auoeke.eson.element.EsonArray;
import net.auoeke.eson.element.EsonBoolean;
import net.auoeke.eson.element.EsonElement;
import net.auoeke.eson.element.EsonFloat;
import net.auoeke.eson.element.EsonInteger;
import net.auoeke.eson.element.EsonMap;
import net.auoeke.eson.element.EsonNull;
import net.auoeke.eson.element.EsonPair;
import net.auoeke.eson.element.EsonPrimitive;
import net.auoeke.eson.element.EsonString;
import net.auoeke.reflect.Accessor;
import net.auoeke.reflect.Classes;
import net.auoeke.reflect.Constructors;
import net.auoeke.reflect.Fields;
import net.auoeke.reflect.Flags;
import net.auoeke.reflect.Types;

public class EsonSerializer {
    private final Map<Class<?>, EsonTypeSerializer<?, ?>> serializers = Map.of();

    public synchronized EsonElement toEson(Object object) {
        if (object == null) return EsonNull.instance;
        if (object instanceof String string) return new EsonString(string);
        if (object instanceof Boolean boolea) return EsonBoolean.of(boolea);
        if (object instanceof Byte byt) return new EsonInteger(byt);
        if (object instanceof Character character) return new EsonString(String.valueOf(character));
        if (object instanceof Short shor) return new EsonInteger(shor);
        if (object instanceof Integer integer) return new EsonInteger(integer);
        if (object instanceof Long lon) return new EsonInteger(lon);
        if (object instanceof BigInteger integer) return new EsonInteger(integer);
        if (object instanceof Float floa) return new EsonFloat(floa);
        if (object instanceof Double doubl) return new EsonFloat(doubl);
        if (object instanceof BigDecimal decimal) return new EsonFloat(decimal);
        if (object instanceof EsonElement eson) return eson;
        if (object instanceof Stream<?> stream) return stream.map(this::toEson).collect(Collectors.toCollection(EsonArray::new));
        if (object instanceof Collection<?> collection) return this.toEson(collection.stream());
        if (object.getClass().isArray()) return this.toEson(Stream.of(Types.box(object)));

        if (object instanceof Map<?, ?> map) {
            var esonMap = new EsonMap();
            map.forEach((key, value) -> esonMap.put(String.valueOf(key), this.toEson(value)));

            return esonMap;
        }

        var map = new EsonMap();
        Fields.all(object).filter(field -> !Flags.any(field, Flags.STATIC | Flags.SYNTHETIC | Flags.TRANSIENT)).forEach(field -> map.put(field.getName(), this.toEson(Accessor.get(object, field))));

        return map;
    }

    public synchronized <T> T fromEson(Class<T> type, EsonElement eson) {
        if (type == null) {
            return (T) this.fromEson(eson);
        }

        if (eson == EsonNull.instance) {
            if (type.isPrimitive()) {
                throw new ClassCastException("cannot convert null to primitive");
            }

            return null;
        }

        if (CharSequence.class.isAssignableFrom(type)) return (T) cast(EsonString.class, CharSequence.class, eson).value;
        if (Types.equals(type, boolean.class)) return (T) (Object) cast(EsonBoolean.class, type, eson).value;
        if (Types.equals(type, byte.class)) return (T) (Object) cast(EsonInteger.class, type, eson).value().byteValueExact();

        if (Types.equals(type, char.class)) {
            var string = cast(EsonString.class, type, eson).value;

            if (string.length() != 1) {
                throw new ClassCastException("string \"%s\" is not convertible to char".formatted(string));
            }

            return (T) (Object) string.charAt(0);
        }

        if (Types.equals(type, short.class)) return (T) (Object) cast(EsonInteger.class, type, eson).value().shortValueExact();
        if (Types.equals(type, int.class)) return (T) (Object) cast(EsonInteger.class, type, eson).value().intValueExact();
        if (Types.equals(type, long.class)) return (T) (Object) cast(EsonInteger.class, type, eson).value().longValueExact();
        if (Types.equals(type, float.class)) return (T) (Object) cast(EsonFloat.class, type, eson).floatValue();
        if (Types.equals(type, double.class)) return (T) (Object) cast(EsonFloat.class, type, eson).doubleValue();
        if (type == BigInteger.class) return (T) cast(EsonInteger.class, type, eson).value();
        if (type == BigDecimal.class) return (T) cast(EsonFloat.class, type, eson).value();
        if (type == EsonElement.class) return (T) eson;
        if (type == Stream.class) return (T) cast(EsonArray.class, type, eson).stream().map(element -> this.fromEson(null, element));
        if (type == List.class) return (T) cast(EsonArray.class, type, eson).stream().map(element -> this.fromEson(null, element)).toList();
        if (EsonElement.class.isAssignableFrom(type)) return (T) cast((Class<? extends EsonElement>) type, type, eson);

        if (type.isArray()) {
            var componentType = type.componentType();
            return Types.convert(
                cast(EsonArray.class, type, eson).stream()
                    .map(element -> this.fromEson(componentType, element))
                    .toArray(componentType.isPrimitive() ? Object[]::new : length -> (Object[]) Array.newInstance(componentType, length)),
                componentType
            );
        }

        if (type == Map.class) {
            return (T) this.fromEson(cast(EsonMap.class, type, eson));
        }

        if (eson instanceof EsonMap map) {
            var object = Constructors.instantiate(type);
            map.forEach((key, value) -> {
                var field = Objects.requireNonNull(Fields.of(object, key), () -> "%s does not have a field named \"%s\"".formatted(type, key));
                Accessor.put(object, field, this.fromEson(field.getType(), value));
            });

            return object;
        }

        throw new IllegalArgumentException("no deserializer for %s was found".formatted(type));
    }

    public synchronized Object fromEson(EsonElement eson) {
        if (eson == EsonNull.instance) return null;
        if (eson instanceof EsonBoolean boolea) return this.fromEson(boolea);
        if (eson instanceof EsonInteger integer) return this.fromEson(integer);
        if (eson instanceof EsonFloat floa) return this.fromEson(floa);
        if (eson instanceof EsonString string) return this.fromEson(string);
        if (eson instanceof EsonArray array) return this.fromEson(array);
        if (eson instanceof EsonMap map) return this.fromEson(map);
        if (eson instanceof EsonPair pair) return this.fromEson(pair);

        return null;
    }

    public boolean fromEson(EsonBoolean boolea) {
        return boolea.value;
    }

    public BigInteger fromEson(EsonInteger integer) {
        return integer.value();
    }

    public BigDecimal fromEson(EsonFloat floa) {
        return floa.value();
    }

    public String fromEson(EsonString string) {
        return string.value;
    }

    public Map<String, Object> fromEson(EsonPair pair) {
        var map = new LinkedHashMap<String, Object>();
        map.put(this.fromEson(EsonPrimitive.class, pair.a).stringValue(), this.fromEson(pair.b));

        return map;
    }

    public List<Object> fromEson(EsonArray array) {
        return array.stream().map(this::fromEson).collect(Collectors.toList());
    }

    public synchronized Map<String, Object> fromEson(EsonMap map) {
        var hashMap = new LinkedHashMap<String, Object>();
        map.forEach((key, value) -> hashMap.put(key, this.fromEson(value)));

        return hashMap;
    }

    private static <T extends EsonElement> T cast(Class<T> type, Class<?> target, EsonElement eson) {
        if (Types.canCast(type, eson.getClass())) {
            return (T) eson;
        }

        throw new ClassCastException("cannot convert %s %s to %s".formatted(eson.getClass().getSimpleName(), eson, target.getName()));
    }
}

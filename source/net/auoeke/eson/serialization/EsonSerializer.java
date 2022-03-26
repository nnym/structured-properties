package net.auoeke.eson.serialization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
    private final Map<Class<?>, EsonTypeSerializer<?, ?>> serializers = new HashMap<>();
    private final Map<Class<?>, EsonTypeSerializer<?, ?>> hierarchySerializers = new HashMap<>();
    private final Map<Class<?>, EsonTypeSerializer<?, ?>> cachedHierarchySerializers = new HashMap<>();

    public EsonSerializer() {
        this.registerSerializer(boolean.class, BooleanSerializer.instance);
        this.registerSerializer(byte.class, ByteSerializer.instance);
        this.registerSerializer(char.class, CharacterSerializer.instance);
        this.registerSerializer(short.class, ShortSerializer.instance);
        this.registerSerializer(int.class, IntegerSerializer.instance);
        this.registerSerializer(long.class, LongSerializer.instance);
        this.registerSerializer(float.class, FloatSerializer.instance);
        this.registerSerializer(double.class, DoubleSerializer.instance);
        this.registerSerializer(Boolean.class, BooleanSerializer.instance);
        this.registerSerializer(Byte.class, ByteSerializer.instance);
        this.registerSerializer(Character.class, CharacterSerializer.instance);
        this.registerSerializer(Short.class, ShortSerializer.instance);
        this.registerSerializer(Integer.class, IntegerSerializer.instance);
        this.registerSerializer(Long.class, LongSerializer.instance);
        this.registerSerializer(Float.class, FloatSerializer.instance);
        this.registerSerializer(Double.class, DoubleSerializer.instance);
        this.registerSerializer(BigInteger.class, BigIntegerSerializer.instance);
        this.registerSerializer(BigDecimal.class, BigDecimalSerializer.instance);

        this.registerHierarchySerializer(EsonElement.class, EsonElementSerializer.instance);
        this.registerHierarchySerializer(CharSequence.class, CharSequenceSerializer.instance);
        this.registerHierarchySerializer(Collection.class, CollectionSerializer.instance);
        this.registerHierarchySerializer(Map.class, MapSerializer.instance);
    }

    public <A> void registerSerializer(Class<A> type, EsonTypeSerializer<A, ?> serializer) {
        var previous = this.serializers.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(serializer));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has a serializer (%s)".formatted(type, previous));
        }
    }

    public <A> void registerHierarchySerializer(Class<A> type, EsonTypeSerializer<? extends A, ?> serializer) {
        var previous = this.hierarchySerializers.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(serializer));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has a serializer (%s)".formatted(type, previous));
        }
    }

    public synchronized EsonElement toEson(Object object) {
        if (object == null) return EsonNull.instance;

        var serializer = this.serializer(object.getClass());

        if (serializer != null) {
            return serializer.toEson(Classes.cast(object), this);
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

        var serializer = this.serializer(type);

        if (serializer != null) {
            return serializer.fromEson(Classes.cast(eson), this);
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

    private <A> EsonTypeSerializer<A, ?> serializer(Class<A> type) {
        var serializer = this.serializers.get(type);

        if (serializer != null) {
            return (EsonTypeSerializer<A, ?>) serializer;
        }

        return (EsonTypeSerializer<A, ?>) this.cachedHierarchySerializers.computeIfAbsent(type, t -> Types.hierarchy(type)
            .map(this.hierarchySerializers::get)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(() -> type.isArray() ? Classes.cast(new ArraySerializer(type.componentType())) : null)
        );
    }
}

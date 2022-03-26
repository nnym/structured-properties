package net.auoeke.eson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
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
import net.auoeke.eson.parser.Parser;
import net.auoeke.eson.parser.lexer.error.SyntaxException;
import net.auoeke.reflect.Accessor;
import net.auoeke.reflect.Classes;
import net.auoeke.reflect.Constructors;
import net.auoeke.reflect.Fields;
import net.auoeke.reflect.Flags;
import net.auoeke.reflect.Types;

public class Eson {
    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> -1);

    private final Map<Class<?>, EsonAdapter<?, ?>> serializers = new HashMap<>();
    private final Map<Class<?>, EsonAdapter<?, ?>> hierarchySerializers = new HashMap<>();
    private final Map<Class<?>, EsonAdapter<?, ?>> cachedHierarchySerializers = new HashMap<>();
    private final EsonSerializer serializer = new EsonSerializer("    ");

    public Eson() {
        this.registerSerializer(boolean.class, BooleanAdapter.instance);
        this.registerSerializer(byte.class, ByteAdapter.instance);
        this.registerSerializer(char.class, CharacterAdapter.instance);
        this.registerSerializer(short.class, ShortAdapter.instance);
        this.registerSerializer(int.class, IntegerAdapter.instance);
        this.registerSerializer(long.class, LongAdapter.instance);
        this.registerSerializer(float.class, FloatAdapter.instance);
        this.registerSerializer(double.class, DoubleAdapter.instance);
        this.registerSerializer(Boolean.class, BooleanAdapter.instance);
        this.registerSerializer(Byte.class, ByteAdapter.instance);
        this.registerSerializer(Character.class, CharacterAdapter.instance);
        this.registerSerializer(Short.class, ShortAdapter.instance);
        this.registerSerializer(Integer.class, IntegerAdapter.instance);
        this.registerSerializer(Long.class, LongAdapter.instance);
        this.registerSerializer(Float.class, FloatAdapter.instance);
        this.registerSerializer(Double.class, DoubleAdapter.instance);
        this.registerSerializer(BigInteger.class, BigIntegerAdapter.instance);
        this.registerSerializer(BigDecimal.class, BigDecimalAdapter.instance);

        this.registerHierarchySerializer(EsonElement.class, EsonElementAdapter.instance);
        this.registerHierarchySerializer(CharSequence.class, CharSequenceAdapter.instance);
        this.registerHierarchySerializer(Collection.class, CollectionAdapter.instance);
        this.registerHierarchySerializer(Map.class, MapAdapter.instance);
    }

    public static EsonElement parse(String eson, Option... options) {
        return new Parser(eson, options).parse();
    }

    public static EsonElement parse(byte[] eson) {
        return parse(new String(eson));
    }

    public static EsonElement parseResource(URL eson) {
        return parseResource(eson, "file".equals(eson.getProtocol()) ? eson.getPath() : eson.toString());
    }

    public static EsonElement parseResource(Path eson) {
        return parseResource(eson.toUri().toURL(), eson.toString());
    }

    private static EsonElement parseResource(URL eson, String source) {
        try {
            try (var stream = eson.openStream()) {
                return parse(stream.readAllBytes());
            }
        } catch (SyntaxException exception) {
            exception.source = source;

            throw exception;
        }
    }

    public <A> void registerSerializer(Class<A> type, EsonAdapter<A, ?> serializer) {
        var previous = this.serializers.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(serializer));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has a serializer (%s)".formatted(type, previous));
        }
    }

    public <A> void registerHierarchySerializer(Class<A> type, EsonAdapter<? extends A, ?> serializer) {
        var previous = this.hierarchySerializers.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(serializer));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has a serializer (%s)".formatted(type, previous));
        }
    }

    public EsonElement toEson(Object object) {
        if (object == null) return EsonNull.instance;

        var serializer = this.adapter(object.getClass());

        if (serializer != null) {
            return serializer.toEson(Classes.cast(object), this);
        }

        var map = new EsonMap();
        Fields.all(object)
            .filter(field -> !Flags.any(field, Flags.STATIC | Flags.SYNTHETIC | Flags.TRANSIENT))
            .forEach(field -> map.put(field.getName(), this.toEson(Accessor.get(object, field))));

        return map;
    }

    public <T> T fromEson(Class<T> type, EsonElement eson) {
        if (type == null) {
            return (T) this.fromEson(eson);
        }

        if (eson == EsonNull.instance) {
            if (type.isPrimitive()) {
                throw new ClassCastException("cannot convert null to primitive");
            }

            return null;
        }

        var serializer = this.adapter(type);

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

    public Object fromEson(EsonElement eson) {
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

    public Map<String, Object> fromEson(EsonMap map) {
        var hashMap = new LinkedHashMap<String, Object>();
        map.forEach((key, value) -> hashMap.put(key, this.fromEson(value)));

        return hashMap;
    }

    public synchronized Appendable serialize(Appendable output, EsonElement element) {
        return this.serializer.serialize(output, element);
    }

    private <A> EsonAdapter<A, ?> adapter(Class<A> type) {
        var serializer = this.serializers.get(type);

        if (serializer != null) {
            return (EsonAdapter<A, ?>) serializer;
        }

        return (EsonAdapter<A, ?>) this.cachedHierarchySerializers.computeIfAbsent(type, t -> Types.hierarchy(type)
            .map(this.hierarchySerializers::get)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseGet(() -> type.isArray() ? Classes.cast(new ArrayAdapter(type.componentType())) : null)
        );
    }

    public enum Option {
        RETAIN_COMMENTS
    }
}

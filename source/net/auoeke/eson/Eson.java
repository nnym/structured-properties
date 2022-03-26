package net.auoeke.eson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
import net.auoeke.reflect.Classes;

public class Eson {
    private final Map<Class<?>, PolymorphicToEsonAdapter<?, ?>> toEsonAdapters = new HashMap<>();
    private final List<PolymorphicToEsonAdapter<?, ?>> polymorphicToEsonAdapters = new ArrayList<>();
    private final Map<Class<?>, PolymorphicToEsonAdapter<?, ?>> cachedToEsonAdapters = new HashMap<>();
    private final Map<AdapterKey, PolymorphicFromEsonAdapter<?, ?>> fromEsonAdapters = new HashMap<>();
    private final Map<Class<? extends EsonElement>, List<PolymorphicFromEsonAdapter<?, ?>>> polymorphicFromEsonAdapters = new LinkedHashMap<>();
    private final Map<AdapterKey, PolymorphicFromEsonAdapter<?, ?>> cachedFromEsonAdapters = new HashMap<>();
    private final EsonSerializer adapter = new EsonSerializer("    ");

    public Eson() {
        this.adapt(EsonMap.class, ObjectAdapter.instance);
        this.adapt(EsonString.class, EnumAdapter.instance);
        this.adapt(EsonArray.class, ArrayAdapter.instance);

        this.adapt(boolean.class, EsonBoolean.class, BooleanAdapter.instance);
        this.adapt(byte.class, EsonInteger.class, ByteAdapter.instance);
        this.adapt(char.class, EsonString.class, CharacterAdapter.instance);
        this.adapt(short.class, EsonInteger.class, ShortAdapter.instance);
        this.adapt(int.class, EsonInteger.class, IntegerAdapter.instance);
        this.adapt(long.class, EsonInteger.class, LongAdapter.instance);
        this.adapt(float.class, EsonFloat.class, FloatAdapter.instance);
        this.adapt(double.class, EsonFloat.class, DoubleAdapter.instance);
        this.adapt(Boolean.class, EsonBoolean.class, BooleanAdapter.instance);
        this.adapt(Byte.class, EsonInteger.class, ByteAdapter.instance);
        this.adapt(Character.class, EsonString.class, CharacterAdapter.instance);
        this.adapt(Short.class, EsonInteger.class, ShortAdapter.instance);
        this.adapt(Integer.class, EsonInteger.class, IntegerAdapter.instance);
        this.adapt(Long.class, EsonInteger.class, LongAdapter.instance);
        this.adapt(Float.class, EsonFloat.class, FloatAdapter.instance);
        this.adapt(Double.class, EsonFloat.class, DoubleAdapter.instance);
        this.adapt(BigInteger.class, EsonInteger.class, BigIntegerAdapter.instance);
        this.adapt(BigDecimal.class, EsonFloat.class, BigDecimalAdapter.instance);

        this.adaptBase(EsonElement.class, EsonElement.class, EsonElementAdapter.instance);
        this.adaptBase(CharSequence.class, EsonString.class, CharSequenceAdapter.instance);
        this.adaptBase(Collection.class, EsonArray.class, CollectionAdapter.instance);
        this.adaptBase(Map.class, EsonMap.class, MapAdapter.instance);
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

    public <A> void adaptToEson(Class<A> type, ToEsonAdapter<? extends A, ?> adapter) {
        var previous = this.toEsonAdapters.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(adapter));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has a ToEsonAdapter (%s)".formatted(type, previous));
        }
    }

    public void adaptToEson(PolymorphicToEsonAdapter<?, ?> adapter) {
        this.polymorphicToEsonAdapters.add(Objects.requireNonNull(adapter));
    }

    public <A, B extends EsonElement> void adaptBaseToEson(Class<A> type, EsonAdapter<? extends A, B> adapter) {
        this.adaptToEson(new PolymorphicToEsonAdapter<A, B>() {
            @Override public boolean accept(Class<?> t) {
                return type.isAssignableFrom(t);
            }

            @Override public B toEson(A a, Eson serializer) {
                return adapter.toEson(Classes.cast(a), serializer);
            }
        });
    }

    public <A, B extends EsonElement> void adaptFromEson(Class<A> type, Class<B> esonType, FromEsonAdapter<A, B> adapter) {
        var previous = this.fromEsonAdapters.putIfAbsent(new AdapterKey(Objects.requireNonNull(type), Objects.requireNonNull(esonType)), Objects.requireNonNull(adapter));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has an adapter from %s (%s)".formatted(type, esonType.getSimpleName(), previous));
        }
    }

    public <B extends EsonElement> void adaptFromEson(Class<B> esonType, PolymorphicFromEsonAdapter<?, B> adapter) {
        this.polymorphicFromEsonAdapters.computeIfAbsent(Objects.requireNonNull(esonType), e -> new ArrayList<>()).add(Objects.requireNonNull(adapter));
    }

    public <A, B extends EsonElement> void adaptBaseFromEson(Class<A> type, Class<B> esonType, FromEsonAdapter<? extends A, B> adapter) {
        this.adaptFromEson(esonType, new PolymorphicFromEsonAdapter<A, B>() {
            @Override public boolean accept(Class<?> t) {
                return type.isAssignableFrom(t);
            }

            @Override public A fromEson(Class<A> type, B eson, Eson serializer) {
                return adapter.fromEson(eson, serializer);
            }
        });
    }

    public <A, B extends EsonElement> void adapt(Class<A> type, Class<B> esonType, EsonAdapter<A, B> adapter) {
        this.adaptToEson(type, adapter);
        this.adaptFromEson(type, esonType, adapter);
    }

    public <A, B extends EsonElement> void adapt(Class<B> esonType, PolymorphicEsonAdapter<A, B> adapter) {
        this.adaptToEson(adapter);
        this.adaptFromEson(esonType, adapter);
    }

    public <A, B extends EsonElement> void adaptBase(Class<A> type, Class<B> esonType, EsonAdapter<? extends A, B> adapter) {
        this.adaptBaseToEson(type, adapter);
        this.adaptBaseFromEson(type, esonType, adapter);
    }

    public EsonElement toEson(Object object) {
        return object == null ? EsonNull.instance : this.toEsonAdapter(object.getClass()).toEson(Classes.cast(object), this);
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

        return this.fromEsonAdapter(type, eson.getClass()).fromEson(type, Classes.cast(eson), this);
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
        return this.adapter.serialize(output, element);
    }

    private <A, B extends EsonElement> PolymorphicToEsonAdapter<A, B> toEsonAdapter(Class<A> type) {
        return (PolymorphicToEsonAdapter<A, B>) this.cachedToEsonAdapters.computeIfAbsent(type, t -> Optional.ofNullable(this.toEsonAdapters.get(t))
            .orElseGet(() -> Classes.cast(this.polymorphicToEsonAdapters.stream()
                .filter(adapter -> adapter.accept(type))
                .reduce((a, b) -> b)
            .orElseThrow(() -> new IllegalArgumentException("no %s -> eson adapter was found".formatted(type.getName())))))
        );
    }

    private <A, B extends EsonElement> PolymorphicFromEsonAdapter<A, B> fromEsonAdapter(Class<A> type, Class<B> esonType) {
        return (PolymorphicFromEsonAdapter<A, B>) this.cachedFromEsonAdapters.computeIfAbsent(new AdapterKey(type, esonType), k -> Optional.ofNullable(this.fromEsonAdapters.get(k))
            .orElseGet(() -> Classes.cast(Optional.ofNullable(this.polymorphicFromEsonAdapters.get(esonType)).flatMap(adapters -> adapters.stream()
                .filter(adapter -> adapter.accept(type))
                .reduce((a, b) -> b)
            ).orElseThrow(() -> new IllegalArgumentException("no %s -> %s adapter was found".formatted(type.getName(), esonType.getSimpleName())))))
        );
    }

    public enum Option {
        RETAIN_COMMENTS
    }

    private record AdapterKey(Class<?> type, Class<?> esonType) {}
}

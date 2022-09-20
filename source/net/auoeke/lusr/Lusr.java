package net.auoeke.lusr;

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
import net.auoeke.lusr.element.LusrArray;
import net.auoeke.lusr.element.LusrBoolean;
import net.auoeke.lusr.element.LusrElement;
import net.auoeke.lusr.element.LusrFloat;
import net.auoeke.lusr.element.LusrInteger;
import net.auoeke.lusr.element.LusrMap;
import net.auoeke.lusr.element.LusrNull;
import net.auoeke.lusr.element.LusrPair;
import net.auoeke.lusr.element.LusrPrimitive;
import net.auoeke.lusr.element.LusrString;
import net.auoeke.lusr.parser.Parser;
import net.auoeke.lusr.parser.lexer.error.SyntaxException;
import net.auoeke.reflect.Classes;

public class Lusr {
    private final Map<Class<?>, PolymorphicToLusrAdapter<?, ?>> toLusrAdapters = new HashMap<>();
    private final Map<AdapterKey, PolymorphicFromLusrAdapter<?, ?>> fromLusrAdapters = new HashMap<>();
    private final List<PolymorphicToLusrAdapter<?, ?>> polymorphicToLusrAdapters = new ArrayList<>();
    private final Map<Class<? extends LusrElement>, List<PolymorphicFromLusrAdapter<?, ?>>> polymorphicFromLusrAdapters = new LinkedHashMap<>();
    private final Map<Class<?>, PolymorphicToLusrAdapter<?, ?>> cachedToLusrAdapters = new HashMap<>();
    private final Map<AdapterKey, PolymorphicFromLusrAdapter<?, ?>> cachedFromLusrAdapters = new HashMap<>();
    private final LusrSerializer adapter = new LusrSerializer("    ");

    public Lusr() {
        this.adapt(LusrMap.class, ObjectAdapter.instance);
        this.adapt(LusrString.class, EnumAdapter.instance);
        this.adapt(LusrArray.class, ArrayAdapter.instance);

        this.adapt(boolean.class, LusrBoolean.class, BooleanAdapter.instance);
        this.adapt(byte.class, LusrInteger.class, ByteAdapter.instance);
        this.adapt(char.class, LusrString.class, CharacterAdapter.instance);
        this.adapt(short.class, LusrInteger.class, ShortAdapter.instance);
        this.adapt(int.class, LusrInteger.class, IntegerAdapter.instance);
        this.adapt(long.class, LusrInteger.class, LongAdapter.instance);
        this.adapt(float.class, LusrFloat.class, FloatAdapter.instance);
        this.adapt(double.class, LusrFloat.class, DoubleAdapter.instance);
        this.adapt(Boolean.class, LusrBoolean.class, BooleanAdapter.instance);
        this.adapt(Byte.class, LusrInteger.class, ByteAdapter.instance);
        this.adapt(Character.class, LusrString.class, CharacterAdapter.instance);
        this.adapt(Short.class, LusrInteger.class, ShortAdapter.instance);
        this.adapt(Integer.class, LusrInteger.class, IntegerAdapter.instance);
        this.adapt(Long.class, LusrInteger.class, LongAdapter.instance);
        this.adapt(Float.class, LusrFloat.class, FloatAdapter.instance);
        this.adapt(Double.class, LusrFloat.class, DoubleAdapter.instance);
        this.adapt(BigInteger.class, LusrInteger.class, BigIntegerAdapter.instance);
        this.adapt(BigDecimal.class, LusrFloat.class, BigDecimalAdapter.instance);

        this.adaptFromLusr(float.class, LusrInteger.class, (lusr, serializer) -> lusr.floatValue());
        this.adaptFromLusr(double.class, LusrInteger.class, (lusr, serializer) -> lusr.doubleValue());
        this.adaptFromLusr(Float.class, LusrInteger.class, (lusr, serializer) -> lusr.floatValue());
        this.adaptFromLusr(Double.class, LusrInteger.class, (lusr, serializer) -> lusr.doubleValue());
        this.adaptFromLusr(BigDecimal.class, LusrInteger.class, (lusr, serializer) -> new BigDecimal(lusr.value()));

        this.adaptBase(LusrElement.class, LusrElement.class, LusrElementAdapter.instance);
        this.adaptBase(CharSequence.class, LusrString.class, CharSequenceAdapter.instance);
        this.adaptBase(Collection.class, LusrArray.class, CollectionAdapter.instance);
        this.adaptBase(Map.class, LusrMap.class, MapAdapter.instance);
    }

    public static LusrElement parse(String lusr, Option... options) {
        return new Parser(lusr, options).parse();
    }

    public static LusrElement parse(byte[] lusr) {
        return parse(new String(lusr));
    }

    public static LusrElement parseResource(URL lusr) {
        return parseResource(lusr, "file".equals(lusr.getProtocol()) ? lusr.getPath() : lusr.toString());
    }

    public static LusrElement parseResource(Path lusr) {
        return parseResource(lusr.toUri().toURL(), lusr.toString());
    }

    private static LusrElement parseResource(URL lusr, String source) {
        try {
            try (var stream = lusr.openStream()) {
                return parse(stream.readAllBytes());
            }
        } catch (SyntaxException exception) {
            exception.source = source;

            throw exception;
        }
    }

    public <A> void adaptToLusr(Class<A> type, ToLusrAdapter<? extends A, ?> adapter) {
        var previous = this.toLusrAdapters.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(adapter));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has a ToLusrAdapter (%s)".formatted(type, previous));
        }
    }

    public void adaptToLusr(PolymorphicToLusrAdapter<?, ?> adapter) {
        this.polymorphicToLusrAdapters.add(Objects.requireNonNull(adapter));
    }

    public <A, B extends LusrElement> void adaptBaseToLusr(Class<A> type, LusrAdapter<? extends A, B> adapter) {
        this.adaptToLusr(new PolymorphicToLusrAdapter<A, B>() {
            @Override public boolean accept(Class<?> t) {
                return type.isAssignableFrom(t);
            }

            @Override public B toLusr(A a, Lusr serializer) {
                return adapter.toLusr(Classes.cast(a), serializer);
            }
        });
    }

    public <A, B extends LusrElement> void adaptFromLusr(Class<A> type, Class<B> lusrType, FromLusrAdapter<A, B> adapter) {
        var previous = this.fromLusrAdapters.putIfAbsent(new AdapterKey(Objects.requireNonNull(type), Objects.requireNonNull(lusrType)), Objects.requireNonNull(adapter));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has an adapter from %s (%s)".formatted(type, lusrType.getSimpleName(), previous));
        }
    }

    public <B extends LusrElement> void adaptFromLusr(Class<B> lusrType, PolymorphicFromLusrAdapter<?, B> adapter) {
        this.polymorphicFromLusrAdapters.computeIfAbsent(Objects.requireNonNull(lusrType), e -> new ArrayList<>()).add(Objects.requireNonNull(adapter));
    }

    public <A, B extends LusrElement> void adaptBaseFromLusr(Class<A> type, Class<B> lusrType, FromLusrAdapter<? extends A, B> adapter) {
        this.adaptFromLusr(lusrType, new PolymorphicFromLusrAdapter<A, B>() {
            @Override public boolean accept(Class<?> t) {
                return type.isAssignableFrom(t);
            }

            @Override public A fromLusr(Class<A> type, B lusr, Lusr serializer) {
                return adapter.fromLusr(lusr, serializer);
            }
        });
    }

    public <A, B extends LusrElement> void adapt(Class<A> type, Class<B> lusrType, LusrAdapter<A, B> adapter) {
        this.adaptToLusr(type, adapter);
        this.adaptFromLusr(type, lusrType, adapter);
    }

    public <A, B extends LusrElement> void adapt(Class<B> lusrType, PolymorphicLusrAdapter<A, B> adapter) {
        this.adaptToLusr(adapter);
        this.adaptFromLusr(lusrType, adapter);
    }

    public <A, B extends LusrElement> void adaptBase(Class<A> type, Class<B> lusrType, LusrAdapter<? extends A, B> adapter) {
        this.adaptBaseToLusr(type, adapter);
        this.adaptBaseFromLusr(type, lusrType, adapter);
    }

    public LusrElement toLusr(Object object) {
        return object == null ? LusrNull.instance : this.toLusrAdapter(object.getClass()).toLusr(Classes.cast(object), this);
    }

    public <T> T fromLusr(Class<T> type, LusrElement lusr) {
        if (type == null) {
            return (T) this.fromLusr(lusr);
        }

        if (lusr == LusrNull.instance) {
            if (type.isPrimitive()) {
                throw new ClassCastException("cannot convert null to primitive");
            }

            return null;
        }

        return this.fromLusrAdapter(type, lusr.getClass()).fromLusr(type, Classes.cast(lusr), this);
    }

    public Object fromLusr(LusrElement element) {
        if (element == LusrNull.instance) return null;
        if (element instanceof LusrBoolean boolea) return this.fromLusr(boolea);
        if (element instanceof LusrInteger integer) return this.fromLusr(integer);
        if (element instanceof LusrFloat floa) return this.fromLusr(floa);
        if (element instanceof LusrString string) return this.fromLusr(string);
        if (element instanceof LusrArray array) return this.fromLusr(array);
        if (element instanceof LusrMap map) return this.fromLusr(map);
        if (element instanceof LusrPair pair) return this.fromLusr(pair);

        return null;
    }

    public boolean fromLusr(LusrBoolean boolea) {
        return boolea.value;
    }

    public BigInteger fromLusr(LusrInteger integer) {
        return integer.value();
    }

    public BigDecimal fromLusr(LusrFloat floa) {
        return floa.value();
    }

    public String fromLusr(LusrString string) {
        return string.value;
    }

    public Map<String, Object> fromLusr(LusrPair pair) {
        var map = new LinkedHashMap<String, Object>();
        map.put(this.fromLusr(LusrPrimitive.class, pair.a).stringValue(), this.fromLusr(pair.b));

        return map;
    }

    public List<Object> fromLusr(LusrArray array) {
        return array.stream().map(this::fromLusr).collect(Collectors.toList());
    }

    public Map<String, Object> fromLusr(LusrMap map) {
        var hashMap = new LinkedHashMap<String, Object>();
        map.forEach((key, value) -> hashMap.put(key, this.fromLusr(value)));

        return hashMap;
    }

    public synchronized Appendable serialize(Appendable output, LusrElement element) {
        return this.adapter.serialize(output, element);
    }

    private <A, B extends LusrElement> PolymorphicToLusrAdapter<A, B> toLusrAdapter(Class<A> type) {
        return (PolymorphicToLusrAdapter<A, B>) this.cachedToLusrAdapters.computeIfAbsent(type, t -> Optional.ofNullable(this.toLusrAdapters.get(t))
            .orElseGet(() -> Classes.cast(this.polymorphicToLusrAdapters.stream()
                .filter(adapter -> adapter.accept(type))
                .reduce((a, b) -> b)
            .orElseThrow(() -> new IllegalArgumentException("no %s -> lusr adapter was found".formatted(type.getName())))))
        );
    }

    private <A, B extends LusrElement> PolymorphicFromLusrAdapter<A, B> fromLusrAdapter(Class<A> type, Class<B> lusrType) {
        return (PolymorphicFromLusrAdapter<A, B>) this.cachedFromLusrAdapters.computeIfAbsent(new AdapterKey(type, lusrType), k -> Optional.ofNullable(this.fromLusrAdapters.get(k))
            .orElseGet(() -> Classes.cast(Optional.ofNullable(this.polymorphicFromLusrAdapters.get(lusrType)).flatMap(adapters -> adapters.stream()
                .filter(adapter -> adapter.accept(type))
                .reduce((a, b) -> b)
            ).orElseThrow(() -> new IllegalArgumentException("no %s -> %s adapter was found".formatted(lusrType.getSimpleName(), type.getName())))))
        );
    }

    public enum Option {
        RETAIN_COMMENTS
    }

    private record AdapterKey(Class<?> type, Class<?> lusrType) {}
}

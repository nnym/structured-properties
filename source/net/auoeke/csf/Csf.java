package net.auoeke.csf;

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
import net.auoeke.csf.element.CsfArray;
import net.auoeke.csf.element.CsfBoolean;
import net.auoeke.csf.element.CsfElement;
import net.auoeke.csf.element.CsfFloat;
import net.auoeke.csf.element.CsfInteger;
import net.auoeke.csf.element.CsfMap;
import net.auoeke.csf.element.CsfNull;
import net.auoeke.csf.element.CsfPair;
import net.auoeke.csf.element.CsfPrimitive;
import net.auoeke.csf.element.CsfString;
import net.auoeke.csf.parser.Parser;
import net.auoeke.csf.parser.lexer.error.SyntaxException;
import net.auoeke.reflect.Classes;

public class Csf {
    private final Map<Class<?>, PolymorphicToCsfAdapter<?, ?>> toCsfAdapters = new HashMap<>();
    private final Map<AdapterKey, PolymorphicFromCsfAdapter<?, ?>> fromCsfAdapters = new HashMap<>();
    private final List<PolymorphicToCsfAdapter<?, ?>> polymorphicToCsfAdapters = new ArrayList<>();
    private final Map<Class<? extends CsfElement>, List<PolymorphicFromCsfAdapter<?, ?>>> polymorphicFromCsfAdapters = new LinkedHashMap<>();
    private final Map<Class<?>, PolymorphicToCsfAdapter<?, ?>> cachedToCsfAdapters = new HashMap<>();
    private final Map<AdapterKey, PolymorphicFromCsfAdapter<?, ?>> cachedFromCsfAdapters = new HashMap<>();
    private final CsfSerializer adapter = new CsfSerializer("    ");

    public Csf() {
        this.adapt(CsfMap.class, ObjectAdapter.instance);
        this.adapt(CsfString.class, EnumAdapter.instance);
        this.adapt(CsfArray.class, ArrayAdapter.instance);

        this.adapt(boolean.class, CsfBoolean.class, BooleanAdapter.instance);
        this.adapt(byte.class, CsfInteger.class, ByteAdapter.instance);
        this.adapt(char.class, CsfString.class, CharacterAdapter.instance);
        this.adapt(short.class, CsfInteger.class, ShortAdapter.instance);
        this.adapt(int.class, CsfInteger.class, IntegerAdapter.instance);
        this.adapt(long.class, CsfInteger.class, LongAdapter.instance);
        this.adapt(float.class, CsfFloat.class, FloatAdapter.instance);
        this.adapt(double.class, CsfFloat.class, DoubleAdapter.instance);
        this.adapt(Boolean.class, CsfBoolean.class, BooleanAdapter.instance);
        this.adapt(Byte.class, CsfInteger.class, ByteAdapter.instance);
        this.adapt(Character.class, CsfString.class, CharacterAdapter.instance);
        this.adapt(Short.class, CsfInteger.class, ShortAdapter.instance);
        this.adapt(Integer.class, CsfInteger.class, IntegerAdapter.instance);
        this.adapt(Long.class, CsfInteger.class, LongAdapter.instance);
        this.adapt(Float.class, CsfFloat.class, FloatAdapter.instance);
        this.adapt(Double.class, CsfFloat.class, DoubleAdapter.instance);
        this.adapt(BigInteger.class, CsfInteger.class, BigIntegerAdapter.instance);
        this.adapt(BigDecimal.class, CsfFloat.class, BigDecimalAdapter.instance);

        this.adaptFromCsf(float.class, CsfInteger.class, (csf, serializer) -> csf.floatValue());
        this.adaptFromCsf(double.class, CsfInteger.class, (csf, serializer) -> csf.doubleValue());
        this.adaptFromCsf(Float.class, CsfInteger.class, (csf, serializer) -> csf.floatValue());
        this.adaptFromCsf(Double.class, CsfInteger.class, (csf, serializer) -> csf.doubleValue());
        this.adaptFromCsf(BigDecimal.class, CsfInteger.class, (csf, serializer) -> new BigDecimal(csf.value()));

        this.adaptBase(CsfElement.class, CsfElement.class, CsfElementAdapter.instance);
        this.adaptBase(CharSequence.class, CsfString.class, CharSequenceAdapter.instance);
        this.adaptBase(Collection.class, CsfArray.class, CollectionAdapter.instance);
        this.adaptBase(Map.class, CsfMap.class, MapAdapter.instance);
    }

    public static CsfElement parse(String csf, Option... options) {
        return new Parser(csf, options).parse();
    }

    public static CsfElement parse(byte[] csf) {
        return parse(new String(csf));
    }

    public static CsfElement parseResource(URL csf) {
        return parseResource(csf, "file".equals(csf.getProtocol()) ? csf.getPath() : csf.toString());
    }

    public static CsfElement parseResource(Path csf) {
        return parseResource(csf.toUri().toURL(), csf.toString());
    }

    private static CsfElement parseResource(URL csf, String source) {
        try {
            try (var stream = csf.openStream()) {
                return parse(stream.readAllBytes());
            }
        } catch (SyntaxException exception) {
            exception.source = source;

            throw exception;
        }
    }

    public <A> void adaptToCsf(Class<A> type, ToCsfAdapter<? extends A, ?> adapter) {
        var previous = this.toCsfAdapters.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(adapter));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has a ToCsfAdapter (%s)".formatted(type, previous));
        }
    }

    public void adaptToCsf(PolymorphicToCsfAdapter<?, ?> adapter) {
        this.polymorphicToCsfAdapters.add(Objects.requireNonNull(adapter));
    }

    public <A, B extends CsfElement> void adaptBaseToCsf(Class<A> type, CsfAdapter<? extends A, B> adapter) {
        this.adaptToCsf(new PolymorphicToCsfAdapter<A, B>() {
            @Override public boolean accept(Class<?> t) {
                return type.isAssignableFrom(t);
            }

            @Override public B toCsf(A a, Csf serializer) {
                return adapter.toCsf(Classes.cast(a), serializer);
            }
        });
    }

    public <A, B extends CsfElement> void adaptFromCsf(Class<A> type, Class<B> csfType, FromCsfAdapter<A, B> adapter) {
        var previous = this.fromCsfAdapters.putIfAbsent(new AdapterKey(Objects.requireNonNull(type), Objects.requireNonNull(csfType)), Objects.requireNonNull(adapter));

        if (previous != null) {
            throw new IllegalArgumentException("%s already has an adapter from %s (%s)".formatted(type, csfType.getSimpleName(), previous));
        }
    }

    public <B extends CsfElement> void adaptFromCsf(Class<B> csfType, PolymorphicFromCsfAdapter<?, B> adapter) {
        this.polymorphicFromCsfAdapters.computeIfAbsent(Objects.requireNonNull(csfType), e -> new ArrayList<>()).add(Objects.requireNonNull(adapter));
    }

    public <A, B extends CsfElement> void adaptBaseFromCsf(Class<A> type, Class<B> csfType, FromCsfAdapter<? extends A, B> adapter) {
        this.adaptFromCsf(csfType, new PolymorphicFromCsfAdapter<A, B>() {
            @Override public boolean accept(Class<?> t) {
                return type.isAssignableFrom(t);
            }

            @Override public A fromCsf(Class<A> type, B csf, Csf serializer) {
                return adapter.fromCsf(csf, serializer);
            }
        });
    }

    public <A, B extends CsfElement> void adapt(Class<A> type, Class<B> csfType, CsfAdapter<A, B> adapter) {
        this.adaptToCsf(type, adapter);
        this.adaptFromCsf(type, csfType, adapter);
    }

    public <A, B extends CsfElement> void adapt(Class<B> csfType, PolymorphicCsfAdapter<A, B> adapter) {
        this.adaptToCsf(adapter);
        this.adaptFromCsf(csfType, adapter);
    }

    public <A, B extends CsfElement> void adaptBase(Class<A> type, Class<B> csfType, CsfAdapter<? extends A, B> adapter) {
        this.adaptBaseToCsf(type, adapter);
        this.adaptBaseFromCsf(type, csfType, adapter);
    }

    public CsfElement toCsf(Object object) {
        return object == null ? CsfNull.instance : this.toCsfAdapter(object.getClass()).toCsf(Classes.cast(object), this);
    }

    public <T> T fromCsf(Class<T> type, CsfElement csf) {
        if (type == null) {
            return (T) this.fromCsf(csf);
        }

        if (csf == CsfNull.instance) {
            if (type.isPrimitive()) {
                throw new ClassCastException("cannot convert null to primitive");
            }

            return null;
        }

        return this.fromCsfAdapter(type, csf.getClass()).fromCsf(type, Classes.cast(csf), this);
    }

    public Object fromCsf(CsfElement element) {
        if (element == CsfNull.instance) return null;
        if (element instanceof CsfBoolean boolea) return this.fromCsf(boolea);
        if (element instanceof CsfInteger integer) return this.fromCsf(integer);
        if (element instanceof CsfFloat floa) return this.fromCsf(floa);
        if (element instanceof CsfString string) return this.fromCsf(string);
        if (element instanceof CsfArray array) return this.fromCsf(array);
        if (element instanceof CsfMap map) return this.fromCsf(map);
        if (element instanceof CsfPair pair) return this.fromCsf(pair);

        return null;
    }

    public boolean fromCsf(CsfBoolean boolea) {
        return boolea.value;
    }

    public BigInteger fromCsf(CsfInteger integer) {
        return integer.value();
    }

    public BigDecimal fromCsf(CsfFloat floa) {
        return floa.value();
    }

    public String fromCsf(CsfString string) {
        return string.value;
    }

    public Map<String, Object> fromCsf(CsfPair pair) {
        var map = new LinkedHashMap<String, Object>();
        map.put(this.fromCsf(CsfPrimitive.class, pair.a).stringValue(), this.fromCsf(pair.b));

        return map;
    }

    public List<Object> fromCsf(CsfArray array) {
        return array.stream().map(this::fromCsf).collect(Collectors.toList());
    }

    public Map<String, Object> fromCsf(CsfMap map) {
        var hashMap = new LinkedHashMap<String, Object>();
        map.forEach((key, value) -> hashMap.put(key, this.fromCsf(value)));

        return hashMap;
    }

    public synchronized Appendable serialize(Appendable output, CsfElement element) {
        return this.adapter.serialize(output, element);
    }

    private <A, B extends CsfElement> PolymorphicToCsfAdapter<A, B> toCsfAdapter(Class<A> type) {
        return (PolymorphicToCsfAdapter<A, B>) this.cachedToCsfAdapters.computeIfAbsent(type, t -> Optional.ofNullable(this.toCsfAdapters.get(t))
            .orElseGet(() -> Classes.cast(this.polymorphicToCsfAdapters.stream()
                .filter(adapter -> adapter.accept(type))
                .reduce((a, b) -> b)
            .orElseThrow(() -> new IllegalArgumentException("no %s -> csf adapter was found".formatted(type.getName())))))
        );
    }

    private <A, B extends CsfElement> PolymorphicFromCsfAdapter<A, B> fromCsfAdapter(Class<A> type, Class<B> csfType) {
        return (PolymorphicFromCsfAdapter<A, B>) this.cachedFromCsfAdapters.computeIfAbsent(new AdapterKey(type, csfType), k -> Optional.ofNullable(this.fromCsfAdapters.get(k))
            .orElseGet(() -> Classes.cast(Optional.ofNullable(this.polymorphicFromCsfAdapters.get(csfType)).flatMap(adapters -> adapters.stream()
                .filter(adapter -> adapter.accept(type))
                .reduce((a, b) -> b)
            ).orElseThrow(() -> new IllegalArgumentException("no %s -> %s adapter was found".formatted(csfType.getSimpleName(), type.getName())))))
        );
    }

    public enum Option {
        RETAIN_COMMENTS
    }

    private record AdapterKey(Class<?> type, Class<?> csfType) {}
}

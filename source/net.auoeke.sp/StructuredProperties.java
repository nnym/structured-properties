package net.auoeke.sp;

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
import net.auoeke.reflect.Classes;
import net.auoeke.sp.element.SpArray;
import net.auoeke.sp.element.SpBoolean;
import net.auoeke.sp.element.SpElement;
import net.auoeke.sp.element.SpFloat;
import net.auoeke.sp.element.SpInteger;
import net.auoeke.sp.element.SpMap;
import net.auoeke.sp.element.SpNull;
import net.auoeke.sp.element.SpPair;
import net.auoeke.sp.element.SpPrimitive;
import net.auoeke.sp.element.SpString;
import net.auoeke.sp.internal.ArrayAdapter;
import net.auoeke.sp.internal.BigDecimalAdapter;
import net.auoeke.sp.internal.BigIntegerAdapter;
import net.auoeke.sp.internal.BooleanAdapter;
import net.auoeke.sp.internal.ByteAdapter;
import net.auoeke.sp.internal.CharSequenceAdapter;
import net.auoeke.sp.internal.CharacterAdapter;
import net.auoeke.sp.internal.CollectionAdapter;
import net.auoeke.sp.internal.DoubleAdapter;
import net.auoeke.sp.internal.EnumAdapter;
import net.auoeke.sp.internal.FloatAdapter;
import net.auoeke.sp.internal.IntegerAdapter;
import net.auoeke.sp.internal.LongAdapter;
import net.auoeke.sp.internal.MapAdapter;
import net.auoeke.sp.internal.ObjectAdapter;
import net.auoeke.sp.internal.ShortAdapter;
import net.auoeke.sp.internal.SpElementAdapter;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.ParseResult;
import net.auoeke.sp.source.Parser;
import net.auoeke.sp.source.lexeme.BooleanLexeme;
import net.auoeke.sp.source.lexeme.FloatLexeme;
import net.auoeke.sp.source.lexeme.IntegerLexeme;
import net.auoeke.sp.source.lexeme.NullLexeme;
import net.auoeke.sp.source.lexeme.StringLexeme;
import net.auoeke.sp.source.tree.ArrayTree;
import net.auoeke.sp.source.tree.MapTree;
import net.auoeke.sp.source.tree.PairTree;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.StringTree;
import net.auoeke.sp.source.tree.Tree;

public class StructuredProperties {
	private final Map<Class<?>, PolymorphicToSpAdapter<?, ?>> toSpAdapters = new HashMap<>();
	private final Map<AdapterKey, PolymorphicFromSpAdapter<?, ?>> fromSpAdapters = new HashMap<>();
	private final List<PolymorphicToSpAdapter<?, ?>> polymorphicToSpAdapters = new ArrayList<>();
	private final Map<Class<? extends SpElement>, List<PolymorphicFromSpAdapter<?, ?>>> polymorphicFromSpAdapters = new LinkedHashMap<>();
	private final Map<Class<?>, PolymorphicToSpAdapter<?, ?>> cachedToSpAdapters = new HashMap<>();
	private final Map<AdapterKey, PolymorphicFromSpAdapter<?, ?>> cachedFromSpAdapters = new HashMap<>();
	private final SpSerializer adapter = new SpSerializer("    ");

	public StructuredProperties() {
		this.adapt(SpMap.class, ObjectAdapter.instance);
		this.adapt(SpString.class, EnumAdapter.instance);
		this.adapt(SpArray.class, ArrayAdapter.instance);

		this.adapt(boolean.class, SpBoolean.class, BooleanAdapter.instance);
		this.adapt(byte.class, SpInteger.class, ByteAdapter.instance);
		this.adapt(char.class, SpString.class, CharacterAdapter.instance);
		this.adapt(short.class, SpInteger.class, ShortAdapter.instance);
		this.adapt(int.class, SpInteger.class, IntegerAdapter.instance);
		this.adapt(long.class, SpInteger.class, LongAdapter.instance);
		this.adapt(float.class, SpFloat.class, FloatAdapter.instance);
		this.adapt(double.class, SpFloat.class, DoubleAdapter.instance);
		this.adapt(Boolean.class, SpBoolean.class, BooleanAdapter.instance);
		this.adapt(Byte.class, SpInteger.class, ByteAdapter.instance);
		this.adapt(Character.class, SpString.class, CharacterAdapter.instance);
		this.adapt(Short.class, SpInteger.class, ShortAdapter.instance);
		this.adapt(Integer.class, SpInteger.class, IntegerAdapter.instance);
		this.adapt(Long.class, SpInteger.class, LongAdapter.instance);
		this.adapt(Float.class, SpFloat.class, FloatAdapter.instance);
		this.adapt(Double.class, SpFloat.class, DoubleAdapter.instance);
		this.adapt(BigInteger.class, SpInteger.class, BigIntegerAdapter.instance);
		this.adapt(BigDecimal.class, SpFloat.class, BigDecimalAdapter.instance);

		this.adaptFromSp(float.class, SpInteger.class, (sp, serializer) -> sp.floatValue());
		this.adaptFromSp(double.class, SpInteger.class, (sp, serializer) -> sp.doubleValue());
		this.adaptFromSp(Float.class, SpInteger.class, (sp, serializer) -> sp.floatValue());
		this.adaptFromSp(Double.class, SpInteger.class, (sp, serializer) -> sp.doubleValue());
		this.adaptFromSp(BigDecimal.class, SpInteger.class, (sp, serializer) -> new BigDecimal(sp.value()));

		this.adaptBase(SpElement.class, SpElement.class, SpElementAdapter.instance);
		this.adaptBase(CharSequence.class, SpString.class, CharSequenceAdapter.instance);
		this.adaptBase(Collection.class, SpArray.class, CollectionAdapter.instance);
		this.adaptBase(Map.class, SpMap.class, MapAdapter.instance);
	}

	public static SpElement parse(String source, Option... options) {
		return parse(null, source, options);
	}

	public static SpElement parse(byte[] source) {
		return parse(new String(source));
	}

	public static SpElement parseResource(URL source) {
		return parseResource(source, "file".equals(source.getProtocol()) ? source.getPath() : source.toString());
	}

	public static SpElement parseResource(Path source) {
		return parseResource(source.toUri().toURL(), source.toString());
	}

	public static SpElement toElement(ParseResult result) {
		return toElement(result.success());
	}

	private static SpElement parseResource(URL source, String location) {
		try (var stream = source.openStream()) {
			return parse(location, new String(stream.readAllBytes()));
		}
	}

	private static SpElement parse(String location, String source, Option... options) {
		return toElement(Parser.parse(location, source));
	}

	private static SpElement toElement(Tree tree) {
		return tree.accept(new NodeTransformer<>() {
			@Override public SpElement transform(SourceUnit node) {
				return node.stream().filter(Node::isValue).findFirst().map(n -> {
					var element = n.accept(this);

					if (element.isPair()) {
						var map = new SpMap();
						var pair = (SpPair) element;
						map.put(pair.a.toString(), pair.b);

						return map;
					}

					return element;
				}).orElseGet(SpMap::new);
			}

			@Override public SpElement transform(StringTree node) {
				return new SpString(node.stringValue());
			}

			@Override public SpElement transform(PairTree node) {
				return new SpPair(node.a().accept(this), node.b().accept(this));
			}

			@Override public SpElement transform(ArrayTree node) {
				var array = new SpArray();
				node.elements.forEach(element -> array.add(element.accept(this)));

				return array;
			}

			@Override public SpElement transform(MapTree node) {
				var map = new SpMap();
				node.pairs.forEach((key, pair) -> map.put(key, pair.b().accept(this)));

				return map;
			}

			@Override public SpElement transform(NullLexeme node) {
				return SpNull.instance;
			}

			@Override public SpElement transform(BooleanLexeme node) {
				return SpBoolean.of(node.value);
			}

			@Override public SpElement transform(IntegerLexeme node) {
				return new SpInteger(node.value);
			}

			@Override public SpElement transform(FloatLexeme node) {
				return new SpFloat(node.value);
			}

			@Override public SpElement transform(StringLexeme node) {
				return new SpString(node.value);
			}
		});
	}

	public <A> void adaptToSp(Class<A> type, ToSpAdapter<? extends A, ?> adapter) {
		var previous = this.toSpAdapters.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(adapter));

		if (previous != null) {
			throw new IllegalArgumentException("%s already has a ToSpAdapter (%s)".formatted(type, previous));
		}
	}

	public void adaptToSp(PolymorphicToSpAdapter<?, ?> adapter) {
		this.polymorphicToSpAdapters.add(Objects.requireNonNull(adapter));
	}

	public <A, B extends SpElement> void adaptBaseToSp(Class<A> type, ToSpAdapter<? extends A, B> adapter) {
		this.adaptToSp(new PolymorphicToSpAdapter<A, B>() {
			@Override public boolean accept(Class<?> t) {
				return type.isAssignableFrom(t);
			}

			@Override public B toSp(A a, StructuredProperties serializer) {
				return adapter.toSp(Classes.cast(a), serializer);
			}
		});
	}

	public <A, B extends SpElement> void adaptFromSp(Class<A> type, Class<B> spType, FromSpAdapter<A, B> adapter) {
		var previous = this.fromSpAdapters.putIfAbsent(new AdapterKey(Objects.requireNonNull(type), Objects.requireNonNull(spType)), Objects.requireNonNull(adapter));

		if (previous != null) {
			throw new IllegalArgumentException("%s already has an adapter from %s (%s)".formatted(type, spType.getSimpleName(), previous));
		}
	}

	public <B extends SpElement> void adaptFromSp(Class<B> spType, PolymorphicFromSpAdapter<?, B> adapter) {
		this.polymorphicFromSpAdapters.computeIfAbsent(Objects.requireNonNull(spType), e -> new ArrayList<>()).add(Objects.requireNonNull(adapter));
	}

	public <A, B extends SpElement> void adaptBaseFromSp(Class<A> type, Class<B> spType, FromSpAdapter<? extends A, B> adapter) {
		this.adaptFromSp(spType, new PolymorphicFromSpAdapter<A, B>() {
			@Override public boolean accept(Class<?> t) {
				return type.isAssignableFrom(t);
			}

			@Override public A fromSp(Class<A> type, B sp, StructuredProperties serializer) {
				return adapter.fromSp(sp, serializer);
			}
		});
	}

	public <A, B extends SpElement> void adapt(Class<A> type, Class<B> spType, SpAdapter<A, B> adapter) {
		this.adaptToSp(type, adapter);
		this.adaptFromSp(type, spType, adapter);
	}

	public <A, B extends SpElement> void adapt(Class<B> spType, PolymorphicSpAdapter<A, B> adapter) {
		this.adaptToSp(adapter);
		this.adaptFromSp(spType, adapter);
	}

	public <A, B extends SpElement> void adaptBase(Class<A> type, Class<B> spType, SpAdapter<? extends A, B> adapter) {
		this.adaptBaseToSp(type, adapter);
		this.adaptBaseFromSp(type, spType, adapter);
	}

	public SpElement toSp(Object object) {
		return object == null ? SpNull.instance : this.toSpAdapter(object.getClass()).toSp(Classes.cast(object), this);
	}

	public <T> T fromSp(Class<T> type, SpElement sp) {
		if (type == null) {
			return (T) this.fromSp(sp);
		}

		if (sp == SpNull.instance) {
			if (type.isPrimitive()) {
				throw new ClassCastException("cannot convert null to primitive");
			}

			return null;
		}

		return this.fromSpAdapter(type, sp.getClass()).fromSp(type, Classes.cast(sp), this);
	}

	public Object fromSp(SpElement element) {
		// @formatter:off
        return element == SpNull.instance ? null
             : element instanceof SpBoolean boolea ? this.fromSp(boolea)
             : element instanceof SpInteger integer ? this.fromSp(integer)
             : element instanceof SpFloat floa ? this.fromSp(floa)
             : element instanceof SpString string ? this.fromSp(string)
             : element instanceof SpArray array ? this.fromSp(array)
             : element instanceof SpMap map ? this.fromSp(map)
             : element instanceof SpPair pair ? this.fromSp(pair)
             : null;
        // @formatter:on
	}

	public boolean fromSp(SpBoolean boolea) {
		return boolea.value;
	}

	public BigInteger fromSp(SpInteger integer) {
		return integer.value();
	}

	public BigDecimal fromSp(SpFloat floa) {
		return floa.value();
	}

	public String fromSp(SpString string) {
		return string.value;
	}

	public Map<String, Object> fromSp(SpPair pair) {
		var map = new LinkedHashMap<String, Object>();
		map.put(this.fromSp(SpPrimitive.class, pair.a).stringValue(), this.fromSp(pair.b));

		return map;
	}

	public List<Object> fromSp(SpArray array) {
		return array.stream().map(this::fromSp).collect(Collectors.toList());
	}

	public Map<String, Object> fromSp(SpMap map) {
		var hashMap = new LinkedHashMap<String, Object>();
		map.forEach((key, value) -> hashMap.put(key, this.fromSp(value)));

		return hashMap;
	}

	public synchronized Appendable serialize(Appendable output, SpElement element) {
		return this.adapter.serialize(output, element);
	}

	private <A, B extends SpElement> PolymorphicToSpAdapter<A, B> toSpAdapter(Class<A> type) {
		return (PolymorphicToSpAdapter<A, B>) this.cachedToSpAdapters.computeIfAbsent(type, t -> Optional.ofNullable(this.toSpAdapters.get(t))
			.orElseGet(() -> Classes.cast(this.polymorphicToSpAdapters.stream()
				.filter(adapter -> adapter.accept(type))
				.reduce((a, b) -> b)
				.orElseThrow(() -> new IllegalArgumentException("no %s -> sp adapter was found".formatted(type.getName())))
			)));
	}

	private <A, B extends SpElement> PolymorphicFromSpAdapter<A, B> fromSpAdapter(Class<A> type, Class<B> spType) {
		return (PolymorphicFromSpAdapter<A, B>) this.cachedFromSpAdapters.computeIfAbsent(new AdapterKey(type, spType), k -> Optional.ofNullable(this.fromSpAdapters.get(k))
			.orElseGet(() -> Classes.cast(Optional.ofNullable(this.polymorphicFromSpAdapters.get(spType))
				.flatMap(adapters -> adapters.stream()
					.filter(adapter -> adapter.accept(type))
					.reduce((a, b) -> b)
				).orElseThrow(() -> new IllegalArgumentException("no %s -> %s adapter was found".formatted(spType.getSimpleName(), type.getName())))
			))
		);
	}

	public enum Option {
		RETAIN_WHITESPACE,
		RETAIN_COMMENTS
	}

	private record AdapterKey(Class<?> type, Class<?> spType) {}
}

package net.auoeke.sp.element;

public sealed interface SpElement permits SpPrimitive, SpPair, SpArray, SpMap {
	Type type();

	default boolean is(Type type) {
		return this.type() == type;
	}

	default boolean isNull() {
		return this.is(Type.NULL);
	}

	default boolean isBoolean() {
		return this.is(Type.BOOLEAN);
	}

	default boolean isInteger() {
		return this.is(Type.INTEGER);
	}

	default boolean isFloat() {
		return this.is(Type.FLOAT);
	}

	default boolean isString() {
		return this.is(Type.STRING);
	}

	default boolean isPair() {
		return this.is(Type.PAIR);
	}

	default boolean isArray() {
		return this.is(Type.ARRAY);
	}

	default boolean isMap() {
		return this.is(Type.MAP);
	}

	enum Type {
		NULL,
		BOOLEAN,
		INTEGER,
		FLOAT,
		STRING,
		PAIR,
		ARRAY,
		MAP;

		public boolean primitive() {
			return switch (this) {
				case NULL, BOOLEAN, INTEGER, FLOAT, STRING -> true;
				default -> false;
			};
		}

		public boolean structure() {
			return this == ARRAY || this == MAP;
		}

		public boolean compound() {
			return this.structure() || this == PAIR;
		}
	}
}

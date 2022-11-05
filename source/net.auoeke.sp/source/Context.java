package net.auoeke.sp.source;

import net.auoeke.sp.source.lexeme.Token;

enum Context {
	TOP,
	ARRAY,
	MAP;

	public Token begin() {
		return switch (this) {
			case ARRAY -> Token.ARRAY_BEGIN;
			case MAP -> Token.MAP_BEGIN;
			case TOP -> null;
		};
	}

	public Token end() {
		return switch (this) {
			case ARRAY -> Token.ARRAY_END;
			case MAP -> Token.MAP_END;
			case TOP -> null;
		};
	}

	public String description() {
		return switch (this) {
			case ARRAY -> "an array";
			case MAP -> "a map";
			case TOP -> "a file";
		};
	}

	public static Context of(char character) {
		return switch (character) {
			case '[' -> ARRAY;
			case '{' -> MAP;
			default -> throw new Error();
		};
	}
}

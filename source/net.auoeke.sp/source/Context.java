package net.auoeke.sp.source;

enum Context {
	TOP,
	ARRAY,
	MAP;

	public Node.Type begin() {
		return switch (this) {
			case ARRAY -> Node.Type.LBRACKET;
			case MAP -> Node.Type.LBRACE;
			case TOP -> null;
		};
	}

	public Node.Type end() {
		return switch (this) {
			case ARRAY -> Node.Type.RBRACKET;
			case MAP -> Node.Type.RBRACE;
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

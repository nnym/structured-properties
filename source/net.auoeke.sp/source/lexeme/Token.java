package net.auoeke.sp.source.lexeme;

public enum Token {
	NEWLINE('\n'),
	WHITESPACE,
	LINE_COMMENT,
	BLOCK_COMMENT,
	STRING,
	STRING_DELIMITER,
	ESCAPE,
	COMMA(','),
	MAPPING('='),
	ARRAY_BEGIN('['),
	ARRAY_END(']'),
	MAP_BEGIN('{'),
	MAP_END('}');

	private final char character;

	Token(char character) {
		this.character = character;
	}

	Token() {
		this(Character.MAX_VALUE);
	}

	public static Token delimiter(char character) {
		return switch (character) {
			case '[' -> ARRAY_BEGIN;
			case ']' -> ARRAY_END;
			case '{' -> MAP_BEGIN;
			case '}' -> MAP_END;
			default -> throw new IllegalArgumentException(String.valueOf(character));
		};
	}

	public char character() {
		if (this.character == Character.MAX_VALUE) {
			throw new UnsupportedOperationException();
		}

		return this.character;
	}

	public boolean newline() {
		return this == NEWLINE;
	}

	public boolean begin() {
		return this == ARRAY_BEGIN || this == MAP_BEGIN;
	}

	public boolean end() {
		return this == ARRAY_END || this == MAP_END;
	}

	public boolean separator() {
		return this == COMMA || this == NEWLINE;
	}
}

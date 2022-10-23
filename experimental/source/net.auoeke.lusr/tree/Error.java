package net.auoeke.lusr.tree;

import net.auoeke.lusr.parser.lexer.error.ErrorKey;
import net.auoeke.lusr.parser.lexer.lexeme.Lexeme;

public record Error(Lexeme lexeme, Offset offset, String error) {
	public Error(Lexeme lexeme, Offset offset, ErrorKey key, Object... arguments) {
		this(lexeme, offset, key.template.formatted(arguments));
	}

	public enum Offset {
		NONE,
		BEFORE,
		AFTER
	}
}

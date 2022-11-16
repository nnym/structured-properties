package net.auoeke.sp.source;

import net.auoeke.sp.source.lexeme.Lexeme;

public record LexResult(String location, CharSequence source, Lexeme first, Lexeme last) {
	public ParseResult parse() {
		return new Parser(this.source, this.first).parse0(this.location);
	}
}

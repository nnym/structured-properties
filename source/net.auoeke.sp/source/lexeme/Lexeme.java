package net.auoeke.sp.source.lexeme;

import java.util.stream.Stream;
import net.auoeke.sp.source.Node;

/**
 A terminal symbol.
 */
public abstract sealed class Lexeme extends Node permits BooleanLexeme, CharacterLexeme, DelimiterLexeme, EscapedLexeme, FloatLexeme, IntegerLexeme, LineCommentLexeme, NullLexeme, StringLexeme, WhitespaceLexeme {
	public final Position position;

	public Lexeme(Position position) {
		this.position = position;
	}

	@Override public Position start() {
		return this.position;
	}

	@Override public Stream<Node> stream() {
		return Stream.empty();
	}

	@Override public Stream<Node> family() {
		return Stream.of(this);
	}

	@Override public Stream<Node> deepStream() {
		return Stream.empty();
	}

	@Override public Stream<Node> deepFamily() {
		return Stream.of(this);
	}
}

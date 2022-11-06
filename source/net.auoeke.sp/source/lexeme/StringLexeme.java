package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class StringLexeme extends Lexeme {
	public String value;

	public StringLexeme(Position position, String value) {
		super(position);

		this.value = value;
	}

	@Override public Token token() {
		return Token.STRING;
	}

	@Override public boolean is(Token token) {
		return token == Token.STRING;
	}

	@Override public boolean isPrimitive() {
		return true;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public String toString() {
		return this.value;
	}

	@Override public boolean isValue() {
		return true;
	}

	@Override public int hashCode() {
		return this.value.hashCode();
	}

	@Override public boolean equals(Object object) {
		return object instanceof StringLexeme string && this.value.equals(string.value);
	}
}

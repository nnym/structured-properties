package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class DelimiterLexeme extends Lexeme {
	public Type type;
	public String value;

	public DelimiterLexeme(Position position, Type type, String value) {
		super(position);

		this.type = type;
		this.value = value;
	}

	@Override public Type type() {
		return this.type;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public DelimiterLexeme clone() {
		return new DelimiterLexeme(this.position, this.type, this.value);
	}

	@Override public String toString() {
		return this.value;
	}
}

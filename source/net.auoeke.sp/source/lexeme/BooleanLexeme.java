package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class BooleanLexeme extends Lexeme {
	public boolean value;

	public BooleanLexeme(Position position, boolean value) {
		super(position);

		this.value = value;
	}

	@Override public Type type() {
		return Type.BOOLEAN;
	}

	@Override public boolean isValue() {
		return true;
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

	@Override public BooleanLexeme clone() {
		return new BooleanLexeme(this.position, this.value);
	}

	@Override public String toString() {
		return String.valueOf(this.value);
	}
}

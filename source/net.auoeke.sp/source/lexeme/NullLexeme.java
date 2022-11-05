package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class NullLexeme extends Lexeme {
	public NullLexeme(Position position) {
		super(position);
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public String toString() {
		return "null";
	}

	@Override public boolean isValue() {
		return true;
	}

	@Override public Token token() {
		return Token.NULL;
	}

	@Override public boolean isPrimitive() {
		return true;
	}
}

package net.auoeke.sp.source.lexeme;

import java.math.BigInteger;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class IntegerLexeme extends Lexeme {
	public String source;
	public BigInteger value;

	public IntegerLexeme(Position position, String source, BigInteger value) {
		super(position);

		this.source = source;
		this.value = value;
	}

	@Override public Token token() {
		return Token.INTEGER;
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
		return this.source;
	}

	@Override public boolean isValue() {
		return true;
	}
}

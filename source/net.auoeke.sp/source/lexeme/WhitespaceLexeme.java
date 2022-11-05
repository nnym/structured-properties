package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class WhitespaceLexeme extends Lexeme {
	public final String value;

	public WhitespaceLexeme(Position position, String value) {
		super(position);

		this.value = value;
	}

	@Override public Token token() {
		return Token.WHITESPACE;
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
}

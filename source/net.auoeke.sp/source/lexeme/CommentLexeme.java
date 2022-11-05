package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class CommentLexeme extends Lexeme {
	public final String value;
	public final Token token;

	public CommentLexeme(Position position, Token token, String value) {
		super(position);

		this.token = token;
		this.value = value;
	}

	@Override public Token token() {
		return this.token;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public String toString() {
		return switch (this.token) {
			case LINE_COMMENT -> "##" + this.value;
			case BLOCK_COMMENT -> "/*" + this.value + "*/";
			default -> throw new IllegalStateException(this.token.toString());
		};
	}
}

package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class CharacterLexeme extends Lexeme {
	public final Type type;

	public CharacterLexeme(Position position, Type type) {
		super(position);

		this.type = type;
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

	@Override public CharacterLexeme clone() {
		return new CharacterLexeme(this.position, this.type);
	}

	@Override public String toString() {
		return String.valueOf(this.type.character());
	}
}

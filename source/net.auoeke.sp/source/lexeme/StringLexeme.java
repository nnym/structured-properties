package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class StringLexeme extends Lexeme {
	public Type type;
	public CharSequence value;

	public StringLexeme(Position position, Type type, CharSequence value) {
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

	@Override public StringLexeme clone() {
		return new StringLexeme(this.position, this.type, this.value);
	}

	@Override public boolean isValue() {
		return true;
	}

	@Override public boolean isPrimitive() {
		return this.is(Type.STRING);
	}

	@Override public String toString() {
		return this.value.toString();
	}

	@Override public int hashCode() {
		return this.type.hashCode() ^ this.value.hashCode();
	}

	@Override public boolean equals(Object object) {
		return this == object || object instanceof StringLexeme string && this.value.equals(string.value);
	}
}

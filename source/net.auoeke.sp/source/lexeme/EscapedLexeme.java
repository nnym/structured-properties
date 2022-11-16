package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class EscapedLexeme extends Lexeme {
	public char character;

	public EscapedLexeme(Position position, char character) {
		super(position);

		this.character = character;
	}

	@Override public Type type() {
		return Type.ESCAPE;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public EscapedLexeme clone() {
		return new EscapedLexeme(this.position, this.character);
	}

	@Override public String stringValue() {
		return switch (this.character) {
			case 'n' -> String.valueOf('\n');
			case 't' -> String.valueOf('\t');
			default -> String.valueOf(this.character);
		};
	}

	@Override public String toString() {
		return "\\" + this.character;
	}
}

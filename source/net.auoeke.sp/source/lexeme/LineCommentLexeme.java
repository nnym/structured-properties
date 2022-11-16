package net.auoeke.sp.source.lexeme;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class LineCommentLexeme extends Lexeme {
	public final CharSequence value;

	public LineCommentLexeme(Position position, CharSequence value) {
		super(position);

		this.value = value;
	}

	@Override public Type type() {
		return Type.LINE_COMMENT;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public LineCommentLexeme clone() {
		return new LineCommentLexeme(this.position, this.value);
	}

	@Override public String toString() {
		return "##" + this.value;
	}
}

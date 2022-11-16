package net.auoeke.sp.source.tree;

import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class SourceUnit extends Tree {
	public final String location;
	public final CharSequence source;
	public Node element;

	public SourceUnit(String location, CharSequence source) {
		this.location = location;
		this.source = source;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public Type type() {
		return Type.FILE;
	}

	@Override public void parent(Tree parent) {
		throw new UnsupportedOperationException();
	}

	@Override protected Tree cloneChildless() {
		return new SourceUnit(this.location, this.source);
	}
}

package net.auoeke.sp.source.tree;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class SourceUnit extends Tree {
	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public void parent(Tree parent) {
		throw new UnsupportedOperationException();
	}
}

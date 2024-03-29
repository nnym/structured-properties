package net.auoeke.sp.source.tree;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class StringTree extends DelimitedTree {
	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public Tree cloneChildless() {
		return new StringTree();
	}

	@Override public Type type() {
		return Type.STRING_TREE;
	}

	@Override public boolean isValue() {
		return true;
	}

	@Override public boolean isPrimitive() {
		return true;
	}
}

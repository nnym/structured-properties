package net.auoeke.sp.source.tree;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeVisitor;

/**
 Not a pear tree.
 */
public final class PairTree extends Tree {
	public PairTree() {}

	public Node a() {
		return this.first.isValue() ? this.first : null;
	}

	public Node b() {
		return this.last != null && this.last.isValue() ? this.last : null;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public boolean isValue() {
		return true;
	}

	@Override protected Tree cloneChildless() {
		return new PairTree();
	}
}

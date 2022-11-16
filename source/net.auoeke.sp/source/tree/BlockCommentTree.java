package net.auoeke.sp.source.tree;

import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class BlockCommentTree extends DelimitedTree {
	@Override public Type type() {
		return Type.BLOCK_COMMENT;
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public Tree cloneChildless() {
		return new BlockCommentTree();
	}
}

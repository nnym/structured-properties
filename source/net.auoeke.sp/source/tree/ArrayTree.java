package net.auoeke.sp.source.tree;

import java.util.ArrayList;
import java.util.List;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeVisitor;

public final class ArrayTree extends Tree {
	public final List<Node> elements = new ArrayList<>();

	public void element(Node element) {
		this.elements.add(element);
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public Type type() {
		return Type.ARRAY;
	}

	@Override public boolean isValue() {
		return true;
	}

	@Override protected Tree cloneChildless() {
		var clone = new ArrayTree();
		this.elements.forEach(clone::element);

		return clone;
	}
}

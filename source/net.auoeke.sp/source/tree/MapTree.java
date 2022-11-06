package net.auoeke.sp.source.tree;

import java.util.LinkedHashMap;
import java.util.Map;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.NodeVisitor;

public final class MapTree extends Tree {
	public final Map<String, PairTree> pairs = new LinkedHashMap<>();

	public PairTree entry(PairTree pair) {
		return this.pairs.put(pair.a().stringValue(), pair);
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
}

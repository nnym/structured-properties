package net.auoeke.sp.tree;

import java.util.HashMap;
import java.util.Map;

public class MapTree extends Tree {
	public final Map<Node, PairTree> pairs = new HashMap<>();

	public PairTree entry(PairTree pair) {
		this.add(pair);
		return this.pairs.put(pair.a, pair);
	}
}

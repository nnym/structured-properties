package net.auoeke.lusr.tree;

import java.util.ArrayList;
import java.util.List;

public class ArrayTree extends Tree {
	public final List<Node> elements = new ArrayList<>();

	public void element(Node element) {
		this.add(element);
		this.elements.add(element);
	}
}

package net.auoeke.sp.tree;

/**
 Not a pear tree.
 */
public class PairTree extends Tree {
    public Node a;
    public Node b;

	public PairTree(Node a, Node b) {
		this.a = a;
		this.b = b;
	}
}

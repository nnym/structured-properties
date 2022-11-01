package net.auoeke.sp.source;

import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.lexeme.Position;
import net.auoeke.sp.source.tree.Tree;

public abstract sealed class Node implements CharSequence permits Lexeme, Tree {
	public Node previous;
	public Node next;
	protected Tree parent;

	public abstract void accept(NodeVisitor visitor);

	public abstract <T> T accept(NodeTransformer<T> transformer);

	public abstract Position start();

	@Override public abstract String toString();

	public Tree parent() {
		return this.parent;
	}

	public Node previous() {
		return this.previous;
	}

	public Node next() {
		return this.next;
	}

	public void parent(Tree parent) {
		if (this.parent != null) {
			this.parent.remove(this);
		}

		this.parent = parent;
	}

	public void set(Node replacement) {
		replacement.link(this.previous, this.next);
		replacement.parent(this.parent);
		this.parent(null);
	}

	public void previous(Node previous) {
		this.previous = previous;
	}

	public void next(Node next) {
		this.next = next;
	}

	public void linkPrevious(Node previous) {
		if (this.previous != null) {
			this.previous.next(null);
		}

		if (previous != null) {
			previous.next(this);
		}

		this.previous(previous);
	}

	public void linkNext(Node next) {
		if (this.next != null) {
			this.next.previous(null);
		}

		if (next != null) {
			next.previous(this);
		}

		this.next(next);
	}

	public void link(Node previous, Node next) {
		this.linkPrevious(previous);
		this.linkNext(next);
	}

	public void insertPrevious(Node previous) {
		previous.link(this.previous(), this);
		previous.parent(this.parent);
	}

	public void insertNext(Node next) {
		next.link(this, this.next());
		next.parent(this.parent);
	}

	public void remove() {
		if (this.previous != null) this.previous.next(this.next);
		if (this.next != null) this.next.previous(this.previous);

		this.parent(null);
		this.previous(null);
		this.next(null);
	}

	public String stringValue() {
		return this.toString();
	}

	public boolean isValue() {
		return false;
	}

	public boolean isPrimitive() {
		return false;
	}

	@Override public int length() {
		return this.toString().length();
	}

	@Override public char charAt(int index) {
		return this.toString().charAt(index);
	}

	@Override public CharSequence subSequence(int start, int end) {
		return this.toString().substring(start, end);
	}
}

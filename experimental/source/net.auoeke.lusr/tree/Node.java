package net.auoeke.lusr.tree;

public abstract class Node {
    public Tree parent;
    public Node next;
    public Node previous;

    public Tree parent() {
        return this.parent;
    }

    public Node next() {
        return this.next;
    }

    public Node previous() {
        return this.previous;
    }

    public void parent(Tree parent) {
        if (this.parent != null) {
            this.parent.remove(this);
        }

        this.parent = parent;
    }

    public void next(Node next) {
        this.next = next;
    }

    public void previous(Node previous) {
        this.previous = previous;
    }

    public void insertNext(Node next) {
        var oldNext = this.next();
        this.next(next);
        next.previous(this);
        next.next(oldNext);

        if (oldNext != null) {
            oldNext.previous(next);
        }
    }

    public void insertPrevious(Node previous) {
        var oldPrevious = this.previous();
        this.previous(previous);
        previous.previous(oldPrevious);
        previous.next(this);

        if (oldPrevious != null) {
            oldPrevious.next(previous);
        }
    }

    public void remove() {
        this.parent().remove(this);
        this.parent(null);
        this.previous(null);
        this.next(null);
    }

	public boolean isValue() {
		return false;
	}

	public boolean isPrimitive() {
		return false;
	}
}

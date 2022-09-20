package net.auoeke.lusr.tree;

public abstract class Tree extends Node {
    public Node first;
    public Node last;

    public Node first() {
        return this.first;
    }

    public Node last() {
        return this.last;
    }

    public void first(Node first) {
        var next = this.first.next();

        if (next != null) {
            next.previous(first);
        }

        this.first = first;
        first.previous(null);
    }

    public void last(Node last) {
        var previous = this.last.previous();

        if (previous != null) {
            previous.next(last);
        }

        this.last = last;
        last.next(null);
    }

    public void addFirst(Node first) {
        if (this.first != null) {
            this.first.insertPrevious(first);
        }

        this.first = first;
    }

    public void add(Node last) {
        if (this.last != null) {
            this.last.insertNext(last);
        }

        this.last = last;
    }

    void remove(Node node) {
        var next = node.next();
        var previous = node.previous();

        if (node == this.first()) {
            this.first(next);
        } else if (node == this.last()) {
            this.last(previous);
        }

        if (next != null) {
            next.previous(previous);
        }

        if (previous != null) {
            previous.next(next);
        }
    }
}

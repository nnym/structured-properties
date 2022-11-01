package net.auoeke.sp.source.tree;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.lexeme.Position;

/**
 A nonterminal.
 */
public abstract sealed class Tree extends Node implements Iterable<Node> permits ArrayTree, MapTree, PairTree, SourceUnit, StringTree {
	public Node first;
	public Node last;

	public Node first() {
		return this.first;
	}

	public Node last() {
		return this.last;
	}

	public void first(Node first) {
		this.first = first;
	}

	public void last(Node last) {
		this.last = last;
	}

	public void linkFirst(Node first) {
		if (this.first == null) {
			this.last = first;
			first.linkPrevious(null);
		} else {
			this.first.set(first);
		}

		this.first = first;
	}

	public void linkLast(Node last) {
		if (this.last == null) {
			this.first = last;
			last.linkNext(null);
		} else {
			this.last.set(last);
		}

		this.last = last;
	}

	public void addFirst(Node first) {
		if (this.first == null) {
			this.last = first;
			first.linkPrevious(null);
		} else {
			this.first.insertPrevious(first);
		}

		this.first = first;
	}

	public void addLast(Node last) {
		if (this.last == null) {
			this.first = last;
			last.linkNext(null);
		} else {
			this.last.insertNext(last);
		}

		this.last = last;
	}

	public void remove(Node node) {
		if (node == this.first()) this.linkFirst(node.next());
		if (node == this.last()) this.linkLast(node.previous());

		node.remove();
	}

	public Stream<Node> stream() {
		return Stream.iterate(this.first, node -> node != null && node.previous != this.last && node.previous != this && node != this.next, Node::next);
	}

	@Override public Iterator<Node> iterator() {
		return this.stream().iterator();
	}

	@Override public Position start() {
		return this.first.start();
	}

	@Override public String stringValue() {
		return this.stream().map(Node::stringValue).collect(Collectors.joining());
	}

	@Override public int length() {
		return this.stream().mapToInt(Node::length).sum();
	}

	@Override public String toString() {
		return this.stream().map(Node::toString).collect(Collectors.joining());
	}
}

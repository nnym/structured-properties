package net.auoeke.sp.source.tree;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.lexeme.Position;

/**
 A nonterminal.
 */
public abstract sealed class Tree extends Node implements Iterable<Node> permits ArrayTree, DelimitedTree, MapTree, PairTree, SourceUnit {
	public Node first;
	public Node last;

	protected abstract Tree cloneChildless();

	public Node first() {
		return this.first;
	}

	public Node last() {
		return this.last;
	}

	public Node deepFirst() {
		var first = this.first;

		while (first instanceof Tree tree) {
			first = tree.first;
		}

		return first;
	}

	public Node deepLast() {
		var last = this.last;

		while (last instanceof Tree tree) {
			last = tree.last;
		}

		return last;
	}

	public void first(Node first) {
		this.first = first;
	}

	public void last(Node last) {
		this.last = last;
	}

	public void linkFirst(Node first) {
		if (first != null) {
			first.parent(this);
			this.linkPrevious(first.previous);
		}

		if (this.first == null) {
			this.last = first;
		} else if (this.first != this.last && first != null) {
			first.linkNext(this.first.next);
		}

		this.first = first;
	}

	public void linkLast(Node last) {
		if (last != null) {
			last.parent(this);
			this.linkNext(last.next);
		}

		if (this.last == null) {
			this.first = last;
		} else if (this.last != this.first && last != null) {
			last.linkPrevious(this.last.previous);
		}

		this.last = last;

		if (last != this.first) {
			this.first.iterateStrictlyNext().forEach(child -> child.parent(this));
		}
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
			last.parent(this);
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

	@Override public Stream<Node> stream() {
		return Stream.iterate(this.first, Objects::nonNull, Node::next);
	}

	@Override public Stream<Node> family() {
		return Stream.concat(Stream.of(this), this.stream());
	}

	@Override public Stream<Node> deepStream() {
		return this.stream().flatMap(Node::deepFamily);
	}

	@Override public Stream<Node> deepFamily() {
		return Stream.concat(Stream.of(this), this.deepStream());
	}

	@Override public Iterator<Node> iterator() {
		return this.stream().iterator();
	}

	@Override public void forEach(Consumer<? super Node> action) {
		this.stream().forEach(action);
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

	@Override public Tree clone() {
		var clone = this.cloneChildless();

		if (this.first != null) {
			clone.first(this.first.clone());
			clone.last(clone.first);
			clone.first.parent(clone);

			this.first.iterateStrictlyNext().forEach(node -> {
				clone.last.linkNext(node.clone());
				clone.last(clone.last.next);
				clone.last.parent(clone);
			});
		}

		return clone;
	}
}

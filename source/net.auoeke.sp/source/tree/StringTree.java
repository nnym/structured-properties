package net.auoeke.sp.source.tree;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeVisitor;
import net.auoeke.sp.source.lexeme.StringDelimiterLexeme;

public final class StringTree extends Tree {
	public StringDelimiterLexeme opener() {
		return this.first instanceof StringDelimiterLexeme delimiter ? delimiter : null;
	}

	public StringDelimiterLexeme closer() {
		return this.last instanceof StringDelimiterLexeme delimiter ? delimiter : null;
	}

	public boolean isRaw() {
		return this.opener() == null;
	}

	public Stream<Node> value() {
		return this.isRaw() ? Stream.iterate(this.first, Objects::nonNull, Node::next) : Stream.iterate(this.first.next, node -> node != this.closer(), Node::next);
	}

	@Override public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	@Override public <T> T accept(NodeTransformer<T> transformer) {
		return transformer.transform(this);
	}

	@Override public Tree cloneChildless() {
		return new StringTree();
	}

	@Override public String stringValue() {
		return this.value().map(Node::stringValue).collect(Collectors.joining());
	}

	@Override public boolean isValue() {
		return true;
	}

	@Override public boolean isPrimitive() {
		return true;
	}
}

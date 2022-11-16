package net.auoeke.sp.source.tree;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.lexeme.DelimiterLexeme;

public sealed abstract class DelimitedTree extends Tree permits BlockCommentTree, StringTree {
	public DelimiterLexeme opener() {
		return this.first instanceof DelimiterLexeme delimiter ? delimiter : null;
	}

	public DelimiterLexeme closer() {
		return this.last instanceof DelimiterLexeme delimiter ? delimiter : null;
	}

	public Stream<Node> value() {
		return Stream.iterate(this.first.next, node -> node != this.closer(), Node::next);
	}

	@Override public String stringValue() {
		return this.value().map(Node::stringValue).collect(Collectors.joining());
	}
}

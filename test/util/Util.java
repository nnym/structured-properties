package util;

import java.util.List;
import java.util.stream.Stream;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.Tree;

public class Util {
	public static List<Node> orphans(SourceUnit tree) {
		return tree.accept(new NodeTransformer<Stream<Node>>() {
			private static Stream<Node> check(Node lexeme) {
				return lexeme.parent() == null ? Stream.of(lexeme) : Stream.empty();
			}

			@Override public Stream<Node> transformTree(Tree node) {
				return Stream.concat(check(node), node.stream().flatMap(n -> n.accept(this)));
			}

			@Override public Stream<Node> transformLexeme(Lexeme node) {
				return check(node);
			}
		}).toList();
	}
}

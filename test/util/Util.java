package util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.Tree;

public class Util {
	public static String formatVariables(Object... arguments) {
		var message = new StringBuilder();
		var iterator = Arrays.asList(arguments).iterator();

		if (arguments.length % 2 != 0) {
			message.append(iterator.next());
		}

		while (iterator.hasNext()) {
			message.append('\n').append((String) iterator.next()).append(": ").append(iterator.next());
		}

		return message.toString();
	}

	public static List<Node> orphans(SourceUnit tree) {
		return orphans0(tree).toList();
	}

	private static Stream<Node> orphans0(Node node) {
		var orhpans = node.parent() == null ? Stream.of(node) : Stream.<Node>empty();

		return switch (node) {
			case Lexeme ignored -> orhpans;
			case Tree tree -> Stream.concat(orhpans, tree.stream().flatMap(Util::orphans0));
		};
	}
}

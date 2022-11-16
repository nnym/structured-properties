package util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.element.SpElement;
import net.auoeke.sp.source.Lexer;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.ParseResult;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.tree.Tree;

public class Verification {
	public static SpElement resourceElement(String name) {
		return StructuredProperties.toElement(parseResource(name));
	}

	public static SpElement resourceElement(Path path) {
		return StructuredProperties.toElement(parse(path.toString(), Files.readString(path)));
	}

	public static ParseResult parseResource(String name) {
		var resource = Verification.class.getClassLoader().getResource(name);

		try (var stream = resource.openStream()) {
			return parse(resource.getFile(), new String(stream.readAllBytes()));
		}
	}

	public static ParseResult parse(String location, String source) {
		var lex = Lexer.lex(location, source);

		lex.first().iterateNext().filter(Node::hasNext).forEach(node -> {
			var start = node.start().index();
			var nextStart = node.next.start().index();
			var length = node.length();
			var end = start + length;

			assert node.next == null || end == nextStart : Util.formatVariables("end != nextStart",
				"start", start,
				"length", length,
				"end", end,
				"nextStart", nextStart,
				"node", node,
				"next", node.next
			);
		});

		var lexLexemes = lex.first().iterateNext().toList();
		var lexCount = lexLexemes.size();
		var result = lex.parse();
		var tree = result.tree();

		assert tree.previous == null && tree.next == null;

		var parseLexemes = tree.deepStream().filter(Lexeme.class::isInstance).toList();
		var parseCount = parseLexemes.size();

		assert parseCount == lexCount : Util.formatVariables("parseCount != lexCount",
			"lexCount", lexCount,
			"parseCount", parseCount,
			"lexLexemes", String.join("", lexLexemes),
			"parseLexemes", String.join("", parseLexemes)
		);

		var length = tree.length();
		var sourceLength = source.length();

		assert length == sourceLength : Util.formatVariables("length != sourceLength",
			"length", length,
			"sourceLength", sourceLength,
			"string", '"' + tree.toString() + '"'
		);

		verify(source, tree);

		return result;
	}

	public static void verify(String source, Node node) {
		var start = node.start().index();
		var length = node.length();
		var end = start + length;
		var string = node.toString();
		var substring = source.substring(start, end);

		assert string.equals(substring) : Util.formatVariables("toString() != source.substring(start, end)",
			"start", start,
			"length", length,
			"end", end,
			"string", string,
			"substring", substring
		);

		if (node instanceof Tree tree) {
			assert tree.first == null || tree.first.previous == null;
			assert tree.last == null || tree.last.next == null;
			assert tree.ancestors().noneMatch(ancestor -> ancestor == tree) : "circle: " + Stream.concat(Stream.of(tree), tree.ancestors()).toList();

			tree.forEach(child -> {
				var parent = child.parent();

				assert parent == tree : Util.formatVariables("wrong parent",
					"actual", parent,
					"required", tree
				);

				assert child.previous == null || child.previous.next == child;
				assert child.next == null || child.next.previous == child;

				verify(source, child);
			});
		}
	}
}

package net.auoeke.sp.intellij;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.Parser;
import net.auoeke.sp.source.error.Error;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.tree.Tree;
import org.jetbrains.annotations.NotNull;

public class SpPsiParser implements PsiParser {
	@NotNull
	@Override public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
		var result = Parser.parse(builder.getOriginalText().toString());
		var errors = result.errors().stream().collect(Collectors.groupingBy(Error::node));
		var marker = builder.mark();
		buildChildren(builder, result.tree(), errors);

		while (builder.getTokenType() != null) {
			builder.advanceLexer();
		}

		marker.done(root);
		return builder.getTreeBuilt();
	}

	private static void advance(PsiBuilder builder, int index) {
		while (builder.getCurrentOffset() < index) {
			builder.advanceLexer();
		}
	}

	private static void build(PsiBuilder builder, Node node, Map<Node, List<Error>> errors) {
		var start = node.start().index();

		advance(builder, start);
		var marker = builder.mark();
		var nodeErrors = errors.get(node);

		if (nodeErrors != null) {
			builder.error(nodeErrors.get(0).description());
		}

		if (node instanceof Tree tree) {
			buildChildren(builder, tree, errors);
		}

		advance(builder, start + node.length());
		marker.done(node instanceof Lexeme lexeme ? SpLexer.type(lexeme) : SpParserDefinition.type((Tree) node));
	}

	private static void buildChildren(PsiBuilder builder, Tree tree, Map<Node, List<Error>> errors) {
		tree.forEach(child -> build(builder, child, errors));
	}
}

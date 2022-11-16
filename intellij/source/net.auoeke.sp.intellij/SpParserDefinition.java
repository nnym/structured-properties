package net.auoeke.sp.intellij;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import net.auoeke.sp.source.tree.Tree;
import org.jetbrains.annotations.NotNull;

public class SpParserDefinition implements ParserDefinition {
	public static final IFileElementType fileElementType = new IFileElementType(SpFileType.language);
	public static final IElementType
		stringTreeType = SpLexer.elementType("STRING_TREE"),
		pairType = SpLexer.elementType("PAIR"),
		arrayType = SpLexer.elementType("ARRAY"),
		mapType = SpLexer.elementType("MAP"),
		blockCommentType = SpLexer.elementType("BLOCK_COMMENT");

	@SuppressWarnings("DuplicatedCode")
	static IElementType type(Tree tree) {
		return switch (tree.type()) {
			case FILE -> fileElementType;
			case STRING_TREE -> stringTreeType;
			case PAIR -> pairType;
			case ARRAY -> arrayType;
			case MAP -> mapType;
			case BLOCK_COMMENT -> blockCommentType;
			default -> throw new AssertionError();
		};
	}

	@NotNull
	@Override public Lexer createLexer(Project project) {
		return new SpLexer();
	}

	@NotNull
	@Override public PsiParser createParser(Project project) {
		return new SpPsiParser();
	}

	@NotNull
	@Override public IFileElementType getFileNodeType() {
		return fileElementType;
	}

	@NotNull
	@Override public TokenSet getCommentTokens() {
		return SpTokenSets.comments;
	}

	@NotNull
	@Override public TokenSet getStringLiteralElements() {
		return SpTokenSets.stringLiterals;
	}

	@NotNull
	@Override public PsiElement createElement(ASTNode node) {
		return new ASTWrapperPsiElement(node);
	}

	@NotNull
	@Override public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
		return new SpFile(viewProvider);
	}
}

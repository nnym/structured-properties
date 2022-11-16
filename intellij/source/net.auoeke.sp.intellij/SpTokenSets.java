package net.auoeke.sp.intellij;

import com.intellij.psi.tree.TokenSet;

public class SpTokenSets {
	public static final TokenSet stringLiterals = TokenSet.create(SpLexer.stringType, SpLexer.stringDelimiterType);
	public static final TokenSet structures = TokenSet.create(SpLexer.lbracketType, SpLexer.rbracketType, SpLexer.lbraceType, SpLexer.rbraceType);
	public static final TokenSet keywords = TokenSet.create(SpLexer.booleanLiteralType, SpLexer.nullLiteralType);
	public static final TokenSet literals = TokenSet.create(SpLexer.stringType, SpLexer.nullLiteralType, SpLexer.booleanLiteralType, SpLexer.integerLiteralType, SpLexer.floatLiteralType);
	public static final TokenSet comments = TokenSet.create(SpLexer.lineCommentType, SpParserDefinition.blockCommentType/*, SpLexer.blockCommentStringType, SpLexer.blockCommentStartType, SpLexer.blockCommentEndType*/);
}

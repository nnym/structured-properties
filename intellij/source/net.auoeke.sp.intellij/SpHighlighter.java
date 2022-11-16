package net.auoeke.sp.intellij;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpHighlighter extends SyntaxHighlighterBase {
	public static final TextAttributesKey
		lineCommentKey = TextAttributesKey.createTextAttributesKey("SP.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT),
		blockCommentKey = TextAttributesKey.createTextAttributesKey("SP.BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT),
		blockCommentDelimiterKey = TextAttributesKey.createTextAttributesKey("SP.BLOCK_COMMENT_DELIMITER", blockCommentKey),
		stringKey = TextAttributesKey.createTextAttributesKey("SP.STRING", DefaultLanguageHighlighterColors.STRING),
		stringDelimiterKey = TextAttributesKey.createTextAttributesKey("SP.STRING_DELIMITER", stringKey),
		numberKey = TextAttributesKey.createTextAttributesKey("SP.NUMBER", DefaultLanguageHighlighterColors.NUMBER),
		keywordKey = TextAttributesKey.createTextAttributesKey("SP.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD),
		escapeKey = TextAttributesKey.createTextAttributesKey("SP.ESCAPE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE),
		commaKey = TextAttributesKey.createTextAttributesKey("SP.COMMA", DefaultLanguageHighlighterColors.COMMA),
		equalsKey = TextAttributesKey.createTextAttributesKey("SP.EQUALS", DefaultLanguageHighlighterColors.OPERATION_SIGN),
		bracketKey = TextAttributesKey.createTextAttributesKey("SP.BRACKETS", DefaultLanguageHighlighterColors.BRACKETS),
		braceKey = TextAttributesKey.createTextAttributesKey("SP.BRACES", DefaultLanguageHighlighterColors.BRACES);

	private static final Map<IElementType, TextAttributesKey[]> keys = Stream.of(
		Map.entry(SpLexer.lineCommentType, lineCommentKey),
		Map.entry(SpLexer.blockCommentStringType, blockCommentKey),
		Map.entry(SpLexer.blockCommentStartType, blockCommentDelimiterKey),
		Map.entry(SpLexer.blockCommentEndType, blockCommentDelimiterKey),
		Map.entry(SpLexer.stringType, stringKey),
		Map.entry(SpLexer.stringDelimiterType, stringDelimiterKey),
		Map.entry(SpLexer.booleanLiteralType, keywordKey),
		Map.entry(SpLexer.integerLiteralType, numberKey),
		Map.entry(SpLexer.floatLiteralType, numberKey),
		Map.entry(SpLexer.nullLiteralType, keywordKey),
		Map.entry(SpLexer.escapeType, escapeKey),
		Map.entry(SpLexer.commaType, commaKey),
		Map.entry(SpLexer.equalsType, equalsKey),
		Map.entry(SpLexer.lbracketType, bracketKey),
		Map.entry(SpLexer.rbracketType, bracketKey),
		Map.entry(SpLexer.lbraceType, braceKey),
		Map.entry(SpLexer.rbraceType, braceKey)
	).collect(Collectors.toMap(Map.Entry::getKey, entry -> pack(entry.getValue()), (a, b) -> null, HashMap::new));

	@NotNull
	@Override public Lexer getHighlightingLexer() {
		return new SpLexer();
	}

	@Override public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
		return keys.computeIfAbsent(tokenType, t -> pack(null));
	}

	public static class Factory extends SyntaxHighlighterFactory {
		@Override public @NotNull SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile file) {
			return new SpHighlighter();
		}
	}
}

package net.auoeke.sp.intellij;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.Lexer;
import net.auoeke.sp.source.lexeme.Lexeme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpHighlighter extends LexerBase implements SyntaxHighlighter {
	private static final IElementType
		whitespaceType = elementType("WHITESPACE"),
		lineCommentType = elementType("LINE_COMMENT"),
		blockCommentType = elementType("BLOCK_COMMENT"),
		stringType = elementType("STRING"),
		stringDelimiterType = elementType("STRING_DELIMITER"),
		booleanLiteralType = elementType("BOOLEAN_LITERAL"),
		integerLiteralType = elementType("INTEGER_LITERAL"),
		floatLiteralType = elementType("FLOAT_LITERAL"),
		nullLiteralType = elementType("NULL_LITERAL"),
		escapeType = elementType("ESCAPE"),
		commaType = elementType("COMMA"),
		mappingType = elementType("MAPPING"),
		arrayBeginType = elementType("ARRAY_BEGIN"),
		arrayEndType = elementType("ARRAY_END"),
		mapBeginType = elementType("MAP_BEGIN"),
		mapEndType = elementType("MAP_END");

	private static final TextAttributesKey
		lineCommentKey = TextAttributesKey.createTextAttributesKey("SP.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT),
		blockCommentKey = TextAttributesKey.createTextAttributesKey("SP.BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT),
		stringKey = TextAttributesKey.createTextAttributesKey("SP.STRING", DefaultLanguageHighlighterColors.STRING),
		stringDelimiterKey = TextAttributesKey.createTextAttributesKey("SP.STRING_DELIMITER", stringKey),
		numberKey = TextAttributesKey.createTextAttributesKey("SP.NUMBER", DefaultLanguageHighlighterColors.NUMBER),
		keywordKey = TextAttributesKey.createTextAttributesKey("SP.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD),
		escapeKey = TextAttributesKey.createTextAttributesKey("SP.ESCAPE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE),
		commaKey = TextAttributesKey.createTextAttributesKey("SP.COMMA", DefaultLanguageHighlighterColors.COMMA),
		mappingKey = TextAttributesKey.createTextAttributesKey("SP.OPERATION_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN),
		bracketKey = TextAttributesKey.createTextAttributesKey("SP.BRACKETS", DefaultLanguageHighlighterColors.BRACKETS),
		braceKey = TextAttributesKey.createTextAttributesKey("SP.BRACES", DefaultLanguageHighlighterColors.BRACES);

	private static final Map<IElementType, TextAttributesKey[]> keys = Stream.concat(
		Stream.of(Map.entry(whitespaceType, new TextAttributesKey[0])),
		Stream.of(
			Map.entry(lineCommentType, lineCommentKey),
			Map.entry(blockCommentType, blockCommentKey),
			Map.entry(stringType, stringKey),
			Map.entry(stringDelimiterType, stringDelimiterKey),
			Map.entry(booleanLiteralType, keywordKey),
			Map.entry(integerLiteralType, numberKey),
			Map.entry(floatLiteralType, numberKey),
			Map.entry(nullLiteralType, keywordKey),
			Map.entry(escapeType, escapeKey),
			Map.entry(commaType, commaKey),
			Map.entry(mappingType, mappingKey),
			Map.entry(arrayBeginType, bracketKey),
			Map.entry(arrayEndType, bracketKey),
			Map.entry(mapBeginType, braceKey),
			Map.entry(mapEndType, braceKey)
		).map(entry -> Map.entry(entry.getKey(), new TextAttributesKey[]{entry.getValue()}))
	).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	private Lexer lexer;
	private Lexeme lexeme;
	private CharSequence buffer;
	private int start;
	private int end;
	private int state;

	private SpHighlighter() {}

	private static IElementType elementType(String name) {
		return new IElementType(name, SpFileType.language);
	}

	@Override public @NotNull SpHighlighter getHighlightingLexer() {
		return this;
	}

	@Override public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
		return keys.get(tokenType);
	}

	@Override public void start(@NotNull CharSequence buffer, int start, int end, int initialState) {
		this.lexer = new Lexer(buffer.subSequence(start, end).toString(), StructuredProperties.Option.RETAIN_COMMENTS, StructuredProperties.Option.RETAIN_WHITESPACE);
		this.lexeme = this.lexer.first();
		this.buffer = buffer;
		this.start = start;
		this.end = end;
		this.state = initialState;
	}

	@Override public int getState() {
		return this.state;
	}

	@SuppressWarnings("DuplicatedCode")
	@Override public @Nullable IElementType getTokenType() {
		return this.lexeme == null ? null : switch (this.lexeme.token()) {
			case NEWLINE, WHITESPACE -> whitespaceType;
			case LINE_COMMENT -> lineCommentType;
			case BLOCK_COMMENT -> blockCommentType;
			case STRING -> stringType;
			case STRING_DELIMITER -> stringDelimiterType;
			case BOOLEAN -> booleanLiteralType;
			case INTEGER -> integerLiteralType;
			case FLOAT -> floatLiteralType;
			case NULL -> nullLiteralType;
			case ESCAPE -> escapeType;
			case COMMA -> commaType;
			case MAPPING -> mappingType;
			case ARRAY_BEGIN -> arrayBeginType;
			case ARRAY_END -> arrayEndType;
			case MAP_BEGIN -> mapBeginType;
			case MAP_END -> mapEndType;
		};
	}

	@Override public int getTokenStart() {
		return this.start + this.lexeme.start().index();
	}

	@Override public int getTokenEnd() {
		return this.getTokenStart() + this.lexeme.length();
	}

	@Override public void advance() {
		this.lexeme = (Lexeme) this.lexeme.next();
	}

	@Override public @NotNull CharSequence getBufferSequence() {
		return this.buffer;
	}

	@Override public int getBufferEnd() {
		return this.end;
	}

	public static class Factory extends SyntaxHighlighterFactory {
		@Override public @NotNull SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile file) {
			return new SpHighlighter();
		}
	}
}

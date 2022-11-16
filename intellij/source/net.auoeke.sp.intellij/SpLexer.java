package net.auoeke.sp.intellij;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import net.auoeke.sp.source.Lexer;
import net.auoeke.sp.source.lexeme.Lexeme;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpLexer extends LexerBase {
	public static final IElementType
		whitespaceType = elementType("WHITESPACE"),
		lineCommentType = elementType("LINE_COMMENT"),
		blockCommentStringType = elementType("BLOCK_COMMENT_STRING"),
		blockCommentStartType = elementType("BLOCK_COMMENT_START"),
		blockCommentEndType = elementType("BLOCK_COMMENT_END"),
		stringType = elementType("STRING"),
		stringDelimiterType = elementType("STRING_DELIMITER"),
		booleanLiteralType = elementType("BOOLEAN_LITERAL"),
		integerLiteralType = elementType("INTEGER_LITERAL"),
		floatLiteralType = elementType("FLOAT_LITERAL"),
		nullLiteralType = elementType("NULL_LITERAL"),
		escapeType = elementType("ESCAPE"),
		commaType = elementType("COMMA"),
		equalsType = elementType("EQUALS"),
		lbracketType = elementType("LBRACKET"),
		rbracketType = elementType("RBRACKET"),
		lbraceType = elementType("LBRACE"),
		rbraceType = elementType("RBRACE");

	private Lexer lexer;
	private Lexeme lexeme;
	private CharSequence buffer;
	private int end;
	private int state;

	static IElementType elementType(String name) {
		return new IElementType(name, SpFileType.language);
	}

	@SuppressWarnings("DuplicatedCode")
	static IElementType type(Lexeme lexeme) {
		return switch (lexeme.type()) {
			case NEWLINE, WHITESPACE -> whitespaceType;
			case LINE_COMMENT -> lineCommentType;
			case BLOCK_COMMENT_STRING -> blockCommentStringType;
			case BLOCK_COMMENT_START -> blockCommentStartType;
			case BLOCK_COMMENT_END -> blockCommentEndType;
			case STRING -> stringType;
			case STRING_DELIMITER -> stringDelimiterType;
			case BOOLEAN -> booleanLiteralType;
			case INTEGER -> integerLiteralType;
			case FLOAT -> floatLiteralType;
			case NULL -> nullLiteralType;
			case ESCAPE -> escapeType;
			case COMMA -> commaType;
			case EQUALS -> equalsType;
			case LBRACKET -> lbracketType;
			case RBRACKET -> rbracketType;
			case LBRACE -> lbraceType;
			case RBRACE -> rbraceType;
			default -> throw new AssertionError();
		};
	}

	@Override public void start(@NotNull CharSequence buffer, int start, int end, int initialState) {
		this.lexer = new Lexer(buffer.subSequence(0, end));
		this.lexer.advance();
		this.lexeme = this.lexer.first();

		if (this.lexeme != null) {
			while (this.getTokenStart() < start) {
				this.advance();
			}
		}

		this.buffer = buffer;
		this.end = end;
		this.state = initialState;
	}

	@Override public int getState() {
		return this.state;
	}

	@Nullable
	@Override public IElementType getTokenType() {
		return this.lexeme == null ? null : type(this.lexeme);
	}

	@Override public int getTokenStart() {
		return this.lexeme.start().index();
	}

	@Override public int getTokenEnd() {
		return this.getTokenStart() + this.lexeme.length();
	}

	@Override public void advance() {
		if (this.lexeme.next == null) {
			this.lexer.advance();
		}

		var end = this.getTokenEnd();
		this.lexeme = (Lexeme) this.lexeme.next();

		if (this.lexeme != null) {
			var start = this.getTokenStart();

			if (end != start) {
				var bp = true;
			}
		}
	}

	@NotNull
	@Override public CharSequence getBufferSequence() {
		return this.buffer;
	}

	@Override public int getBufferEnd() {
		return this.end;
	}
}

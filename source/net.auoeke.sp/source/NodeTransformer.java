package net.auoeke.sp.source;

import net.auoeke.sp.source.lexeme.BooleanLexeme;
import net.auoeke.sp.source.lexeme.CharacterLexeme;
import net.auoeke.sp.source.lexeme.CommentLexeme;
import net.auoeke.sp.source.lexeme.EscapedLexeme;
import net.auoeke.sp.source.lexeme.FloatLexeme;
import net.auoeke.sp.source.lexeme.IntegerLexeme;
import net.auoeke.sp.source.lexeme.NullLexeme;
import net.auoeke.sp.source.lexeme.StringDelimiterLexeme;
import net.auoeke.sp.source.lexeme.StringLexeme;
import net.auoeke.sp.source.lexeme.WhitespaceLexeme;
import net.auoeke.sp.source.tree.ArrayTree;
import net.auoeke.sp.source.tree.MapTree;
import net.auoeke.sp.source.tree.PairTree;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.StringTree;

public interface NodeTransformer<T> {
	default T transform(SourceUnit node) {
		node.forEach(child -> child.accept(this));
		return null;
	}

	default T transform(StringTree node) {
		node.forEach(child -> child.accept(this));
		return null;
	}

	default T transform(PairTree node) {
		node.forEach(child -> child.accept(this));
		return null;
	}

	default T transform(ArrayTree node) {
		node.forEach(child -> child.accept(this));
		return null;
	}

	default T transform(MapTree node) {
		node.forEach(child -> child.accept(this));
		return null;
	}

	default T transform(NullLexeme node) {
		return null;
	}

	default T transform(BooleanLexeme node) {
		return null;
	}

	default T transform(StringLexeme node) {
		return null;
	}

	default T transform(IntegerLexeme node) {
		return null;
	}

	default T transform(FloatLexeme node) {
		return null;
	}

	default T transform(StringDelimiterLexeme node) {
		return null;
	}

	default T transform(EscapedLexeme node) {
		return null;
	}

	default T transform(CommentLexeme node) {
		return null;
	}

	default T transform(WhitespaceLexeme node) {
		return null;
	}

	default T transform(CharacterLexeme node) {
		return null;
	}
}

package net.auoeke.sp.source;

import net.auoeke.sp.source.lexeme.BooleanLexeme;
import net.auoeke.sp.source.lexeme.CharacterLexeme;
import net.auoeke.sp.source.lexeme.DelimiterLexeme;
import net.auoeke.sp.source.lexeme.EscapedLexeme;
import net.auoeke.sp.source.lexeme.FloatLexeme;
import net.auoeke.sp.source.lexeme.IntegerLexeme;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.lexeme.LineCommentLexeme;
import net.auoeke.sp.source.lexeme.NullLexeme;
import net.auoeke.sp.source.lexeme.StringLexeme;
import net.auoeke.sp.source.lexeme.WhitespaceLexeme;
import net.auoeke.sp.source.tree.ArrayTree;
import net.auoeke.sp.source.tree.BlockCommentTree;
import net.auoeke.sp.source.tree.MapTree;
import net.auoeke.sp.source.tree.PairTree;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.StringTree;
import net.auoeke.sp.source.tree.Tree;

public interface NodeTransformer<T> {
	default T transformLexeme(Lexeme node) {
		return null;
	}

	default T transformTree(Tree node) {
		return null;
	}

	default T transform(SourceUnit node) {
		return this.transformTree(node);
	}

	default T transform(StringTree node) {
		return this.transformTree(node);
	}

	default T transform(PairTree node) {
		return this.transformTree(node);
	}

	default T transform(ArrayTree node) {
		return this.transformTree(node);
	}

	default T transform(MapTree node) {
		return this.transformTree(node);
	}

	default T transform(BlockCommentTree node) {
		return this.transformTree(node);
	}

	default T transform(NullLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(BooleanLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(StringLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(IntegerLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(FloatLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(DelimiterLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(EscapedLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(LineCommentLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(WhitespaceLexeme node) {
		return this.transformLexeme(node);
	}

	default T transform(CharacterLexeme node) {
		return this.transformLexeme(node);
	}
}

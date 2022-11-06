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

public interface NodeVisitor {
	default void visit(SourceUnit node) {
		node.forEach(child -> child.accept(this));
	}

	default void visit(StringTree node) {
		node.forEach(child -> child.accept(this));
	}

	default void visit(PairTree node) {
		node.forEach(child -> child.accept(this));
	}

	default void visit(ArrayTree node) {
		node.forEach(child -> child.accept(this));
	}

	default void visit(MapTree node) {
		node.forEach(child -> child.accept(this));
	}

	default void visit(NullLexeme node) {}

	default void visit(BooleanLexeme node) {}

	default void visit(StringLexeme node) {}

	default void visit(IntegerLexeme node) {}

	default void visit(FloatLexeme node) {}

	default void visit(StringDelimiterLexeme node) {}

	default void visit(EscapedLexeme node) {}

	default void visit(CommentLexeme node) {}

	default void visit(WhitespaceLexeme node) {}

	default void visit(CharacterLexeme node) {}
}

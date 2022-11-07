package net.auoeke.sp.source;

import net.auoeke.sp.source.lexeme.BooleanLexeme;
import net.auoeke.sp.source.lexeme.CharacterLexeme;
import net.auoeke.sp.source.lexeme.CommentLexeme;
import net.auoeke.sp.source.lexeme.EscapedLexeme;
import net.auoeke.sp.source.lexeme.FloatLexeme;
import net.auoeke.sp.source.lexeme.IntegerLexeme;
import net.auoeke.sp.source.lexeme.Lexeme;
import net.auoeke.sp.source.lexeme.NullLexeme;
import net.auoeke.sp.source.lexeme.StringDelimiterLexeme;
import net.auoeke.sp.source.lexeme.StringLexeme;
import net.auoeke.sp.source.lexeme.WhitespaceLexeme;
import net.auoeke.sp.source.tree.ArrayTree;
import net.auoeke.sp.source.tree.MapTree;
import net.auoeke.sp.source.tree.PairTree;
import net.auoeke.sp.source.tree.SourceUnit;
import net.auoeke.sp.source.tree.StringTree;
import net.auoeke.sp.source.tree.Tree;

public interface NodeVisitor {
	default void visitLexeme(Lexeme node) {}

	default void visitTree(Tree node) {
		node.forEach(child -> child.accept(this));
	}

	default void visit(SourceUnit node) {
		this.visitTree(node);
	}

	default void visit(StringTree node) {
		this.visitTree(node);
	}

	default void visit(PairTree node) {
		this.visitTree(node);
	}

	default void visit(ArrayTree node) {
		this.visitTree(node);
	}

	default void visit(MapTree node) {
		this.visitTree(node);
	}

	default void visit(NullLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(BooleanLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(StringLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(IntegerLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(FloatLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(StringDelimiterLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(EscapedLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(CommentLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(WhitespaceLexeme node) {
		this.visitLexeme(node);
	}

	default void visit(CharacterLexeme node) {
		this.visitLexeme(node);
	}
}

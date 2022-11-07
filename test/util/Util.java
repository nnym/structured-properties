package util;

import java.util.List;
import java.util.stream.Stream;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeTransformer;
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
import net.auoeke.sp.source.tree.Tree;

public class Util {
	public static List<Node> orphans(SourceUnit tree) {
		return tree.accept(new NodeTransformer<Stream<Node>>() {
			private static Stream<Node> check(Node lexeme) {
				return lexeme.parent() == null ? Stream.of(lexeme) : Stream.empty();
			}

			private Stream<Node> check(Tree tree) {
				return Stream.concat(check((Node) tree), this.scan(tree));
			}

			private Stream<Node> scan(Tree tree) {
				return tree.stream().flatMap(node -> node.accept(this));
			}

			@Override public Stream<Node> transform(SourceUnit node) {
				return this.check(node);
			}

			@Override public Stream<Node> transform(StringTree node) {
				return this.check(node);
			}

			@Override public Stream<Node> transform(PairTree node) {
				return this.check(node);
			}

			@Override public Stream<Node> transform(ArrayTree node) {
				return this.check(node);
			}

			@Override public Stream<Node> transform(MapTree node) {
				return this.check(node);
			}

			@Override public Stream<Node> transform(NullLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(BooleanLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(StringLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(IntegerLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(FloatLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(StringDelimiterLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(EscapedLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(CommentLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(WhitespaceLexeme node) {
				return check(node);
			}

			@Override public Stream<Node> transform(CharacterLexeme node) {
				return check(node);
			}
		}).toList();
	}
}

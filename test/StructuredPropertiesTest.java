import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.Lexer;
import net.auoeke.sp.source.Node;
import net.auoeke.sp.source.NodeTransformer;
import net.auoeke.sp.source.Parser;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import util.Stopwatch;
import util.Stuff;

@Testable
@ExtendWith(Stopwatch.class)
class StructuredPropertiesTest {
	@Test void test() {
		var lexer = new Lexer("[a ##comment\n]", StructuredProperties.Option.RETAIN_WHITESPACE, StructuredProperties.Option.RETAIN_COMMENTS);
		var loader = this.getClass().getClassLoader();
		var string = StructuredProperties.parseResource(loader.getResource("string.str"));
		var array = StructuredProperties.parseResource(loader.getResource("array.str"));
		var map = StructuredProperties.parseResource(loader.getResource("map.str"));
		var example = StructuredProperties.parseResource(Path.of("example.str"));
		var result = Parser.parse(loader.getResource("errors.str").getPath(), "][1,,,, {[] a, b = 1, b = 2, c =}, a = } '); drop table users; --");

		var sp = new StructuredProperties();
		var stuff = new Stuff();
		var stuffSp = sp.toSp(stuff);
		var newStuff = sp.fromSp(Stuff.class, stuffSp);
		// sp.serialize(System.out, stuffSp);

		var bp = true;

		result.success();
	}

	private static void time(Runnable test) {
		time(() -> {
			test.run();
			return null;
		});
	}

	private static <T> T time(Supplier<T> test) {
		var start = System.nanoTime();

		try {
			return test.get();
		} finally {
			System.out.println((System.nanoTime() - start) / 1e9);
		}
	}
}

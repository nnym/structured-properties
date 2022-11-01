import java.nio.file.Path;
import java.util.function.Supplier;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.Lexer;
import net.auoeke.sp.source.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;

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
		var parser = new Parser("][1,,,, {[] a, b = 1, b = 2, c =}, a = } '); drop table users; --");
		var result = parser.parse();
		new Parser("''").parse();

		var sp = new StructuredProperties();
		var stuff = new Stuff();
		var stuffSp = sp.toSp(stuff);
		var newStuff = sp.fromSp(Stuff.class, stuffSp);
		// sp.serialize(System.out, stuffSp);

		var bp = true;
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

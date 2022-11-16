import java.nio.file.Path;
import java.util.function.Supplier;
import net.auoeke.sp.StructuredProperties;
import net.auoeke.sp.source.Lexer;
import net.auoeke.sp.source.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import util.Stopwatch;
import lib.Stuff;
import util.Verification;

@Testable
@ExtendWith(Stopwatch.class)
class StructuredPropertiesTest {
	@Test void test() {
		Parser.parse("abc, 1");
		var lexer = Lexer.lex("[a ##comment\n]");
		var string = Verification.resourceElement("string.str");
		var array = Verification.resourceElement("array.str");
		var map = Verification.resourceElement("map.str");
		var example = Verification.resourceElement(Path.of("example.str"));
		var result = Verification.parseResource("errors.str");

		var sp = new StructuredProperties();
		var stuff = new Stuff();
		var stuffSp = sp.toSp(stuff);
		var newStuff = sp.fromSp(Stuff.class, stuffSp);
		// sp.serialize(System.out, stuffSp);

		var bp = true;

		System.out.println(result.message());
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

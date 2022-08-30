import java.nio.file.Path;
import java.util.function.Supplier;
import net.auoeke.csf.Csf;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
class CsfTest {
    @Test void test() {
        var loader = this.getClass().getClassLoader();
        var string = Csf.parseResource(loader.getResource("string.csf"));
        var array = Csf.parseResource(loader.getResource("array.csf"));
        var map = Csf.parseResource(loader.getResource("map.csf"));
        var example = Csf.parseResource(Path.of("example.csf"));

        var csf = new Csf();
        // var stuff = new Stuff();
        // var stuffCsf = csf.toCsf(stuff);
        // var newStuff = csf.fromCsf(Stuff.class, stuffCsf);
        // csf.serialize(System.out, stuffCsf);

        Csf.parse("a = 1");

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

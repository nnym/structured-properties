import java.nio.file.Path;
import java.util.function.Supplier;
import net.auoeke.lusr.Lusr;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
class LusrTest {
    @Test void test() {
        var loader = this.getClass().getClassLoader();
        var string = Lusr.parseResource(loader.getResource("string.lusr"));
        var array = Lusr.parseResource(loader.getResource("array.lusr"));
        var map = Lusr.parseResource(loader.getResource("map.lusr"));
        var example = Lusr.parseResource(Path.of("example.lusr"));

        var lusr = new Lusr();
        // var stuff = new Stuff();
        // var stuffLusr = lusr.toLusr(stuff);
        // var newStuff = lusr.fromLusr(Stuff.class, stuffLusr);
        // lusr.serialize(System.out, stuffLusr);

        Lusr.parse("a = 1");

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

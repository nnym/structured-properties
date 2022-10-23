import java.nio.file.Path;
import java.util.function.Supplier;
import net.auoeke.sp.StructuredProperties;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
class StructuredPropertiesTest {
    @Test void test() {
        var loader = this.getClass().getClassLoader();
        var string = StructuredProperties.parseResource(loader.getResource("string.sp"));
        var array = StructuredProperties.parseResource(loader.getResource("array.sp"));
        var map = StructuredProperties.parseResource(loader.getResource("map.sp"));
        var example = StructuredProperties.parseResource(Path.of("example.sp"));

        var sp = new StructuredProperties();
        // var stuff = new Stuff();
        // var stuffSp = sp.toSp(stuff);
        // var newStuff = sp.fromSp(Stuff.class, stuffSp);
        // sp.serialize(System.out, stuffSp);

        StructuredProperties.parse("a = 1");

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

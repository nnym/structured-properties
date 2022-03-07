import java.nio.file.Path;
import net.auoeke.eson.Eson;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@SuppressWarnings("resource")
@Testable
class EsonTest {
    @Test
    void test() {
        var loader = this.getClass().getClassLoader();
        var string = Eson.parseResource(loader.getResource("string.eson"));
        var array = Eson.parseResource(loader.getResource("array.eson"));
        var map = Eson.parseResource(loader.getResource("map.eson"));
        var example = Eson.parseResource(Path.of("example.eson"));

        var bp = true;
    }
}

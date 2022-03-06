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
        var string = Eson.parse(loader.getResourceAsStream("string.eson").readAllBytes());
        var array = Eson.parse(loader.getResourceAsStream("array.eson").readAllBytes());
        var map = Eson.parse(loader.getResourceAsStream("map.eson").readAllBytes());
        var example = Eson.parseResource(Path.of("example.eson"));

        var bp = true;
    }
}

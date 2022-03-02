import java.nio.file.Path;
import net.auoeke.cin.Cin;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@SuppressWarnings("resource")
@Testable
class CinTest {
    @Test
    void test() {
        var loader = this.getClass().getClassLoader();
        var string = Cin.parse(loader.getResourceAsStream("string.cin").readAllBytes());
        var array = Cin.parse(loader.getResourceAsStream("array.cin").readAllBytes());
        var map = Cin.parse(loader.getResourceAsStream("map.cin").readAllBytes());
        var example = Cin.parseResource(Path.of("example.cin"));

        var bp = true;
    }
}

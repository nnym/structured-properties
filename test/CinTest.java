import java.nio.file.Files;
import java.nio.file.Path;
import net.auoeke.cin.Cin;
import net.auoeke.cin.lexer.Lexer;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@SuppressWarnings("resource")
@Testable
class CinTest {
    @Test
    void test() {
        var loader = this.getClass().getClassLoader();
        var string = new Lexer(new String(loader.getResourceAsStream("string.cin").readAllBytes()), true, true);
        var array = new Lexer(new String(loader.getResourceAsStream("array.cin").readAllBytes()), true, true);
        var map = new Lexer(new String(loader.getResourceAsStream("map.cin").readAllBytes()), true, true);
        var example = new Lexer(Files.readString(Path.of("example.cin")), true, true);

        var test = Path.of("test.cin");
        System.out.println(Cin.parseResource(test));
    }
}

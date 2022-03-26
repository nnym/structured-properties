import java.nio.file.Path;
import net.auoeke.eson.Eson;
import net.auoeke.eson.element.EsonElement;
import net.auoeke.eson.serialization.EsonSerializer;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
class EsonTest {
    @Test
    void test() {
        var loader = this.getClass().getClassLoader();
        var string = Eson.parseResource(loader.getResource("string.eson"));
        var array = Eson.parseResource(loader.getResource("array.eson"));
        var map = Eson.parseResource(loader.getResource("map.eson"));
        var example = Eson.parseResource(Path.of("example.eson"));

        var serializer = new EsonSerializer();
        var stuff = new Stuff();
        var stuffEson = serializer.toEson(stuff);
        System.out.println(stuffEson);
        var newStuff = serializer.fromEson(Stuff.class, stuffEson);

        var bp = true;
    }
}

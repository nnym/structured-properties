import net.auoeke.cin.Cin
import net.auoeke.cin.parser.*
import net.auoeke.extensions.*
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable
import kotlin.io.path.*
import kotlin.io.println

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "SameParameterValue")
@Testable
class Tests {
    @Test
    fun test() {
        var a = Lexer(type.loader!!.resource("array.cin")!!.readText(), retainSource = true)

        println(Cin.parseResource("test.cin"))

        assert(Cin.parseResource("test.cin") == Cin.parseFile("test/test.cin"))
    }
}

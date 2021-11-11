import net.auoeke.cin.Cin
import net.auoeke.cin.lexer.*
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
        var string = Lexer(type.loader!!.resource("string.cin")!!.readText(), retainSource = true)
        var array = Lexer(type.loader!!.resource("array.cin")!!.readText(), retainSource = true)
        var map = Lexer(type.loader!!.resource("map.cin")!!.readText(), retainSource = true)

        println(Cin.parseResource("test.cin"))

        assert(Cin.parseResource("test.cin") == Cin.parseFile("test/test.cin"))
    }
}

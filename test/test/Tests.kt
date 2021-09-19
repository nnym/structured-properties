package test

import net.auoeke.cin.Cin
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Testable
class Tests {
    @Test
    fun test() {
        println(Cin.parse(read("test.cin")))
    }

    private fun read(file: String): String = javaClass.getResource("/$file").readText()
}

package test

import net.auoeke.cin.Cin
import net.auoeke.cin.element.Element
import org.junit.jupiter.api.Test
import org.junit.platform.commons.annotation.Testable

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "SameParameterValue")
@Testable
class Tests {
    @Test
    fun test() {
        println(parse("test.cin"))
    }

    private fun parse(file: String): Element? = Cin.parseResource(file)
}

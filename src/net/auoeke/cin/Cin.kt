package net.auoeke.cin

import net.auoeke.cin.element.*
import net.auoeke.cin.lexer.*
import net.auoeke.cin.lexer.lexeme.Lexeme
import net.auoeke.extensions.*
import java.net.URI
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.io.path.readText
import kotlin.io.path.toPath

@Suppress("ControlFlowWithEmptyBody")
class Cin(cin: String) : Iterator<Lexeme> {
    private val iterator = Lexer(cin, false).iterator()
    private var context: Context = Context.FILE

    private inline val nextIndex get() = iterator.nextIndex()
    private inline val previousIndex get() = iterator.previousIndex()

    private inline val Char.isStringDelimiter: Boolean get() = this == '\'' || this == '"' || this == '`'

    override operator fun hasNext(): Boolean = iterator.hasNext()

    override operator fun next(): Lexeme = iterator.next()

    private fun parseElement(): Element? = null.also {
    }

    private fun parsePair(map: MapElement) {
    }

    private fun parseFile(): Element? {
        return null
    }

    private inline fun <T : Element> withContext(context: Context, element: T, action: (T) -> Unit): T = element.also {
        this.context = this.context.also {
            this.context = context
            action(element)
        }
    }

    private fun throwException(position: String, message: String) {
        throw SyntaxException(position, message)
    }

    @Suppress("MemberVisibilityCanBePrivate", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    companion object {
        fun parse(cin: String): Element? = Cin(cin).parseElement()

        fun parseFile(cin: Path): Element? {
            try {
                return parse(cin.readText())
            } catch (exception: SyntaxException) {
                throw exception.apply {file = cin.absolute().string}
            }
        }

        fun parseFile(cin: String): Element? = parseFile(Path(cin))
        fun parseFile(cin: URI): Element? = parseFile(cin.toPath())
        fun parseFile(cin: URL): Element? = parseFile(cin.toURI())
        fun parseResource(cin: String): Element? = parseFile(type.loader!!.getResource(cin))
    }
}

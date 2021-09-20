package net.auoeke.cin

import net.auoeke.cin.element.*
import net.auoeke.extensions.listIterator
import net.auoeke.extensions.string
import java.lang.invoke.MethodHandles.throwException
import java.net.URI
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.toPath

@Suppress("ControlFlowWithEmptyBody")
class Cin(private val cin: String) : Iterator<Char> {
    private val iterator: ListIterator<Char> = cin.listIterator()
    private var line: Int = 1
    private var column: Int = 0
    private var context: Context = Context.FILE

    private inline val position get() = "$line:$column"
    private inline val nextIndex get() = iterator.nextIndex()
    private inline val previousIndex get() = iterator.previousIndex()

    private inline val Char.isStringDelimiter: Boolean get() = this == '\'' || this == '"' || this == '`'

    override operator fun hasNext(): Boolean = iterator.hasNext()

    override operator fun next(): Char = iterator.next().also {
        if (it == '\n') {
            line++
            column = 0
        }

        column++
    }

    private fun next(count: Int) {
        repeat(count) {next()}
    }

    private fun undo() {
        if (iterator.previous() == '\n') {
            line--
        }

        column--
    }

    private fun expect(vararg chars: Char) {
        val position = position

        forEach {
            if (it in chars) {
                return
            }
        }

        throwException(position, "Expected any of [$chars] after %s.")
    }

    private fun parseElement(): Element? {
        for (char in this) {
            if (char.isWhitespace() || comment(char) > 0 ) {
                continue
            }

            when (char) {
                ']' -> {
                    if (context !== Context.ARRAY) {
                        throwException("found ']' at %s but the surrounding context is not an array.")
                    }

                    undo(); break
                }
                '}' -> {
                    if (context !== Context.MAP) {
                        throwException("found '}' at %s but the surrounding context is not a map.")
                    }

                    undo(); break
                }
            }

            return when (char) {
                '[' -> withContext(Context.ARRAY, ArrayElement()) {
                    while (cin[nextIndex] != ']') {
                        parseElement()?.let(it::add)
                    }
                }
                '{' -> withContext(Context.MAP, MapElement()) {
                    while (cin[nextIndex] != '}') {
                        parsePair(it)
                    }
                }
                else -> string(char)
            }.also {
                for (next in this) {
                    when (next) {
                        '\n', ',' -> break
                        ']', '}' -> {
                            undo()

                            break
                        }
                    }

                    if (!next.isWhitespace() && comment(next) == 0) {
                        throwException("Expected at least one of ,\\n]} before $next.")
                    }
                }
            }
        }

        return null
    }

    private fun parsePair(map: MapElement) {
        for (char in this) {
            if (char.isWhitespace() || comment(char) > 0) {
                continue
            }

            val key = string(char)
        }
    }

    private inline fun <T : Element> withContext(context: Context, element: T, action: (T) -> Unit): T = element.also {
        val previousContext = this.context
        this.context = context

        action(element)

        this.context = previousContext
    }

    private fun comment(char: Char): Int {
        val start = nextIndex
        var length = 0

        if (char == '/') {
            if (cin[start] == '/') {
                next()

                if (cin[start + 1] == '/') {
                    length = 3
                    var count = 0

                    next()

                    for (next in this) {
                        length++

                        if (next == '/' && ++count == 3) {
                            break
                        }
                    }
                } else {
                    length = 2

                    while (next() != '\n') {
                        length++
                    }
                }
            }
        }

        return length
    }

    private fun string(delimiter: Char): Element {
        val start = previousIndex

        if (delimiter.isStringDelimiter) {
            val position = position
            val length = delimiterLength(start, delimiter)

            if (length == 2) {
                return StringElement("")
            }

            var count = 0

            forEach {char ->
                if (char == delimiter) {
                    if (++count == length) {
                        return StringElement(cin.substring(start + length, nextIndex - length).trimIndent().trim().replace("\\$delimiter", delimiter.toString()))
                    }
                } else {
                    count = 0
                }
            }

            throwException(position, "The string at %s is not closed.")
        }

        val comments = ArrayList<Int>()

        for ((index, next) in this.withIndex()) {
            if (next in "\n,]}") {
                undo()
                break
            }

            comment(next).also {
                if (it > 0) {
                    undo()

                    comments.add(index)
                    comments.add(index + it + 1)
                }
            }
        }

        var string = cin.substring(start, nextIndex)

        for (index in comments.indices.reversed() step 2) {
            string = string.replaceRange(comments[index - 1], comments[index], "")
        }

        return when (string.trim()) {
            "false" -> BooleanElement(false)
            "true" -> BooleanElement(true)
            "null" -> NullElement
            else -> try {
                IntegerElement(string.toLong())
            } catch (exception: NumberFormatException) {
                try {
                    FloatElement(string.toDouble())
                } catch (exception: NumberFormatException) {
                    StringElement(string)
                }
            }
        }
    }

    private fun delimiterLength(index: Int, delimiter: Char): Int {
        var length = 1

        while (cin[index + length] == delimiter) {
            length++
            next()
        }

        return length
    }

    private fun throwException(position: String, message: String) {
        throw SyntaxException(position, message)
    }

    private fun throwException(message: String) = throwException(position, message)

    @Suppress("MemberVisibilityCanBePrivate", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    companion object {
        fun parse(cin: String): Element? = Cin(cin).parseElement()

        fun parseFile(cin: Path): Element? {
            try {
                return parse(cin.readText())
            } catch (exception: SyntaxException) {
                throw exception.apply {file = cin.string}
            }
        }

        fun parseFile(cin: String): Element? = parseFile(Path(cin))
        fun parseFile(cin: URI): Element? = parseFile(cin.toPath())
        fun parseFile(cin: URL): Element? = parseFile(cin.toURI())
        fun parseResource(cin: String): Element? = parseFile(Cin::class.java.classLoader.getResource(cin))
    }
}

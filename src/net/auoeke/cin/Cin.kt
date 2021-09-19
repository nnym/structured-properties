package net.auoeke.cin

import net.auoeke.cin.type.*
import net.auoeke.extensions.listIterator

@Suppress("ControlFlowWithEmptyBody")
class Cin(private val cin: String) : Iterator<Char> {
    private val iterator: ListIterator<Char> = cin.listIterator()
    private var line: Int = 1
    private var column: Int = 0

    private inline val nextIndex get() = iterator.nextIndex()
    private inline val previousIndex get() = iterator.previousIndex()

    private inline val Char.isDelimiter: Boolean get() = this == '\'' || this == '"' || this == '`'
    private inline val Char.isNumber: Boolean get() = this >= 49.toChar() && this <= 57.toChar()

    override fun hasNext(): Boolean = iterator.hasNext()

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

    private fun parseElement(): Element? {
        for (char in this) {
            if (char == ']' || char == '}') {
                undo()

                break
            }

            if (char.isWhitespace() || comment(char)) {
                continue
            }

            if (char.isNumber) {
                return when (val number = number()) {
                    null -> {
                        val start = previousIndex

                        for (next in this) {
                            if (next == '\n' || next == ']' || next == '}') {
                                undo()

                                break
                            }
                        }

                        StringElement(cin.substring(start, cin.indexOf('\n', start)).trim())
                    }
                    else -> number
                }
            }

            val start = previousIndex

            when {
                cin.startsWith("false", start) -> return BooleanElement(false).also {next(4)}
                cin.startsWith("true", start) -> return BooleanElement(true).also {next(3)}
                cin.startsWith("null", start) -> return NullElement.also {next(3)}
            }

            string(char)?.let {return it}

            if (char == '[') {
                val array = ArrayElement()

                while (cin[nextIndex] != ']') {
                    parseElement()?.let(array::add)
                }

                return array
            }

            if (char == '{') {
                val map = MapElement()

                // while (cin[nextIndex] != '}') {
                //     parseElement(cin)?.let {map[]}
                // }
            }
        }

        return null
    }

    private fun parsePair() {

    }

    private fun comment(char: Char): Boolean {
        val start = nextIndex

        if (char == '/') {
            if (cin[start] == '/') {
                next()

                if (cin[start + 1] == '/') {
                    next()

                    while (next() != '/' || next() != '/' || next() != '/') {}
                } else {
                    while (next() != '\n') {}
                }

                return true
            }
        }

        return false
    }

    private fun number(): NumberElement? {
        val start = previousIndex
        var float = false

        for (char in this) {
            if (char == '.' && !float) {
                require(next().isNumber) {"Expected a number after the decimal point at ${line}:${column} but found '${cin[previousIndex]}'."}

                float = true
            } else if (!char.isNumber) {
                undo()
                val number = cin.substring(start, nextIndex)

                return when {
                    float -> FloatElement(number.toDouble())
                    else -> IntegerElement(number.toLong())
                }
            }
        }

        return null
    }

    private fun string(delimiter: Char): StringElement? {
        val start = previousIndex

        if (delimiter.isDelimiter) {
            val length = delimiterLength(start, delimiter)
            var foundLength = 0

            for (char in this) {
                if (char == delimiter) {
                    if (++foundLength == length) {
                        return StringElement(cin.substring(start + length, nextIndex - length).trimIndent().trim())
                    }
                } else {
                    foundLength = 0
                }
            }
        }

        return null
    }

    private fun delimiterLength(index: Int, delimiter: Char): Int {
        var length = 1

        while (cin[index + length] == delimiter) {
            length++
            next()
        }

        return length
    }

    companion object {
        fun parse(cin: String): Element? = Cin(cin).parseElement()
    }
}

package net.auoeke.cin.lexer

import net.auoeke.cin.lexer.lexeme.*
import net.auoeke.cin.lexer.lexeme.Lexeme.Type
import net.auoeke.extensions.listIterator
import net.auoeke.extensions.repeat
import net.auoeke.extensions.string
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
class Lexer(private val cin: String, retainSource: Boolean = false, private val jsonCompatible: Boolean = false, private val throwErrors: Boolean = true) : Iterable<Lexeme> {
    private val iterator: ListIterator<Char> = cin.listIterator()
    private val lexemes = ArrayList<Lexeme>()

    private var line = 1
    private var column = 0
    private var context: Context? = null
    private var elementLexeme: Lexeme? = null

    private inline val position get() = "$line:$column"
    private inline val previousIndex: Int get() = iterator.previousIndex()
    private inline val nextIndex: Int get() = iterator.nextIndex()

    private val acceptsColon get() = jsonCompatible && elementLexeme.let {it is StringLexeme && it.delimiter == "\""}

    init {
        eachChar {
            when {
                else -> process(it)
            }
        }

        eachChar(::process)

        if (!retainSource) {
            lexemes.removeIf {it.type.isComment || it.type == Type.WHITESPACE}
        }
    }

    override fun iterator(): ListIterator<Lexeme> = lexemes.listIterator()

    private fun next() = iterator.next().also {
        if (it == '\n') {
            line++
            column = 0
        }

        column++
    }

    private fun previous() = iterator.previous().also {
        if (it == '\n') {
            line--
        }

        column--
    }

    private fun peek(distance: Int = 0) = cin[nextIndex + distance]

    /** @return `false` if processed. */
    private fun processWhitespaceOrComment(char: Char): Boolean = false.also {
        when {
            char == '\n' -> this += NewlineLexeme(line, column)
            char.isWhitespace() -> whitespace(char)
            char == '/' && ifNext('/') -> comment()
            else -> return true
        }
    }

    private inline fun requireNext(message: String, matchNewline: Boolean = false, predicate: (Char) -> Boolean) = run {
        eachChar {
            when {
                !matchNewline && it == '\n' -> this += NewlineLexeme(line, column)
                it.isWhitespace() -> whitespace(it)
                it == '/' && ifNext('/') -> comment()
                predicate(it) -> {
                    process(it)
                    return
                }
                else -> return@run
            }
        }
    }.let {error(message)}

    private fun require(char: Char, message: String) = position.let {position ->
        eachChar {
            when (it) {
                char -> {
                    structure(it)
                    return
                }
                else -> process(it)
            }
        }

        error(position, message)
    }

    private fun process(char: Char, key: Boolean = false) {
        if (processWhitespaceOrComment(char)) when {
            char == ',' -> comma()
            char == '=' -> mapping(Type.EQUALS)
            char == ':' && acceptsColon -> mapping(Type.COLON)
            else -> {
                when {
                    !key && char in "{}[]" -> structure(char)
                    char in "\"'`" -> string(char)
                    else -> rawString(char, key)
                }

                when {
                    key -> requireNext("%s: Every key in a map must be followed by an assignment, an array or a map.") {
                        it in "={[" || it == ':' && acceptsColon
                    }
                    else -> eachChar {
                        when {
                            key && it == '=' || it in "\n,/}]" -> if (it != '/' || peek() == '/') {
                                previous()
                                return
                            }
                            it.isWhitespace() -> whitespace(it)
                            else -> error("Expected a separator before %s.")
                        }
                    }
                }
            }
        }
    }

    private inline fun eachChar(action: (Char) -> Unit) {
        while (iterator.hasNext()) {
            action(next())
        }
    }

    private infix fun add(lexeme: Lexeme?) {
        lexeme?.let {
            lexemes.add(lexeme)

            when (lexeme.type) {
                Type.COMMA, Type.LINE_COMMENT, Type.BLOCK_COMMENT, Type.NEWLINE, Type.WHITESPACE -> {}
                else -> elementLexeme = lexeme
            }
        }
    }

    private inline operator fun plusAssign(lexeme: Lexeme?) = this add lexeme

    private inline fun ifNext(target: Char, action: () -> Unit = {}): Boolean = (next() == target).also {
        when {
            it -> action()
            else -> previous()
        }
    }

    private fun error(position: String, message: String) = when {
        throwErrors -> throw SyntaxException(position, message)
        else -> elementLexeme?.error = SyntaxError(message)
    }

    private fun error(message: String) = error(position, message)

    private fun whitespace(whitespace: Char) = this add WhitespaceLexeme(line, column, buildString {
        append(whitespace)

        eachChar {
            when {
                !it.isWhitespace() || it == '\n' -> {
                    previous()
                    return@buildString
                }
                else -> append(it)
            }
        }
    })

    private fun comma() = this add CommaLexeme(line, column)

    private fun mapping(operator: Type) = this add MappingLexeme(line, column, operator)

    private fun comment() = this add when {
        ifNext('/') -> CommentLexeme(line, column, Type.BLOCK_COMMENT, buildString {
            eachChar {
                if (it == '/' && next() == '/') {
                    when (val char = next()) {
                        '/' -> return@buildString
                        else -> append(char)
                    }

                    append('/')
                }

                append(it)
            }
        })
        else -> CommentLexeme(line, column, Type.LINE_COMMENT, buildString {
            eachChar {
                if (it == '\n') {
                    previous()
                    return@buildString
                }

                append(it)
            }
        })
    }

    private fun structure(char: Char) {
        this += DelimiterLexeme(line, column, char)

        when (char) {
            '}' -> if (context !== Context.MAP) {
                error("Found '$char' at %s outside a map.")
            }
            ']' -> if (context !== Context.ARRAY) {
                error("Found '$char' at %s outside an array.")
            }
            else -> context = context.also {
                when (char) {
                    '{' -> {
                        context = Context.MAP
                        require('}', "%s: Map is not closed.")
                    }
                    '[' -> {
                        context = Context.ARRAY
                        require(']', "%s: Array is not closed.")
                    }
                }
            }
        }
    }

    private fun delimiterLength(delimiter: Char): Int {
        var length = 1

        eachChar {
            if (it == delimiter) {
                length++
            } else {
                previous()
                return length
            }
        }

        return length
    }

    private fun string(delimiter: Char) = this add when (val length = delimiterLength(delimiter)) {
        2 -> StringLexeme(line, column, delimiter.string, "")
        else -> StringLexeme(line, column, delimiter.repeat(length), buildString {
            eachChar {
                if (it != delimiter) {
                    append(it)
                } else {
                    val next = next()

                    if (next != delimiter) {
                        append(it)
                        append(next)
                    } else {
                        val next1 = next()

                        if (next1 == delimiter) {
                            return@buildString
                        }

                        append(it)
                        append(next)
                        append(next1)
                    }
                }
            }

            error("%s: The string is not closed.")
        })
    }

    private fun rawString(char: Char, key: Boolean = false) {
        val additionalLexemes = ArrayList<Lexeme>()

        this += when (val string = buildString {
            append(char)

            var whitespace: Int = -1

            eachChar {
                when {
                    key && char in "={[" || !key && it in "\n,/}]" -> if (it != '/' || peek() == '/') {
                        if (whitespace != -1) {
                            additionalLexemes += WhitespaceLexeme(line, column, cin.substring(whitespace, previousIndex))
                        }

                        when (char) {
                            '=' -> additionalLexemes += MappingLexeme(line, column, Type.EQUALS)
                            else -> previous()
                        }

                        return@buildString
                    }
                    else -> when {
                        it.isWhitespace() -> if (whitespace == -1) {
                            whitespace = previousIndex
                        }
                        else -> when (whitespace) {
                            -1 -> append(it)
                            else -> {
                                whitespace.rangeTo(previousIndex).forEach {index -> append(cin[index])}
                                whitespace = -1
                            }
                        }
                    }
                }
            }
        }) {
            "false" -> BooleanLexeme(line, column, false)
            "true" -> BooleanLexeme(line, column, true)
            "null" -> NullLexeme(line, column)
            else -> try {
                IntegerLexeme(line, column, string.toLong())
            } catch (exception: NumberFormatException) {
                try {
                    FloatLexeme(line, column, string.toDouble(), string.endsWith('.'))
                } catch (exception: NumberFormatException) {
                    StringLexeme(line, column, null, string)
                }
            }
        }

        additionalLexemes.forEach(::add)
    }
}

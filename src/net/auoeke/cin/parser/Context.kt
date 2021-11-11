package net.auoeke.cin.parser

import kotlin.contracts.contract

enum class Context {
    FILE,
    ARRAY,
    MAP;

    val start: Char get() = when (this) {
        ARRAY -> '['
        MAP -> '{'
        FILE -> Char.MIN_VALUE
    }

    val description: String get() = when (this) {
            ARRAY -> "an array"
            MAP -> "a map"
            FILE -> "a file"
        }

    companion object {
        val Char.context: Context get() = when (this) {
            '[' -> ARRAY
            '{' -> MAP
            else -> throw Error()
        }
    }
}

package net.auoeke.cin.parser.token

import net.auoeke.cin.parser.token.type.MappingOperator
import net.auoeke.cin.parser.token.type.TokenType

class MappingToken(line: Int, column: Int, val operator: MappingOperator) : Token(line, column) {
    override val type: TokenType get() = TokenType.MAPPING


    override fun toString(): String = when (operator) {
        MappingOperator.EQUALS -> "="
        MappingOperator.COLON -> ":"
    }
}

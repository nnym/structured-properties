package net.auoeke.cin.parser

import net.auoeke.extensions.letIf

class SyntaxException(private val position: String, message: String) : RuntimeException(message) {
    var file: String? = null

    override val message: String get() = super.message!!.format(position.letIf(file !== null) {"$file:$it"})
}

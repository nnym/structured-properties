package net.auoeke.cin.element

class MapElement : HashMap<String, Element>(), Element {
    override val type: ElementType get() = ElementType.MAP

    override fun clone(): MapElement = MapElement().also {it += this}

    override fun toString(): String = when (size) {
        0 -> "{}"
        1 -> entries.first().let {
            "{${format(it)}}"
        }
        else -> buildString {
            append("{\n")

            this@MapElement.forEach {entry ->
                append(format(entry).prependIndent("    "))
                append('\n')
            }

            append('}')
        }
    }

    private fun format(entry: Map.Entry<String, Element>): String = entry.let {(key, value) ->
        when (value.type) {
            ElementType.ARRAY, ElementType.MAP -> "$key $value"
            else -> "$key = $value"
        }
    }
}

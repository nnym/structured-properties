package net.auoeke.cin.element

class StringElement(var value: String) : Element {
    override val type: ElementType get() = ElementType.STRING

    override fun equals(other: Any?): Boolean = other is StringElement && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "\"$value\""
}

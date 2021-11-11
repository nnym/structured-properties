package net.auoeke.cin.element

class BooleanElement(var value: Boolean) : Element {
    override val type: ElementType get() = ElementType.BOOLEAN

    override fun equals(other: Any?): Boolean = other is BooleanElement && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = value.toString()
}

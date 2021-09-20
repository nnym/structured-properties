package net.auoeke.cin.element

class BooleanElement(var value: Boolean) : Element {
    override val type: ElementType get() = ElementType.BOOLEAN

    override fun toString(): String = value.toString()
}

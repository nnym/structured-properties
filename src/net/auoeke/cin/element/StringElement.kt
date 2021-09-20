package net.auoeke.cin.element

class StringElement(var value: String) : Element {
    override val type: ElementType get() = ElementType.STRING

    override fun toString(): String = "\"$value\""
}

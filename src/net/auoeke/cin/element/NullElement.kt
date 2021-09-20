package net.auoeke.cin.element

object NullElement : Element {
    override val type: ElementType get() = ElementType.NULL

    override fun toString(): String = "null"
}

package net.auoeke.cin.element

class ArrayElement : ArrayList<Element>(), Element {
    override val type: ElementType get() = ElementType.ARRAY

    override fun clone(): ArrayElement = ArrayElement().also {
        it.addAll(this)
    }
}

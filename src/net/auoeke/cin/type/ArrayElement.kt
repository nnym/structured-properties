package net.auoeke.cin.type

class ArrayElement : ArrayList<Element>(), Element {
    override fun clone(): ArrayElement = ArrayElement().also {
        it.addAll(this)
    }
}

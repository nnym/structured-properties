package net.auoeke.cin.type

class IntegerElement(var value: Long) : NumberElement {
    override fun toString(): String = value.toString()
}

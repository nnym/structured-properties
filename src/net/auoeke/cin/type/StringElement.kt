package net.auoeke.cin.type

class StringElement(var value: String) : Element {
    override fun toString(): String = "\"$value\""
}

package net.auoeke.cin.element

class IntegerElement(var value: Long) : NumberElement() {
    override val type: ElementType get() = ElementType.INTEGER

    override fun toByte(): Byte = value.toByte()
    override fun toChar(): Char = value.toInt().toChar()
    override fun toDouble(): Double = value.toDouble()
    override fun toFloat(): Float = value.toFloat()
    override fun toInt(): Int = value.toInt()
    override fun toLong(): Long = value
    override fun toShort(): Short = value.toShort()
    override fun toString(): String = value.toString()
}

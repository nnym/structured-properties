package net.auoeke.cin.element

class FloatElement(var value: Double) : NumberElement() {
    override val type: ElementType get() = ElementType.FLOAT

    override fun toByte(): Byte = value.toInt().toByte()
    override fun toChar(): Char = value.toInt().toChar()
    override fun toDouble(): Double = value
    override fun toFloat(): Float = value.toFloat()
    override fun toInt(): Int = value.toInt()
    override fun toLong(): Long = value.toLong()
    override fun toShort(): Short = value.toInt().toShort()
    override fun toString(): String = value.toString()
}

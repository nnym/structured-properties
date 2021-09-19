package net.auoeke.cin.type

class MapElement : HashMap<String, Element>(), Element {
    override fun clone(): MapElement = MapElement().also {it += this}
}

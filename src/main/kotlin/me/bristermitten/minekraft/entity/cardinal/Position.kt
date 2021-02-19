package me.bristermitten.minekraft.entity.cardinal

/**
 * x is 26 bit
 * y is 12 bit
 * z is 26 bit
 */
open class Position(
    open val x: Int,
    open val y: Int,
    open val z: Int
) {

    fun toProtocol() = ((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF)
}

fun Long.toPosition() = Position((this shr 38).toInt(), (this and 0xFFF).toInt(), (this shl 26 shr 38).toInt())
package me.bristermitten.minekraft.entities.block

import kotlin.math.floor

class BlockPosition(
    val x: Int,
    val y: Int,
    val z: Int
) {

    constructor(x: Double, y: Double, z: Double) : this(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())

    fun toLong(): Long = ((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF)

    companion object {

        @JvmStatic
        val ZERO = BlockPosition(0.0, 0.0, 0.0)
    }
}
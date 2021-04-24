package org.kryptonmc.krypton.util

import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World

/**
 * Convert a [Vector] to a single long with its X coordinate packed into the most significant 26
 * bits, its Z packed into the middle 26 bits, and its Y packed into the least significant 12 bits
 *
 * @author Callum Seabrook
 */
fun Vector.toProtocol() = ((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF)

/**
 * @see Vector.toProtocol
 */
fun Location.toProtocol() = ((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF)

/**
 * Convert a single long to a [Vector]
 *
 * @author Callum Seabrook
 */
fun Long.toVector() = Vector((this shr 38).toDouble(), (this and 0xFFF).toDouble(), (this shl 26 shr 38).toDouble())

/**
 * @see Long.toVector
 */
fun Long.toLocation(world: World) = Location(world, (this shr 38).toDouble(), (this and 0xFFF).toDouble(), (this shl 26 shr 38).toDouble())

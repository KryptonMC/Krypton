package org.kryptonmc.krypton.extension

import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import org.kryptonmc.krypton.api.world.World

fun Vector.toProtocol() = ((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF)

fun Location.toProtocol() = ((x.toLong() and 0x3FFFFFF) shl 38) or ((z.toLong() and 0x3FFFFFF) shl 12) or (y.toLong() and 0xFFF)

fun Long.toPosition() = Vector((this shr 38).toDouble(), (this and 0xFFF).toDouble(), (this shl 26 shr 38).toDouble())

fun Long.toLocation(world: World) = Location(world, (this shr 38).toDouble(), (this and 0xFFF).toDouble(), (this shl 26 shr 38).toDouble())
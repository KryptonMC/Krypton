package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.world.Location
import kotlin.math.floor

data class ChunkPosition(val x: Int, val z: Int) {

    val regionX = x shr 5
    val regionZ = z shr 5
    val regionLocalX = x and 0x1F
    val regionLocalZ = z and 0x1F

    operator fun contains(location: Location) =
        floor(location.x / 16.0).toInt() == x && floor(location.z / 16.0).toInt() == z

    companion object {

        val ZERO = ChunkPosition(0, 0)
    }
}
package org.kryptonmc.krypton.world.chunk

import org.kryptonmc.krypton.api.world.Location
import kotlin.math.floor

/**
 * Holds a pair of chunk coordinates (x and z)
 *
 * @author Callum Seabrook
 */
data class ChunkPosition(val x: Int, val z: Int) {

    /**
     * Region coordinates for this chunk coordinate pair
     */
    val regionX = x shr 5
    val regionZ = z shr 5

    /**
     * Region local values are the coordinates relative to the region they are in
     */
    val regionLocalX = x and 0x1F
    val regionLocalZ = z and 0x1F

    operator fun contains(location: Location) =
        floor(location.x / 16.0).toInt() == x && floor(location.z / 16.0).toInt() == z

    companion object {

        val ZERO = ChunkPosition(0, 0)
    }
}
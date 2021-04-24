package org.kryptonmc.krypton.world.dimension

import org.kryptonmc.krypton.api.space.Vector
import java.util.UUID

data class EndDimensionData(
    val exitPortalLocation: Vector,
    val gateways: IntArray,
    val dragonKilled: Boolean,
    val dragonUUID: UUID,
    val previouslyKilled: Boolean
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EndDimensionData
        return exitPortalLocation == other.exitPortalLocation && gateways.contentEquals(other.gateways) && dragonKilled == other.dragonKilled && dragonUUID == other.dragonUUID && previouslyKilled == other.previouslyKilled
    }

    override fun hashCode() =
        arrayOf(exitPortalLocation, gateways, dragonKilled, dragonUUID, previouslyKilled).contentDeepHashCode()
}

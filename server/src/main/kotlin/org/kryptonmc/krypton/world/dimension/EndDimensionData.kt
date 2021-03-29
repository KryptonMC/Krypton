package org.kryptonmc.krypton.world.dimension

import org.kryptonmc.krypton.api.space.Vector
import java.util.*

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

    override fun hashCode(): Int {
        var result = exitPortalLocation.hashCode()
        result = 31 * result + gateways.contentHashCode()
        result = 31 * result + dragonKilled.hashCode()
        result = 31 * result + dragonUUID.hashCode()
        result = 31 * result + previouslyKilled.hashCode()
        return result
    }
}
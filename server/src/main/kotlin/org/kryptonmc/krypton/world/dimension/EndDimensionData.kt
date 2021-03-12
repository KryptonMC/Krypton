package org.kryptonmc.krypton.world.dimension

import org.kryptonmc.krypton.api.space.Vector
import java.util.*

data class EndDimensionData(
    val exitPortalLocation: Vector,
    val gateways: List<Int>,
    val dragonKilled: Boolean,
    val dragonUUID: UUID,
    val previouslyKilled: Boolean
)
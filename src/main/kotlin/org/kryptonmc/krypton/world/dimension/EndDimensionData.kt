package org.kryptonmc.krypton.world.dimension

import org.kryptonmc.krypton.space.Position
import java.util.*

data class EndDimensionData(
    val exitPortalLocation: Position,
    val gateways: List<Int>,
    val dragonKilled: Boolean,
    val dragonUUID: UUID,
    val previouslyKilled: Boolean
)
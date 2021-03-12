package org.kryptonmc.krypton.entity

import org.kryptonmc.krypton.world.LocationBuilder
import java.util.*

data class Entity(
    val id: Int,
    val uuid: UUID,
    val type: EntityType,
    val location: LocationBuilder,
    val data: Int,
    val velocityX: Short,
    val velocityY: Short,
    val velocityZ: Short
)
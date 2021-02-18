package me.bristermitten.minekraft.entity

import me.bristermitten.minekraft.world.Location
import java.util.*

data class Entity(
    val entityId: Int,
    val uuid: UUID,
    val type: EntityType,
    val location: Location,
    val data: Int,
    val velocityX: Short,
    val velocityY: Short,
    val velocityZ: Short
)
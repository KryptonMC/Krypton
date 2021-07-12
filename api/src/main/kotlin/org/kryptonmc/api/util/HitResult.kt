package org.kryptonmc.api.util

import org.kryptonmc.api.entity.Entity
import org.spongepowered.math.vector.Vector3i

abstract class HitResult(val clickLocation: Vector3i) {

    abstract val type: Type

    fun distanceTo(entity: Entity): Double {
        val distanceX = clickLocation.x() - entity.location.x
        val distanceY = clickLocation.y() - entity.location.y
        val distanceZ = clickLocation.z() - entity.location.z
        return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ
    }

    enum class Type {

        MISS,
        BLOCK,
        ENTITY;
    }
}

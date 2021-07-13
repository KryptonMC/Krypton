/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
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

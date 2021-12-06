/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util

import org.kryptonmc.api.entity.Entity
import org.spongepowered.math.vector.Vector3d

/**
 * The result of a player hitting (attacking) something.
 */
interface HitResult {

    /**
     * The location where the player clicked.
     */
    val clickLocation: Vector3d

    /**
     * The type of this hit result.
     */
    val type: Type

    /**
     * Gets the distance squared from the [clickLocation] to the given
     * [entity]'s location, where the distance squared is calculated as
     * the following:
     *
     * `(clickedX - entityX) ^ 2 + (clickedY - entityY) ^ 2 + (clickedZ - entityZ) ^ 2`
     *
     * @param entity the entity to calculate the distance to
     * @return the distance squared from the click location to the given entity
     */
    fun distanceTo(entity: Entity): Double {
        val distanceX = clickLocation.x() - entity.location.x()
        val distanceY = clickLocation.y() - entity.location.y()
        val distanceZ = clickLocation.z() - entity.location.z()
        return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ
    }

    /**
     * The type of hit result.
     */
    enum class Type {

        MISS,
        BLOCK,
        ENTITY
    }
}

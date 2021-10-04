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
import org.spongepowered.math.vector.Vector3d

/**
 * The result of a player hitting (attacking) something.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface HitResult {

    /**
     * The location where the player clicked.
     */
    @get:JvmName("clickLocation")
    public val clickLocation: Vector3d

    /**
     * The type of this hit result.
     */
    @get:JvmName("type")
    public val type: Type

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
    public fun distanceTo(entity: Entity): Double {
        val distanceX = clickLocation.x() - entity.location.x()
        val distanceY = clickLocation.y() - entity.location.y()
        val distanceZ = clickLocation.z() - entity.location.z()
        return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ
    }

    /**
     * The type of hit result.
     */
    public enum class Type {

        MISS,
        BLOCK,
        ENTITY
    }
}

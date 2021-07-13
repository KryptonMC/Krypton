/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import org.kryptonmc.api.space.Direction
import org.kryptonmc.api.util.HitResult
import org.spongepowered.math.vector.Vector3i

class BlockHitResult(
    clickLocation: Vector3i,
    val position: Vector3i,
    val direction: Direction,
    private val missed: Boolean,
    val isInside: Boolean
) : HitResult(clickLocation) {

    override val type = if (missed) Type.MISS else Type.BLOCK

    fun withPosition(position: Vector3i) = BlockHitResult(clickLocation, position, direction, missed, isInside)

    fun withDirection(direction: Direction) = BlockHitResult(clickLocation, position, direction, missed, isInside)
}

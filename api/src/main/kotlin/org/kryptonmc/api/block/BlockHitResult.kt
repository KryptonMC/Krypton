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

/**
 * Represents the result of a player hitting/attacking a block.
 *
 * @param clickLocation the location that the player clicked
 * @param position the position of the block
 * @param direction the direction the block was hit from
 * @param missed if the block was missed or not
 * @param isInside if the player is inside of this block
 */
class BlockHitResult(
    clickLocation: Vector3i,
    val position: Vector3i,
    val direction: Direction,
    private val missed: Boolean,
    val isInside: Boolean
) : HitResult(clickLocation) {

    override val type = if (missed) Type.MISS else Type.BLOCK
}

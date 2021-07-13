/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.space

import org.kryptonmc.api.util.floor
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * This abstract class defines common defaults between both of its expected implementations,
 * [Vector] and [org.kryptonmc.api.world.Location].
 *
 * To keep consistency, a [Position] will always be destructured into (x, y, z), regardless
 * if the implementation has any other fields (such as [org.kryptonmc.api.world.Location] having
 * world, yaw and pitch).
 *
 * The copy function will also only world on the X, Y and Z coordinates of the location. The
 * implementation may also have its own copy function however that can operate on more than
 * just these three.
 */
abstract class AbstractPosition(
    final override val x: Double,
    final override val y: Double,
    final override val z: Double
) : Position {

    final override val length by lazy { sqrt(lengthSquared) }
    final override val lengthSquared = x * x + y * y + z * z
    final override val blockX = x.floor()
    final override val blockY = y.floor()
    final override val blockZ = z.floor()
    final override val isNormalized = abs(lengthSquared - 1) < Position.EPSILON

    /**
     * The X component of this position, for destructuring
     */
    operator fun component1() = x

    /**
     * The Y component of this position, for destructuring
     */
    operator fun component2() = y

    /**
     * The Z component of this position, for destructuring
     */
    operator fun component3() = z
}

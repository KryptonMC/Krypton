/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.dimension

import net.kyori.adventure.key.Key
import java.util.OptionalLong

data class DimensionType(
    val key: Key,
    val isPiglinSafe: Boolean,
    val isNatural: Boolean,
    val isUltrawarm: Boolean,
    val hasSkylight: Boolean,
    val hasCeiling: Boolean,
    val hasRaids: Boolean,
    val bedWorks: Boolean,
    val respawnAnchorWorks: Boolean,
    val ambientLight: Float,
    val fixedTime: Long?,
    val infiniburn: Key,
    val effects: Key,
    val minimumY: Int,
    val height: Int,
    val logicalHeight: Int,
    val coordinateScale: Double,
)

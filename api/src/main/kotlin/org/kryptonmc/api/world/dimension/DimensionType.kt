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

/**
 * Represents data for a dimension.
 *
 * @param key the key for this dimension
 * @param isPiglinSafe if [Piglin]s will transform into [ZombifiedPiglin]s
 *        over time
 * @param isNatural if portals created will spawn [ZombifiedPiglin]s
 *        naturally, and if the compass works properly
 * @param isUltrawarm if water will evaporate or wet sponges will become
 *        regular sponges, and if lava will flow faster and thinner
 * @param hasSkylight if the world has global lighting (light from the sky)
 * @param hasCeiling if the world has a ceiling made of blocks
 * @param hasRaids if raids will spawn
 * @param bedWorks if beds can be slept in. If false, beds will explode when
 *        used
 * @param respawnAnchorWorks if respawn anchors can be charged and used
 * @param ambientLight the amount of lighting a client will show whilst in
 *        the dimension
 * @param fixedTime the time it will always be. If null, this means that time
 *        may progress as normal
 * @param infiniburn the settings to use to define what blocks burn infinitely
 *        in this dimension
 * @param effects the effects settings to use
 * @param minimumY the minimum Y level that can be built at
 * @param height the maximum Y level that can be built at
 * @param logicalHeight the maximum logical height that can be built at. For example,
 *        the nether is 128 blocks tall, with a roof at the top, which is why this
 *        value is set to 128. Though you can still build above the roof, it is not
 *        intended for you to do so
 * @param coordinateScale the scale of coordinates. For example, in the nether,
 *        1 block is equivalent to 8 blocks in the overworld, so the coordinate scale
 *        for the nether is 8.0
 */
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

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
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface DimensionType {

    /**
     * If [Piglin]s will transform in to [ZombifiedPiglin]s over time.
     */
    public val isPiglinSafe: Boolean

    /**
     * If portals created will spawn [ZombifiedPiglin]s naturally, and if the
     * compass works properly.
     */
    public val isNatural: Boolean

    /**
     * If water will evaporate or wet sponges will become regular sponges when
     * placed, and if laval will flow faster and thinner.
     */
    public val isUltrawarm: Boolean

    /**
     * If there is global lighting (light from the sky).
     */
    @get:JvmName("hasSkylight")
    public val hasSkylight: Boolean

    /**
     * If there is a ceiling made of blocks.
     */
    @get:JvmName("hasCeiling")
    public val hasCeiling: Boolean

    /**
     * If raids will spawn naturally.
     */
    @get:JvmName("hasRaids")
    public val hasRaids: Boolean

    /**
     * If beds can be slept in. If false, beds will explode when used (boom).
     */
    @get:JvmName("bedWorks")
    public val bedWorks: Boolean

    /**
     * If respawn anchors can be charged and used.
     */
    @get:JvmName("respawnAnchorWorks")
    public val respawnAnchorWorks: Boolean

    /**
     * The amount of lighting clients will display when in this dimension.
     */
    @get:JvmName("ambientLight")
    public val ambientLight: Float

    /**
     * The time it will always be. If null, the time will progress normally.
     */
    @get:JvmName("fixedTime")
    public val fixedTime: Long?

    /**
     * The settings used to define which blocks burn infinitely.
     */
    @get:JvmName("infiniburn")
    public val infiniburn: Key

    /**
     * The minimum Y level that can be built at.
     */
    @get:JvmName("minimumY")
    public val minimumY: Int

    /**
     * The maximum Y level that can be built at.
     */
    @get:JvmName("height")
    public val height: Int

    /**
     * The maximum logical Y level that can be built at.
     *
     * For example, in the nether, there is a bedrock roof at 128 blocks, so
     * the logical height for the nether is 128, as whilst you can still build
     * above he nether roof, it is not intended for you to do so.
     */
    @get:JvmName("logicalHeight")
    public val logicalHeight: Int

    /**
     * The scale of coordinates. For example, in the nether, the coordinate
     * scale is 8.0, as for every 1 block you walk in the nether, you will walk
     * 8 blocks in dimensions with a coordinate scale of 1.0, such as the
     * overworld and the end.
     */
    @get:JvmName("coordinateScale")
    public val coordinateScale: Double
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.banner

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.util.provide

/**
 * A pattern for a banner. These are immutable, and may be reused.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BannerPattern {

    /**
     * The type of this banner pattern.
     */
    @get:JvmName("type")
    public val type: BannerPatternType

    /**
     * The colour of this banner pattern.
     */
    @get:JvmName("color")
    public val color: DyeColor

    @ApiStatus.Internal
    public interface Factory {

        public fun of(type: BannerPatternType, color: DyeColor): BannerPattern
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new banner pattern with the given values.
         *
         * @param type the type
         * @param color the colour
         * @return a new banner pattern
         */
        @JvmStatic
        public fun of(type: BannerPatternType, color: DyeColor): BannerPattern = FACTORY.of(type, color)
    }
}

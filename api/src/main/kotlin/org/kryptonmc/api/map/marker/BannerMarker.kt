/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.map.marker

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.util.provide
import org.spongepowered.math.vector.Vector3i

/**
 * A marker that marks the position of specific block in the world on a map
 * with an icon of a banner.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface BannerMarker : MapMarker {

    /**
     * The colour of this marker on the map.
     */
    @get:JvmName("color")
    public val color: DyeColor

    /**
     * The custom display name of this marker, or null if this marker does not
     * have a custom display name.
     */
    @get:JvmName("name")
    public val name: Component?

    @ApiStatus.Internal
    public interface Factory {

        public fun of(position: Vector3i, color: DyeColor, name: Component?): BannerMarker
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new banner marker with the given [position], [color], and
         * [name].
         *
         * @param position the position of the marked block
         * @param color the colour of the banner
         * @param name the custom display name of the banner
         * @return a new banner marker
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(position: Vector3i, color: DyeColor, name: Component): BannerMarker = FACTORY.of(position, color, name)

        /**
         * Creates a new banner marker with the given [position] and [color].
         *
         * @param position the position of the marked block
         * @param color the colour of the banner
         * @return a new banner marker
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun of(position: Vector3i, color: DyeColor): BannerMarker = FACTORY.of(position, color, null)
    }
}

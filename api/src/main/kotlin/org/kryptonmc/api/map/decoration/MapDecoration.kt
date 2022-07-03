/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.map.decoration

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide

/**
 * A decoration that appears on a map at a specific point.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface MapDecoration {

    /**
     * The type of the decoration.
     */
    @get:JvmName("type")
    public val type: MapDecorationType

    /**
     * The X coordinate of this decoration on the map.
     */
    @get:JvmName("x")
    public val x: Int

    /**
     * The Y coordinate of this decoration on the map.
     */
    @get:JvmName("y")
    public val y: Int

    /**
     * The orientation of this decoration on the map.
     */
    @get:JvmName("orientation")
    public val orientation: MapDecorationOrientation

    /**
     * The custom display name of this decoration, or null if this decoration
     * does not have a custom display name.
     */
    @get:JvmName("name")
    public val name: Component?

    @ApiStatus.Internal
    public interface Factory {

        public fun of(type: MapDecorationType, x: Int, y: Int, orientation: MapDecorationOrientation, name: Component?): MapDecoration
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new map decoration with the given [type] and [name]
         * appearing at the given [x] and [y] coordinates on the map in the
         * given [orientation].
         *
         * @param type the type of the decoration
         * @param x the X coordinate of the decoration on the map
         * @param y the Y coordinate of the decoration on the map
         * @param orientation the orientation of the decoration on the map
         * @param name the custom display name of the decoration
         * @return a new map decoration
         */
        @JvmStatic
        @Contract("_, _, _, _, _ -> new", pure = true)
        public fun of(type: MapDecorationType, x: Int, y: Int, orientation: MapDecorationOrientation, name: Component): MapDecoration =
            FACTORY.of(type, x, y, orientation, name)

        /**
         * Creates a new map decoration with the given [type] appearing at the
         * given [x] and [y] coordinates on the map in the given [orientation].
         *
         * @param type the type of the decoration
         * @param x the X coordinate of the decoration on the map
         * @param y the Y coordinate of the decoration on the map
         * @param orientation the orientation of the decoration on the map
         * @return a new map decoration
         */
        @JvmStatic
        @Contract("_, _, _, _ -> new", pure = true)
        public fun of(type: MapDecorationType, x: Int, y: Int, orientation: MapDecorationOrientation): MapDecoration =
            FACTORY.of(type, x, y, orientation, null)
    }
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.map

import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.map.color.MapColor
import org.kryptonmc.api.map.color.MapColorBrightness
import org.kryptonmc.api.map.color.MapColorType
import org.kryptonmc.api.map.decoration.MapDecoration
import org.kryptonmc.api.map.marker.BannerMarker
import org.kryptonmc.api.map.marker.ItemFrameMarker
import org.kryptonmc.api.map.marker.MapMarker
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.provide
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector2i
import java.awt.Color
import java.awt.Image

/**
 * Information for a map that players may be viewing.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface MapInfo {

    /**
     * The absolute X coordinate of the centre of this map.
     */
    @get:JvmName("centerX")
    public val centerX: Int

    /**
     * The absolute Z coordinate of the centre of this map.
     */
    @get:JvmName("centerZ")
    public val centerZ: Int

    /**
     * The dimension that this map is in.
     */
    @get:JvmName("dimension")
    public val dimension: ResourceKey<World>

    /**
     * The scale of this map (how zoomed in it is for viewers).
     */
    @get:JvmName("scale")
    public val scale: Int

    /**
     * If this map has been locked in a cartography table.
     */
    public val isLocked: Boolean

    /**
     * If this map is tracking the position of the viewer and will display a
     * positional arrow when near the centre.
     */
    public val isTrackingPosition: Boolean

    /**
     * If the positional arrow indicating the position of the viewer will get
     * smaller when the viewer is out of the standard range rather than
     * disappearing as soon as the viewer reaches the edge.
     */
    public val isTrackingUnlimited: Boolean

    /**
     * All the decorations on this map.
     */
    @get:JvmName("decorations")
    public val decorations: Collection<MapDecoration>

    /**
     * All the banner markers on this map.
     */
    @get:JvmName("bannerMarkers")
    public val bannerMarkers: Collection<BannerMarker>

    /**
     * All the item frame markers on this map.
     */
    @get:JvmName("itemFrameMarkers")
    public val itemFrameMarkers: Collection<ItemFrameMarker>

    /**
     * Gets the map colour at the given [x] and [y] coordinates relative to
     * this map.
     *
     * Map coordinates are values between 0 and 128, where (0, 0) is the top
     * left pixel on the map, and (127, 127) is the bottom right pixel on the
     * map, as all maps are 128x128.
     *
     * @param x the X coordinate on this map
     * @param y the Y coordinate on this map
     * @return the colour at the coordinates
     */
    public fun getColor(x: Int, y: Int): MapColor

    /**
     * Sets the map colour at the given [x] and [y] coordinates relative to
     * this map to the given [color].
     *
     * @param x the X coordinate on this map
     * @param y the Y coordinate on this map
     * @param color the colour
     */
    public fun setColor(x: Int, y: Int, color: MapColor)

    /**
     * Sets the map colour at the given [x] and [y] coordinates relative to
     * this map to the given [color].
     *
     * @param x the X coordinate on this map
     * @param y the Y coordinate on this map
     * @param color the colour
     */
    public fun setColor(x: Int, y: Int, color: Color)

    /**
     * Sets the map colour at the given [x] and [y] coordinates relative to
     * this map to the given RGB integer [color].
     *
     * @param x the X coordinate on this map
     * @param y the Y coordinate on this map
     * @param color the RGB integer colour
     */
    public fun setColor(x: Int, y: Int, color: Int)

    /**
     * Sets the map colour at the given [x] and [y] coordinates relative to
     * this map to a colour constructed from the given [type] and [brightness].
     *
     * @param x the X coordinate on this map
     * @param y the Y coordinate on this map
     * @param type the type of the colour
     * @param brightness the brightness of the colour
     */
    public fun setColor(x: Int, y: Int, type: MapColorType, brightness: MapColorBrightness)

    /**
     * Gets the banner marker at the given absolute [x], [y], and [z]
     * coordinates in the world.
     *
     * @param x the absolute X coordinate in the world
     * @param y the absolute Y coordinate in the world
     * @param z the absolute Z coordinate in the world
     * @return the marker at the coordinates, or null if not present
     */
    public fun getBannerMarker(x: Int, y: Int, z: Int): BannerMarker?

    /**
     * Adds a banner marker to this map at the given [x], [y], and [z]
     * coordinates in the world.
     *
     * @param x the absolute X coordinate in the world
     * @param y the absolute Y coordinate in the world
     * @param z the absolute Z coordinate in the world
     * @param marker the marker
     */
    public fun addBannerMarker(x: Int, y: Int, z: Int, marker: BannerMarker)

    /**
     * Removes the banner marker at the given [x], [y], and [z] coordinates in
     * the world and returns the removed marker.
     *
     * @param x the absolute X coordinate in the world
     * @param y the absolute Y coordinate in the world
     * @param z the absolute Z coordinate in the world
     * @return the removed marker, or null if no marker was removed
     */
    public fun removeBannerMarker(x: Int, y: Int, z: Int): BannerMarker?

    /**
     * Gets the item frame marker marking the given [entity].
     *
     * @param entity the marked entity
     * @return the marker at the coordinates, or null if not present
     */
    public fun getItemFrameMarker(entity: Entity): ItemFrameMarker?

    /**
     * Adds the given item frame [marker] to this map.
     *
     * @param marker the marker
     */
    public fun addItemFrameMarker(marker: ItemFrameMarker)

    /**
     * Removes the given item frame [marker] from this map.
     *
     * @param marker the marker
     */
    public fun removeItemFrameMarker(marker: ItemFrameMarker)

    /**
     * A builder for building maps.
     */
    public interface Builder : AbstractBuilder<MapInfo> {

        /**
         * Sets the centre X coordinate of the map to the given [x] coordinate.
         *
         * @param x the central X coordinate
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun centerX(x: Int): Builder

        /**
         * Sets the centre Z coordinate of the map to the given [z] coordinate.
         *
         * @param z the central Z coordinate
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun centerZ(z: Int): Builder

        /**
         * Sets the centre position of the map to the given [position].
         *
         * @param position the position
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun center(position: Vector2i): Builder

        /**
         * Sets the dimension that the map is in to the given [dimension].
         *
         * @param dimension the dimension
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun dimension(dimension: ResourceKey<World>): Builder

        /**
         * Sets the scale of the map to the given [scale].
         *
         * @param scale the scale
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun scale(scale: Int): Builder

        /**
         * Locks the map, meaning it will never update with the terrain that it
         * represents (if it does represent captured terrain).
         *
         * @return this builder
         */
        @Contract("-> this", mutates = "this")
        public fun lock(): Builder

        /**
         * Sets the map to track the position of the viewer viewing the map.
         *
         * @return this builder
         */
        @Contract("-> this", mutates = "this")
        public fun trackPosition(): Builder

        /**
         * Removes the viewer tracking limit of the edge of the map, meaning
         * the map will always display the viewer's position.
         *
         * The arrow marking the viewer's position when in this mode will
         * shrink as the viewer travels further away from the map, rather than
         * just disappearing as soon as the viewer reaches the edge of the
         * map's boundary.
         *
         * @return this builder
         */
        @Contract("-> this", mutates = "this")
        public fun unlimitedTracking(): Builder

        /**
         * Sets the displayed colour at the given [x] and [y] coordinates on
         * the map to the given [color].
         *
         * @param x the X coordinate on the map
         * @param y the Y coordinate on the map
         * @param color the colour
         * @return this builder
         */
        @Contract("_, _, _ -> this", mutates = "this")
        public fun color(x: Int, y: Int, color: MapColor): Builder

        /**
         * Sets the displayed colour at the given [x] and [y] coordinates on
         * the map to the given RGB [color].
         *
         * @param x the X coordinate on the map
         * @param y the Y coordinate on the map
         * @param color the RGB colour
         * @return this builder
         */
        @Contract("_, _, _ -> this", mutates = "this")
        public fun color(x: Int, y: Int, color: Color): Builder

        /**
         * Sets the displayed colour at the given [x] and [y] coordinates on
         * the map to the given RGB integer [color].
         *
         * @param x the X coordinate on the map
         * @param y the Y coordinate on the map
         * @param color the RGB integer colour
         * @return this builder
         */
        @Contract("_, _, _ -> this", mutates = "this")
        public fun color(x: Int, y: Int, color: Int): Builder

        /**
         * Sets the displayed colour at the given [x] and [y] coordinates on
         * the map to the map colour with the given [type] and [brightness].
         *
         * @param x the X coordinate on the map
         * @param y the Y coordinate on the map
         * @param type the type of map colour
         * @param brightness the brightness of the map colour
         * @return this builder
         */
        @Contract("_, _, _, _ -> this", mutates = "this")
        public fun color(x: Int, y: Int, type: MapColorType, brightness: MapColorBrightness): Builder

        /**
         * Decorates the map with the given [decoration], adding it to the
         * list of displayed decorations on the map.
         *
         * @param decoration the decoration
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun decorate(decoration: MapDecoration): Builder

        /**
         * Marks a specific location on the map with the given [marker], adding
         * the marker to the list of markers of its type.
         *
         * @param marker the marker
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun mark(marker: MapMarker): Builder

        /**
         * Draws the given [image] on this map.
         *
         * @param image the image
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fromImage(image: Image): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building a map.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = FACTORY.builder()
    }
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i
import javax.annotation.concurrent.Immutable

/**
 * Item metadata for a compass.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@Immutable
public interface CompassMeta : ScopedItemMeta<CompassMeta.Builder, CompassMeta> {

    /**
     * Whether the compass is tracking a lodestone.
     */
    public val isTrackingLodestone: Boolean

    /**
     * The dimension that the tracked lodestone is in.
     */
    @get:JvmName("lodestoneDimension")
    public val lodestoneDimension: ResourceKey<World>?

    /**
     * The position of the lodestone the compass is tracking.
     */
    @get:JvmName("lodestonePosition")
    public val lodestonePosition: Vector3i?

    /**
     * Creates new item metadata tracking the lodestone in the given
     * [dimension] at the given [position].
     *
     * This will set [isTrackingLodestone] to true.
     *
     * @param dimension the dimension the lodestone is in
     * @param position the position the lodestone is at
     * @return new item metadata
     */
    @Contract("_, _ -> new", pure = true)
    public fun withLodestone(dimension: ResourceKey<World>, position: Vector3i): CompassMeta

    /**
     * Creates new item metadata without a tracked lodestone.
     *
     * This will set [isTrackingLodestone] to false, and both
     * [lodestoneDimension] and [lodestonePosition] to null.
     *
     * @return new item metadata
     */
    @Contract("-> new", pure = true)
    public fun withoutLodestone(): CompassMeta

    /**
     * A builder for building compass metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, CompassMeta> {

        /**
         * Sets the dimension and position of the lodestone the compass is
         * tracking to the given [dimension] and [position].
         *
         * @param dimension the dimension the lodestone is in
         * @param position the position the lodestone is at
         * @return this builder
         */
        @MetaDsl
        @Contract("_, _ -> this", mutates = "this")
        public fun lodestone(dimension: ResourceKey<World>, position: Vector3i): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building compass metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(CompassMeta::class.java)
    }
}

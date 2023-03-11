/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.api.world.World
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for a compass.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
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
    public val lodestonePosition: Vec3i?

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
    public fun withLodestone(dimension: ResourceKey<World>, position: Vec3i): CompassMeta

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
        public fun lodestone(dimension: ResourceKey<World>, position: Vec3i): Builder
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

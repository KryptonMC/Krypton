package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.spongepowered.math.vector.Vector3i

/**
 * Item metadata for a compass.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface CompassMeta : ScopedItemMeta<CompassMeta> {

    /**
     * Whether the compass is tracking a lodestone.
     */
    public val isTrackingLodestone: Boolean

    /**
     * The dimension that the tracked loadstone is in.
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
    public fun withLodestone(dimension: ResourceKey<World>, position: Vector3i): CompassMeta

    /**
     * Creates new item metadata without a tracked lodestone.
     *
     * This will set [isTrackingLodestone] to false, and both
     * [lodestoneDimension] and [lodestonePosition] to null.
     *
     * @return new item metadata
     */
    public fun withoutLodestone(): CompassMeta

    /**
     * A builder for building compass metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, CompassMeta> {

        /**
         * Sets the dimension and position of the lodestone the compass is
         * tracking to the given [dimension] and [position].
         *
         * @param dimension the dimension the lodestone is in
         * @param position the position the lodestone is at
         * @return this builder
         */
        @Contract("_, _ -> this", mutates = "this")
        public fun lodestone(dimension: ResourceKey<World>, position: Vector3i): Builder
    }
}

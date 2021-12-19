package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.data.FireworkEffect

/**
 * Item metadata for a firework star.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface FireworkStarMeta : ScopedItemMeta<FireworkStarMeta> {

    /**
     * The effect displayed when the star explodes.
     */
    @get:JvmName("effect")
    public val effect: FireworkEffect

    /**
     * Creates new item metadata with the given [effect].
     *
     * @param effect the new effect
     * @return new item metadata
     */
    public fun withEffect(effect: FireworkEffect): FireworkStarMeta

    /**
     * A builder for building firework star metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, FireworkStarMeta> {

        /**
         * Sets the effect of the star to the given [effect].
         *
         * @param effect the effect
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun effect(effect: FireworkEffect): Builder
    }
}

package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.data.FireworkEffect

/**
 * Item metadata for a firework rocket.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface FireworkRocketMeta : ScopedItemMeta<FireworkRocketMeta> {

    /**
     * The effects that will display from the stars that the rocket produces
     * when it explodes.
     */
    @get:JvmName("explosions")
    public val effects: List<FireworkEffect>

    /**
     * The flight duration of the firework rocket.
     */
    @get:JvmName("flightDuration")
    public val flightDuration: Int

    /**
     * Creates new item metadata with the given [effects].
     *
     * @param effects the new effects
     * @return new item metadata
     */
    public fun withEffects(effects: List<FireworkEffect>): FireworkRocketMeta

    /**
     * Creates new item metadata with the given [effect] added to the end of
     * the effects list.
     *
     * @param effect the effect to add
     * @return new item metadata
     */
    public fun addEffect(effect: Component): FireworkRocketMeta

    /**
     * Creates new item metadata with the effect at the given [index] removed
     * from the effects list.
     *
     * @param index the index of the effect to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    public fun removeEffect(index: Int): FireworkRocketMeta

    /**
     * Creates new item metadata with the given [effect] removed from the
     * effects list.
     *
     * @param effect the effect to remove
     * @return new item metadata
     */
    public fun removeEffect(effect: Component): FireworkRocketMeta

    /**
     * Creates new item metadata with the given flight [duration].
     *
     * @param duration the new flight duration
     * @return new item metadata
     */
    public fun withFlightDuration(duration: Int): FireworkRocketMeta

    /**
     * A builder for building firework rocket metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, FireworkRocketMeta> {

        /**
         * Sets the list of effects for the rocket to the given [effects].
         *
         * @param effects the effects
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun effects(effects: Iterable<FireworkEffect>): Builder

        /**
         * Sets the list of effects for the rocket to the given [effects].
         *
         * @param effects the effects
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun effects(vararg effects: FireworkEffect): Builder

        /**
         * Sets the list of effects for the rocket to the given [effects].
         *
         * @param effects the effects
         * @return this builder
         */
        @JvmSynthetic
        @JvmName("effectsArray")
        @Contract("_ -> this", mutates = "this")
        public fun effects(effects: Array<FireworkEffect>): Builder

        /**
         * Adds the given [effect] to the list of effects for the rocket.
         *
         * @param effect the effect to add
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun addEffect(effect: FireworkEffect): Builder

        /**
         * Sets the flight duration of the rocket to the given [duration].
         *
         * @param duration the flight duration
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun flightDuration(duration: Int): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building firework rocket metadata.
         *
         * @return a new builder
         */
        @JvmStatic
        public fun builder(): Builder = ItemMeta.FACTORY.builder(FireworkRocketMeta::class.java)
    }
}

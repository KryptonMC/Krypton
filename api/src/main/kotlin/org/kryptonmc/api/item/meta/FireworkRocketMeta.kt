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
import org.jetbrains.annotations.Unmodifiable
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.dsl.MetaDsl

/**
 * Item metadata for a firework rocket.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface FireworkRocketMeta : ScopedItemMeta<FireworkRocketMeta.Builder, FireworkRocketMeta> {

    /**
     * The effects that will display from the stars that the rocket produces
     * when it explodes.
     */
    @get:JvmName("explosions")
    public val effects: @Unmodifiable List<FireworkEffect>

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
    @Contract("_ -> new", pure = true)
    public fun withEffects(effects: List<FireworkEffect>): FireworkRocketMeta

    /**
     * Creates new item metadata with the given [effect] added to the end of
     * the effects list.
     *
     * @param effect the effect to add
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withEffect(effect: FireworkEffect): FireworkRocketMeta

    /**
     * Creates new item metadata with the effect at the given [index] removed
     * from the effects list.
     *
     * @param index the index of the effect to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    @Contract("_ -> new", pure = true)
    public fun withoutEffect(index: Int): FireworkRocketMeta

    /**
     * Creates new item metadata with the given [effect] removed from the
     * effects list.
     *
     * @param effect the effect to remove
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withoutEffect(effect: FireworkEffect): FireworkRocketMeta

    /**
     * Creates new item metadata with the given flight [duration].
     *
     * @param duration the new flight duration
     * @return new item metadata
     */
    @Contract("_ -> new", pure = true)
    public fun withFlightDuration(duration: Int): FireworkRocketMeta

    /**
     * A builder for building firework rocket metadata.
     */
    @MetaDsl
    public interface Builder : ItemMetaBuilder<Builder, FireworkRocketMeta> {

        /**
         * Sets the list of effects for the rocket to the given [effects].
         *
         * @param effects the effects
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun effects(effects: Collection<FireworkEffect>): Builder

        /**
         * Sets the list of effects for the rocket to the given [effects].
         *
         * @param effects the effects
         * @return this builder
         */
        @MetaDsl
        @Contract("_ -> this", mutates = "this")
        public fun effects(vararg effects: FireworkEffect): Builder = effects(effects.asList())

        /**
         * Sets the flight duration of the rocket to the given [duration].
         *
         * @param duration the flight duration
         * @return this builder
         */
        @MetaDsl
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
        @Contract("-> new", pure = true)
        public fun builder(): Builder = ItemMeta.builder(FireworkRocketMeta::class.java)
    }
}

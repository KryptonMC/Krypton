/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.data

import com.google.common.collect.ImmutableList
import net.kyori.adventure.builder.AbstractBuilder
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.Color
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * An effect that may be produced from a firework star exploding.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface FireworkEffect {

    /**
     * The type of this effect.
     */
    @get:JvmName("type")
    public val type: FireworkEffectType

    /**
     * Whether this effect flickers.
     */
    @get:JvmName("hasFlicker")
    public val hasFlicker: Boolean

    /**
     * Whether this effect has a trail.
     */
    @get:JvmName("hasTrail")
    public val hasTrail: Boolean

    /**
     * The primary colours of this effect.
     */
    @get:JvmName("colors")
    public val colors: List<Color>

    /**
     * The fading colours of this effect.
     */
    @get:JvmName("fadeColors")
    public val fadeColors: List<Color>

    /**
     * A builder for building firework effects.
     */
    @FireworkEffectDsl
    public interface Builder : AbstractBuilder<FireworkEffect> {

        /**
         * Makes the firework effect flicker.
         *
         * @return this builder
         */
        @FireworkEffectDsl
        @Contract("-> this", mutates = "this")
        public fun flickers(): Builder

        /**
         * Makes the effect have a trail.
         *
         * @return this builder
         */
        @FireworkEffectDsl
        @Contract("-> this", mutates = "this")
        public fun trail(): Builder

        /**
         * Sets the colours of the firework effect to the given [colors].
         *
         * @param colors the colours
         * @return this builder
         */
        @FireworkEffectDsl
        @Contract("_ -> this", mutates = "this")
        public fun colors(colors: Collection<Color>): Builder

        /**
         * Sets the colours of the firework effect to the given [colors].
         *
         * @param colors the colours
         * @return this builder
         */
        @FireworkEffectDsl
        @Contract("_ -> this", mutates = "this")
        public fun colors(vararg colors: Color): Builder = colors(ImmutableList.copyOf(colors))

        /**
         * Sets the fade colours of the firework effect to the given [colors].
         *
         * @param colors the fade colours
         * @return this builder
         */
        @FireworkEffectDsl
        @Contract("_ -> this", mutates = "this")
        public fun fadeColors(colors: Collection<Color>): Builder

        /**
         * Sets the fade colours of the firework effect to the given [colors].
         *
         * @param colors the fade colours
         * @return this builder
         */
        @FireworkEffectDsl
        @Contract("_ -> this", mutates = "this")
        public fun fadeColors(vararg colors: Color): Builder = fadeColors(ImmutableList.copyOf(colors))
    }

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun builder(type: FireworkEffectType): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building a firework effect with the given
         * [type].
         *
         * @param type the type of the effect
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: FireworkEffectType): Builder = Krypton.factory<Factory>().builder(type)
    }
}

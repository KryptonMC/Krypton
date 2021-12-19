package org.kryptonmc.api.item.data

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import java.awt.Color

/**
 * An effect that may be produced from a firework star exploding.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
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
    public interface Builder : Buildable.Builder<FireworkEffect> {

        /**
         * Sets the type of the firework effect to the given [type].
         *
         * @param type the type
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun type(type: FireworkEffectType): Builder

        /**
         * Sets whether the firework effect flickers to the given [value].
         *
         * @param value whether the effect flickers
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun flicker(value: Boolean): Builder

        /**
         * Makes the firework effect flicker.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun flickers(): Builder

        /**
         * Sets whether the firework effect has a trail to the given [value].
         *
         * @param value whether the effect has a trail
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun trail(value: Boolean): Builder

        /**
         * Makes the effect have a trail.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun trail(): Builder

        /**
         * Sets the colours of the firework effect to the given [colors].
         *
         * @param colors the colors
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun colors(colors: List<Color>): Builder

        /**
         * Adds the given [color] to the list of colours for this firework
         * effect.
         *
         * @param color the colour to add
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun addColor(color: Color): Builder

        /**
         * Sets the fade colours of the firework effect to the given [colors].
         *
         * @param colors the fade colours
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun fadeColors(colors: List<Color>): Builder

        /**
         * Adds the given [color] to the list of fade colours for this
         * firework effect.
         *
         * @param color the fade colour to add
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun addFadeColor(color: Color): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(type: FireworkEffectType): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building a firework effect with the given
         * [type].
         *
         * @param type the type of the effect
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: FireworkEffectType): Builder = FACTORY.builder(type)
    }
}

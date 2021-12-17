package org.kryptonmc.api.world.weather

import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import java.time.Duration

/**
 * Data for the current weather for something.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Weather : Buildable<Weather, Weather.Builder> {

    /**
     * The type of weather that is currently occurring.
     */
    @get:JvmName("type")
    public val type: WeatherType

    /**
     * How long the weather will last from the time when it was applied.
     *
     * If null, this weather will never expire.
     */
    @get:JvmName("duration")
    public val duration: Duration?

    /**
     * The strength of this weather.
     */
    @get:JvmName("strength")
    public val strength: Float

    /**
     * A builder for building weather.
     */
    public interface Builder : Buildable.Builder<Weather> {

        /**
         * Sets the type of the weather to the given [type].
         *
         * @param type the weather type
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun type(type: WeatherType): Builder

        /**
         * Sets the duration of the weather to the given [duration].
         *
         * @param duration the duration
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun duration(duration: Duration?): Builder

        /**
         * Makes the weather last forever.
         *
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun eternal(): Builder = duration(null)

        /**
         * Sets the strength of the weather to the given [strength].
         *
         * Note: this will only be effective if the type is [WeatherType.RAIN]
         * or [WeatherType.THUNDER]. Clear weather does not have a strength
         * value.
         *
         * @param strength the strength
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun strength(strength: Float): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun of(type: WeatherType, duration: Duration?, strength: Float): Weather

        public fun builder(type: WeatherType): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates new weather with the given values.
         *
         * @param type the type of the weather
         * @param duration how long the weather will last after it is applied
         * @param strength the strength of the weather
         * @return new weather
         */
        @JvmStatic
        @JvmOverloads
        public fun of(type: WeatherType, duration: Duration?, strength: Float = 0F): Weather = FACTORY.of(type, duration, strength)

        /**
         * Creates a new weather builder for building weather.
         *
         * @param type the type of weather
         * @return a new weather builder
         */
        public fun builder(type: WeatherType): Builder = FACTORY.builder(type)

        /**
         * Creates new clear weather, optionally with the given [duration].
         *
         * @param duration how long the weather will last, null for all
         * eternity
         * @return new clear weather
         */
        @JvmStatic
        @JvmOverloads
        public fun clear(duration: Duration? = null): Weather = of(WeatherType.CLEAR, duration, 0F)

        /**
         * Creates new rain with the given values.
         *
         * @param duration how long the rain will last, null for all eternity
         * @param strength the strength of the rain
         * @return new rain
         */
        @JvmStatic
        public fun rain(duration: Duration?, strength: Float): Weather = of(WeatherType.RAIN, duration, strength)

        /**
         * Creates new infinitely lasting rain with the given [strength].
         *
         * @param strength the strength of the rain
         * @return new infinitely lasting rain
         */
        @JvmStatic
        public fun rain(strength: Float): Weather = rain(null, strength)

        /**
         * Creates new thunder with the given values.
         *
         * @param duration how long the thunder will last, null for all
         * eternity
         * @param strength the strength of the thunder
         * @return new thunder
         */
        @JvmStatic
        public fun thunder(duration: Duration?, strength: Float): Weather = of(WeatherType.THUNDER, duration, strength)

        /**
         * Creates new infinitely lasting thunder with the given [strength].
         *
         * @param strength the strength of the thunder
         * @return new thunder
         */
        @JvmStatic
        public fun thunder(strength: Float): Weather = thunder(null, strength)
    }
}

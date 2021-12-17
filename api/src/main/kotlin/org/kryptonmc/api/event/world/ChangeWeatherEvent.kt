package org.kryptonmc.api.event.world

import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.world.weather.Weather
import org.kryptonmc.api.world.weather.WeatherContainer

/**
 * Called when the weather changes for the given [container].
 *
 * The [container] will usually only either be a world or a player. If you wish
 * to perform different actions depending on which one of those it is, you can
 * cast it.
 *
 * @param container the container that the weather changed for
 * @param oldWeather the old weather
 * @param newWeather the new weather
 * @param cause the cause of the weather change
 */
public class ChangeWeatherEvent(
    public val container: WeatherContainer,
    public val oldWeather: Weather,
    public val newWeather: Weather,
    public val cause: Cause
) : ResultedEvent<WeatherChangeResult> {

    override var result: WeatherChangeResult = WeatherChangeResult.allowed()

    /**
     * The cause of a weather change.
     */
    public enum class Cause {

        /**
         * The server has loaded weather data and determined the current
         * weather.
         */
        LOAD,

        /**
         * A request was made to change the weather.
         */
        API,

        /**
         * The weather controller changed the weather from a queued request.
         */
        CONTROLLER,

        /**
         * The weather was naturally changed by randomness.
         */
        NATURAL
    }
}

/**
 * The result of a [ChangeWeatherEvent].
 *
 * @param weather the replacement weather
 */
@JvmRecord
public data class WeatherChangeResult(
    override val isAllowed: Boolean,
    public val weather: Weather?
) : ResultedEvent.Result {

    public companion object {

        private val ALLOWED = WeatherChangeResult(true, null)
        private val DENIED = WeatherChangeResult(false, null)

        /**
         * Gets the result that allows the event to continue as normal.
         *
         * @return the allowed result
         */
        @JvmStatic
        public fun allowed(): WeatherChangeResult = ALLOWED

        /**
         * Creates a new result that allows the event to continue, but silently
         * replaces the new weather with the given [weather].
         *
         * @param weather the replacement weather
         * @return a new allowed result
         */
        @JvmStatic
        public fun allowed(weather: Weather): WeatherChangeResult = WeatherChangeResult(true, weather)

        /**
         * Gets the result that denies the event from continuing, meaning the
         * weather change will not occur.
         *
         * @return the denied result
         */
        @JvmStatic
        public fun denied(): WeatherChangeResult = DENIED
    }
}

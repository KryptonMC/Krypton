package org.kryptonmc.api.world.weather

import java.time.Instant

/**
 * A controller for weather for something that can have weather.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface WeatherController {

    /**
     * The current weather that is occurring.
     */
    @get:JvmName("currentWeather")
    public val currentWeather: Weather

    /**
     * The time when the current weather was first started.
     */
    @get:JvmName("startTime")
    public val startTime: Instant

    /**
     * The time remaining, in milliseconds, until the current weather will no
     * longer be in effect.
     */
    @get:JvmName("remainingTime")
    public val remainingTime: Long

    /**
     * Starts the given [weather], overriding the current weather.
     *
     * This will take effect immediately on invocation, and the weather will
     * be changed. If you want to have the weather only take effect after the
     * current weather is finished, use [queue].
     *
     * @param weather the weather to start
     */
    public fun start(weather: Weather)

    /**
     * Queues the given [weather] up, ready to take effect.
     *
     * All implementations of this should use a FIFO queue, to ensure that all
     * weather is queued fairly, and applied in order of the queueing. However,
     * this is not a strict requirement, and FIFO behaviour should not be
     * depended on.
     *
     * @param weather the weather to queue
     */
    public fun queue(weather: Weather)

    /**
     * Immediately clears the current weather.
     */
    public fun clear()
}

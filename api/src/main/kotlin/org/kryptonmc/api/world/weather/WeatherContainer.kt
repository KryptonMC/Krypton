package org.kryptonmc.api.world.weather

/**
 * Something that can contain weather.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface WeatherContainer {

    /**
     * The weather controller for this container.
     */
    @get:JvmName("controller")
    public val weatherController: WeatherController
}

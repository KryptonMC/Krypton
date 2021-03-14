package org.kryptonmc.krypton.api.event

/**
 * The event bus is used to register and dispatch events
 *
 * This is heavily based off of BungeeCord's EventBus
 *
 * @author Callum Seabrook
 */
interface EventBus {

    /**
     * Call an event
     *
     * @param event the event to call
     */
    fun call(event: Any)

    /**
     * Register a new listener
     *
     * @param listener the listener to register
     */
    fun register(listener: Any)
}
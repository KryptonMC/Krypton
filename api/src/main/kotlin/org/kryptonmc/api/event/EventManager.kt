/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/event/EventManager.java
 */
@file:JvmSynthetic
package org.kryptonmc.api.event

import java.util.concurrent.CompletableFuture

/**
 * The event manager. Used to register listeners/handlers and dispatch
 * events.
 */
public interface EventManager {

    /**
     * Fires the specified [event] to the event bus asynchronously, and returns
     * its result as a [CompletableFuture].
     *
     * The asynchronous execution allows Krypton to continue functioning whilst
     * listeners/handlers for this event are still executing, which avoids the
     * possibility of the event handler holding up the server unnecessarily.
     *
     * @param event the event to fire
     * @return the result of firing this event, as a [CompletableFuture]
     */
    public fun <E> fire(event: E): CompletableFuture<E>

    /**
     * Fires the specified [event] to the event bus asynchronously and discards
     * the result.
     *
     * @param event the event to fire
     */
    public fun fireAndForget(event: Any)

    /**
     * Requests that the specified [listener] be registered with this manager
     * to listen for events, and the listener will be associated with the
     * specified [plugin].
     *
     * @param plugin the plugin to associate with the listener
     * @param listener the listener to register
     */
    public fun register(plugin: Any, listener: Any)

    /**
     * Requests that the specified [handler] be registered with this manager to
     * listen for and handle events of type [E] at
     * [medium priority][ListenerPriority.MEDIUM], and the handler will be
     * associated with the specified [plugin].
     *
     * @param plugin the plugin to associate the handler with
     * @param eventClass the class of the event
     * @param handler the handler to register
     * @param E the type of the event
     */
    public fun <E> register(plugin: Any, eventClass: Class<E>, handler: EventHandler<E>)

    /**
     * Requests that the specified [handler] be registered with this manager to
     * listen for and handle events of type [E], and the handler will be
     * associated with the specified [plugin].
     *
     * @param plugin the plugin to associate the handler with
     * @param eventClass the class of the event
     * @param priority the priority of this handler
     * @param handler the handler to register
     * @param E the type of the event
     */
    public fun <E> register(plugin: Any, eventClass: Class<E>, priority: ListenerPriority, handler: EventHandler<E>)

    /**
     * Unregisters all registered listeners for the specified [plugin]
     * from the event bus.
     *
     * @param plugin the plugin to unregister all listeners for
     */
    public fun unregisterListeners(plugin: Any)

    /**
     * Unregisters the specified [listener] for the specified [plugin]
     * from the event bus.
     *
     * @param plugin the plugin the listener is associated with
     * @param listener the listener to unregister
     */
    public fun unregisterListener(plugin: Any, listener: Any)

    /**
     * Unregisters the specified [handler] for the specified [plugin]
     * from the event bus.
     *
     * @param plugin the plugin this handler is associated with
     * @param handler the handler to unregister
     */
    public fun <E> unregister(plugin: Any, handler: EventHandler<E>)
}

/**
 * Requests that the specified [handler] be registered with this manager to
 * listen for and handle events of type [E] at
 * [medium priority][ListenerPriority.MEDIUM], and the handler will be
 * associated with the specified [plugin].
 *
 * @param plugin the plugin to associate the handler with
 * @param handler the handler to register
 * @param E the type of the event
 */
@JvmSynthetic
public inline fun <reified E> EventManager.registerHandler(plugin: Any, handler: EventHandler<E>) {
    register(plugin, E::class.java, handler)
}

/**
 * Requests that the specified [handler] be registered with this manager to
 * listen for and handle events of type [E], and the handler will be associated
 * with the specified [plugin].
 *
 * @param plugin the plugin to associate the handler with
 * @param priority the priority of this handler
 * @param handler the handler to register
 * @param E the type of the event
 */
@JvmSynthetic
public inline fun <reified E> EventManager.register(plugin: Any, priority: ListenerPriority, handler: EventHandler<E>) {
    register(plugin, E::class.java, priority, handler)
}

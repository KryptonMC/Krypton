/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.function.BiPredicate
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * A single node in an event tree.
 *
 * A node may contain any number of children and/or listeners. When an event
 * is called on a node, the node will first check if it has any listeners for
 * the event, calling those first, then the event will propagate down the tree
 * to all children of the node, in order of their priority.
 *
 * If an event in the tree is cancelled, it will not propagate to any other
 * nodes, and its dispatching will be considered complete. No further event
 * listeners will receive the event.
 *
 * Every event node has a name, which should be uniquely identifiable, and
 * will be useful for debugging, as well as identifying the node in the tree.
 */
public interface EventNode<T : Event> {

    /**
     * The type of events this node can have listeners for.
     */
    public val eventType: Class<T>

    /**
     * The name of this node.
     *
     * This is used to identify the node in the tree, and so should ideally be
     * unique, and not conflict with other nodes.
     */
    public val name: String

    /**
     * The parent of this node.
     *
     * If this node is at the root of the tree, this will be null.
     */
    public val parent: EventNode<in T>?

    /**
     * The children of this node.
     *
     * If this node is a leaf on the tree, this will be empty.
     */
    public val children: Set<EventNode<T>>

    /**
     * The priority of this node.
     *
     * Due to the design of the event system, this should rarely need to be
     * used. It is useful when you wish to override the default execution
     * order of event nodes, which is based on the order in which they are
     * registered.
     */
    public var priority: Int

    /**
     * Checks if this event node has a listener for the given event [type].
     *
     * Due to the nature of event nodes, this method will check both this node
     * and all of its children for listeners.
     *
     * @param type the event type
     * @return true if this node has a listener for the given event type
     */
    public fun hasListener(type: Class<out T>): Boolean

    /**
     * Fires the given [event] on this event node.
     *
     * Due to the nature of event nodes, this method will propagate the event
     * to any nodes that are registered children of this event node.
     *
     * @param E the event type
     * @param event the event to fire
     */
    public fun <E : T> fire(event: E): E

    /**
     * Adds the given [node] as a child of this event node.
     *
     * @param node the node to add
     * @return this event node
     */
    public fun addChild(node: EventNode<out T>)

    /**
     * Removes the given [node] as a child of this event node.
     *
     * @param node the node to remove
     * @return this event node
     */
    public fun removeChild(node: EventNode<out T>)

    /**
     * Registers the given event [listener] to this event node.
     *
     * @param listener the listener to add
     */
    public fun registerListener(listener: EventListener<out T>)

    /**
     * Registers an event listener for the given [eventType] to this event
     * node, calling the given [handler] when the event is fired.
     *
     * @param E the type of event to listen for
     * @param eventType the type of event to listen for
     * @param handler the handler for the event
     * @return this event node
     */
    public fun <E : T> registerListener(eventType: Class<E>, handler: Consumer<E>) {
        registerListener(EventListener.of(eventType, handler))
    }

    /**
     * Registers all event listeners in the given [listenerClass] to this
     * event node.
     *
     * This is used to register event listeners that are defined in a class
     * using the [Listener] annotation.
     *
     * @param listenerClass the listener class
     * @return this event node
     */
    public fun registerListeners(listenerClass: Any)

    /**
     * Unregisters the given event [listener] from this event node, meaning it
     * will not be called when an event that it listens for is fired.
     *
     * @param listener the listener to unregister
     * @return this event node
     */
    public fun unregisterListener(listener: EventListener<out T>)

    /**
     * Unregisters all listeners in the given [listenerClass] from this event
     * node.
     *
     * This is used to unregister event listeners that are defined in a class
     * using the [Listener] annotation.
     *
     * @param listenerClass the listener class
     * @return this event node
     */
    public fun unregisterListeners(listenerClass: Any)

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun <T : Event, V> create(name: String, filter: EventFilter<T, V>, predicate: BiPredicate<T, V?>?): EventNode<T>
    }

    public companion object {

        /**
         * Creates a new event node that accepts any event.
         *
         * @param name the name of the node
         * @return a new event node
         */
        @JvmStatic
        public fun all(name: String): EventNode<Event> = forType(name, EventFilter.ALL)

        /**
         * Creates a new event node that accepts any event of the given event
         * type [T] that passes the given [filter].
         *
         * For example, you could create an event node that only accepts
         * entity events:
         *
         * Java:
         * ```java
         * final EventNode<PlayerEvent> playerNode = EventNode.forType("abc", EventFilter.PLAYER);
         * ```
         * Kotlin:
         * ```kotlin
         * val playerNode = EventNode.forType("abc", EventFilter.PLAYER)
         * ```
         *
         * @param T the event type
         * @param V the value type
         * @param name the name of the node
         * @param filter the event filter
         * @return a new event node
         */
        @JvmStatic
        public fun <T : Event, V> forType(name: String, filter: EventFilter<T, V>): EventNode<T> {
            return Krypton.factory<Factory>().create(name, filter, null)
        }

        /**
         * Creates a new event node that accepts any event of the given event
         * type [T] that passes the given [filter] and the given [predicate].
         *
         * The filter defines the required event type for the node, as well as
         * how to retrieve the handle object to filter on from the event.
         * The predicate is used to check if the event is valid for the node.
         *
         * For example, you could create an event node that only accepts
         * player events where the player is in creative mode:
         *
         * Java:
         * ```java
         * final EventNode<PlayerEvent> playerInCreative = EventNode.filteredForType("abc", EventFilter.PLAYER,
         *         (event, player) -> player.getGameMode() == GameMode.CREATIVE);
         * ```
         * Kotlin:
         * ```kotlin
         * val playerInCreative = EventNode.filteredForType("abc", EventFilter.PLAYER) { event, player -> player.gameMode == GameMode.CREATIVE }
         * ```
         * @param T the event type
         * @param V the value type
         * @param name the name of the node
         * @param filter the event filter
         * @param predicate the predicate to test
         * @return a new event node
         */
        @JvmStatic
        public fun <T : Event, V> filteredForType(name: String, filter: EventFilter<T, V>, predicate: BiPredicate<T, V?>): EventNode<T> {
            return Krypton.factory<Factory>().create(name, filter, predicate)
        }

        /**
         * Creates a new event node that accepts any event of the given event
         * type [T] that passes the given [filter] and the given [predicate].
         *
         * This functions identically to [filteredForType], except that the
         * predicate does not provide the handle object.
         *
         * @param T the event type
         * @param V the value type
         * @param name the name of the node
         * @param filter the event filter
         * @param predicate the predicate to test
         * @return a new event node
         */
        @JvmStatic
        public fun <T : Event, V> filteredForEvent(name: String, filter: EventFilter<T, V>, predicate: Predicate<T>): EventNode<T> {
            return filteredForType(name, filter) { event, _ -> predicate.test(event) }
        }

        /**
         * Creates a new event node that accepts any event of the given event
         * type [T] that passes the given [filter] and the given [predicate].
         *
         * This functions identically to [filteredForType], except that the
         * predicate only provides the handle object.
         *
         * @param T the event type
         * @param V the value type
         * @param name the name of the node
         * @param filter the event filter
         * @param predicate the predicate to test
         * @return a new event node
         */
        @JvmStatic
        public fun <T : Event, V> filteredForValue(name: String, filter: EventFilter<T, V>, predicate: Predicate<V?>): EventNode<T> {
            return filteredForType(name, filter) { _, handle -> predicate.test(handle) }
        }
    }
}

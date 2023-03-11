/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.event

import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventFilter
import org.kryptonmc.api.event.EventListener
import org.kryptonmc.api.event.EventNode
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import java.util.function.BiPredicate
import java.util.function.Consumer

open class KryptonEventNode<T : Event>(
    override val name: String,
    private val filter: EventFilter<T, *>,
    private val predicate: BiPredicate<T, Any?>?
) : EventNode<T> {

    private val handleMap = ConcurrentHashMap<Class<*>, Handle<T>>()
    private val listenerMap = ConcurrentHashMap<Class<out T>, ListenerEntry<T>>()
    private val listenerMethods = ConcurrentHashMap<Any, MutableList<EventListener<out T>>>()
    private val childrenSet = CopyOnWriteArraySet<KryptonEventNode<T>>()

    @Volatile
    override var priority: Int = 0
    @Volatile
    private var parentNode: KryptonEventNode<in T>? = null

    override val eventType: Class<T> = filter.eventType
    override val children: Set<EventNode<T>>
        get() = Collections.unmodifiableSet(childrenSet)
    override val parent: EventNode<in T>?
        get() = parentNode

    override fun hasListener(type: Class<out T>): Boolean = getHandle(type).hasListener()

    override fun <E : T> fire(event: E): E {
        val handle = getHandle(event.javaClass)
        try {
            handle.call(event)
        } catch (exception: Throwable) {
            LOGGER.error("An error occurred while dispatching event $event!", exception)
        }
        return event
    }

    @Suppress("UNCHECKED_CAST")
    private fun <E : T> getHandle(eventType: Class<E>): Handle<E> {
        return handleMap.computeIfAbsent(eventType) { Handle(this, it as Class<T>) } as Handle<E>
    }

    @Suppress("UNCHECKED_CAST")
    override fun addChild(node: EventNode<out T>) {
        synchronized(GLOBAL_CHILD_LOCK) {
            val child = node as KryptonEventNode<out T>
            require(child.parent == null) { "The provided node $node is already registered to another node!" }
            require(parent != node) { "The provided node $node is already the parent of this node!" }
            if (!childrenSet.add(child as KryptonEventNode<T>)) return
            child.parentNode = this
            child.invalidateEventsFor(this)
        }
    }

    override fun removeChild(node: EventNode<out T>) {
        synchronized(GLOBAL_CHILD_LOCK) {
            val child = node as KryptonEventNode<out T>
            if (!childrenSet.remove(child)) return // Child not found
            child.parentNode = null
            child.invalidateEventsFor(this)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun registerListener(listener: EventListener<out T>) {
        val eventType = listener.eventType
        synchronized(GLOBAL_CHILD_LOCK) {
            val entry = getEntry(eventType)
            entry.listeners.add(listener as EventListener<T>)
            invalidateEvent(eventType)
        }
    }

    override fun registerListeners(listenerClass: Any) {
        val methods = EventMethodScanner.collectMethods(listenerClass.javaClass, eventType)
        for (methodInfo in methods.values) {
            val handler = Consumer<T> { methodInfo.method.invoke(listenerClass, it) }
            val listener = EventListener.of(methodInfo.eventType, handler)
            listenerMethods.computeIfAbsent(listenerClass) { CopyOnWriteArrayList() }.add(listener)
            registerListener(listener)
        }
    }

    override fun unregisterListener(listener: EventListener<out T>) {
        val eventType = listener.eventType
        synchronized(GLOBAL_CHILD_LOCK) {
            val entry = listenerMap.get(eventType) ?: return // There is no listener with such type
            if (entry.listeners.remove(listener)) invalidateEvent(eventType)
        }
    }

    override fun unregisterListeners(listenerClass: Any) {
        val listeners = listenerMethods.remove(listenerClass) ?: return
        for (listener in listeners) {
            unregisterListener(listener)
        }
    }

    private fun invalidateEventsFor(node: KryptonEventNode<in T>) {
        assert(Thread.holdsLock(GLOBAL_CHILD_LOCK)) { "Expected current thread to hold global child lock when invalidating events!" }
        listenerMap.keys.forEach { node.invalidateEvent(it) }
        childrenSet.forEach { it.invalidateEventsFor(node) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun invalidateEvent(eventClass: Class<out T>) {
        forTargetEvents(eventClass) { type ->
            val handle = handleMap.computeIfAbsent(type) { Handle(this, it as Class<T>) }
            handle.invalidate()
        }
        parentNode?.invalidateEvent(eventClass)
    }

    private fun getEntry(type: Class<out T>): ListenerEntry<T> = listenerMap.computeIfAbsent(type) { ListenerEntry() }

    private class ListenerEntry<T : Event> {

        val listeners: MutableList<EventListener<T>> = CopyOnWriteArrayList()
        val bindingConsumers: MutableSet<Consumer<T>> = CopyOnWriteArraySet()
    }

    private class Handle<E : Event>(private val node: KryptonEventNode<E>, private val eventType: Class<E>) {

        private var listener: Consumer<E>? = null
        @Volatile
        private var updated = false

        fun call(event: E) {
            val listener = updatedListener() ?: return
            listener.accept(event)
        }

        fun hasListener(): Boolean = updatedListener() != null

        fun invalidate() {
            updated = false
        }

        private fun updatedListener(): Consumer<E>? {
            if (updated) return listener
            synchronized(GLOBAL_CHILD_LOCK) {
                if (updated) return listener
                val listener = createConsumer()
                this.listener = listener
                updated = true
                return listener
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun createConsumer(): Consumer<E>? {
            val listeners = getListeners()

            val childrenListeners = node.childrenSet.stream()
                .filter { child -> child.eventType.isAssignableFrom(eventType) } // Invalid event type
                .sorted(Comparator.comparingInt(EventNode<*>::priority))
                .map { child ->
                    @Suppress("UNCHECKED_CAST")
                    val handle = child.getHandle(eventType)
                    handle.updatedListener()
                }
                .filter { it != null }
                .toArray { arrayOfNulls<Consumer<E>>(it) } as Array<Consumer<E>>

            val predicate = node.predicate
            val filter = node.filter
            val hasPredicate = predicate != null
            val hasListeners = listeners.isNotEmpty()
            val hasChildren = childrenListeners.isNotEmpty()
            if (!hasListeners && !hasChildren) {
                // No listener
                return null
            }

            return Consumer { event ->
                // Filtering
                if (hasPredicate) {
                    val value = filter.getHandler(event)
                    if (!predicate!!.test(event, value)) return@Consumer
                }
                // Normal listeners
                if (hasListeners) listeners.forEach { listener -> listener.accept(event) }
                // Children
                if (hasChildren) childrenListeners.forEach { listener -> listener.accept(event) }
            }
        }

        private fun getListeners(): Array<Consumer<E>> {
            val listeners = ArrayList<Consumer<E>>()
            forTargetEvents(eventType) { type ->
                val entry = node.listenerMap.get(type) ?: return@forTargetEvents
                val result = listenersConsumer(entry)
                if (result != null) listeners.add(result)
            }
            return listeners.toTypedArray()
        }

        private fun listenersConsumer(entry: ListenerEntry<E>): Consumer<E>? {
            val listenersCopy = entry.listeners.toTypedArray()
            val bindingsCopy = entry.bindingConsumers.toTypedArray()
            val listenersEmpty = listenersCopy.isEmpty()
            val bindingsEmpty = bindingsCopy.isEmpty()

            if (listenersEmpty && bindingsEmpty) {
                // No listeners
                return null
            }
            if (bindingsEmpty && listenersCopy.size == 1) {
                // Only one normal listener
                val listener = listenersCopy[0]
                return Consumer { callListener(listener, it) }
            }

            // Worse case scenario, try to run everything
            return Consumer { event ->
                if (!listenersEmpty) listenersCopy.forEach { listener -> callListener(listener, event) }
                if (!bindingsEmpty) bindingsCopy.forEach { binding -> binding.accept(event) }
            }
        }

        private fun callListener(listener: EventListener<E>, event: E) {
            val result = listener.run(event)
            if (result == EventListener.Result.EXPIRED) {
                node.unregisterListener(listener)
                invalidate()
            }
        }
    }

    object Factory : EventNode.Factory {

        override fun <T : Event, V> create(name: String, filter: EventFilter<T, V>, predicate: BiPredicate<T, V?>?): EventNode<T> {
            @Suppress("UNCHECKED_CAST")
            return KryptonEventNode(name, filter, predicate as? BiPredicate<T, Any?>)
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val GLOBAL_CHILD_LOCK = Any()

        @JvmStatic
        private fun forTargetEvents(type: Class<*>, consumer: Consumer<Class<*>>) {
            consumer.accept(type)
            val superTypes = EventSuperTypesTracker.getSuperTypesOf(type)
            for (superType in superTypes) {
                consumer.accept(superType)
            }
        }
    }
}

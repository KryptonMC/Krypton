/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/PaperMC/Velocity/blob/0097359a99c23de4fc6b92c59a401a10208b4c4a/proxy/src/main/java/com/velocitypowered/proxy/plugin/VelocityEventManager.java
 */
package org.kryptonmc.krypton.event

import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import net.kyori.event.EventSubscriber
import net.kyori.event.SimpleEventBus
import net.kyori.event.method.MethodScanner
import net.kyori.event.method.MethodSubscriptionAdapter
import net.kyori.event.method.SimpleMethodSubscriptionAdapter
import net.kyori.event.method.asm.ASMEventExecutorFactory
import org.kryptonmc.api.event.EventHandler
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.ListenerPriority
import org.kryptonmc.krypton.plugin.KryptonPluginManager
import org.kryptonmc.krypton.plugin.PluginClassLoader
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.pool.daemonThreadFactory
import org.kryptonmc.krypton.util.pool.ThreadPoolBuilder
import java.lang.reflect.Method
import java.util.IdentityHashMap
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

// TODO: Probably overhaul most of the event API as it's based on an older version of Velocity's event API
//  that isn't even used by them anymore. It also has cases where it's unideal for us.
object KryptonEventManager : EventManager {

    private val LOGGER = logger<KryptonEventManager>()
    private val registeredListenersByPlugin: Multimap<Any, Any> = Multimaps.synchronizedListMultimap(
        Multimaps.newListMultimap(IdentityHashMap<Any, Collection<Any>>(), ::ArrayList)
    )
    private val registeredHandlersByPlugin: Multimap<Any, EventHandler<*>> = Multimaps.synchronizedListMultimap(
        Multimaps.newListMultimap(IdentityHashMap<Any, Collection<EventHandler<*>>>(), ::ArrayList)
    )
    private val bus: SimpleEventBus<Any>
    private val methodAdapter: MethodSubscriptionAdapter<Any>
    private val service = ThreadPoolBuilder.fixed(Runtime.getRuntime().availableProcessors())
        .factory(daemonThreadFactory("Krypton Event Executor %d"))
        .build()

    init {
        val loader = PluginClassLoader().addToLoaders()

        bus = object : SimpleEventBus<Any>(Any::class.java) {
            // No cancellable or generic events (from event) in Krypton, so we don't need to perform those checks
            override fun shouldPost(event: Any, subscriber: EventSubscriber<*>): Boolean = true
        }
        methodAdapter = SimpleMethodSubscriptionAdapter(bus, ASMEventExecutorFactory(loader), KryptonMethodScanner())
    }

    fun <E> fireSync(event: E): E {
        requireNotNull(event)
        if (bus.hasSubscribers(event.javaClass)) fireEvent(event)
        return event
    }

    fun fireAndForgetSync(event: Any) {
        if (bus.hasSubscribers(event.javaClass)) fireEvent(event)
    }

    fun shutdown() {
        service.shutdown()
        service.awaitTermination(10, TimeUnit.SECONDS)
    }

    fun registerUnchecked(plugin: Any, listener: Any) {
        registeredListenersByPlugin.put(plugin, listener)
        methodAdapter.register(listener)
    }

    override fun register(plugin: Any, listener: Any) {
        checkPlugin(plugin)
        require(plugin != listener || registeredListenersByPlugin.containsEntry(plugin, listener)) {
            "The plugin main instance is automatically registered!"
        }
        registerUnchecked(plugin, listener)
    }

    override fun <E> register(plugin: Any, eventClass: Class<E>, handler: EventHandler<E>) {
        register(plugin, eventClass, ListenerPriority.MEDIUM, handler)
    }

    override fun <E> register(plugin: Any, eventClass: Class<E>, priority: ListenerPriority, handler: EventHandler<E>) {
        checkPlugin(plugin)
        registeredHandlersByPlugin.put(plugin, handler)
        bus.register(eventClass, KyoriToKryptonHandler(handler, priority))
    }

    override fun <E> fire(event: E): CompletableFuture<E> {
        requireNotNull(event) { "Attempted to fire a null event!" } // Required to access event's class
        if (!bus.hasSubscribers(event.javaClass)) return CompletableFuture.completedFuture(event)
        return CompletableFuture.supplyAsync({
            fireEvent(event)
            return@supplyAsync event
        }, service)
    }

    override fun fireAndForget(event: Any) {
        if (!bus.hasSubscribers(event.javaClass)) return
        service.execute { fireEvent(event) }
    }

    override fun unregisterListeners(plugin: Any) {
        checkPlugin(plugin)
        registeredListenersByPlugin.removeAll(plugin).forEach(methodAdapter::unregister)
        registeredHandlersByPlugin.removeAll(plugin).forEach(::unregisterHandler)
    }

    override fun unregisterListener(plugin: Any, listener: Any) {
        checkPlugin(plugin)
        if (!registeredListenersByPlugin.remove(plugin, listener)) return
        methodAdapter.unregister(listener)
    }

    override fun <E> unregister(plugin: Any, handler: EventHandler<E>) {
        checkPlugin(plugin)
        if (!registeredHandlersByPlugin.remove(plugin, handler)) return
        unregisterHandler(handler)
    }

    private fun fireEvent(event: Any) {
        val result = bus.post(event)
        if (result.exceptions().isEmpty()) return
        LOGGER.error("Some errors occurred whilst posting event $event!")
        result.exceptions().values.forEachIndexed { index, exception -> LOGGER.error("#${index + 1}: \n", exception) }
    }

    private fun unregisterHandler(handler: EventHandler<*>) {
        bus.unregister { subscriber: EventSubscriber<*> -> subscriber is KyoriToKryptonHandler && subscriber.handler == handler }
    }

    private fun checkPlugin(plugin: Any) {
        require(KryptonPluginManager.fromInstance(plugin) != null) { "The specified plugin is not loaded!" }
    }

    private class KryptonMethodScanner : MethodScanner<Any> {

        override fun shouldRegister(listener: Any, method: Method): Boolean = method.isAnnotationPresent(Listener::class.java)

        override fun postOrder(listener: Any, method: Method): Int = method.getAnnotation(Listener::class.java).priority.ordinal

        override fun consumeCancelledEvents(listener: Any, method: Method): Boolean = true
    }

    private class KyoriToKryptonHandler<E>(val handler: EventHandler<E>, val priority: ListenerPriority) : EventSubscriber<E> {

        override fun invoke(event: E & Any) {
            handler.execute(event)
        }

        override fun postOrder(): Int = priority.ordinal
    }
}

/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
 */
package org.kryptonmc.krypton.plugin

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
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.krypton.util.daemon
import org.kryptonmc.krypton.util.doPrivileged
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.threadFactory
import java.lang.reflect.Method
import java.util.IdentityHashMap
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class KryptonEventManager(private val pluginManager: PluginManager) : EventManager {

    private val registeredListenersByPlugin = IdentityHashMap<Any, List<Any>>().toSynchronizedListMultimap(::ArrayList)
    private val registeredHandlersByPlugin = IdentityHashMap<Any, List<EventHandler<*>>>().toSynchronizedListMultimap(::ArrayList)
    private val bus: SimpleEventBus<Any>
    private val methodAdapter: MethodSubscriptionAdapter<Any>
    private val service: ExecutorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors(),
        threadFactory("Krypton Event Executor #%d") { daemon() }
    )

    init {
        val loader = doPrivileged { PluginClassLoader() }.apply { addToLoaders() }

        bus = object : SimpleEventBus<Any>(Any::class.java) {
            // No cancellable or generic events (from event) in Krypton, so we don't need to perform those checks
            override fun shouldPost(event: Any, subscriber: EventSubscriber<*>) = true
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

    override fun register(plugin: Any, listener: Any) {
        checkPlugin(plugin)
        require(plugin != listener || registeredListenersByPlugin.containsEntry(plugin, listener)) { "The plugin main instance is automatically registered!" }
        registeredListenersByPlugin.put(plugin, listener)
        methodAdapter.register(listener)
    }

    override fun <E> register(plugin: Any, eventClass: Class<E>, priority: ListenerPriority, handler: EventHandler<E>) {
        checkPlugin(plugin)
        registeredHandlersByPlugin.put(plugin, handler)
        bus.register(eventClass, KyoriToKryptonHandler(handler, priority))
    }

    override fun <E> fire(event: E): CompletableFuture<E> {
        requireNotNull(event) // Required to access event's class
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

    private fun unregisterHandler(handler: EventHandler<*>) = bus.unregister { it: EventSubscriber<*> ->
        it is KyoriToKryptonHandler && it.handler == handler
    }

    private fun checkPlugin(plugin: Any) = require(pluginManager.fromInstance(plugin) != null) { "The specified plugin is not loaded!" }

    companion object {

        private val LOGGER = logger<KryptonEventManager>()
    }
}

private class KryptonMethodScanner : MethodScanner<Any> {

    override fun shouldRegister(listener: Any, method: Method) = method.isAnnotationPresent(Listener::class.java)
    override fun postOrder(listener: Any, method: Method) = method.getAnnotation(Listener::class.java).priority.ordinal
    override fun consumeCancelledEvents(listener: Any, method: Method) = true
}

private class KyoriToKryptonHandler<E>(
    val handler: EventHandler<E>,
    val priority: ListenerPriority
) : EventSubscriber<E> {

    override fun invoke(event: E) = handler.execute(event)
    override fun postOrder() = priority.ordinal
}

private fun <K, V> Map<K, List<V>>.toSynchronizedListMultimap(factory: () -> List<V>) =
    Multimaps.synchronizedMultimap(Multimaps.newListMultimap(this, factory))

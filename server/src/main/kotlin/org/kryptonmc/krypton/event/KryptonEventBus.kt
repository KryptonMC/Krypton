package org.kryptonmc.krypton.event

import org.kryptonmc.krypton.api.event.EventBus
import org.kryptonmc.krypton.api.event.Listener
import org.kryptonmc.krypton.extension.logger
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

/**
 * I got so fed up with the event system that I basically copied BungeeCord's
 * EventBus. All credit goes to md_5 for making this originally
 */
class KryptonEventBus : EventBus {

    private val byListenerAndPriority = ConcurrentHashMap<Class<*>, MutableMap<Byte, MutableMap<Any, Set<Method>>>>()
    private val byEventBaked = ConcurrentHashMap<Class<*>, Set<EventHandlerMethod>>()
    private val lock = ReentrantLock()

    override fun call(event: Any) = byEventBaked[event::class.java]?.forEach { listener -> listener(event) } ?: Unit

    override fun register(listener: Any) {
        val handlers = findHandlers(listener)
        lock.lock()
        handlers.forEach { handler ->
            val priorities = byListenerAndPriority.getOrPut(handler.key) { mutableMapOf() }
            handler.value.forEach {
                priorities.getOrPut(it.key) { mutableMapOf() }.apply { put(listener, it.value) }
            }
            byEventBaked[handler.key] = bakeHandlers(handler.key) ?: return@forEach
        }
        lock.unlock()
    }

    private fun findHandlers(listener: Any): MutableMap<Class<*>, MutableMap<Byte, MutableSet<Method>>> {
        val handlers = mutableMapOf<Class<*>, MutableMap<Byte, MutableSet<Method>>>()
        listener::class.java.declaredMethods.forEach {
            val annotation = it.getAnnotation(Listener::class.java) ?: return@forEach

            val parameters = it.parameterTypes
            if (parameters.size != 1) {
                LOGGER.warn("Method $it in class ${listener::class.java} annotated with $annotation does not have a single argument!")
                return@forEach
            }

            val priorities = handlers.getOrPut(parameters[0]) { mutableMapOf() }
            priorities.getOrPut(annotation.priority) { mutableSetOf() }.add(it)
        }
        return handlers
    }

    private fun bakeHandlers(eventClass: Class<*>): Set<EventHandlerMethod>? {
        val handlersByPriority = byListenerAndPriority[eventClass]
        if (handlersByPriority == null) {
            byEventBaked -= eventClass
            return null
        }

        val handlers = mutableSetOf<EventHandlerMethod>()
        for (i in Byte.MAX_VALUE downTo Byte.MIN_VALUE) {
            handlersByPriority[i.toByte()]?.forEach { handler ->
                handler.value.forEach { handlers += EventHandlerMethod(handler.key, it) }
            }
        }

        return handlers
    }

    internal fun unregisterAll() {
        byListenerAndPriority.clear()
        byEventBaked.clear()
    }

    companion object {

        private val LOGGER = logger<KryptonEventBus>()
    }
}
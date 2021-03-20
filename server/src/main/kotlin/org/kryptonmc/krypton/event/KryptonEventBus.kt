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

    private val byListenerAndPriority = ConcurrentHashMap<Class<*>, MutableMap<Byte, MutableMap<Any, MutableSet<Method>>>>()
    private val byEventBaked = ConcurrentHashMap<Class<*>, MutableSet<EventHandlerMethod>>()
    private val lock = ReentrantLock()

    override fun call(event: Any) = byEventBaked[event::class.java]?.forEach { it(event) } ?: Unit

    override fun register(listener: Any) {
        val handler = findHandlers(listener)
        lock.lock()
        handler.forEach { entry ->
            val priorities = byListenerAndPriority.getOrPut(entry.key) { mutableMapOf() }
            entry.value.forEach {
                val currentPriorities = priorities.getOrPut(it.key) { mutableMapOf() }
                currentPriorities[listener] = it.value
            }
            bakeHandlers(entry.key)
        }
        lock.unlock()
    }

    private fun findHandlers(listener: Any): MutableMap<Class<*>, MutableMap<Byte, MutableSet<Method>>> {
        val handler = mutableMapOf<Class<*>, MutableMap<Byte, MutableSet<Method>>>()
        listener::class.java.declaredMethods.forEach {
            val annotation = it.getAnnotation(Listener::class.java) ?: return@forEach

            val parameters = it.parameterTypes
            if (parameters.size != 1) {
                LOGGER.warn("Method $it in class ${listener::class.java} annotated with $annotation does not have a single argument!")
                return@forEach
            }

            val priorities = handler.getOrPut(parameters[0]) { mutableMapOf() }

            val priority = priorities.getOrPut(annotation.priority.toByte()) { mutableSetOf() }
            priority.add(it)
        }
        return handler
    }

    private fun bakeHandlers(eventClass: Class<*>) {
        val handlersByPriority = byListenerAndPriority[eventClass]
        if (handlersByPriority == null) {
            byEventBaked -= eventClass
            return
        }

        val handlers = mutableSetOf<EventHandlerMethod>()

        var value = Byte.MAX_VALUE
        do {
            handlersByPriority[value]?.forEach { handler ->
                handler.value.forEach { handlers += EventHandlerMethod(handler.key, it) }
            } ?: continue
        } while (value-- > Byte.MIN_VALUE)
        byEventBaked[eventClass] = handlers
    }

    companion object {

        private val LOGGER = logger<EventBus>()
    }
}
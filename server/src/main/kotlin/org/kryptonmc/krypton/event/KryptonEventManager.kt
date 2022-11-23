/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2018 Velocity Contributors
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
 * https://github.com/PaperMC/Velocity/blob/1761755d4dfc16cd020aee90c48761d98552531b/proxy/src/main/java/com/velocitypowered/proxy/event/VelocityEventManager.java
 */
package org.kryptonmc.krypton.event

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.common.base.VerifyException
import com.google.common.collect.ArrayListMultimap
import com.google.common.reflect.TypeToken
import org.kryptonmc.api.event.Continuation
import org.kryptonmc.api.event.EventHandler
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.event.EventTask
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.ListenerPriority
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.executor.ThreadPoolBuilder
import org.kryptonmc.krypton.util.executor.daemonThreadFactory
import org.lanternpowered.lmbda.LambdaFactory
import org.lanternpowered.lmbda.LambdaType
import org.lanternpowered.lmbda.kt.lambdaType
import java.lang.invoke.MethodHandles
import java.lang.invoke.VarHandle
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import kotlin.concurrent.read
import kotlin.concurrent.write

class KryptonEventManager(private val pluginManager: PluginManager) : EventManager {

    private val handlersByType = ArrayListMultimap.create<Class<*>, HandlerRegistration>()
    private val handlersCache = Caffeine.newBuilder().build(::bakeHandlers)
    private val untargetedMethodHandlers = Caffeine.newBuilder().weakValues().build(::createUntargetedMethodHandler)
    private val asyncExecutor = ThreadPoolBuilder.fixed(Runtime.getRuntime().availableProcessors())
        .factory(daemonThreadFactory("Krypton Async Event Executor #%d"))
        .build()

    private val lock = ReentrantReadWriteLock()
    private val handlerAdapters = ArrayList<CustomHandlerAdapter<*>>()
    private val eventTypeTracker = EventTypeTracker()

    init {
        Coroutines.registerHandlerAdapter(this)
    }

    @Suppress("UNCHECKED_CAST")
    fun <F : Any> registerHandlerAdapter(
        name: String,
        invokeFunctionType: TypeToken<F>,
        filter: Predicate<Method>,
        validator: BiConsumer<Method, MutableList<String>>,
        handlerBuilder: Function<F, BiFunction<Any, Any, EventTask>>
    ) {
        val lambdaType = LambdaType.of(invokeFunctionType.rawType) as LambdaType<F>
        handlerAdapters.add(CustomHandlerAdapter(name, handlerBuilder, filter, validator, lambdaType, LOOKUP))
    }

    fun shutdown(): Boolean {
        asyncExecutor.shutdown()
        return asyncExecutor.awaitTermination(SHUTDOWN_TIMEOUT_TIME, TimeUnit.SECONDS)
    }

    override fun register(plugin: Any, listener: Any) {
        require(plugin !== listener) { "The plugin main instance is automatically registered!" }
        registerUnchecked(getContainer(plugin), listener)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <E> register(plugin: Any, eventClass: Class<E>, priority: ListenerPriority, handler: EventHandler<E>) {
        val order = priority.ordinal.toShort()
        register(listOf(HandlerRegistration(getContainer(plugin), order, eventClass, handler as EventHandler<Any>, AsyncType.SOMETIMES, handler)))
    }

    fun registerUnchecked(plugin: PluginContainer, listener: Any) {
        val collected = HashMap<String, MethodHandlerInfo>()
        collectMethods(listener.javaClass, collected)

        val registrations = ArrayList<HandlerRegistration>()
        for (methodInfo in collected.values) {
            if (methodInfo.errors != null) {
                LOGGER.info("Invalid listener method ${methodInfo.method.name} in ${methodInfo.method.declaringClass.name}: ${methodInfo.errors}")
                continue
            }
            val untargetedHandler = checkNotNull(untargetedMethodHandlers.get(methodInfo.method)) {
                "Untargeted handler for ${methodInfo.method.name} in ${methodInfo.method.declaringClass.name} was somehow null!"
            }
            if (methodInfo.eventType == null) throw VerifyException("Event type is not present and there are no errors!")
            val handler = untargetedHandler.createHandler(listener)
            registrations.add(HandlerRegistration(plugin, methodInfo.priority, methodInfo.eventType, handler, methodInfo.asyncType, listener))
        }
        register(registrations)
    }

    private fun register(registrations: List<HandlerRegistration>) {
        lock.write { registrations.forEach { handlersByType.put(it.eventType, it) } }
        invalidateHandlers(registrations)
    }

    override fun <E> fire(event: E): CompletableFuture<E> {
        requireNotNull(event) { "Attempted to fire a null event!" } // Required to access event's class
        val handlersCache = handlersCache.get(event.javaClass) ?: return CompletableFuture.completedFuture(event) // Optimisation: nobody's listening
        val future = CompletableFuture<E>()
        fire(future, event, handlersCache)
        return future
    }

    override fun fireAndForget(event: Any) {
        val handlersCache = handlersCache.get(event.javaClass) ?: return // Optimisation: nobody's listening
        fire(null, event, handlersCache)
    }

    // For this function and the one below:
    // This is a bit hacky, but we avoid needing a future here by avoiding all async execution, even if the client specifies that
    // they want their listener to be async. Not sure if this is the best way to do it, as it seems to defeat the point of the client
    // being able to specify whether their listener should be sync or not, this needs reviewing.
    // TODO: Review the way we implement always synchronous event firing
    fun <E> fireSync(event: E): E {
        requireNotNull(event) { "Attempted to fire a null event!" } // Required to access event's class
        val handlersCache = handlersCache.get(event.javaClass) ?: return event // Optimisation: nobody's listening
        fire(null, event, handlersCache, true)
        return event
    }

    fun fireAndForgetSync(event: Any) {
        val handlersCache = handlersCache.get(event.javaClass) ?: return
        fire(null, event, handlersCache, true)
    }

    private fun <E> fire(future: CompletableFuture<E>?, event: E, handlersCache: HandlersCache, alwaysSync: Boolean = false) {
        if (!alwaysSync && handlersCache.asyncType == AsyncType.ALWAYS) {
            // We already know that the event needs to be handled async, so execute it asynchronously from the start
            asyncExecutor.execute { fire(future, event, 0, true, handlersCache.handlers) }
            return
        }
        fire(future, event, 0, false, handlersCache.handlers, alwaysSync)
    }

    override fun unregisterListeners(plugin: Any) {
        unregisterAll { it.plugin === getContainer(plugin) }
    }

    override fun unregisterListener(plugin: Any, listener: Any) {
        unregisterAll { it.plugin === getContainer(plugin) && it.instance === listener }
    }

    override fun <E> unregister(plugin: Any, handler: EventHandler<E>) {
        unregisterListener(plugin, handler)
    }

    private fun unregisterAll(predicate: Predicate<HandlerRegistration>) {
        val removed = ArrayList<HandlerRegistration>()
        lock.write {
            val iterator = handlersByType.values().iterator()
            while (iterator.hasNext()) {
                val registration = iterator.next()
                if (predicate.test(registration)) {
                    iterator.remove()
                    removed.add(registration)
                }
            }
        }
        invalidateHandlers(removed)
    }

    private fun invalidateHandlers(handlers: List<HandlerRegistration>) {
        handlersCache.invalidateAll(handlers.asSequence().flatMap { eventTypeTracker.getFriendsOf(it.eventType) }.distinct().toList())
    }

    private fun <E> fire(
        future: CompletableFuture<E>?,
        event: E,
        offset: Int,
        currentlyAsync: Boolean,
        registrations: Array<HandlerRegistration>,
        alwaysSync: Boolean = false
    ) {
        for (i in offset until registrations.size) {
            val registration = registrations[i]
            try {
                val task = registration.handler.execute(event!!) ?: continue
                val continuationTask = ContinuationTask(task, i, registrations, future, currentlyAsync, event)
                if (alwaysSync || currentlyAsync || !task.mustBeAsync) {
                    if (continuationTask.execute()) continue
                } else {
                    asyncExecutor.execute(continuationTask)
                }
                return
            } catch (exception: Throwable) {
                logHandlerException(registration, exception)
            }
        }
        future?.complete(event)
    }

    private fun createUntargetedMethodHandler(method: Method): UntargetedEventHandler {
        for (adapter in handlerAdapters) {
            if (adapter.filter.test(method)) return adapter.createUntargetedHandler(method)
        }
        val type = when {
            EventTask::class.java.isAssignableFrom(method.returnType) -> UNTARGETED_EVENT_TASK_HANDLER_TYPE
            method.parameterCount == 2 -> UNTARGETED_WITH_CONTINUATION_HANDLER_TYPE
            else -> UNTARGETED_VOID_HANDLER_TYPE
        }
        val lookup = MethodHandles.privateLookupIn(method.declaringClass, LOOKUP)
        return LambdaFactory.create(type.defineClassesWith(lookup), lookup.unreflect(method))
    }

    private fun bakeHandlers(eventType: Class<*>): HandlersCache? {
        val baked = ArrayList<HandlerRegistration>()

        val types = eventTypeTracker.getFriendsOf(eventType)
        lock.read { types.forEach { baked.addAll(handlersByType.get(it)) } }

        if (baked.isEmpty()) return null
        baked.sortWith(HANDLER_COMPARATOR)

        val asyncType = when {
            baked.any { it.asyncType == AsyncType.ALWAYS } -> AsyncType.ALWAYS
            baked.any { it.asyncType == AsyncType.SOMETIMES } -> AsyncType.SOMETIMES
            else -> AsyncType.NEVER
        }
        return HandlersCache(asyncType, baked.toTypedArray())
    }

    private fun collectMethods(targetClass: Class<*>, collected: MutableMap<String, MethodHandlerInfo>) {
        for (method in targetClass.declaredMethods) {
            val listener = method.getAnnotation(Listener::class.java) ?: continue

            var key = "${method.name}(${method.parameterTypes.joinToString(",") { it.name }})"
            if (Modifier.isPrivate(method.modifiers)) key = "${targetClass.name}$$key"
            if (collected.containsKey(key)) continue

            val errors = HashSet<String>()
            if (Modifier.isStatic(method.modifiers)) errors.add("method must not be static")
            if (Modifier.isAbstract(method.modifiers)) errors.add("method must not be abstract")

            var eventType: Class<*>? = null
            var continuationType: Class<*>? = null
            var handlerAdapter: CustomHandlerAdapter<*>? = null

            val parameterCount = method.parameterCount
            if (parameterCount == 0) {
                errors.add("method must have at least one parameter which is the event")
            } else {
                val parameterTypes = method.parameterTypes
                eventType = parameterTypes[0]
                for (candidate in handlerAdapters) {
                    if (candidate.filter.test(method)) {
                        handlerAdapter = candidate
                        continue
                    }
                }
                if (handlerAdapter != null) {
                    val adapterErrors = ArrayList<String>()
                    handlerAdapter.validator.accept(method, adapterErrors)
                    if (adapterErrors.isNotEmpty()) errors.add("${handlerAdapter.name} adapter errors: [${adapterErrors.joinToString(", ")}]")
                } else if (parameterCount == 2) {
                    continuationType = parameterTypes[1]
                    if (continuationType != Continuation::class.java) {
                        errors.add("method is allowed to have a continuation as a second parameter, but ${continuationType.name} is invalid")
                    }
                }
            }

            var asyncType = AsyncType.NEVER
            if (handlerAdapter == null) {
                when {
                    method.returnType != Void.TYPE && continuationType == Continuation::class.java ->
                        errors.add("method return type must be void if a continuation parameter is provided")
                    method.returnType != Void.TYPE && method.returnType != EventTask::class.java ->
                        errors.add("method return type must be void, AsyncTask, AsyncTask.Basic, or AsyncTask.WithContinuation")
                    method.returnType == EventTask::class.java -> asyncType = AsyncType.SOMETIMES
                }
            } else {
                asyncType = AsyncType.SOMETIMES
            }
            if (listener.mustBeAsync) asyncType = AsyncType.ALWAYS

            val priority = listener.priority.ordinal.toShort()
            val errorsJoined = if (errors.isEmpty()) null else errors.joinToString(",")
            collected.put(key, MethodHandlerInfo(method, asyncType, eventType, priority, errorsJoined, continuationType))
        }
        val superclass = targetClass.superclass
        if (superclass != Any::class.java) collectMethods(superclass, collected)
    }

    private fun getContainer(plugin: Any): PluginContainer =
        requireNotNull(pluginManager.fromInstance(plugin)) { "Cannot register a listener for a plugin that isn't loaded!" }

    @JvmRecord
    private data class HandlerRegistration(
        val plugin: PluginContainer,
        val priority: Short,
        val eventType: Class<*>,
        val handler: EventHandler<Any>,
        val asyncType: AsyncType,
        val instance: Any
    )

    private class HandlersCache(val asyncType: AsyncType, val handlers: Array<HandlerRegistration>)

    private enum class AsyncType {

        /**
         * The complete event will be handled on an async thread.
         */
        ALWAYS,

        /**
         * The event will initially start on the Netty thread, and possibly
         * switch over to an async thread.
         */
        SOMETIMES,

        /**
         * The event will never run async, everything is handled on
         * the Netty thread.
         */
        NEVER
    }

    @JvmRecord
    private data class MethodHandlerInfo(
        val method: Method,
        val asyncType: AsyncType,
        val eventType: Class<*>?,
        val priority: Short,
        val errors: String?,
        val continuationType: Class<*>?
    )

    private inner class ContinuationTask<E>(
        private val task: EventTask,
        private val index: Int,
        private val registrations: Array<HandlerRegistration>,
        private val future: CompletableFuture<E>?,
        private val currentlyAsync: Boolean,
        private val event: E
    ) : Continuation, Runnable {

        @Volatile
        private var state = TASK_STATE_DEFAULT
        @Suppress("UNUSED", "UnusedPrivateMember") // Used by means of a VarHandle
        @Volatile
        private var resumed = false

        override fun run() {
            if (execute()) fire(future, event, index + 1, currentlyAsync, registrations)
        }

        override fun resume() {
            resume(null, true)
        }

        override fun resumeWithException(exception: Throwable) {
            resume(exception, true)
        }

        fun execute(): Boolean {
            state = TASK_STATE_EXECUTING
            try {
                task.execute(this)
            } catch (exception: Throwable) {
                resume(exception, false)
            }
            return !CONTINUATION_TASK_STATE.compareAndSet(this, TASK_STATE_EXECUTING, TASK_STATE_DEFAULT)
        }

        private fun resume(exception: Throwable?, validateOnlyOnce: Boolean) {
            val changed = CONTINUATION_TASK_RESUMED.compareAndSet(this, false, true)
            check(changed || !validateOnlyOnce) { "The continuation can only be resumed once!" }
            val registration = registrations[index]
            if (exception != null) logHandlerException(registration, exception)
            if (!changed) return
            if (index + 1 == registrations.size) {
                // Optimisation: don't schedule a task just to complete the future
                future?.complete(event)
                return
            }
            if (!CONTINUATION_TASK_STATE.compareAndSet(this, TASK_STATE_EXECUTING, TASK_STATE_CONTINUE_IMMEDIATELY)) {
                asyncExecutor.execute { fire(future, event, index + 1, true, registrations) }
            }
        }
    }

    companion object {

        private val LOGGER = logger<KryptonEventManager>()
        private const val SHUTDOWN_TIMEOUT_TIME = 10L

        private const val TASK_STATE_DEFAULT = 0
        private const val TASK_STATE_EXECUTING = 1
        private const val TASK_STATE_CONTINUE_IMMEDIATELY = 2

        private val CONTINUATION_TASK_RESUMED: VarHandle
        private val CONTINUATION_TASK_STATE: VarHandle

        private val LOOKUP = MethodHandles.lookup()
        private val UNTARGETED_EVENT_TASK_HANDLER_TYPE = lambdaType<UntargetedEventHandler.Task>()
        private val UNTARGETED_VOID_HANDLER_TYPE = lambdaType<UntargetedEventHandler.Void>()
        private val UNTARGETED_WITH_CONTINUATION_HANDLER_TYPE = lambdaType<UntargetedEventHandler.WithContinuation>()

        private val HANDLER_COMPARATOR = Comparator.comparingInt<HandlerRegistration> { it.priority.toInt() }

        init {
            try {
                val lookup = MethodHandles.privateLookupIn(ContinuationTask::class.java, MethodHandles.lookup())
                CONTINUATION_TASK_RESUMED = lookup.findPrimitiveVarHandle<ContinuationTask<*>, Boolean>("resumed")
                CONTINUATION_TASK_STATE = lookup.findPrimitiveVarHandle<ContinuationTask<*>, Int>("state")
            } catch (exception: ReflectiveOperationException) {
                throw IllegalStateException("Could not find VarHandle for ContinuationTask field!", exception)
            }
        }

        @JvmStatic
        private fun logHandlerException(registration: HandlerRegistration, exception: Throwable) {
            LOGGER.error("Could not pass ${registration.eventType.simpleName} to ${registration.plugin.description.id}!", exception)
        }
    }
}

private inline fun <reified T, reified R : Any> MethodHandles.Lookup.findPrimitiveVarHandle(name: String): VarHandle =
    findVarHandle(T::class.java, name, R::class.javaPrimitiveType)

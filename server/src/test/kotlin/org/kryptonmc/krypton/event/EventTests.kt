/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
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
 */
package org.kryptonmc.krypton.event

import com.google.common.reflect.TypeToken
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.kryptonmc.api.event.Continuation
import org.kryptonmc.api.event.EventTask
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.ListenerPriority
import org.kryptonmc.api.event.registerHandler
import org.kryptonmc.krypton.testutil.MockPluginManager
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Suppress("UnusedPrivateMember")
class EventTests {

    private val eventManager = KryptonEventManager(MockPluginManager())

    @AfterAll
    fun shutdown() {
        eventManager.shutdown()
    }

    private fun handleMethodListener(listener: Any) {
        eventManager.registerListener(MockPluginManager.PLUGIN_A, listener)
        try {
            eventManager.fire(TestEvent()).get()
        } finally {
            eventManager.unregisterListeners(MockPluginManager.PLUGIN_A)
        }
    }

    @Test
    fun `test always sync`() {
        val listener = AlwaysSyncListener()
        handleMethodListener(listener)
        assertSyncThread(listener.thread!!)
        assertEquals(1, listener.result)
    }

    @Test
    fun `ensure listener order is preserved`() {
        val listenerAInvoked = AtomicLong()
        val listenerBInvoked = AtomicLong()
        val listenerCInvoked = AtomicLong()
        eventManager.registerHandler<TestEvent>(MockPluginManager.PLUGIN_A) {
            listenerAInvoked.set(System.nanoTime())
            null
        }
        eventManager.registerHandler<TestEvent>(MockPluginManager.PLUGIN_A) {
            listenerBInvoked.set(System.nanoTime())
            null
        }
        eventManager.registerHandler<TestEvent>(MockPluginManager.PLUGIN_A) {
            listenerCInvoked.set(System.nanoTime())
            null
        }
        try {
            eventManager.fire(TestEvent()).get()
        } finally {
            eventManager.unregisterListeners(MockPluginManager.PLUGIN_A)
        }
        assertTrue(listenerAInvoked.get() < listenerBInvoked.get(), "Listener B invoked before A!")
        assertTrue(listenerBInvoked.get() < listenerCInvoked.get(), "Listener C invoked before B!")
    }

    @Test
    fun `test always async`() {
        val listener = AlwaysAsyncListener()
        handleMethodListener(listener)
        assertAsyncThread(listener.threadA!!)
        assertAsyncThread(listener.threadB!!)
        assertAsyncThread(listener.threadC!!)
        assertEquals(3, listener.result)
    }

    @Test
    fun `test always sync when event is fired sync`() {
        val listener = AlwaysAsyncListener()
        eventManager.registerListener(MockPluginManager.PLUGIN_A, listener)
        try {
            eventManager.fireSync(TestEvent())
        } finally {
            eventManager.unregisterListeners(MockPluginManager.PLUGIN_A)
        }
        assertSyncThread(listener.threadA!!)
        assertSyncThread(listener.threadB!!)
        assertSyncThread(listener.threadC!!)
        assertEquals(3, listener.result)
    }

    @Test
    fun `test sometimes async`() {
        val listener = SometimesAsyncListener()
        handleMethodListener(listener)
        assertSyncThread(listener.threadA!!)
        assertSyncThread(listener.threadB!!)
        assertAsyncThread(listener.threadC!!)
        assertAsyncThread(listener.threadD!!)
        assertEquals(3, listener.result)
    }

    @Test
    fun `test continuation`() {
        val listener = ContinuationListener()
        handleMethodListener(listener)
        assertSyncThread(listener.threadA!!)
        assertSyncThread(listener.threadB!!)
        assertAsyncThread(listener.threadC!!)
        assertEquals(2, listener.value.get())
    }

    @Test
    fun `test resume continuation immediately`() {
        val listener = ResumeContinuationImmediatelyListener()
        handleMethodListener(listener)
        assertSyncThread(listener.threadA!!)
        assertSyncThread(listener.threadB!!)
        assertSyncThread(listener.threadC!!)
        assertEquals(2, listener.result)
    }

    @Test
    fun `test continuation parameter`() {
        val listener = ContinuationParameterListener()
        handleMethodListener(listener)
        assertSyncThread(listener.threadA!!)
        assertSyncThread(listener.threadB!!)
        assertAsyncThread(listener.threadC!!)
        assertEquals(3, listener.result.get())
    }

    @Test
    fun `test fancy continuation parameter`() {
        val type = object : TypeToken<TriConsumer<Any, Any, FancyContinuation>>() {}
        val filter = Predicate<Method> { it.parameterCount > 1 && it.parameterTypes[1] == FancyContinuation::class.java }
        val validator = BiConsumer<Method, MutableList<String>> { method, errors ->
            if (method.returnType != Void.TYPE) errors.add("method return type must be void")
            if (method.parameterCount == 0) {
                errors.add("method must have exactly two parameters, the first is the event and the second is the fancy continuation")
            }
        }
        val handlerBuilder = Function<TriConsumer<Any, Any, FancyContinuation>, BiFunction<Any, Any, EventTask>> { function ->
            BiFunction { instance, event -> EventTask.withContinuation { function.accept(instance, event, FancyContinuationImpl(it)) } }
        }
        eventManager.registerHandlerAdapter("fancy", type, filter, validator, handlerBuilder)
        val listener = FancyContinuationListener()
        handleMethodListener(listener)
        assertEquals(1, listener.result)
    }

    @Test
    fun `test coroutine suspending listener`() {
        val listener = CoroutineSuspendingListener()
        handleMethodListener(listener)
        assertEquals(1, listener.result)
    }

    private class TestEvent

    private class AlwaysSyncListener {

        var thread: Thread? = null
        var result: Int = 0

        @Listener
        fun sync(event: TestEvent) {
            result++
            thread = Thread.currentThread()
        }
    }

    private class AlwaysAsyncListener {

        var threadA: Thread? = null
        var threadB: Thread? = null
        var threadC: Thread? = null
        var result: Int = 0

        @Listener(mustBeAsync = true)
        fun firstAsync(event: TestEvent) {
            result++
            threadA = Thread.currentThread()
        }

        @Listener
        fun secondAsync(event: TestEvent): EventTask {
            threadB = Thread.currentThread()
            return EventTask.async { result++ }
        }

        @Listener
        fun thirdAsync(event: TestEvent) {
            result++
            threadC = Thread.currentThread()
        }
    }

    private class SometimesAsyncListener {

        var threadA: Thread? = null
        var threadB: Thread? = null
        var threadC: Thread? = null
        var threadD: Thread? = null
        var result: Int = 0

        @Listener(ListenerPriority.HIGH)
        fun notAsync(event: TestEvent) {
            result++
            threadA = Thread.currentThread()
        }

        @Listener
        fun notAsyncUntilTask(event: TestEvent): EventTask {
            threadB = Thread.currentThread()
            return EventTask.async {
                threadC = Thread.currentThread()
                result++
            }
        }

        @Listener(ListenerPriority.LOW)
        fun stillAsyncAfterTask(event: TestEvent) {
            threadD = Thread.currentThread()
            result++
        }
    }

    private class ContinuationListener {

        var threadA: Thread? = null
        var threadB: Thread? = null
        var threadC: Thread? = null
        val value: AtomicInteger = AtomicInteger()

        @Listener(ListenerPriority.HIGH)
        fun continuation(event: TestEvent): EventTask {
            threadA = Thread.currentThread()
            return EventTask.withContinuation {
                value.incrementAndGet()
                threadB = Thread.currentThread()
                Thread {
                    try {
                        Thread.sleep(100)
                    } catch (exception: InterruptedException) {
                        exception.printStackTrace()
                    }
                    value.incrementAndGet()
                    it.resume()
                }.start()
            }
        }

        @Listener(ListenerPriority.LOW)
        fun afterContinuation(event: TestEvent) {
            threadC = Thread.currentThread()
        }
    }

    private class ResumeContinuationImmediatelyListener {

        var threadA: Thread? = null
        var threadB: Thread? = null
        var threadC: Thread? = null
        var result: Int = 0

        @Listener(ListenerPriority.HIGH)
        fun continuation(event: TestEvent): EventTask {
            threadA = Thread.currentThread()
            return EventTask.withContinuation {
                threadB = Thread.currentThread()
                result++
                it.resume()
            }
        }

        @Listener(ListenerPriority.LOW)
        fun afterContinuation(event: TestEvent) {
            threadC = Thread.currentThread()
            result++
        }
    }

    private class ContinuationParameterListener {

        var threadA: Thread? = null
        var threadB: Thread? = null
        var threadC: Thread? = null
        val result: AtomicInteger = AtomicInteger()

        @Listener
        fun resume(event: TestEvent, continuation: Continuation) {
            threadA = Thread.currentThread()
            result.incrementAndGet()
            continuation.resume()
        }

        @Listener(ListenerPriority.LOW)
        fun resumeFromCustomThread(event: TestEvent, continuation: Continuation) {
            threadB = Thread.currentThread()
            Thread {
                try {
                    Thread.sleep(100)
                } catch (exception: InterruptedException) {
                    exception.printStackTrace()
                }
                result.incrementAndGet()
                continuation.resume()
            }.start()
        }

        @Listener(ListenerPriority.NONE)
        fun afterCustomThread(event: TestEvent, continuation: Continuation) {
            threadC = Thread.currentThread()
            result.incrementAndGet()
            continuation.resume()
        }
    }

    private class CoroutineSuspendingListener {

        var result: Int = 0

        @Listener
        suspend fun suspending(event: TestEvent) {
            result++
        }
    }

    private class FancyContinuationListener {

        var result: Int = 0

        @Listener
        fun continuation(event: TestEvent, continuation: Continuation) {
            result++
            continuation.resume()
        }
    }

    private interface FancyContinuation {

        fun resume()

        fun resumeWithException(exception: Exception)
    }

    private class FancyContinuationImpl(private val continuation: Continuation) : FancyContinuation {

        override fun resume() {
            continuation.resume()
        }

        override fun resumeWithException(exception: Exception) {
            continuation.resumeWithException(exception)
        }
    }

    fun interface TriConsumer<A, B, C> {

        fun accept(a: A, b: B, c: C)
    }

    companion object {

        @JvmStatic
        private fun assertAsyncThread(thread: Thread) {
            assertTrue(thread.name.contains("Krypton Async Event Executor"))
        }

        @JvmStatic
        private fun assertSyncThread(thread: Thread) {
            assertEquals(Thread.currentThread(), thread);
        }
    }
}

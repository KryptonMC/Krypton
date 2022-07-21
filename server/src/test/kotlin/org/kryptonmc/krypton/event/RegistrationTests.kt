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

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertDoesNotThrow
import org.kryptonmc.api.event.EventHandler
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.event.EventTask
import org.kryptonmc.api.event.Listener
import org.kryptonmc.api.event.registerHandler
import org.kryptonmc.krypton.event.RegistrationTests.EventGenerator
import org.kryptonmc.krypton.event.RegistrationTests.TestFunction
import org.kryptonmc.krypton.util.MockPluginManager
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertSame

class RegistrationTests {

    private var eventManager: EventManager by Delegates.notNull()

    @BeforeEach
    fun setup() {
        resetEventManager()
    }

    @TestFactory
    fun `simple register and unregister`(): Iterable<DynamicNode> = composeTests("simple register and unregister") { annotated, generator ->
        if (annotated) eventManager.register(PLUGIN_A, AnnotatedListener()) else eventManager.registerHandler(PLUGIN_A, HandlerListener())
        generator.assertFiredEventValue(1)
        eventManager.unregisterListeners(PLUGIN_A)
        generator.assertFiredEventValue(0)
        assertDoesNotThrow("Extra unregister should do nothing!") { eventManager.unregisterListeners(PLUGIN_A) }
    }

    @TestFactory
    fun `double register listener`(): Iterable<DynamicNode> = composeTests("double register listener", createDoubleRegisterFunction(PLUGIN_A))

    @TestFactory
    fun `double register listener for different plugins`(): Iterable<DynamicNode> =
        composeTests("double register listener for different plugins", createDoubleRegisterFunction(PLUGIN_B))

    @TestFactory
    fun `double unregister listener`(): Iterable<DynamicNode> = composeTests("double unregister listener") { annotated, generator ->
        if (annotated) {
            val annotatedListener = AnnotatedListener()
            eventManager.register(PLUGIN_A, annotatedListener)
            eventManager.unregisterListener(PLUGIN_A, annotatedListener)
            assertDoesNotThrow("Extra unregister should do nothing!") { eventManager.unregisterListener(PLUGIN_A, annotatedListener) }
        } else {
            val handler = HandlerListener()
            eventManager.registerHandler(PLUGIN_A, handler)
            eventManager.unregister(PLUGIN_A, handler)
            assertDoesNotThrow("Extra unregister should do nothing!") { eventManager.unregister(PLUGIN_A, handler) }
        }
        generator.assertFiredEventValue(0)
    }

    private fun createDoubleRegisterFunction(pluginB: Any): TestFunction = TestFunction { annotated, generator ->
        if (annotated) {
            val annotatedListener = AnnotatedListener()
            eventManager.register(PLUGIN_A, annotatedListener)
            eventManager.register(pluginB, annotatedListener)
        } else {
            val handler = HandlerListener()
            eventManager.registerHandler(PLUGIN_A, handler)
            eventManager.registerHandler(pluginB, handler)
        }
        generator.assertFiredEventValue(2)
    }

    private fun resetEventManager() {
        eventManager = KryptonEventManager(MockPluginManager())
    }

    private fun composeTests(name: String, testFunction: TestFunction): Iterable<DynamicNode> = buildSet {
        TRUE_AND_FALSE.forEach { annotated ->
            TRUE_AND_FALSE.forEach { subclassed ->
                val generator = EventGenerator { value ->
                    val simpleEvent = if (subclassed) SimpleSubclassedEvent() else SimpleEvent()
                    val shouldBeSame = eventManager.fire(simpleEvent).join()
                    assertSame(simpleEvent, shouldBeSame)
                    assertEquals(value, simpleEvent.value)
                }
                add(DynamicTest.dynamicTest("$name. Annotated: $annotated, Subclassed: $subclassed") {
                    try {
                        testFunction.runTest(annotated, generator)
                    } finally {
                        resetEventManager()
                    }
                })
            }
        }
    }

    // Must be public to generate a method calling it
    open class SimpleEvent {

        var value: Int = 0
    }

    class SimpleSubclassedEvent : SimpleEvent()

    class HandlerListener : EventHandler<SimpleEvent> {

        override fun execute(event: SimpleEvent): EventTask? {
            event.value++
            return null
        }
    }

    class AnnotatedListener {

        @Listener
        fun increment(event: SimpleEvent) {
            event.value++
        }
    }

    private fun interface EventGenerator {

        fun assertFiredEventValue(value: Int)
    }

    private fun interface TestFunction {

        fun runTest(annotated: Boolean, generator: EventGenerator)
    }

    companion object {

        private val PLUGIN_A = MockPluginManager.PLUGIN_A
        private val PLUGIN_B = MockPluginManager.PLUGIN_B
        private val TRUE_AND_FALSE = booleanArrayOf(true, false)
    }
}

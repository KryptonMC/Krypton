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

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.event.Listener
import org.kryptonmc.krypton.testutil.Bootstrapping
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

@Suppress("UnusedPrivateMember")
class AnnotatedEventListenerTest {

    @Test
    fun `ensure listener with no annotated methods registers nothing`() {
        val node = EventNode.all("no_annotated_methods_test")
        assertDoesNotThrow { node.registerListeners(NoAnnotation()) }
        assertFalse(node.hasListener(SimpleEvent::class.java), "No event listeners should be registered!")
    }

    @Test
    fun `ensure static event listener is not allowed`() {
        val node = EventNode.all("static_method_test")
        assertThrows<IllegalArgumentException> { node.registerListeners(StaticMethod) }
        assertFalse(node.hasListener(SimpleEvent::class.java), "No event listeners should be registered!")
    }

    @Test
    fun `ensure standard listener registers correctly and fires`() {
        val node = EventNode.all("standard_listener_test")
        val listener = StandardListener()

        assertDoesNotThrow { node.registerListeners(listener) }
        assertTrue(node.hasListener(SimpleEvent::class.java), "Event listener method should be registered!")

        node.fire(SimpleEvent())
        assertEquals(1, listener.result, "Event listener method should have been called once!")
    }

    class SimpleEvent : Event

    private class NoAnnotation {

        fun onSimpleEvent(event: SimpleEvent) {
            fail("This method should not be called because it does not have the @Listener annotation!")
        }
    }

    private object StaticMethod {

        @JvmStatic
        @Listener
        fun onSimpleEvent(event: SimpleEvent) {
            fail("This method should not be called because it is static!")
        }
    }

    private class StandardListener {

        var result: Int = 0

        @Listener
        fun onSimpleEvent(event: SimpleEvent) {
            result++
        }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `setup factories`() {
            Bootstrapping.loadFactories()
        }
    }
}

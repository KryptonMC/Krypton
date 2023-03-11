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
import org.kryptonmc.api.event.Event
import org.kryptonmc.api.event.EventFilter
import org.kryptonmc.api.event.EventListener
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.event.create
import org.kryptonmc.api.event.of
import org.kryptonmc.api.event.type.AbstractDeniableEvent
import org.kryptonmc.krypton.testutil.Bootstrapping
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class EventNodeTest {

    @Test
    fun `ensure fire calls registered listener on node`() {
        val node = EventNode.all("fire_test")
        val result = AtomicBoolean(false)
        val listener = EventListener.of<SimpleEvent> { result.set(true) }
        node.registerListener(listener)

        assertFalse(result.get(), "The event listener should not be called before the event is fired!")
        node.fire(SimpleEvent())
        assertTrue(result.get(), "The event listener should be called when the event is fired!")
    }

    @Test
    fun `ensure fire calls registered listener for supertype`() {
        val node = EventNode.all("fire_supertype_test")
        val result = AtomicBoolean(false)
        val listener = EventListener.of<SimpleEvent> { result.set(true) }
        node.registerListener(listener)

        assertFalse(result.get(), "The event listener should not be called before the event is fired!")
        node.fire(SimpleEventSubclass())
        assertTrue(result.get(), "The event listener should be called when the event is fired!")
    }

    @Test
    fun `ensure fire does not call unregistered listener`() {
        val node = EventNode.all("unregister_test")
        val result = AtomicBoolean(false)
        val listener = EventListener.of<SimpleEvent> { result.set(true) }
        node.registerListener(listener)

        node.unregisterListener(listener)
        node.fire(SimpleEvent())
        assertFalse(result.get(), "The event listener should not be called after it has been unregistered!")
    }

    @Test
    fun `ensure fire does not propagate deniable events after they are denied`() {
        val node = EventNode.all("deniable_propagation_test")
        val result = AtomicBoolean(false)
        val listener = EventListener.of<SimpleDeniableEvent> { event ->
            event.deny()
            result.set(true)
            assertFalse(event.isAllowed(), "Deniable event should not be allowed after denying it!")
        }
        node.registerListener(listener)

        node.fire(SimpleDeniableEvent())
        assertTrue(result.get(), "The event listener should not be called when the event is fired!")
        node.registerListener(SimpleDeniableEvent::class.java) {
            fail("The event should not be propagated to other listeners after being denied!")
        }
        node.fire(SimpleDeniableEvent())
    }

    @Test
    fun `ensure order of child execution is by priority`() {
        val node = EventNode.all("child_priority_test")
        val result = AtomicInteger(0)
        val child1 = EventNode.all("child1")
        child1.priority = 1
        child1.registerListener(SimpleEvent::class.java) {
            assertEquals(0, result.get(), "child1 should be called before child2!")
            result.set(1)
        }
        val child2 = EventNode.all("child2")
        child2.priority = 2
        child2.registerListener(SimpleEvent::class.java) {
            assertEquals(1, result.get(), "child2 should be called after child1!")
            result.set(2)
        }

        node.addChild(child1)
        node.addChild(child2)
        assertEquals(2, node.children.size, "The event node should have 2 children!")

        node.fire(SimpleEvent())
        assertEquals(2, result.get(), "The event listener should be called when the event is fired!")
    }

    @Test
    fun `ensure removed children do not receive events`() {
        val node = EventNode.all("child_removal_test")
        val result = AtomicInteger(0)
        val child1 = EventNode.all("child1")
        child1.priority = 1
        child1.registerListener(SimpleEvent::class.java) {
            assertEquals(0, result.get(), "child1 should be called before child2!")
            result.set(1)
        }
        val child2 = EventNode.all("child2")
        child2.priority = 2
        child2.registerListener(SimpleEvent::class.java) {
            assertEquals(1, result.get(), "child2 should be called after child1!")
            result.set(2)
        }

        node.addChild(child1)
        node.addChild(child2)
        node.removeChild(child2)
        assertEquals(node.children.size, 1, "The event node should only have one child!")
        node.fire(SimpleEvent())
        assertEquals(1, result.get(), "child2 should have been removed!")

        result.set(0)
        node.removeChild(child1)
        assertEquals(node.children.size, 0, "The event node should have no children!")
        node.fire(SimpleEvent())
        assertEquals(0, result.get(), "The event listener should not be called after children are removed!")
    }

    @Test
    fun `ensure filtered out events are not passed to listeners`() {
        val result = AtomicBoolean(false)
        val childResult = AtomicBoolean(false)

        val filter = EventFilter.create<SimpleFilterableEvent, String> { it.value }
        val node1 = EventNode.filteredForType("filter_test_1", filter) { _, value -> value == "abc" }
        val node2 = EventNode.filteredForType("filter_test_2", filter) { _, value -> value == "def" }
        node2.registerListener(SimpleFilterableEvent::class.java) { childResult.set(true) }
        node1.addChild(node2)

        var listener = EventListener.of(SimpleFilterableEvent::class.java) { fail("The event listener should not be called!") }
        node1.registerListener(listener)
        node1.fire(SimpleFilterableEvent("def"))
        assertFalse(childResult.get())

        node1.unregisterListener(listener)
        listener = EventListener.of(SimpleFilterableEvent::class.java) { result.set(true) }
        node1.registerListener(listener)
        node1.fire(SimpleFilterableEvent("abc"))
        assertTrue(result.get(), "The event listener should be called when the event is fired!")
        assertTrue(result.get(), "The child event listener should be called when the event is fired!")
    }

    open class SimpleEvent : Event

    class SimpleDeniableEvent : AbstractDeniableEvent()

    class SimpleFilterableEvent(val value: String) : Event

    class SimpleEventSubclass : SimpleEvent()

    companion object {

        @JvmStatic
        @BeforeAll
        fun `setup factories`() {
            Bootstrapping.loadFactories()
        }
    }
}

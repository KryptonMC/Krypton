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
package org.kryptonmc.api.event

import java.util.function.Consumer
import java.util.function.Function

/**
 * Creates a new event filter for the given event type [E] and handler
 * type [H] on the event, using the given [handlerGetter], if given, to get
 * the handler object from the event.
 *
 * If the handler getter is null, [EventFilter.getHandler] on the filter will
 * always return null.
 *
 * @param E the type of event to filter for
 * @param H the type of handler object to filter from the event
 * @param handlerGetter the function to get the handler object from the event
 * @return the event filter
 */
public inline fun <reified E : Event, reified H> EventFilter.Companion.create(handlerGetter: Function<E, H>?): EventFilter<E, H> {
    return create(E::class.java, H::class.java, handlerGetter)
}

/**
 * Registers an event listener for the given event type [T] to this event
 * node, calling the given [handler] when the event is fired.
 *
 * @param T the type of event the node accepts
 * @param E the type of event to listen for
 * @param handler the handler for the event
 * @return this event node
 */
public inline fun <T : Event, reified E : T> EventNode<T>.registerListener(handler: Consumer<E>) {
    registerListener(E::class.java, handler)
}

/**
 * Creates a new event listener builder for events of the
 * given event type [T].
 *
 * @param T the type of event
 * @return a new builder
 */
public inline fun <reified T : Event> EventListener.Companion.builder(): EventListener.Builder<T> = builder(T::class.java)

/**
 * Creates a new event listener that listens for events of the given event
 * type [T], running the given [handler] when an event of the correct type is
 * fired.
 *
 * @param T the type of events the listener will listen for
 * @param handler the handler for the listener
 * @return a new event listener
 */
public inline fun <reified T : Event> EventListener.Companion.of(crossinline handler: (T) -> Unit): EventListener<T> {
    return of(T::class.java) { handler(it) }
}

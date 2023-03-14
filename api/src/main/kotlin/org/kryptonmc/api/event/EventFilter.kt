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

import java.util.function.Function

/**
 * A filter for a specific type of event.
 *
 * This is useful for creating nodes that only listen to a specific type of
 * event, and can be used to filter out events that are not of the specific
 * type required.
 *
 * You can also filter out events based on the contents of the event itself.
 * For example, you could create a filter that only allows player events where
 * the player is in creative mode.
 *
 * @param E the type of events allowed by the filter
 * @param H the type of handler object to filter on, e.g. a player for a
 * player event
 */
public interface EventFilter<E : Event, H> {

    /**
     * The type of event to filter for.
     */
    public val eventType: Class<E>

    /**
     * The type of handler object to filter from the event.
     *
     * This is the same type that is returned by [getHandler].
     *
     * If this is null, no handler object filtering will be performed,
     * and [getHandler] should always return null, regardless of the event
     * type. However, this restriction is not enforced by the interface.
     */
    public val handlerType: Class<H>?

    /**
     * Gets the handler object from the given [event], if available.
     *
     * If there is no handler object available for the given [event],
     * or the handler object is null, this method will return null.
     *
     * @param event the event to get the handler object from
     * @return the handler object
     */
    public fun getHandler(event: E): H?

    /**
     * Casts the given [event] to the type of event this filter accepts, then
     * gets the handler object from the event.
     *
     * If there is no handler object available for the given [event],
     * or the handler object is null, this method will return null.
     *
     * @param event the event to get the handler object from
     * @return the handler object
     * @throws ClassCastException if the given [event] is not of the required
     * type for this filter
     */
    @Suppress("UNCHECKED_CAST")
    public fun castHandler(event: Any): H? = getHandler(event as E)

    public companion object {

        /**
         * An event filter that does not perform any filtering, and accepts
         * all event types.
         */
        @JvmField
        public val ALL: EventFilter<Event, *> = create<Event, Any>(Event::class.java, null, null)

        /**
         * Creates a new event filter for the given [eventType] and
         * [handlerType] on the event, using the given [handlerGetter], if
         * given, to get the handler object from the event.
         *
         * If the handler getter is null, [getHandler] on the filter will
         * always return null.
         *
         * @param E the event type
         * @param H the handler object type
         * @param eventType the type of event to filter for
         * @param handlerType the type of handler object to filter
         * from the event
         * @param handlerGetter the function to get the handler object from the event
         * @return the event filter
         */
        @JvmStatic
        public fun <E : Event, H> create(eventType: Class<E>, handlerType: Class<H>?, handlerGetter: Function<E, H>?): EventFilter<E, H> {
            return object : EventFilter<E, H> {
                override val eventType: Class<E>
                    get() = eventType
                override val handlerType: Class<H>?
                    get() = handlerType

                override fun getHandler(event: E): H? = handlerGetter?.apply(event)
            }
        }
    }
}

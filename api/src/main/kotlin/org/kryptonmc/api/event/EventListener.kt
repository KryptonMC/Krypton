/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * A listener (handler) for an event type.
 */
public interface EventListener<T : Event> {

    /**
     * The type of events this listener listens for.
     */
    public val eventType: Class<T>

    /**
     * Runs this listener with the given [event], and returns the result.
     *
     * @param event the event to run the listener on
     * @return the result
     */
    public fun run(event: T): Result

    /**
     * The result of running an event listener.
     */
    public enum class Result {

        /**
         * Indicates that the listener ran successfully, without errors or
         * issues.
         */
        SUCCESS,

        /**
         * Indicates that the passed event was invalid for the listener, such
         * as if the event failed to pass one of the filters, or if the event
         * was denied and the listener does not ignore denials.<
         */
        INVALID,

        /**
         * Indicates that the listener has expired, which will happen either
         * when the listener reaches the expire count, if set, or when the
         * expire when condition is met, if set.
         */
        EXPIRED,

        /**
         * Indicates that the listener threw an exception while executing.
         */
        EXCEPTION
    }

    /**
     * A builder for an event listener.
     */
    public interface Builder<T : Event> {

        /**
         * Adds the given [filter] to the list of filters that will be checked
         * when an event is passed to the listener.
         *
         * There can be multiple filters for a listener, and **all** of them
         * must pass for the event to be considered valid and passed on to the
         * listener.
         *
         * @param filter the filter to add
         * @return this builder
         */
        public fun addFilter(filter: Predicate<T>): Builder<T>

        /**
         * Sets whether or not the listener should ignore events that have
         * been denied.
         *
         * @param ignore whether or not to ignore denied events
         * @return this builder
         */
        public fun ignoreDenied(ignore: Boolean): Builder<T>

        /**
         * Sets the number of times the listener can be ran before it is
         * considered expired, and will not run anymore.
         *
         * @param count the amount of times the listener can be ran before it
         * expires
         * @return this builder
         */
        public fun expireCount(count: Int): Builder<T>

        /**
         * Sets the condition that, if met, will cause the listener to be
         * considered expired, and not have it run anymore.
         *
         * @param condition the expiry condition
         * @return this builder
         */
        public fun expireWhen(condition: Predicate<T>): Builder<T>

        /**
         * Sets the handler for the listener.
         *
         * This is what will be called by the event system if the event passes
         * all the set conditions (filters, expiry, etc).
         *
         * @param handler the handler for the listener
         * @return this builder
         */
        public fun handler(handler: Consumer<T>): Builder<T>

        /**
         * Builds the event listener from this builder.
         *
         * @return the built event listener
         */
        public fun build(): EventListener<T>
    }

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun <T : Event> builder(eventType: Class<T>): Builder<T>
    }

    public companion object {

        /**
         * Creates a new event listener builder for events of the
         * given [eventType].
         *
         * @param T the type of event
         * @param eventType the type of events the listener will listen for
         * @return a new builder
         */
        @JvmStatic
        public fun <T : Event> builder(eventType: Class<T>): Builder<T> = Krypton.factory<Factory>().builder(eventType)

        /**
         * Creates a new event listener that listens for events of the
         * given [eventType], running the given [handler] when an event of the
         * correct type is fired.
         *
         * @param T the type of event
         * @param eventType the type of events the listener will listen for
         * @param handler the handler for the listener
         * @return a new event listener
         */
        @JvmStatic
        public fun <T : Event> of(eventType: Class<T>, handler: Consumer<T>): EventListener<T> = builder(eventType).handler(handler).build()
    }
}

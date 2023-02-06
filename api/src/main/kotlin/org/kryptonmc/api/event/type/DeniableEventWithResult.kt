/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.type

/**
 * A type of event that combines the behaviour of [DeniableEvent] and
 * [EventWithResult].
 */
public interface DeniableEventWithResult<T> : DeniableEvent, EventWithResult<T> {

    /**
     * Allows this event to occur, and sets the result to the given [result].
     *
     * This is a shortcut for calling [allow] and setting the [EventWithResult.result]
     * manually.
     *
     * @param result the result to set
     */
    public fun allowWithResult(result: T) {
        allow()
        this.result = result
    }

    /**
     * Denies this event from occur, and sets the result to the given [result].
     *
     * This is a shortcut for calling [deny] and setting the [EventWithResult.result]
     * manually.
     *
     * @param result the result to set
     */
    public fun denyWithResult(result: T) {
        deny()
        this.result = result
    }
}

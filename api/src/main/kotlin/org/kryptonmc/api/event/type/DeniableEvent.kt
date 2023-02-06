/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.type

import org.kryptonmc.api.event.Event

/**
 * A type of event that can be allowed or denied from occurring.
 */
public interface DeniableEvent : Event {

    /**
     * Checks if this event is allowed to happen.
     *
     * This is equivalent to standard event cancellation that may be more
     * familiar to developers, except that it is the other way around.
     * This property indicates whether an event is allowed to occur, rather
     * than if an event is not allowed to occur (cancelled).
     *
     * This will default to true, and can be updated with [allow] and [deny].
     */
    public fun isAllowed(): Boolean

    /**
     * Allows this event to occur.
     *
     * An event is, by default, allowed to occur, and so it is not necessary
     * to call this method to indicate this, unless you wish to allow an event
     * that has been denied.
     */
    public fun allow()

    /**
     * Denies an event from occurring.
     */
    public fun deny()
}

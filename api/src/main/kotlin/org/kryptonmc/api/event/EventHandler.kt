/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/dev/1.1.0/api/src/main/java/com/velocitypowered/api/event/EventHandler.java
 */
package org.kryptonmc.api.event

/**
 * Represents a handler for a given event type [E]. This makes it easier to
 * integrate the event system in to third-party event systems.
 */
public fun interface EventHandler<E> {

    /**
     * The action to take when this event is fired.
     */
    public fun execute(event: E)
}

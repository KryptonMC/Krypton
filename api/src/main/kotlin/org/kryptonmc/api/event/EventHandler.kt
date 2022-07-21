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
 * https://github.com/PaperMC/Velocity/blob/1761755d4dfc16cd020aee90c48761d98552531b/api/src/main/java/com/velocitypowered/api/event/EventHandler.java
 */
package org.kryptonmc.api.event

/**
 * A handler for a given event type [E]. This is designed to make it easier to
 * integrate the event system in to third-party event systems, such as reactor,
 * coroutines, or other event-based systems.
 */
public fun interface EventHandler<E> {

    /**
     * Handles the given [event].
     *
     * @param event the event that was fired
     * @return the task to execute to handle the event
     */
    public fun execute(event: E): EventTask?
}

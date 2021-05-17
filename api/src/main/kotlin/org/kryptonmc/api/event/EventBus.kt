/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event

/**
 * The event bus is used to register and dispatch events
 *
 * This is heavily based off of BungeeCord's EventBus
 */
interface EventBus {

    /**
     * Call an event
     *
     * @param event the event to call
     */
    fun call(event: Any)

    /**
     * Register a new listener
     *
     * @param listener the listener to register
     */
    fun register(listener: Any)
}

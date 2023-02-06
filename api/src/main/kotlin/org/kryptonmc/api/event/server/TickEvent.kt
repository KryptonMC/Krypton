/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.server

import org.kryptonmc.api.event.Event

/**
 * An event that is called when the server starts or ends a tick.
 *
 * These events are called incredibly frequently. On a server with a normal
 * tick speed, these events will be called 20 times per second.
 */
public sealed interface TickEvent : Event {

    /**
     * The number of the tick that has started. This will start from 0, which
     * will be the first tick, and increment by 1 for every completed tick
     * while the server is running.
     *
     * This is NOT a persisted value. It only counts up when the server is
     * running. When the server is restarted, this will reset to 0.
     */
    public val tickNumber: Int
}

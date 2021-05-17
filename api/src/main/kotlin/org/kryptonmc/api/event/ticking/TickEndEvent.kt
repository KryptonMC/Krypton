/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.ticking

import org.kryptonmc.api.event.Event

/**
 * Called when a tick ends.
 *
 * @param tickNumber the tick number
 * @param tickDuration the duration of the tick
 * @param timeEnd the time this tick ended in milliseconds
 */
data class TickEndEvent(
    val tickNumber: Int,
    val tickDuration: Long,
    val timeEnd: Long
) : Event

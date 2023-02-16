/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.server

import org.kryptonmc.api.event.annotation.PerformanceSensitive

/**
 * Called when a tick ends.
 */
@PerformanceSensitive
public interface TickEndEvent : TickEvent {

    /**
     * The approximate duration of the tick, in nanoseconds.
     */
    public val tickDuration: Long

    /**
     * The approximate time when the tick finished processing, in nanoseconds.
     */
    public val endTime: Long
}

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
@Suppress("INAPPLICABLE_JVM_NAME")
@PerformanceSensitive
public interface TickEndEvent : TickEvent {

    /**
     * The estimated duration, in milliseconds, of the tick that ended.
     */
    @get:JvmName("tickDuration")
    public val tickDuration: Long

    /**
     * The estimated time, in milliseconds, when the tick ended.
     */
    @get:JvmName("endTime")
    public val endTime: Long
}

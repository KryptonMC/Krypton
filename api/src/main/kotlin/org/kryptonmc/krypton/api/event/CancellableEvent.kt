/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.krypton.api.event

/**
 * An [Event] that can be explicitly cancelled.
 */
abstract class CancellableEvent : Event {

    /**
     * Whether or not this [Event] has been cancelled
     */
    @Volatile var isCancelled = false

    /**
     * Cancel this [Event]
     */
    fun cancel() {
        isCancelled = true
    }
}

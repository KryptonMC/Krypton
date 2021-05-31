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
 * An [Event] that can be explicitly cancelled.
 */
open class CancellableEvent : Event {

    /**
     * Whether or not this [Event] has been cancelled
     */
    @Volatile
    open var isCancelled = false

    /**
     * Cancel this [Event]
     */
    open fun cancel() {
        isCancelled = true
    }
}

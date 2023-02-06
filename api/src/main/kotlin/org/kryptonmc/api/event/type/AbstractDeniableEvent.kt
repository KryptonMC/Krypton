/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.type

/**
 * A skeletal implementation of [DeniableEvent].
 */
public abstract class AbstractDeniableEvent : DeniableEvent {

    private var allowed = true

    override fun isAllowed(): Boolean = allowed

    override fun allow() {
        allowed = true
    }

    override fun deny() {
        allowed = false
    }
}

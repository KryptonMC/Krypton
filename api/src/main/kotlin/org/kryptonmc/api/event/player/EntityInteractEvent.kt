/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.Entity

/**
 * An interaction event targeting an entity.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public sealed interface EntityInteractEvent : InteractEvent {

    /**
     * The entity that was interacted with.
     */
    @get:JvmName("target")
    public val target: Entity
}

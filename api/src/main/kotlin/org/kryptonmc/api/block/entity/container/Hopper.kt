/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity.container

import org.kryptonmc.api.block.entity.NameableBlockEntity

/**
 * A hopper.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Hopper : ContainerBlockEntity, NameableBlockEntity {

    /**
     * The amount of time, in ticks, before the hopper can transfer another
     * item.
     */
    @get:JvmName("cooldown")
    public val cooldown: Int

    /**
     * Requests this hopper transfers the next item to a connected container.
     */
    public fun transferItem()
}

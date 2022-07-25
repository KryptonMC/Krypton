/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.item.ItemStack

/**
 * A jukebox.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Jukebox : BlockEntity {

    /**
     * The record that this jukebox is currently playing.
     */
    @get:JvmName("record")
    public var record: ItemStack
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

/**
 * A campfire.
 */
public interface Campfire : BlockEntity {

    /**
     * Gets the current progress of the item being cooked in the given [slot].
     *
     * @param slot the slot
     * @return the progress of the item in the slot
     */
    public fun cookingProgress(slot: Int): Int

    /**
     * Gets the amount of time, in ticks, until the item in the given [slot]
     * is cooked.
     *
     * @param slot the slot
     * @return the amount of time until the item in the slot is cooked
     */
    public fun cookingDuration(slot: Int): Int
}

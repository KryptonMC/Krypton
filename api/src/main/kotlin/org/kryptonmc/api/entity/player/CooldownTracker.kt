/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.player

import org.kryptonmc.api.item.ItemType

/**
 * The tracker of item cooldowns for an object.
 */
public interface CooldownTracker {

    /**
     * Checks if the given [item] is currently on cooldown.
     *
     * @param item the item
     * @return true if the item is currently on cooldown, false otherwise
     */
    public fun contains(item: ItemType): Boolean

    /**
     * Gets the current cooldown for the given [item], or returns `-1` if there
     * is no cooldown for the given [item].
     *
     * @param item the item
     * @return the current cooldown for the item, or -1 if not present
     */
    public fun get(item: ItemType): Int

    /**
     * Gets the current cooldown percentage for the given [item].
     *
     * @param item the item
     * @return the percentage
     */
    public fun percentage(item: ItemType): Float

    /**
     * Sets the cooldown for the given [item] to the given amount of [ticks].
     *
     * @param item the item
     * @param ticks the amount of ticks the cooldown will last for
     */
    public fun set(item: ItemType, ticks: Int)

    /**
     * Resets the cooldown for the given [item].
     *
     * @param item the item
     */
    public fun reset(item: ItemType)
}

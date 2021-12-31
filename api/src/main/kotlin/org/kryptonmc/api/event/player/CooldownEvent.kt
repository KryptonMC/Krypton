/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.item.ItemType

/**
 * Called when a cooldown is set on usage of the given [item] for the given
 * [player].
 *
 * @param player the player
 * @param item the item the cooldown is set for
 * @param cooldown the cooldown amount, in ticks
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public data class CooldownEvent(
    @get:JvmName("player") public val player: Player,
    @get:JvmName("item") public val item: ItemType,
    @get:JvmName("cooldown") public val cooldown: Int
) : ResultedEvent<CooldownResult> {

    @get:JvmName("result")
    override var result: CooldownResult = CooldownResult.allowed()
}

/**
 * The result of a [CooldownEvent].
 *
 * @param cooldown the cooldown amount, in ticks
 */
@JvmRecord
public data class CooldownResult(
    override val isAllowed: Boolean,
    public val cooldown: Int
) : ResultedEvent.Result {

    public companion object {

        private val ALLOWED = CooldownResult(true, 0)
        private val DENIED = CooldownResult(false, 0)

        /**
         * Gets the result that allows the cooldown to be set as normal.
         *
         * @return the allowed result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun allowed(): CooldownResult = ALLOWED

        /**
         * Creates a new result that allows the cooldown to be set, but will
         * silently replace the requested cooldown amount with the given
         * [newCooldown], in ticks.
         *
         * Note: Any cooldown value < 0 will be ignored.
         *
         * @param newCooldown the new cooldown amount
         * @return a new allowed result
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun allowed(newCooldown: Int): CooldownResult = CooldownResult(true, newCooldown)

        /**
         * Gets the result that denies the cooldown from being set.
         *
         * @return the denied result
         */
        @JvmStatic
        @Contract(pure = true)
        public fun denied(): CooldownResult = DENIED
    }
}

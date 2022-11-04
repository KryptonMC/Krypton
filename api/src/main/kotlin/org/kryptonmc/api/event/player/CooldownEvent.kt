/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.event.ResultedEvent
import org.kryptonmc.api.item.ItemType
import javax.annotation.concurrent.Immutable

/**
 * Called when a cooldown is set on usage of the given [item] for the given
 * [player].
 */
public interface CooldownEvent : PlayerEvent, ResultedEvent<CooldownEvent.Result> {

    /**
     * The item type that the cooldown is being applied to.
     */
    public val item: ItemType

    /**
     * The time, in ticks, that the cooldown will be in effect for.
     */
    public val cooldown: Int

    /**
     * The result of a [CooldownEvent].
     *
     * @param cooldown the cooldown amount, in ticks
     */
    @JvmRecord
    @Immutable
    public data class Result(override val isAllowed: Boolean, public val cooldown: Int) : ResultedEvent.Result {

        public companion object {

            private val ALLOWED = Result(true, 0)
            private val DENIED = Result(false, 0)

            /**
             * Gets the result that allows the cooldown to be set as normal.
             *
             * @return the allowed result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun allowed(): Result = ALLOWED

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
            public fun allowed(newCooldown: Int): Result = Result(true, newCooldown)

            /**
             * Gets the result that denies the cooldown from being set.
             *
             * @return the denied result
             */
            @JvmStatic
            @Contract(pure = true)
            public fun denied(): Result = DENIED
        }
    }
}

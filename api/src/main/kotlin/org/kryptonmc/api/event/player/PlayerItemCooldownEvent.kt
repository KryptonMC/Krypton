/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.event.type.DeniableEventWithResult
import org.kryptonmc.api.event.type.PlayerEvent
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when a cooldown is set on usage of the given [item] for the given
 * [player].
 */
public interface PlayerItemCooldownEvent : PlayerEvent, DeniableEventWithResult<PlayerItemCooldownEvent.Result> {

    /**
     * The item type that the cooldown is being applied to.
     */
    public val item: ItemType

    /**
     * The time, in ticks, that the cooldown will be in effect for.
     */
    public val cooldown: Int

    /**
     * The result of a cooldown event.
     *
     * This allows plugins to modify the actual cooldown that gets applied to the
     *
     * @property cooldown The resulting cooldown amount, in ticks.
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val cooldown: Int)
}

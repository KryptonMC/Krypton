/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

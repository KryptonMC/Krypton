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
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.event.player.action.KryptonPlayerStartGlidingEvent
import org.kryptonmc.krypton.event.player.action.KryptonPlayerStopGlidingEvent

interface Glider : BaseEntity, Player {

    override var isGliding: Boolean

    // This has vanilla logic in it that we don't want present in the API.
    fun tryStartGliding(): Boolean {
        // TODO: Check for levitation effect
        if (isOnGround || isGliding || isInWater()) return false
        val item = inventory.getArmor(ArmorSlot.CHESTPLATE)
        if (item.type == ItemTypes.ELYTRA && item.meta.damage < item.type.durability - 1) {
            startGliding()
            return true
        }
        return false
    }

    override fun startGliding(): Boolean {
        if (server.eventNode.fire(KryptonPlayerStartGlidingEvent(this)).isAllowed()) {
            isGliding = true
            return true
        }

        // Took this from Spigot. It seems like it's taken from the vanilla thing below, but if we don't have this,
        // it can cause issues like https://hub.spigotmc.org/jira/browse/SPIGOT-5542.
        isGliding = true
        isGliding = false
        return false
    }

    override fun stopGliding(): Boolean {
        if (!server.eventNode.fire(KryptonPlayerStopGlidingEvent(this)).isAllowed()) return false

        // This is a vanilla thing
        isGliding = true
        isGliding = false
        return true
    }
}

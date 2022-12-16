/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.player.PerformActionEvent
import org.kryptonmc.api.event.player.PerformActionEvent.Action
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.event.player.KryptonPerformActionEvent

interface Glider : BaseEntity, Player {

    override var isGliding: Boolean

    // This has vanilla logic in it that we don't want present in the API.
    fun tryStartGliding(): Boolean {
        // TODO: Check for levitation effect
        if (isOnGround || isGliding || isInWater) return false
        val item = inventory.getArmor(ArmorSlot.CHESTPLATE)
        if (item.type == ItemTypes.ELYTRA && item.meta.damage < item.type.durability - 1) {
            startGliding()
            return true
        }
        return false
    }

    override fun startGliding(): Boolean {
        if (fireEvent(Action.START_FLYING_WITH_ELYTRA).result.isAllowed) {
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
        if (!fireEvent(Action.STOP_FLYING_WITH_ELYTRA).result.isAllowed) return false
        // This is a vanilla thing
        isGliding = true
        isGliding = false
        return true
    }

    private fun fireEvent(action: Action): PerformActionEvent = server.eventManager.fireSync(KryptonPerformActionEvent(this, action))
}

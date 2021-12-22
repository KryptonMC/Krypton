/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.entity.player.CooldownTracker
import org.kryptonmc.api.event.player.CooldownEvent
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCooldown
import org.kryptonmc.krypton.util.clamp
import java.util.concurrent.ConcurrentHashMap

class KryptonCooldownTracker(private val player: KryptonPlayer) : CooldownTracker {

    private val cooldowns = ConcurrentHashMap<ItemType, Cooldown>()
    private var tickCount = 0

    fun tick() {
        tickCount++
        if (cooldowns.isEmpty()) return
        val iterator = cooldowns.entries.iterator() // so we can use remove
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value.endTime > tickCount) continue
            iterator.remove()
            onCooldownEnded(entry.key)
        }
    }

    override fun contains(item: ItemType): Boolean = percentage(item) > 0F

    override fun get(item: ItemType): Int {
        val instance = cooldowns[item] ?: return 0
        return instance.endTime - tickCount
    }

    override fun percentage(item: ItemType): Float {
        val instance = cooldowns[item] ?: return 0F
        val totalCooldownTime = (instance.endTime - instance.startTime).toFloat()
        val remainingCooldownTime = (instance.endTime - tickCount).toFloat()
        return (remainingCooldownTime / totalCooldownTime).clamp(0F, 1F)
    }

    override fun set(item: ItemType, ticks: Int) {
        if (ticks < 0) return
        val result = player.server.eventManager.fireSync(CooldownEvent(player, item, ticks)).result
        if (!result.isAllowed) return
        val cooldownAmount = if (result.cooldown > 0) result.cooldown else ticks
        cooldowns[item] = Cooldown(tickCount, cooldownAmount)
        onCooldownStarted(item, ticks)
    }

    override fun reset(item: ItemType) {
        set(item, 0)
    }

    private fun onCooldownStarted(type: ItemType, ticks: Int) {
        player.session.send(PacketOutSetCooldown(type, ticks))
    }

    private fun onCooldownEnded(type: ItemType) {
        player.session.send(PacketOutSetCooldown(type, 0))
    }

    @JvmRecord
    data class Cooldown(val startTime: Int, val endTime: Int)
}

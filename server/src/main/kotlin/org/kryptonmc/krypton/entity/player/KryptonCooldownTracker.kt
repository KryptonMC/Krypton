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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.entity.player.CooldownTracker
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.event.player.KryptonCooldownEvent
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.packet.out.play.PacketOutSetCooldown
import org.kryptonmc.krypton.util.math.Maths
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

    override fun hasCooldown(item: ItemType): Boolean = getCooldownPercentage(item) > 0F

    override fun getCooldown(item: ItemType): Int {
        val instance = cooldowns.get(item) ?: return -1
        return instance.endTime - tickCount
    }

    override fun getCooldownPercentage(item: ItemType): Float {
        val instance = cooldowns.get(item) ?: return 0F
        val totalCooldownTime = (instance.endTime - instance.startTime).toFloat()
        val remainingCooldownTime = (instance.endTime - tickCount).toFloat()
        return Maths.clamp(remainingCooldownTime / totalCooldownTime, 0F, 1F)
    }

    override fun setCooldown(item: ItemType, ticks: Int) {
        if (ticks < 0) return

        val event = player.server.eventNode.fire(KryptonCooldownEvent(player, item, ticks))
        if (!event.isAllowed()) return

        val result = event.result
        cooldowns.put(item, Cooldown(tickCount, result?.cooldown ?: ticks))
        onCooldownStarted(item, ticks)
    }

    override fun resetCooldown(item: ItemType) {
        setCooldown(item, 0)
    }

    private fun onCooldownStarted(type: ItemType, ticks: Int) {
        player.connection.send(PacketOutSetCooldown(type.downcast(), ticks))
    }

    private fun onCooldownEnded(type: ItemType) {
        player.connection.send(PacketOutSetCooldown(type.downcast(), 0))
    }

    @JvmRecord
    data class Cooldown(val startTime: Int, val endTime: Int)
}

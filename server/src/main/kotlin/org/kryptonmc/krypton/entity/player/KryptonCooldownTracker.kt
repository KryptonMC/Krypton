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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.entity.player.CooldownTracker
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.event.player.KryptonPlayerItemCooldownEvent
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

        val event = player.server.eventNode.fire(KryptonPlayerItemCooldownEvent(player, item, ticks))
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

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
package org.kryptonmc.krypton.adventure

import com.google.common.collect.MapMaker
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.entity.components.NetworkPlayer
import org.kryptonmc.krypton.network.PacketGrouping
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar
import java.util.Collections
import java.util.UUID

/**
 * This is based on Velocity's boss bar manager. It's a way to handle boss bars
 * requiring UUIDs, and allows for multiple players to view a boss bar. We need
 * this because Adventure does not provide a way to do this itself.
 */
object BossBarManager : BossBar.Listener {

    private val bars = MapMaker().weakKeys().makeMap<BossBar, BossBarHolder>()

    fun addBar(bar: BossBar, player: NetworkPlayer) {
        val holder = getOrCreate(bar)
        if (holder.subscribers.add(player)) player.connection.send(PacketOutBossBar(holder.id, PacketOutBossBar.AddAction(holder.bar)))
    }

    fun addBar(bar: BossBar, audience: PacketGroupingAudience) {
        val holder = getOrCreate(bar)
        val addedPlayers = audience.players().filter(holder.subscribers::add)
        if (addedPlayers.isNotEmpty()) {
            PacketGrouping.sendGroupedPacket(addedPlayers, PacketOutBossBar(holder.id, PacketOutBossBar.AddAction(holder.bar)))
        }
    }

    fun removeBar(bar: BossBar, player: NetworkPlayer) {
        val holder = bars.get(bar) ?: return
        if (holder.subscribers.remove(player)) player.connection.send(PacketOutBossBar(holder.id, PacketOutBossBar.RemoveAction))
    }

    fun removeBar(bar: BossBar, audience: PacketGroupingAudience) {
        val holder = bars.get(bar) ?: return
        val removedPlayers = audience.players().filter(holder.subscribers::remove)
        if (removedPlayers.isNotEmpty()) {
            PacketGrouping.sendGroupedPacket(removedPlayers, PacketOutBossBar(holder.id, PacketOutBossBar.RemoveAction))
        }
    }

    override fun bossBarNameChanged(bar: BossBar, oldName: Component, newName: Component) {
        update(bar, PacketOutBossBar.UpdateNameAction(newName))
    }

    override fun bossBarProgressChanged(bar: BossBar, oldProgress: Float, newProgress: Float) {
        update(bar, PacketOutBossBar.UpdateProgressAction(newProgress))
    }

    override fun bossBarColorChanged(bar: BossBar, oldColor: BossBar.Color, newColor: BossBar.Color) {
        update(bar, PacketOutBossBar.UpdateStyleAction(newColor, bar.overlay()))
    }

    override fun bossBarOverlayChanged(bar: BossBar, oldOverlay: BossBar.Overlay, newOverlay: BossBar.Overlay) {
        update(bar, PacketOutBossBar.UpdateStyleAction(bar.color(), newOverlay))
    }

    override fun bossBarFlagsChanged(bar: BossBar, flagsAdded: MutableSet<BossBar.Flag>, flagsRemoved: MutableSet<BossBar.Flag>) {
        update(bar, PacketOutBossBar.UpdateFlagsAction(bar.flags()))
    }

    private fun getOrCreate(bar: BossBar): BossBarHolder = bars.computeIfAbsent(bar) { BossBarHolder(it) }.register()

    private fun update(bar: BossBar, action: PacketOutBossBar.Action) {
        val holder = bars.get(bar) ?: return
        val packet = PacketOutBossBar(holder.id, action)
        holder.subscribers.forEach { it.connection.send(packet) }
    }

    private class BossBarHolder(val bar: BossBar) {

        val id: UUID = UUID.randomUUID()
        val subscribers: MutableSet<NetworkPlayer> = Collections.newSetFromMap(MapMaker().weakKeys().makeMap())

        fun register(): BossBarHolder = apply { bar.addListener(BossBarManager) }
    }
}

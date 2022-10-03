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
package org.kryptonmc.krypton.adventure

import com.google.common.collect.MapMaker
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.entity.player.KryptonPlayer
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

    fun addBar(bar: BossBar, player: KryptonPlayer) {
        val holder = getOrCreate(bar)
        if (holder.subscribers.add(player)) player.session.send(PacketOutBossBar(holder.id, PacketOutBossBar.AddAction(holder.bar)))
    }

    fun addBar(bar: BossBar, audience: PacketGroupingAudience) {
        val holder = getOrCreate(bar)
        val addedPlayers = audience.players.filter(holder.subscribers::add)
        if (addedPlayers.isNotEmpty()) {
            audience.sessionManager.sendGrouped(addedPlayers, PacketOutBossBar(holder.id, PacketOutBossBar.AddAction(holder.bar)))
        }
    }

    fun removeBar(bar: BossBar, player: KryptonPlayer) {
        val holder = bars.get(bar) ?: return
        if (holder.subscribers.remove(player)) player.session.send(PacketOutBossBar(holder.id, PacketOutBossBar.RemoveAction))
    }

    fun removeBar(bar: BossBar, audience: PacketGroupingAudience) {
        val holder = bars.get(bar) ?: return
        val addedPlayers = audience.players.filter(holder.subscribers::add)
        if (addedPlayers.isNotEmpty()) {
            audience.sessionManager.sendGrouped(addedPlayers, PacketOutBossBar(holder.id, PacketOutBossBar.RemoveAction))
        }
    }

    override fun bossBarNameChanged(bar: BossBar, oldName: Component, newName: Component) {
        update(bar, PacketOutBossBar.UpdateTitleAction(newName))
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

    private fun getOrCreate(bar: BossBar): BossBarHolder = bars.computeIfAbsent(bar, ::BossBarHolder).register()

    private fun update(bar: BossBar, action: PacketOutBossBar.Action) {
        val holder = bars.get(bar) ?: return
        val packet = PacketOutBossBar(holder.id, action)
        holder.subscribers.forEach { it.session.send(packet) }
    }

    private class BossBarHolder(val bar: BossBar) {

        val id: UUID = UUID.randomUUID()
        val subscribers: MutableSet<KryptonPlayer> = Collections.newSetFromMap(MapMaker().weakKeys().makeMap())

        fun register(): BossBarHolder = apply { bar.addListener(BossBarManager) }
    }
}

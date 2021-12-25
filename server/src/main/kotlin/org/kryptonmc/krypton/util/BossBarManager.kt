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
package org.kryptonmc.krypton.util

import com.google.common.collect.MapMaker
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar
import org.kryptonmc.krypton.packet.out.play.PacketOutBossBar.Action
import java.util.Collections
import java.util.UUID

object BossBarManager : BossBar.Listener {

    private val bars: MutableMap<BossBar, BossBarHolder> = MapMaker().weakKeys().makeMap()

    fun addBar(bar: BossBar, player: KryptonPlayer) {
        val holder = getOrCreate(bar)
        if (holder.subscribers.add(player)) player.session.send(PacketOutBossBar(Action.ADD, holder))
    }

    fun addBar(bar: BossBar, audience: PacketGroupingAudience) {
        val holder = getOrCreate(bar)
        val addedPlayers = audience.players.filter { holder.subscribers.add(it) }
        if (addedPlayers.isNotEmpty()) audience.sessionManager.sendGrouped(addedPlayers, PacketOutBossBar(Action.ADD, holder))
    }

    fun removeBar(bar: BossBar, player: KryptonPlayer) {
        val holder = bars[bar] ?: return
        if (holder.subscribers.remove(player)) player.session.send(PacketOutBossBar(Action.REMOVE, holder))
    }

    fun removeBar(bar: BossBar, audience: PacketGroupingAudience) {
        val holder = bars[bar] ?: return
        val addedPlayers = audience.players.filter { holder.subscribers.add(it) }
        if (addedPlayers.isNotEmpty()) audience.sessionManager.sendGrouped(addedPlayers, PacketOutBossBar(Action.REMOVE, holder))
    }

    override fun bossBarNameChanged(bar: BossBar, oldName: Component, newName: Component) {
        update(bar, Action.UPDATE_TITLE)
    }

    override fun bossBarProgressChanged(bar: BossBar, oldProgress: Float, newProgress: Float) {
        update(bar, Action.UPDATE_HEALTH)
    }

    override fun bossBarColorChanged(bar: BossBar, oldColor: BossBar.Color, newColor: BossBar.Color) {
        update(bar, Action.UPDATE_STYLE)
    }

    override fun bossBarOverlayChanged(bar: BossBar, oldOverlay: BossBar.Overlay, newOverlay: BossBar.Overlay) {
        update(bar, Action.UPDATE_STYLE)
    }

    override fun bossBarFlagsChanged(bar: BossBar, flagsAdded: MutableSet<BossBar.Flag>, flagsRemoved: MutableSet<BossBar.Flag>) {
        update(bar, Action.UPDATE_FLAGS)
    }

    private fun getOrCreate(bar: BossBar): BossBarHolder = bars.getOrPut(bar) { BossBarHolder(bar) }.apply { register() }

    private fun update(bar: BossBar, action: Action) {
        val holder = bars[bar] ?: return
        holder.subscribers.forEach { it.session.send(PacketOutBossBar(action, holder)) }
    }

    class BossBarHolder(val bar: BossBar) {

        val id: UUID = UUID.randomUUID()
        val subscribers: MutableSet<KryptonPlayer> = Collections.newSetFromMap(MapMaker().weakKeys().makeMap())

        fun register() {
            bar.addListener(BossBarManager)
        }
    }
}

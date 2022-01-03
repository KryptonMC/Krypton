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
package org.kryptonmc.krypton.service

import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.service.VanishService
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class KryptonVanishService : VanishService {

    private val hiddenPlayers: Multimap<UUID, UUID> = Multimaps.newSetMultimap(ConcurrentHashMap()) { ConcurrentHashMap.newKeySet() }
    private val vanishedPlayers = ConcurrentHashMap.newKeySet<UUID>()

    override fun isVanished(player: Player): Boolean = vanishedPlayers.contains(player.uuid)

    override fun vanish(player: Player) {
        if (player !is KryptonPlayer || isVanished(player)) return
        vanishedPlayers.add(player.uuid)
        player.world.players.forEach(player::removeViewer)
    }

    override fun unvanish(player: Player) {
        if (player !is KryptonPlayer || !isVanished(player)) return
        vanishedPlayers.remove(player.uuid)
        player.world.players.forEach(player::addViewer)
    }

    override fun show(player: Player, target: Player) {
        if (player === target || player !is KryptonPlayer || target !is KryptonPlayer) return
        if (!hiddenPlayers.containsKey(player.uuid) || !hiddenPlayers[player.uuid].contains(target.uuid)) return
        hiddenPlayers.put(player.uuid, target.uuid)
        target.addViewer(player)
    }

    override fun hide(player: Player, target: Player) {
        if (player === target || player !is KryptonPlayer || target !is KryptonPlayer) return
        if (!hiddenPlayers.containsKey(player.uuid) || hiddenPlayers[player.uuid].contains(target.uuid)) return
        hiddenPlayers.remove(player.uuid, target.uuid)
        target.removeViewer(player)
    }

    override fun canSee(player: Player, target: Player): Boolean {
        if (!hiddenPlayers.containsKey(player.uuid)) return true
        return !hiddenPlayers[player.uuid].contains(target.uuid)
    }
}

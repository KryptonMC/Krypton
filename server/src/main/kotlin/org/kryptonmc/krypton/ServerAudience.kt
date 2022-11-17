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
package org.kryptonmc.krypton

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identified
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import org.kryptonmc.api.Server
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.SessionManager
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.server.PlayerManager
import java.util.Collections

interface ServerAudience : Server, PacketGroupingAudience {

    val sessionManager: SessionManager
    val playerManager: PlayerManager

    override val players: Collection<KryptonPlayer>
        get() = Collections.unmodifiableCollection(playerManager.players)

    override fun audiences(): Iterable<Audience> = players.asSequence().plus(console).asIterable()

    override fun sendMessage(source: Identified, message: Component, type: MessageType) {
        super<PacketGroupingAudience>.sendMessage(source, message, type)
        console.sendMessage(source, message, type)
    }

    override fun sendMessage(source: Identity, message: Component, type: MessageType) {
        super<PacketGroupingAudience>.sendMessage(source, message, type)
        console.sendMessage(source, message, type)
    }

    override fun sendGroupedPacket(players: Collection<KryptonPlayer>, packet: Packet) {
        sessionManager.sendGrouped(players, packet)
    }
}

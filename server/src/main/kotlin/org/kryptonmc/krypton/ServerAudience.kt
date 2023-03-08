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
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.text.Component
import org.kryptonmc.api.Server
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.ConnectionManager
import org.kryptonmc.krypton.server.PlayerManager
import java.util.Collections

interface ServerAudience : Server, PacketGroupingAudience {

    val connectionManager: ConnectionManager
    val playerManager: PlayerManager

    override val players: Collection<KryptonPlayer>
        get() = players()

    override fun players(): Collection<KryptonPlayer> = Collections.unmodifiableCollection(playerManager.players())

    override fun audiences(): Iterable<Audience> = players().asSequence().plus(console).asIterable()

    override fun sendMessage(message: Component) {
        super<PacketGroupingAudience>.sendMessage(message)
        console.sendMessage(message)
    }

    override fun sendMessage(message: Component, boundChatType: ChatType.Bound) {
        super<PacketGroupingAudience>.sendMessage(message, boundChatType)
        console.sendMessage(message, boundChatType)
    }

    override fun sendMessage(signedMessage: SignedMessage, boundChatType: ChatType.Bound) {
        super<PacketGroupingAudience>.sendMessage(signedMessage, boundChatType)
        console.sendMessage(signedMessage, boundChatType)
    }

    override fun deleteMessage(signature: SignedMessage.Signature) {
        super<PacketGroupingAudience>.deleteMessage(signature)
        console.deleteMessage(signature)
    }
}

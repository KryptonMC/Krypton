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

import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.network.SessionManager
import org.kryptonmc.krypton.network.chat.ChatType

interface PlayerPacketGroupingAudience : PacketGroupingAudience<KryptonPlayer> {

    val sessionManager: SessionManager
    val players: Collection<KryptonPlayer>
    override val sender: GroupedPacketSender<KryptonPlayer>
        get() = sessionManager
    override val members: Collection<KryptonPlayer>
        get() = players

    override fun acceptsChatType(member: KryptonPlayer, chatType: ChatType): Boolean = member.acceptsChatType(chatType)
}

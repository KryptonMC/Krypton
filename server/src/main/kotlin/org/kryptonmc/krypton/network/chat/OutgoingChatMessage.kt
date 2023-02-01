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
package org.kryptonmc.krypton.network.chat

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.entity.player.KryptonPlayer

sealed interface OutgoingChatMessage {

    fun content(): Component

    fun sendToPlayer(player: KryptonPlayer, filter: Boolean, type: RichChatType.Bound)

    @JvmRecord
    data class Disguised(private val content: Component) : OutgoingChatMessage {

        override fun content(): Component = content

        override fun sendToPlayer(player: KryptonPlayer, filter: Boolean, type: RichChatType.Bound) {
            player.connection.playHandler().sendDisguisedChatMessage(content, type)
        }
    }

    @JvmRecord
    data class Player(val message: PlayerChatMessage) : OutgoingChatMessage {

        override fun content(): Component = message.decoratedContent()

        override fun sendToPlayer(player: KryptonPlayer, filter: Boolean, type: RichChatType.Bound) {
            val filtered = message.filter(filter)
            if (!filtered.isFullyFiltered()) player.connection.playHandler().sendPlayerChatMessage(filtered, type)
        }
    }

    companion object {

        @JvmStatic
        fun create(message: PlayerChatMessage): OutgoingChatMessage =
            if (message.isSystem()) Disguised(message.decoratedContent()) else Player(message)
    }
}

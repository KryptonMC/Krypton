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

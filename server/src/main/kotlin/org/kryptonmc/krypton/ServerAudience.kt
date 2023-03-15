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
package org.kryptonmc.krypton

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.chat.ChatType
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.text.Component
import org.kryptonmc.api.Server
import org.kryptonmc.krypton.adventure.PacketGroupingAudience
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.server.PlayerManager
import java.util.Collections

interface ServerAudience : Server, PacketGroupingAudience {

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

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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys

object ChatTypes {

    @JvmField
    val CHAT: ResourceKey<RichChatType> = create("chat")
    @JvmField
    val SAY_COMMAND: ResourceKey<RichChatType> = create("say_command")
    @JvmField
    val MESSAGE_COMMAND_INCOMING: ResourceKey<RichChatType> = create("msg_command_incoming")
    @JvmField
    val MESSAGE_COMMAND_OUTGOING: ResourceKey<RichChatType> = create("msg_command_outgoing")
    @JvmField
    val TEAM_MESSAGE_COMMAND_INCOMING: ResourceKey<RichChatType> = create("team_msg_command_incoming")
    @JvmField
    val TEAM_MESSAGE_COMMAND_OUTGOING: ResourceKey<RichChatType> = create("team_msg_command_outgoing")
    @JvmField
    val EMOTE_COMMAND: ResourceKey<RichChatType> = create("emote_command")

    @JvmStatic
    private fun create(key: String): ResourceKey<RichChatType> = KryptonResourceKey.of(KryptonResourceKeys.CHAT_TYPE, Key.key(key))

    @JvmStatic
    fun bootstrap(registry: KryptonRegistry<RichChatType>) {
        val defaultDecoration = ChatTypeDecoration.withSender("chat.type.text")
        val defaultNarration = ChatTypeDecoration.withSender("chat.type.text.narrate")
        KryptonRegistries.register(registry, CHAT, RichChatType(defaultDecoration, defaultNarration))
        KryptonRegistries.register(registry, SAY_COMMAND, RichChatType(ChatTypeDecoration.withSender("chat.type.announcement"), defaultNarration))
        KryptonRegistries.register(registry, MESSAGE_COMMAND_INCOMING,
            RichChatType(ChatTypeDecoration.incomingDirectMessage("commands.message.display.incoming"), defaultNarration))
        KryptonRegistries.register(registry, MESSAGE_COMMAND_OUTGOING,
            RichChatType(ChatTypeDecoration.outgoingDirectMessage("commands.message.display.outgoing"), defaultNarration))
        KryptonRegistries.register(registry, TEAM_MESSAGE_COMMAND_INCOMING,
            RichChatType(ChatTypeDecoration.teamMessage("chat.type.team.text"), defaultNarration))
        KryptonRegistries.register(registry, TEAM_MESSAGE_COMMAND_OUTGOING,
            RichChatType(ChatTypeDecoration.teamMessage("chat.type.team.sent"), defaultNarration))
        KryptonRegistries.register(registry, EMOTE_COMMAND,
            RichChatType(ChatTypeDecoration.withSender("chat.type.emote"), ChatTypeDecoration.withSender("chat.type.emote")))
    }
}

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

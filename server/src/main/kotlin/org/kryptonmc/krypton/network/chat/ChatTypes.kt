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
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.registry.KryptonRegistries

object ChatTypes {

    private val DEFAULT_DECORATION = ChatTypeDecoration.withSender("chat.type.text")
    private val DEFAULT_NARRATION = ChatTypeDecoration.withSender("chat.type.text.narrate")

    @JvmField
    val CHAT: ChatType = register("chat", DEFAULT_DECORATION, DEFAULT_NARRATION)
    @JvmField
    val SAY_COMMAND: ChatType = register("say_command", ChatTypeDecoration.withSender("chat.type.announcement"), DEFAULT_NARRATION)
    @JvmField
    val INCOMING_MESSAGE_COMMAND: ChatType =
        register("msg_command_incoming", ChatTypeDecoration.incomingDirectMessage("commands.message.display.incoming"), DEFAULT_NARRATION)
    @JvmField
    val OUTGOING_MESSAGE_COMMAND: ChatType =
        register("msg_command_outgoing", ChatTypeDecoration.outgoingDirectMessage("commands.message.display.outgoing"), DEFAULT_NARRATION)
    @JvmField
    val INCOMING_TEAM_MESSAGE_COMMAND: ChatType =
        register("team_msg_command_incoming", ChatTypeDecoration.teamMessage("chat.type.team.text"), DEFAULT_NARRATION)
    @JvmField
    val OUTGOING_TEAM_MESSAGE_COMMAND: ChatType =
        register("team_msg_command_outgoing", ChatTypeDecoration.teamMessage("chat.type.team.sent"), DEFAULT_NARRATION)
    @JvmField
    val EMOTE_COMMAND: ChatType =
        register("emote_command", ChatTypeDecoration.withSender("chat.type.emote"), ChatTypeDecoration.withSender("chat.type.emote"))

    @JvmStatic
    private fun register(name: String, chat: ChatTypeDecoration, narration: ChatTypeDecoration): ChatType =
        KryptonRegistries.register(KryptonDynamicRegistries.CHAT_TYPE, Key.key(name), ChatType(chat, narration))
}

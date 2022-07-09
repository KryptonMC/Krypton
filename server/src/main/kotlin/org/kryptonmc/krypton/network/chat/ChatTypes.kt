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
import org.kryptonmc.krypton.network.chat.ChatType.Narration
import org.kryptonmc.krypton.network.chat.ChatType.Narration.Priority
import org.kryptonmc.krypton.network.chat.ChatType.TextDisplay
import org.kryptonmc.krypton.registry.InternalRegistries

object ChatTypes {

    @JvmField
    val CHAT: ChatType = registerChat("chat", ChatDecoration.withSender("chat.type.text"))
    @JvmField
    val SYSTEM: ChatType = register("system", TextDisplay(null), null, Narration(null, Priority.SYSTEM))
    @JvmField
    val GAME_INFO: ChatType = register("game_info", null, TextDisplay(null), null)
    @JvmField
    val SAY_COMMAND: ChatType = registerChat("say_command", ChatDecoration.withSender("chat.type.announcement"))
    @JvmField
    val MESSAGE_COMMAND: ChatType = registerChat("msg_command", ChatDecoration.directMessage("commands.message.display.incoming"))
    @JvmField
    val TEAM_MESSAGE_COMMAND: ChatType = registerChat("team_msg_command", ChatDecoration.teamMessage("chat.type.team.text"))
    @JvmField
    val EMOTE_COMMAND: ChatType = registerChat("emote_command", ChatDecoration.withSender("chat.type.emote"), "chat.type.emote")
    @JvmField
    val TELLRAW_COMMAND: ChatType = register("tellraw_command", TextDisplay(null), null, Narration(null, Priority.CHAT))

    @JvmStatic
    private fun register(name: String, chat: TextDisplay?, overlay: TextDisplay?, narration: Narration?): ChatType {
        val key = Key.key(name)
        return InternalRegistries.CHAT_TYPE.register(key, ChatType(chat, overlay, narration))
    }

    @JvmStatic
    private fun registerChat(name: String, chatDecoration: ChatDecoration, narrationKey: String = "chat.type.text.narrate"): ChatType =
        register(name, TextDisplay(chatDecoration), null, Narration(ChatDecoration.withSender(narrationKey), Priority.CHAT))
}

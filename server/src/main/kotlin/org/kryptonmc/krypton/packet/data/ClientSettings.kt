/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.packet.data

import org.kryptonmc.api.event.play.SkinSettings
import org.kryptonmc.krypton.entity.MainHand

/**
 * Holder for various settings sent by clients. Some of these settings, like [viewDistance],
 * [chatMode], [skinSettings] and [mainHand], we make use of. [locale] and [chatColors] are
 * currently unused, and merely there because the client sends these, and we will make use
 * of them later.
 */
data class ClientSettings(
    val locale: String,
    val viewDistance: Byte,
    val chatMode: ChatMode,
    val chatColors: Boolean,
    val skinSettings: SkinSettings,
    val mainHand: MainHand,
    val disableTextFiltering: Boolean
)

/**
 * The status of the client's chat mode. Enabled indicates that the client is happy to receive any
 * form of media from any source. Commands only indicates that the client only wishes to receive
 * messages from commands and action bars. Hidden indicates the client only wishes to receive action
 * bars.
 */
enum class ChatMode {

    ENABLED,
    COMMANDS_ONLY,
    HIDDEN
}

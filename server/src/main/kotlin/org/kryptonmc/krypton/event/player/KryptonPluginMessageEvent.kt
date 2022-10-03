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
package org.kryptonmc.krypton.event.player

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.player.PluginMessageEvent
import java.util.Objects

@JvmRecord
data class KryptonPluginMessageEvent(override val player: Player, override val channel: Key, override val message: ByteArray) : PluginMessageEvent {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return player == (other as KryptonPluginMessageEvent).player && channel == other.channel && message.contentEquals(other.message)
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + player.hashCode()
        result = 31 * result + channel.hashCode()
        result = 31 * result + message.contentHashCode()
        return result
    }
}

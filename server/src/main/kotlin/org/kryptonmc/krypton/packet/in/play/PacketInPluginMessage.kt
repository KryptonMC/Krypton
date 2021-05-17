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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readAllAvailableBytes
import org.kryptonmc.krypton.util.readString

/**
 * Sent when the client sends a plugin message on the specified [channel]. Currently only triggers
 * the [plugin message event][org.kryptonmc.api.event.events.play.PluginMessageEvent].
 */
class PacketInPluginMessage : PlayPacket(0x0B) {

    /**
     * The channel the plugin message was sent on
     */
    lateinit var channel: Key
        private set

    /**
     * The raw data sent on the channel. Could be a string, could be a number, could just
     * be raw bytes, we don't know, it's context-related.
     */
    lateinit var data: ByteArray
        private set

    override fun read(buf: ByteBuf) {
        channel = Key.key(buf.readString())
        data = buf.readAllAvailableBytes()
    }
}

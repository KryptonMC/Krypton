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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey

/**
 * Designed as a way to allow mods and plugins to send data between client and server. Minecraft itself also
 * uses several plugin channels, of which we use the "minecraft:brand" channel.
 *
 * More information on plugin messaging can be found on Dinnerbone's blog
 * [here](https://dinnerbone.com/blog/2012/01/13/minecraft-plugin-channels-messaging/)
 *
 * @param channelName the name of the channel to send the message on
 * @param content the content of the plugin message
 */
class PacketOutPluginMessage(
    private val channelName: Key,
    private val content: ByteArray
) : PlayPacket(0x18) {

    override fun write(buf: ByteBuf) {
        buf.writeKey(channelName)
        buf.writeBytes(content)
    }
}

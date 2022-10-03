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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readComponent
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeComponent
import org.kryptonmc.krypton.util.writeNullable
import org.kryptonmc.krypton.util.writeString

@JvmRecord
data class PacketOutResourcePack(val uri: String, val hash: String, val forced: Boolean, val prompt: Component?) : Packet {

    constructor(pack: ResourcePack) : this(pack.uri.toString(), pack.hash, pack.isForced, pack.promptMessage)

    constructor(buf: ByteBuf) : this(buf.readString(), buf.readString(), buf.readBoolean(), buf.readNullable(ByteBuf::readComponent))

    override fun write(buf: ByteBuf) {
        buf.writeString(uri)
        buf.writeString(hash)
        buf.writeBoolean(forced)
        buf.writeNullable(prompt, ByteBuf::writeComponent)
    }
}

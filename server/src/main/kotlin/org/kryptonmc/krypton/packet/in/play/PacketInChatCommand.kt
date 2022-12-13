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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.command.argument.ArgumentSignatures
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readInstant
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeInstant
import org.kryptonmc.krypton.util.writeString
import java.time.Instant

@JvmRecord
data class PacketInChatCommand(
    val command: String,
    val timestamp: Instant,
    val argumentSignatures: ArgumentSignatures,
    val signedPreview: Boolean
) : Packet {

    constructor(buf: ByteBuf) : this(buf.readString(), buf.readInstant(), ArgumentSignatures(buf), buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeString(command)
        buf.writeInstant(timestamp)
        argumentSignatures.write(buf)
        buf.writeBoolean(signedPreview)
    }
}

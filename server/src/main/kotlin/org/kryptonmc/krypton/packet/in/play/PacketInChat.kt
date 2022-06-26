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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeString
import java.util.Objects

@JvmRecord
data class PacketInChat(val message: String, val timestamp: Long, val salt: Long, val signature: ByteArray, val signedPreview: Boolean) : Packet {

    constructor(buf: ByteBuf) : this(buf.readString(), buf.readLong(), buf.readLong(), buf.readVarIntByteArray(), buf.readBoolean())

    override fun write(buf: ByteBuf) {
        buf.writeString(message)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return message == (other as PacketInChat).message &&
                timestamp == other.timestamp &&
                salt == other.salt &&
                signature.contentEquals(other.signature) &&
                signedPreview == other.signedPreview
    }

    override fun hashCode(): Int = Objects.hash(message, timestamp, salt, signature, signedPreview)
}

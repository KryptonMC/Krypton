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
package org.kryptonmc.krypton.packet.`in`.login

import io.netty.buffer.ByteBuf
import java.util.Objects
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeVarIntByteArray

@JvmRecord
data class PacketInEncryptionResponse(
    val secret: ByteArray,
    val verifyToken: ByteArray
) : Packet {

    constructor(buf: ByteBuf) : this(buf.readVarIntByteArray(), buf.readVarIntByteArray())

    override fun write(buf: ByteBuf) {
        buf.writeVarIntByteArray(secret)
        buf.writeVarIntByteArray(verifyToken)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return secret.contentEquals((other as PacketInEncryptionResponse).secret) && verifyToken.contentEquals(other.verifyToken)
    }

    override fun hashCode(): Int = Objects.hash(secret, verifyToken)
}

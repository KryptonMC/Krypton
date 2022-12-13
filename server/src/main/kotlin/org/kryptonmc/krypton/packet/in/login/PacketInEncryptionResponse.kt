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
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeVarIntByteArray

@JvmRecord
data class PacketInEncryptionResponse(val secret: ByteArray, val verificationData: VerificationData) : Packet {

    constructor(buf: ByteBuf) : this(buf.readVarIntByteArray(), VerificationData.read(buf))

    override fun write(buf: ByteBuf) {
        buf.writeVarIntByteArray(secret)
        verificationData.write(buf)
    }

    override fun equals(other: Any?): Boolean =
        this === other || other is PacketInEncryptionResponse && secret.contentEquals(other.secret) && verificationData == other.verificationData

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + verificationData.hashCode()
        return result
    }
}

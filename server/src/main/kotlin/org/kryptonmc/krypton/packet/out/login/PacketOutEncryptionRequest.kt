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
package org.kryptonmc.krypton.packet.out.login

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVarIntByteArray
import java.security.PublicKey

/**
 * Sent to instruct the client that we wish to encrypt this connection. The
 * client is provided with our public key, so they can use it to encrypt the
 * shared secret, and a verify token, to attempt to ensure the connection
 * hasn't been tampered with (no hackers listening in).
 */
@JvmRecord
data class PacketOutEncryptionRequest(
    val publicKey: ByteArray,
    val verifyToken: ByteArray
) : Packet {

    override fun write(buf: ByteBuf) {
        buf.writeString("", 20)
        buf.writeVarIntByteArray(publicKey)
        buf.writeVarIntByteArray(verifyToken)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass || !super.equals(other)) return false
        other as PacketOutEncryptionRequest
        return publicKey == other.publicKey && verifyToken.contentEquals(other.verifyToken)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + publicKey.hashCode()
        result = 31 * result + verifyToken.contentHashCode()
        return result
    }
}

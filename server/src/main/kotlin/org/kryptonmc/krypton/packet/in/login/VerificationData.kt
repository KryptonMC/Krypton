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

import com.google.common.primitives.Longs
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.crypto.Encryption
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeVarIntByteArray
import java.util.OptionalLong

@JvmRecord
data class VerificationData(val verifyToken: ByteArray?, val salt: OptionalLong, val signature: ByteArray?) : Writable {

    init {
        require(verifyToken != null && salt.isEmpty && signature == null || verifyToken == null && salt.isPresent && signature != null) {
            "Invalid verification data! Must provide either verify token or both the salt and the signature!"
        }
    }

    constructor(verifyToken: ByteArray) : this(verifyToken, OptionalLong.empty(), null)

    constructor(salt: Long, signature: ByteArray) : this(null, OptionalLong.of(salt), signature)

    fun isTokenValid(expected: ByteArray): Boolean = verifyToken != null && expected.contentEquals(Encryption.decrypt(verifyToken))

    fun isSignatureValid(expected: ByteArray, publicKey: PlayerPublicKey): Boolean {
        if (verifyToken != null) return false
        return publicKey.createSignatureValidator().validate(signature!!) { output ->
            output.update(expected)
            output.update(Longs.toByteArray(salt.orElseThrow()))
        }
    }

    override fun write(buf: ByteBuf) {
        buf.writeBoolean(verifyToken != null)
        if (verifyToken != null) {
            buf.writeVarIntByteArray(verifyToken)
        } else {
            buf.writeLong(salt.orElseThrow())
            buf.writeVarIntByteArray(signature!!)
        }
    }

    override fun equals(other: Any?): Boolean = this === other || other is VerificationData &&
            verifyToken.contentEquals(other.verifyToken) && salt == other.salt && signature.contentEquals(other.signature)

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + verifyToken.contentHashCode()
        result = 31 * result + salt.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    companion object {

        @JvmStatic
        fun read(buf: ByteBuf): VerificationData {
            if (buf.readBoolean()) return VerificationData(buf.readVarIntByteArray())
            return VerificationData(buf.readLong(), buf.readVarIntByteArray())
        }
    }
}

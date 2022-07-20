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
package org.kryptonmc.krypton.entity.player

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.crypto.Encryption
import org.kryptonmc.krypton.util.crypto.InsecurePublicKeyException
import org.kryptonmc.krypton.util.crypto.SignatureValidator
import org.kryptonmc.krypton.util.crypto.toRSAString
import org.kryptonmc.krypton.util.readInstant
import org.kryptonmc.krypton.util.readPublicKey
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeInstant
import org.kryptonmc.krypton.util.writeVarIntByteArray
import java.security.PublicKey
import java.time.Instant
import java.util.Objects

@JvmRecord
data class PlayerPublicKey(val data: Data) {

    fun createSignatureValidator(): SignatureValidator = SignatureValidator.from(data.key, Encryption.SIGNATURE_ALGORITHM)

    @JvmRecord
    data class Data(val expiryTime: Instant, val key: PublicKey, val signature: ByteArray) : Writable {

        constructor(buf: ByteBuf) : this(buf.readInstant(), buf.readPublicKey(), buf.readVarIntByteArray())

        fun hasExpired(): Boolean = expiryTime.isBefore(Instant.now())

        fun validateSignature(validator: SignatureValidator): Boolean =
            validator.validate(createSignedPayload().toByteArray(Charsets.US_ASCII), signature)

        private fun createSignedPayload(): String = expiryTime.toEpochMilli().toString() + key.toRSAString()

        override fun write(buf: ByteBuf) {
            buf.writeInstant(expiryTime)
            buf.writeVarIntByteArray(key.encoded)
            buf.writeVarIntByteArray(signature)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return expiryTime == (other as Data).expiryTime && key == other.key && signature.contentEquals(other.signature)
        }

        override fun hashCode(): Int = Objects.hash(expiryTime, key, signature)
    }

    companion object {

        @JvmStatic
        fun create(validator: SignatureValidator, data: Data): PlayerPublicKey {
            if (data.hasExpired()) throw InsecurePublicKeyException.Invalid("Player public key has expired!")
            if (!data.validateSignature(validator)) throw InsecurePublicKeyException.Invalid("Invalid signature for player public key!")
            return PlayerPublicKey(data)
        }
    }
}

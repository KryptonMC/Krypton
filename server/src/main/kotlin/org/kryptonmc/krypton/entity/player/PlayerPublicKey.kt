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
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.ComponentException
import org.kryptonmc.krypton.util.crypto.Encryption
import org.kryptonmc.krypton.util.crypto.SignatureValidator
import org.kryptonmc.krypton.util.readInstant
import org.kryptonmc.krypton.util.readPublicKey
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeInstant
import org.kryptonmc.krypton.util.writePublicKey
import org.kryptonmc.krypton.util.writeVarIntByteArray
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.PublicKey
import java.time.Duration
import java.time.Instant
import java.util.UUID

@JvmRecord
data class PlayerPublicKey(val data: Data) {

    fun createSignatureValidator(): SignatureValidator = SignatureValidator.from(data.key, Encryption.SIGNATURE_ALGORITHM)

    @JvmRecord
    @Suppress("ArrayInDataClass", "EqualsOrHashCode") // We want to keep the record-generated hash code.
    data class Data(val expiryTime: Instant, val key: PublicKey, val keySignature: ByteArray) : Writable {

        constructor(buf: ByteBuf) : this(buf.readInstant(), buf.readPublicKey(), buf.readVarIntByteArray(MAX_KEY_SIGNATURE_SIZE))

        override fun write(buf: ByteBuf) {
            buf.writeInstant(expiryTime)
            buf.writePublicKey(key)
            buf.writeVarIntByteArray(keySignature)
        }

        fun validateSignature(validator: SignatureValidator, sessionId: UUID): Boolean = validator.validate(signedPayload(sessionId), keySignature)

        private fun signedPayload(sessionId: UUID): ByteArray {
            val encodedKey = key.encoded
            val result = ByteArray(24 + encodedKey.size) // 16 bytes for UUID (all UUIDs are 128 bits) + 8 bytes for expiry time millis as long
            val buffer = ByteBuffer.wrap(result).order(ByteOrder.BIG_ENDIAN)
            buffer.putLong(sessionId.mostSignificantBits).putLong(sessionId.leastSignificantBits).putLong(expiryTime.toEpochMilli()).put(encodedKey)
            return result
        }

        fun hasExpired(): Boolean = expiryTime.isBefore(Instant.now())

        fun hasExpired(duration: Duration): Boolean = expiryTime.plus(duration).isBefore(Instant.now())

        override fun equals(other: Any?): Boolean {
            return other is Data && expiryTime == other.expiryTime && key == other.key && keySignature.contentEquals(other.keySignature)
        }

        companion object {

            private const val MAX_KEY_SIGNATURE_SIZE = 4096
        }
    }

    class ValidationException(message: Component) : ComponentException(message)

    companion object {

        @JvmField
        val EXPIRED_KEY: Component = Component.translatable("multiplayer.disconnect.expired_public_key")
        private val INVALID_SIGNATURE = Component.translatable("multiplayer.disconnect.invalid_public_key_signature")

        @JvmStatic
        fun createValidated(validator: SignatureValidator, sessionId: UUID, data: Data, duration: Duration): PlayerPublicKey {
            if (data.hasExpired(duration)) throw ValidationException(EXPIRED_KEY)
            if (!data.validateSignature(validator, sessionId)) throw ValidationException(INVALID_SIGNATURE)
            return PlayerPublicKey(data)
        }
    }
}

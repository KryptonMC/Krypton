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
package org.kryptonmc.krypton.network.chat

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.crypto.SignatureUpdater
import org.kryptonmc.krypton.util.crypto.SignatureValidator
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt
import java.nio.ByteBuffer
import java.util.Base64

@JvmRecord
data class MessageSignature(val bytes: ByteArray) {

    init {
        check(bytes.size == BYTES) { "Invalid message signature size! Expected $BYTES size, got ${bytes.size}!" }
    }

    fun verify(validator: SignatureValidator, updater: SignatureUpdater): Boolean = validator.validate(bytes, updater)

    fun asByteBuffer(): ByteBuffer? = ByteBuffer.wrap(bytes)

    fun pack(signatureCache: MessageSignatureCache): Packed {
        val packedId = signatureCache.pack(this)
        return if (packedId != Packed.FULL_SIGNATURE_ID) Packed(packedId) else Packed(this)
    }

    override fun equals(other: Any?): Boolean = this === other || other is MessageSignature && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int = bytes.contentHashCode()

    override fun toString(): String = Base64.getEncoder().encodeToString(bytes)

    @JvmRecord
    data class Packed(val id: Int, val fullSignature: MessageSignature?) : Writable {

        constructor(fullSignature: MessageSignature) : this(FULL_SIGNATURE_ID, fullSignature)

        constructor(id: Int) : this(id, null)

        override fun write(buf: ByteBuf) {
            buf.writeVarInt(id + 1)
            fullSignature?.let { write(buf, it) }
        }

        companion object {

            const val FULL_SIGNATURE_ID: Int = -1

            @JvmStatic
            fun read(buf: ByteBuf): Packed {
                val id = buf.readVarInt() - 1
                return if (id == FULL_SIGNATURE_ID) Packed(MessageSignature.read(buf)) else Packed(id)
            }
        }
    }

    companion object {

        private const val BYTES = 256

        @JvmStatic
        fun read(buf: ByteBuf): MessageSignature {
            val bytes = ByteArray(BYTES)
            buf.readBytes(bytes)
            return MessageSignature(bytes)
        }

        @JvmStatic
        fun write(buf: ByteBuf, signature: MessageSignature) {
            buf.writeBytes(signature.bytes)
        }
    }
}

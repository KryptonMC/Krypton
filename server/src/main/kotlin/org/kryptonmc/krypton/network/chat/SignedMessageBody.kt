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

import com.google.common.primitives.Ints
import com.google.common.primitives.Longs
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.crypto.SignatureUpdater
import org.kryptonmc.krypton.util.readInstant
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.writeInstant
import org.kryptonmc.krypton.util.writeString
import java.time.Instant

@JvmRecord
data class SignedMessageBody(val content: String, val timestamp: Instant, val salt: Long, val lastSeen: LastSeenMessages) {

    fun updateSignature(output: SignatureUpdater.Output) {
        output.update(Longs.toByteArray(salt))
        output.update(Longs.toByteArray(timestamp.epochSecond))
        val contentBytes = content.encodeToByteArray()
        output.update(Ints.toByteArray(contentBytes.size))
        output.update(contentBytes)
        lastSeen.updateSignature(output)
    }

    fun pack(signatureCache: MessageSignatureCache): Packed = Packed(content, timestamp, salt, lastSeen.pack(signatureCache))

    @JvmRecord
    data class Packed(val content: String, val timestamp: Instant, val salt: Long, val lastSeen: LastSeenMessages.Packed) : Writable {

        constructor(buf: ByteBuf) : this(buf.readString(MAX_MESSAGE_LENGTH), buf.readInstant(), buf.readLong(), LastSeenMessages.Packed(buf))

        override fun write(buf: ByteBuf) {
            buf.writeString(content, MAX_MESSAGE_LENGTH)
            buf.writeInstant(timestamp)
            buf.writeLong(salt)
            lastSeen.write(buf)
        }
    }

    companion object {

        private const val MAX_MESSAGE_LENGTH = 256

        @JvmStatic
        fun unsigned(message: String): SignedMessageBody = SignedMessageBody(message, Instant.now(), 0L, LastSeenMessages.EMPTY)
    }
}

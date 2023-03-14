/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

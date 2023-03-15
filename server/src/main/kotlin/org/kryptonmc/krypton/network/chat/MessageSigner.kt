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

import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.util.crypto.Crypto
import org.kryptonmc.krypton.util.uuid.UUIDUtil
import java.time.Instant
import java.util.UUID

@JvmRecord
data class MessageSigner(val profileId: UUID, val timestamp: Instant, val salt: Long) : Writable {

    constructor(reader: BinaryReader) : this(reader.readUUID(), reader.readInstant(), reader.readLong())

    override fun write(writer: BinaryWriter) {
        writer.writeUUID(profileId)
        writer.writeInstant(timestamp)
        writer.writeLong(salt)
    }

    fun isSystem(): Boolean = profileId == UUIDUtil.NIL_UUID

    companion object {

        @JvmStatic
        fun create(profileId: UUID): MessageSigner = MessageSigner(profileId, Instant.now(), Crypto.SaltSupplier.getLong())

        @JvmStatic
        fun system(): MessageSigner = create(UUIDUtil.NIL_UUID)
    }
}

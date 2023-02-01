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
import org.kryptonmc.krypton.util.crypto.Crypto
import org.kryptonmc.krypton.util.readInstant
import org.kryptonmc.krypton.util.readUUID
import org.kryptonmc.krypton.util.uuid.UUIDUtil
import org.kryptonmc.krypton.util.writeInstant
import org.kryptonmc.krypton.util.writeUUID
import java.time.Instant
import java.util.UUID

@JvmRecord
data class MessageSigner(val profileId: UUID, val timestamp: Instant, val salt: Long) : Writable {

    constructor(buf: ByteBuf) : this(buf.readUUID(), buf.readInstant(), buf.readLong())

    override fun write(buf: ByteBuf) {
        buf.writeUUID(profileId)
        buf.writeInstant(timestamp)
        buf.writeLong(salt)
    }

    fun isSystem(): Boolean = profileId == UUIDUtil.NIL_UUID

    companion object {

        @JvmStatic
        fun create(profileId: UUID): MessageSigner = MessageSigner(profileId, Instant.now(), Crypto.SaltSupplier.getLong())

        @JvmStatic
        fun system(): MessageSigner = create(UUIDUtil.NIL_UUID)
    }
}

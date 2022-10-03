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
import it.unimi.dsi.fastutil.bytes.ByteArrays
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.adventure.toStableJson
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.crypto.SignatureUpdater
import org.kryptonmc.krypton.util.readInstant
import org.kryptonmc.krypton.util.readVarIntByteArray
import org.kryptonmc.krypton.util.writeInstant
import org.kryptonmc.krypton.util.writeVarIntByteArray
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.Instant
import java.util.UUID

@JvmRecord
data class MessageSignature(val timestamp: Instant, val salt: Long, val signature: ByteArray) : Writable {

    constructor(buf: ByteBuf) : this(buf.readInstant(), buf.readLong(), buf.readVarIntByteArray())

    override fun write(buf: ByteBuf) {
        buf.writeInstant(timestamp)
        buf.writeLong(salt)
        buf.writeVarIntByteArray(signature)
    }

    fun verify(publicKey: PlayerPublicKey?, message: Component, uuid: UUID): Boolean {
        if (publicKey == null) return true
        if (signature.isEmpty()) return false
        return publicKey.createSignatureValidator().validate(signature) { updateSignature(it, message, uuid, timestamp, salt) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return timestamp == (other as MessageSignature).timestamp && salt == other.salt && signature.contentEquals(other.signature)
    }

    override fun hashCode(): Int {
        var result = 1
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + salt.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    companion object {

        @JvmStatic
        fun unsigned(): MessageSignature = MessageSignature(Instant.now(), 0, ByteArrays.EMPTY_ARRAY)

        @JvmStatic
        private fun updateSignature(output: SignatureUpdater.Output, message: Component, uuid: UUID, timestamp: Instant, salt: Long) {
            val bytes = ByteArray(32)
            val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN)
            buffer.putLong(salt)
            buffer.putLong(uuid.mostSignificantBits).putLong(uuid.leastSignificantBits)
            buffer.putLong(timestamp.epochSecond)
            output.update(bytes)
            output.update(message.toStableJson().toByteArray())
        }
    }
}

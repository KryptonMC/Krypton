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
import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.crypto.SignatureUpdater
import org.kryptonmc.krypton.util.readCollection
import org.kryptonmc.krypton.util.readFixedBitSet
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeCollection
import org.kryptonmc.krypton.util.writeFixedBitSet
import org.kryptonmc.krypton.util.writeVarInt
import java.util.BitSet

@JvmRecord
data class LastSeenMessages(val entries: List<MessageSignature>) {

    fun updateSignature(output: SignatureUpdater.Output) {
        output.update(Ints.toByteArray(entries.size))
        entries.forEach { output.update(it.bytes) }
    }

    fun pack(signatureCache: MessageSignatureCache): Packed = Packed(entries.map { it.pack(signatureCache) })

    @JvmRecord
    data class Packed(val entries: List<MessageSignature.Packed>) : Writable {

        constructor(buf: ByteBuf) : this(buf.readCollection(ByteBufExtras.limitValue(::ArrayList, 20), MessageSignature.Packed::read))

        override fun write(buf: ByteBuf) {
            buf.writeCollection(entries) { it.write(buf) }
        }

        companion object {

            @JvmField
            val EMPTY: Packed = Packed(emptyList())
        }
    }

    @JvmRecord
    data class Update(val offset: Int, val acknowledged: BitSet) : Writable {

        constructor(buf: ByteBuf) : this(buf.readVarInt(), buf.readFixedBitSet(LAST_SEEN_MESSAGE_MAX_LENGTH))

        override fun write(buf: ByteBuf) {
            buf.writeVarInt(offset)
            buf.writeFixedBitSet(acknowledged, LAST_SEEN_MESSAGE_MAX_LENGTH)
        }
    }

    companion object {

        @JvmField
        val EMPTY: LastSeenMessages = LastSeenMessages(emptyList())
        private const val LAST_SEEN_MESSAGE_MAX_LENGTH = 20
    }
}

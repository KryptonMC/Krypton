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
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.crypto.SignatureUpdater
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

        constructor(reader: BinaryReader) : this(reader.readCollection(ByteBufExtras.limitValue(::ArrayList, 20), MessageSignature.Packed::read))

        override fun write(writer: BinaryWriter) {
            writer.writeCollection(entries) { it.write(writer) }
        }

        companion object {

            @JvmField
            val EMPTY: Packed = Packed(emptyList())
        }
    }

    @JvmRecord
    data class Update(val offset: Int, val acknowledged: BitSet) : Writable {

        constructor(reader: BinaryReader) : this(reader.readVarInt(), reader.readFixedBitSet(LAST_SEEN_MESSAGE_MAX_LENGTH))

        override fun write(writer: BinaryWriter) {
            writer.writeVarInt(offset)
            writer.writeFixedBitSet(acknowledged, LAST_SEEN_MESSAGE_MAX_LENGTH)
        }
    }

    companion object {

        @JvmField
        val EMPTY: LastSeenMessages = LastSeenMessages(emptyList())
        private const val LAST_SEEN_MESSAGE_MAX_LENGTH = 20
    }
}

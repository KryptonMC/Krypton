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
package org.kryptonmc.krypton.packet.out.play

import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet

@JvmRecord
@Suppress("ArrayInDataClass")
data class PacketOutRemoveEntities(val ids: IntArray) : Packet {

    constructor(reader: BinaryReader) : this(reader.readVarIntArray())

    override fun write(writer: BinaryWriter) {
        writer.writeVarIntArray(ids)
    }

    companion object {

        @JvmStatic
        fun removeSingle(entity: KryptonEntity): PacketOutRemoveEntities = PacketOutRemoveEntities(intArrayOf(entity.id))
    }
}

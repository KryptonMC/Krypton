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

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.metadata.MetadataSerializer
import org.kryptonmc.krypton.entity.metadata.MetadataSerializers
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.EntityPacket

/**
 * The way we construct and use metadata in Krypton is a bit strange, as unlike vanilla, we do not store a
 * reference to this data from within the entities, it is constructed manually when a player joins.
 *
 * This packet informs the client of all the metadata it should assign to the specified [entityId]
 *
 * @param entityId the ID of the entity to set metadata for
 * @param packedEntries the list of packed metadata items to send
 */
@JvmRecord
data class PacketOutSetEntityMetadata(override val entityId: Int, val packedEntries: Collection<MetadataHolder.Entry<*>>?) : EntityPacket {

    constructor(reader: BinaryReader) : this(reader.readVarInt(), readEntries(reader))

    override fun write(writer: BinaryWriter) {
        writer.writeVarInt(entityId)
        writeEntries(writer, packedEntries)
    }

    companion object {

        private const val EOF_MARKER: Byte = 0xFF.toByte()

        @JvmStatic
        private fun writeEntries(writer: BinaryWriter, entries: Collection<MetadataHolder.Entry<*>>?) {
            entries?.forEach { writeEntry(writer, it) }
            writer.writeByte(EOF_MARKER)
        }

        @JvmStatic
        private fun <T> writeEntry(writer: BinaryWriter, entry: MetadataHolder.Entry<T>) {
            val key = entry.key
            writer.writeByte(key.id)
            writer.writeVarInt(MetadataSerializers.getId(key.serializer))
            key.serializer.write(writer, entry.value)
        }

        @JvmStatic
        private fun readEntries(reader: BinaryReader): List<MetadataHolder.Entry<*>>? {
            var entries: PersistentList.Builder<MetadataHolder.Entry<*>>? = null
            var index = reader.readByte()
            while (index != EOF_MARKER) {
                if (entries == null) entries = persistentListOf<MetadataHolder.Entry<*>>().builder()
                val type = reader.readVarInt()
                val serializer = MetadataSerializers.getById(type) ?: error("Unknown serializer type $type!")
                entries.add(createEntry(reader, index, serializer))
                index = reader.readByte()
            }
            return entries?.build()
        }

        @JvmStatic
        private fun <T> createEntry(reader: BinaryReader, id: Byte, serializer: MetadataSerializer<T>): MetadataHolder.Entry<T> {
            return MetadataHolder.Entry(serializer.createKey(id), serializer.read(reader))
        }
    }
}

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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.DecoderException
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.metadata.MetadataSerializer
import org.kryptonmc.krypton.entity.metadata.MetadataSerializers
import org.kryptonmc.krypton.packet.EntityPacket
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeVarInt

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

    constructor(buf: ByteBuf) : this(buf.readVarInt(), readEntries(buf))

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        writeEntries(buf, packedEntries)
    }

    companion object {

        private const val EOF_MARKER = 255

        @JvmStatic
        private fun writeEntries(buf: ByteBuf, entries: Collection<MetadataHolder.Entry<*>>?) {
            entries?.forEach { writeEntry(buf, it) }
            buf.writeByte(EOF_MARKER)
        }

        @JvmStatic
        private fun <T> writeEntry(buf: ByteBuf, entry: MetadataHolder.Entry<T>) {
            val key = entry.key
            buf.writeByte(key.id)
            buf.writeVarInt(MetadataSerializers.idOf(key.serializer))
            key.serializer.write(buf, entry.value)
        }

        @JvmStatic
        private fun readEntries(buf: ByteBuf): List<MetadataHolder.Entry<*>>? {
            var entries: PersistentList.Builder<MetadataHolder.Entry<*>>? = null
            var index = buf.readUnsignedByte().toInt()
            while (index != 255) {
                if (entries == null) entries = persistentListOf<MetadataHolder.Entry<*>>().builder()
                val type = buf.readVarInt()
                val serializer = MetadataSerializers.get(type) ?: throw DecoderException("Unknown serializer type $type!")
                entries.add(createEntry(buf, index, serializer))
                index = buf.readUnsignedByte().toInt()
            }
            return entries?.build()
        }

        @JvmStatic
        private fun <T> createEntry(buf: ByteBuf, id: Int, serializer: MetadataSerializer<T>): MetadataHolder.Entry<T> =
            MetadataHolder.Entry(serializer.createKey(id), serializer.read(buf))
    }
}

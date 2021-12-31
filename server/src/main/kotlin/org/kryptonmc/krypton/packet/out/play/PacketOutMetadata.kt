/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.packet.EntityPacket
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
data class PacketOutMetadata(
    override val entityId: Int,
    val packedEntries: Sequence<MetadataHolder.Entry<*>>
) : EntityPacket {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        writeEntries(buf, packedEntries)
    }

    companion object {

        private const val EOF_MARKER = 255

        @JvmStatic
        private fun writeEntries(buf: ByteBuf, entries: Sequence<MetadataHolder.Entry<*>>) {
            entries.forEach { writeEntry(buf, it) }
            buf.writeByte(EOF_MARKER)
        }

        @JvmStatic
        private fun <T> writeEntry(buf: ByteBuf, entry: MetadataHolder.Entry<T>) {
            val key = entry.key
            buf.writeByte(key.id)
            buf.writeVarInt(key.serializer.id)
            key.serializer.write(buf, entry.value)
        }
    }
}

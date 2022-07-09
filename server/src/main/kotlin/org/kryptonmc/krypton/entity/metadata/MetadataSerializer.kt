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
package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.krypton.util.ByteBufReader
import org.kryptonmc.krypton.util.ByteBufWriter
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeNullable

@CataloguedBy(MetadataSerializers::class)
interface MetadataSerializer<T> {

    fun write(buf: ByteBuf, item: T)

    fun read(buf: ByteBuf): T

    fun createKey(id: Int): MetadataKey<T> = MetadataKey(id, this)

    companion object {

        @JvmStatic
        fun <T> simple(writer: ByteBufWriter<T>, reader: ByteBufReader<T>): MetadataSerializer<T> = object : MetadataSerializer<T> {

            override fun write(buf: ByteBuf, item: T) {
                writer.write(buf, item)
            }

            override fun read(buf: ByteBuf): T = reader.read(buf)
        }

        @JvmStatic
        fun <T> optional(writer: ByteBufWriter<T>, reader: ByteBufReader<T>): MetadataSerializer<T?> =
            simple({ buf, value -> buf.writeNullable(value, writer) }, { it.readNullable(reader) })

        @JvmStatic
        fun <E : Enum<E>> simpleEnum(type: Class<E>): MetadataSerializer<E> = simple(ByteBuf::writeEnum) { type.enumConstants[it.readVarInt()] }

        @JvmStatic
        fun <T : Any> simpleId(registry: Registry<T>): MetadataSerializer<T> =
            simple({ buf, value -> buf.writeId(registry, value) }, { it.readById(registry)!! })
    }
}

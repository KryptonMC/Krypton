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
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.readById
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.writeEnum
import org.kryptonmc.krypton.util.writeId
import org.kryptonmc.krypton.util.writeNullable

@CataloguedBy(MetadataSerializers::class)
interface MetadataSerializer<T> {

    fun read(buf: ByteBuf): T

    fun write(buf: ByteBuf, item: T)

    fun createKey(id: Int): MetadataKey<T> = MetadataKey(id, this)

    companion object {

        @JvmStatic
        inline fun <T> simple(crossinline reader: (ByteBuf) -> T,
                              crossinline writer: (ByteBuf, T) -> Unit): MetadataSerializer<T> = object : MetadataSerializer<T> {

            override fun read(buf: ByteBuf): T = reader(buf)

            override fun write(buf: ByteBuf, item: T) {
                writer(buf, item)
            }
        }

        @JvmStatic
        inline fun <T> nullable(crossinline reader: (ByteBuf) -> T, crossinline writer: (ByteBuf, T) -> Unit): MetadataSerializer<T?> =
            simple({ it.readNullable(reader) }, { buf, value -> buf.writeNullable(value, writer) })

        @JvmStatic
        inline fun <reified E : Enum<E>> simpleEnum(): MetadataSerializer<E> = simple(ByteBuf::readEnum, ByteBuf::writeEnum)

        @JvmStatic
        fun <T : Any> simpleId(registry: KryptonRegistry<T>): MetadataSerializer<T> =
            simple({ it.readById(registry)!! }, { buf, value -> buf.writeId(registry, value) })
    }
}

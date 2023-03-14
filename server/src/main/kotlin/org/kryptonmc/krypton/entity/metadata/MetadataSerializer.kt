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

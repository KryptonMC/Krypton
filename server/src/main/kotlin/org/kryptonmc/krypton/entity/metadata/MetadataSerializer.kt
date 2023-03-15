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

import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.registry.KryptonRegistry

@CataloguedBy(MetadataSerializers::class)
interface MetadataSerializer<T> {

    fun read(reader: BinaryReader): T

    fun write(writer: BinaryWriter, item: T)

    fun createKey(id: Byte): MetadataKey<T> = MetadataKey(id, this)

    companion object {

        @JvmStatic
        inline fun <T> simple(crossinline readerFunction: (BinaryReader) -> T,
                              crossinline writerFunction: (BinaryWriter, T) -> Unit): MetadataSerializer<T> = object : MetadataSerializer<T> {

            override fun read(reader: BinaryReader): T = readerFunction(reader)

            override fun write(writer: BinaryWriter, item: T) {
                writerFunction(writer, item)
            }
        }

        @JvmStatic
        inline fun <T> nullable(crossinline reader: (BinaryReader) -> T, crossinline writer: (BinaryWriter, T) -> Unit): MetadataSerializer<T?> =
            simple({ it.readNullable(reader) }, { buf, value -> buf.writeNullable(value, writer) })

        @JvmStatic
        inline fun <reified E : Enum<E>> simpleEnum(): MetadataSerializer<E> = simple(BinaryReader::readEnum, BinaryWriter::writeEnum)

        @JvmStatic
        fun <T : Any> simpleId(registry: KryptonRegistry<T>): MetadataSerializer<T> {
            return simple({ it.readById(registry)!! }, { buf, value -> buf.writeId(registry, value) })
        }
    }
}

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

import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.serialization.Codec
import java.util.BitSet
import java.util.function.Supplier

class FilterMask private constructor(private val mask: BitSet, private val type: Type) : Writable {

    constructor(bits: Int) : this(BitSet(bits), Type.PARTIALLY_FILTERED)

    fun setFiltered(index: Int) {
        mask.set(index)
    }

    fun apply(text: String): String? = when (type) {
        Type.PASS_THROUGH -> text
        Type.FULLY_FILTERED -> null
        Type.PARTIALLY_FILTERED -> {
            val result = text.toCharArray()
            var i = 0
            while (i < result.size && i < mask.length()) {
                if (mask.get(i)) result[i] = HASH
                ++i
            }
            String(result)
        }
    }

    fun isEmpty(): Boolean = type == Type.PASS_THROUGH

    fun isFullyFiltered(): Boolean = type == Type.FULLY_FILTERED

    override fun write(writer: BinaryWriter) {
        writer.writeEnum(type)
        if (type == Type.PARTIALLY_FILTERED) writer.writeBitSet(mask)
    }

    override fun equals(other: Any?): Boolean = this === other || other is FilterMask && mask == other.mask && type == other.type

    override fun hashCode(): Int = 31 * mask.hashCode() + type.hashCode()

    enum class Type(private val codec: Supplier<Codec<FilterMask>>) {

        PASS_THROUGH({ PASS_THROUGH_CODEC }),
        FULLY_FILTERED({ FULLY_FILTERED_CODEC }),
        PARTIALLY_FILTERED({ PARTIALLY_FILTERED_CODEC });

        private fun codec(): Codec<FilterMask> = codec.get()
    }

    companion object {

        private const val HASH = '#'

        @JvmField
        val FULLY_FILTERED: FilterMask = FilterMask(BitSet(0), Type.FULLY_FILTERED)
        @JvmField
        val PASS_THROUGH: FilterMask = FilterMask(BitSet(0), Type.PASS_THROUGH)

        private val PASS_THROUGH_CODEC = Codec.unit(PASS_THROUGH)
        private val FULLY_FILTERED_CODEC = Codec.unit(FULLY_FILTERED)
        private val PARTIALLY_FILTERED_CODEC = Codecs.BIT_SET.xmap({ FilterMask(it, Type.PARTIALLY_FILTERED) }, { it.mask })

        @JvmStatic
        fun read(reader: BinaryReader): FilterMask = when (val type = reader.readEnum<Type>()) {
            Type.PASS_THROUGH -> PASS_THROUGH
            Type.FULLY_FILTERED -> FULLY_FILTERED
            Type.PARTIALLY_FILTERED -> FilterMask(reader.readBitSet(), type)
        }

        @JvmStatic
        fun write(writer: BinaryWriter, mask: FilterMask) {
            writer.writeEnum(mask.type)
            if (mask.type == Type.PARTIALLY_FILTERED) writer.writeBitSet(mask.mask)
        }
    }
}

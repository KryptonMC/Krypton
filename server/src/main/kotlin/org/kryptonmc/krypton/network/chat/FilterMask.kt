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
package org.kryptonmc.krypton.network.chat

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.network.Writable
import org.kryptonmc.krypton.util.readBitSet
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.serialization.Codecs
import org.kryptonmc.krypton.util.writeBitSet
import org.kryptonmc.krypton.util.writeEnum
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

    override fun write(buf: ByteBuf) {
        buf.writeEnum(type)
        if (type == Type.PARTIALLY_FILTERED) buf.writeBitSet(mask)
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
        fun read(buf: ByteBuf): FilterMask = when (val type = buf.readEnum<Type>()) {
            Type.PASS_THROUGH -> PASS_THROUGH
            Type.FULLY_FILTERED -> FULLY_FILTERED
            Type.PARTIALLY_FILTERED -> FilterMask(buf.readBitSet(), type)
        }

        @JvmStatic
        fun write(buf: ByteBuf, mask: FilterMask) {
            buf.writeEnum(mask.type)
            if (mask.type == Type.PARTIALLY_FILTERED) buf.writeBitSet(mask.mask)
        }
    }
}

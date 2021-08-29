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
package org.kryptonmc.krypton.util.nbt

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.NumberTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.toTagType

class SNBTParser(private val reader: StringReader) {

    private fun readKey(): String {
        reader.skipWhitespace()
        return if (!reader.canRead()) throw ERROR_EXPECTED_KEY.createWithContext(reader) else reader.readString()
    }

    fun readValue(): Tag {
        reader.skipWhitespace()
        if (!reader.canRead()) throw ERROR_EXPECTED_VALUE.createWithContext(reader)
        return when (reader.peek()) {
            COMPOUND_START -> readCompound()
            LIST_START -> readList()
            else -> readTypedValue()
        }
    }

    fun readSingleCompound(): CompoundTag {
        val compound = readCompound()
        reader.skipWhitespace()
        return if (reader.canRead()) throw ERROR_TRAILING_DATA.createWithContext(reader) else compound
    }

    private fun readTypedValue(): Tag {
        reader.skipWhitespace()
        val cursor = reader.cursor
        if (StringReader.isQuotedStringStart(reader.peek())) return StringTag.of(reader.readQuotedString())
        val text = reader.readUnquotedString()
        if (text.isEmpty()) {
            reader.cursor = cursor
            throw ERROR_EXPECTED_VALUE.createWithContext(reader)
        }
        return convert(text)
    }

    private fun readList(): Tag {
        return if (
            reader.canRead(3) &&
            !StringReader.isQuotedStringStart(reader.peek(1)) &&
            reader.peek(2) == ';'
        ) {
            readArrayTag()
        } else {
            readListTag()
        }
    }

    private fun readListTag(): Tag {
        expect(LIST_START)
        reader.skipWhitespace()
        if (!reader.canRead()) throw ERROR_EXPECTED_VALUE.createWithContext(reader)
        val list = ListTag()
        var type: Int = -1

        while (reader.peek() != LIST_END) {
            val cursor = reader.cursor
            val tag = readValue()
            val tagType = tag.type
            if (type == -1) {
                type = tag.id
            } else if (tag.id != type) {
                reader.cursor = cursor
                throw ERROR_INSERT_MIXED_LIST.createWithContext(reader, tagType.name, type.toTagType().name)
            }
            list += tag
            if (!reader.hasElementSeparator()) break
            if (!reader.canRead()) throw ERROR_EXPECTED_VALUE.createWithContext(reader)
        }

        expect(LIST_END)
        return list
    }

    private fun readArrayTag(): Tag {
        expect(LIST_START)
        val cursor = reader.cursor
        val start = reader.read()
        reader.read()
        reader.skipWhitespace()
        if (!reader.canRead()) throw ERROR_EXPECTED_VALUE.createWithContext(reader)
        return when (start) {
            BYTE_ARRAY_START -> ByteArrayTag(readArray<Byte>(ByteArrayTag.ID, ByteTag.ID).toByteArray())
            INT_ARRAY_START -> IntArrayTag(readArray<Int>(IntArrayTag.ID, IntTag.ID).toIntArray())
            LONG_ARRAY_START -> LongArrayTag(readArray<Long>(LongArrayTag.ID, LongTag.ID).toLongArray())
            else -> {
                reader.cursor = cursor
                throw ERROR_INVALID_ARRAY.createWithContext(reader, start.toString())
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Number> readArray(arrayType: Int, elementType: Int): List<T> {
        val list = mutableListOf<T>()
        while (true) {
            if (reader.peek() != LIST_END) {
                val cursor = reader.cursor
                val tag = readValue()
                val type = tag.id
                if (type != elementType) {
                    reader.cursor = cursor
                    throw ERROR_INSERT_MIXED_ARRAY.createWithContext(reader, type.toTagType().name, arrayType.toTagType().name)
                }
                list += when (elementType) {
                    ByteTag.ID -> (tag as NumberTag).value as T
                    LongTag.ID -> (tag as NumberTag).value as T
                    else -> (tag as NumberTag).value as T
                }
                if (reader.hasElementSeparator()) {
                    if (!reader.canRead()) throw ERROR_EXPECTED_VALUE.createWithContext(reader)
                    continue
                }
            }

            expect(LIST_END)
            return list
        }
    }

    fun readCompound(): CompoundTag {
        expect(COMPOUND_START)
        val builder = CompoundTag.builder()
        reader.skipWhitespace()

        while (reader.canRead() && reader.peek() != COMPOUND_END) {
            val cursor = reader.cursor
            val key = readKey()
            if (key.isEmpty()) {
                reader.cursor = cursor
                throw ERROR_EXPECTED_KEY.createWithContext(reader)
            }
            expect(KEY_SEPARATOR)
            builder.put(key, readValue())
            if (!reader.hasElementSeparator()) break
            if (!reader.canRead()) throw ERROR_EXPECTED_KEY.createWithContext(reader)
        }
        expect(COMPOUND_END)
        return builder.build()
    }

    private fun convert(text: String): Tag = when {
        text matches BYTE_REGEX -> ByteTag.of(text.dropLast(1).toByte())
        text matches SHORT_REGEX -> ShortTag.of(text.dropLast(1).toShort())
        text matches INT_REGEX -> IntTag.of(text.toInt())
        text matches LONG_REGEX -> LongTag.of(text.dropLast(1).toLong())
        text matches FLOAT_REGEX -> FloatTag.of(text.dropLast(1).toFloat())
        text matches DOUBLE_REGEX -> DoubleTag.of(text.dropLast(1).toDouble())
        text matches DOUBLE_REGEX_NO_SUFFIX -> DoubleTag.of(text.toDouble())
        text == "true" -> ByteTag.ONE
        text == "false" -> ByteTag.ZERO
        else -> StringTag.of(text)
    }

    private fun expect(char: Char) {
        reader.skipWhitespace()
        reader.expect(char)
    }

    companion object {

        private const val KEY_SEPARATOR = ':'
        private const val LIST_START = '['
        private const val LIST_END = ']'
        private const val COMPOUND_START = '{'
        private const val COMPOUND_END = '}'
        private const val BYTE_ARRAY_START = 'B'
        private const val LONG_ARRAY_START = 'L'
        private const val INT_ARRAY_START = 'I'
        private const val ELEMENT_SEPARATOR = ','

        private val DOUBLE_REGEX_NO_SUFFIX = "[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?".toRegex(RegexOption.IGNORE_CASE)
        private val DOUBLE_REGEX = "[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d".toRegex(RegexOption.IGNORE_CASE)
        private val FLOAT_REGEX = "[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f".toRegex(RegexOption.IGNORE_CASE)
        private val BYTE_REGEX = "[-+]?(?:0|[1-9][0-9]*)b".toRegex(RegexOption.IGNORE_CASE)
        private val LONG_REGEX = "[-+]?(?:0|[1-9][0-9]*)l".toRegex(RegexOption.IGNORE_CASE)
        private val SHORT_REGEX = "[-+]?(?:0|[1-9][0-9]*)s".toRegex(RegexOption.IGNORE_CASE)
        private val INT_REGEX = "[-+]?(?:0|[1-9][0-9]*)".toRegex(RegexOption.IGNORE_CASE)

        private val ERROR_TRAILING_DATA = SimpleCommandExceptionType(translatable("argument.nbt.trailing").toMessage())
        private val ERROR_EXPECTED_KEY = SimpleCommandExceptionType(translatable("argument.nbt.expected.key").toMessage())
        private val ERROR_EXPECTED_VALUE = SimpleCommandExceptionType(translatable("argument.nbt.expected.value").toMessage())
        private val ERROR_INSERT_MIXED_LIST = Dynamic2CommandExceptionType { a, b ->
            translatable("argument.nbt.list.mixed", text(a.toString()), text(b.toString())).toMessage()
        }
        private val ERROR_INSERT_MIXED_ARRAY = Dynamic2CommandExceptionType { a, b ->
            translatable("argument.nbt.array.mixed", text(a.toString()), text(b.toString())).toMessage()
        }
        private val ERROR_INVALID_ARRAY = DynamicCommandExceptionType {
            translatable("argument.nbt.array.invalid", text(it.toString())).toMessage()
        }

        private fun StringReader.hasElementSeparator(): Boolean {
            skipWhitespace()
            val hasSeparator = canRead() && peek() == ELEMENT_SEPARATOR
            if (hasSeparator) {
                skip()
                skipWhitespace()
            }
            return hasSeparator
        }
    }
}

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
package org.kryptonmc.krypton.util.nbt

import com.mojang.brigadier.StringReader
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.nbt.ByteArrayTag
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.DoubleTag
import org.kryptonmc.nbt.EndTag
import org.kryptonmc.nbt.FloatTag
import org.kryptonmc.nbt.ImmutableCompoundTag
import org.kryptonmc.nbt.IntArrayTag
import org.kryptonmc.nbt.IntTag
import org.kryptonmc.nbt.LongArrayTag
import org.kryptonmc.nbt.LongTag
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.NumberTag
import org.kryptonmc.nbt.ShortTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.util.Types

class SNBTParser(private val reader: StringReader) {

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

    fun readCompound(): CompoundTag {
        expect(COMPOUND_START)
        val builder = ImmutableCompoundTag.builder()
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
            if (!hasElementSeparator(reader)) break
            if (!reader.canRead()) throw ERROR_EXPECTED_KEY.createWithContext(reader)
        }
        expect(COMPOUND_END)
        return builder.build()
    }

    private fun readKey(): String {
        reader.skipWhitespace()
        return if (!reader.canRead()) throw ERROR_EXPECTED_KEY.createWithContext(reader) else reader.readString()
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
        if (reader.canRead(3) && !StringReader.isQuotedStringStart(reader.peek(1)) && reader.peek(2) == ';') {
            return readArrayTag()
        }
        return readListTag()
    }

    private fun readListTag(): Tag {
        expect(LIST_START)
        reader.skipWhitespace()
        if (!reader.canRead()) throw ERROR_EXPECTED_VALUE.createWithContext(reader)
        val list = MutableListTag.of(ArrayList(), EndTag.ID)
        var type: Int = -1

        while (reader.peek() != LIST_END) {
            val cursor = reader.cursor
            val tag = readValue()
            if (type == -1) {
                type = tag.id()
            } else if (tag.id() != type) {
                reader.cursor = cursor
                throw ERROR_INSERT_MIXED_LIST.createWithContext(reader, tag.type().name(), Types.of(type).name())
            }
            list.add(tag)
            if (!hasElementSeparator(reader)) break
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
            BYTE_ARRAY_START -> ByteArrayTag.of(readArray<Byte>(ByteArrayTag.ID, ByteTag.ID).toByteArray())
            INT_ARRAY_START -> IntArrayTag.of(readArray<Int>(IntArrayTag.ID, IntTag.ID).toIntArray())
            LONG_ARRAY_START -> LongArrayTag.of(readArray<Long>(LongArrayTag.ID, LongTag.ID).toLongArray())
            else -> {
                reader.cursor = cursor
                throw ERROR_INVALID_ARRAY.createWithContext(reader, start.toString())
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Number> readArray(arrayType: Int, elementType: Int): List<T> {
        val list = ArrayList<T>()
        while (true) {
            if (reader.peek() != LIST_END) {
                val cursor = reader.cursor
                val tag = readValue()
                val type = tag.id()
                if (type != elementType) {
                    reader.cursor = cursor
                    throw ERROR_INSERT_MIXED_ARRAY.createWithContext(reader, Types.of(type).name(), Types.of(arrayType).name())
                }
                val value = when (elementType) {
                    ByteTag.ID -> (tag as ByteTag).value() as T
                    LongTag.ID -> (tag as LongTag).value() as T
                    else -> (tag as NumberTag).asNumber() as T
                }
                list.add(value)
                if (hasElementSeparator(reader)) {
                    if (!reader.canRead()) throw ERROR_EXPECTED_VALUE.createWithContext(reader)
                    continue
                }
            }

            expect(LIST_END)
            return list
        }
    }

    private fun convert(text: String): Tag = when {
        BYTE_REGEX.matches(text) -> ByteTag.of(text.dropLast(1).toByte())
        SHORT_REGEX.matches(text) -> ShortTag.of(text.dropLast(1).toShort())
        INT_REGEX.matches(text) -> IntTag.of(text.toInt())
        LONG_REGEX.matches(text) -> LongTag.of(text.dropLast(1).toLong())
        FLOAT_REGEX.matches(text) -> FloatTag.of(text.dropLast(1).toFloat())
        DOUBLE_REGEX.matches(text) -> DoubleTag.of(text.dropLast(1).toDouble())
        DOUBLE_REGEX_NO_SUFFIX.matches(text) -> DoubleTag.of(text.toDouble())
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

        private val ERROR_TRAILING_DATA = CommandExceptions.simple("argument.nbt.trailing")
        private val ERROR_EXPECTED_KEY = CommandExceptions.simple("argument.nbt.expected.key")
        private val ERROR_EXPECTED_VALUE = CommandExceptions.simple("argument.nbt.expected.value")
        private val ERROR_INSERT_MIXED_LIST = CommandExceptions.dynamic2("argument.nbt.list.mixed")
        private val ERROR_INSERT_MIXED_ARRAY = CommandExceptions.dynamic2("argument.nbt.array.mixed")
        private val ERROR_INVALID_ARRAY = CommandExceptions.dynamic("argument.nbt.array.invalid")

        @JvmStatic
        fun parse(text: String): CompoundTag = SNBTParser(StringReader(text)).readSingleCompound()

        @JvmStatic
        private fun hasElementSeparator(reader: StringReader): Boolean {
            reader.skipWhitespace()
            val hasSeparator = reader.canRead() && reader.peek() == ELEMENT_SEPARATOR
            if (hasSeparator) {
                reader.skip()
                reader.skipWhitespace()
            }
            return hasSeparator
        }
    }
}

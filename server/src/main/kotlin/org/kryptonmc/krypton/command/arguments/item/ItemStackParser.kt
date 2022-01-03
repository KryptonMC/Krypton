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
package org.kryptonmc.krypton.command.arguments.item

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.tags.TagTypes
import org.kryptonmc.krypton.command.toExceptionType
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.util.nbt.SNBTParser
import org.kryptonmc.nbt.CompoundTag

/**
 * A parser that can parse both item stacks and item stack predicates.
 */
class ItemStackParser(val reader: StringReader, private val allowTags: Boolean) { // TODO: Tags for ItemStackPredicate etc.

    fun parseItem(): ItemStackArgument = ItemStackArgument(readItem(reader), readNBT(reader))

    fun parsePredicate(): ItemStackPredicate {
        var tag: String? = null
        var item: ItemType? = null
        var nbt: CompoundTag? = null

        if (reader.canRead() && reader.peek() == '#') tag = readTag(reader) else item = readItem(reader)
        if (reader.canRead() && reader.peek() == '{') nbt = readNBT(reader)

        return ItemStackPredicate {
            if (item != null) {
                if (nbt != null) return@ItemStackPredicate nbt == it.meta.save()
                return@ItemStackPredicate it.type == item
            }
            if (tag != null) {
                val tags = KryptonTagManager[TagTypes.ITEMS, tag] ?: throw UNKNOWN_ITEM_TAG.create(tag.toString())
                return@ItemStackPredicate tags.contains(it.type)
            }
            false
        }
    }

    private fun readItem(reader: StringReader): ItemType {
        val i = reader.cursor
        while (reader.canRead() && isCharacterValid(reader.peek())) {
            reader.skip()
        }

        val string = reader.string.substring(i, reader.cursor)
        val item = Registries.ITEM[Key.key(string)]
        if (item === ItemTypes.AIR) throw ID_INVALID_EXCEPTION.createWithContext(reader, string)
        return item
    }

    private fun readTag(reader: StringReader): String {
        if (allowTags) {
            reader.expect('#')
            val i = reader.cursor
            while (reader.canRead() && isCharacterValid(reader.peek())) {
                reader.skip()
            }
            return reader.string.substring(i, reader.cursor)
        }
        throw TAG_DISALLOWED_EXCEPTION.createWithContext(reader)
    }

    private fun readNBT(reader: StringReader): CompoundTag? {
        if (reader.canRead() && reader.peek() == '{') return SNBTParser(reader).readCompound()
        return null
    }

    companion object {

        /**
         * Thrown when a user tries to parse an item stack with type AIR, which
         * is not allowed (for hopefully obvious reasons).
         */
        @JvmField
        val ID_INVALID_EXCEPTION: DynamicCommandExceptionType = DynamicCommandExceptionType {
            Component.translatable("argument.item.id.invalid", Component.text(it.toString())).toMessage()
        }

        /**
         * Thrown when a user inputs an item tag and the parser is not allowed
         * to parse item tags.
         */
        @JvmField
        val TAG_DISALLOWED_EXCEPTION: SimpleCommandExceptionType = Component.translatable("argument.item.tag.disallowed").toExceptionType()

        /**
         * Thrown when a user inputs an item tag and the parser was unable to
         * resolve the input to a valid item tag.
         */
        @JvmField
        val UNKNOWN_ITEM_TAG: DynamicCommandExceptionType = DynamicCommandExceptionType {
            Component.translatable("arguments.item.tag.unknown", Component.text(it.toString())).toMessage()
        }

        @JvmStatic
        private fun isCharacterValid(character: Char): Boolean = character in '0'..'9' ||
                character in 'a'..'z' ||
                character == '_' ||
                character == ':' ||
                character == '/' ||
                character == '.' ||
                character == '-'
    }
}

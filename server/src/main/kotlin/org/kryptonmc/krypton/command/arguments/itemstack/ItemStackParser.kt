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
package org.kryptonmc.krypton.command.arguments.itemstack

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component.translatable
import org.kryptonmc.api.adventure.toMessage
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.toKey
import org.kryptonmc.krypton.item.meta.KryptonMetaHolder
import org.kryptonmc.krypton.tags.ItemTags
import org.kryptonmc.krypton.util.nbt.SNBTParser
import org.kryptonmc.krypton.util.toComponent
import org.kryptonmc.nbt.CompoundTag

class ItemStackParser(val reader: StringReader, val allowTags: Boolean) { //TODO: Tags for ItemStackPredicate etc.

    private fun readItem(reader: StringReader): ItemType {
        val i = reader.cursor
        while (reader.canRead() && isCharValid(reader.peek())) reader.skip()
        val string = reader.string.substring(i, reader.cursor)
        val item = Registries.ITEM[key(string)]
        if (item == ItemTypes.AIR) throw ID_INVALID_EXCEPTION.createWithContext(reader, string)
        return item
    }

    private fun readTag(reader: StringReader): String {
        if (allowTags) {
            reader.expect('#')
            val i = reader.cursor
            while (reader.canRead() && isCharValid(reader.peek())) reader.skip()
            return reader.string.substring(i, reader.cursor)
        } else throw TAG_DISALLOWED_EXCEPTION.createWithContext(reader)
    }

    private fun readNBT(reader: StringReader) = if (reader.canRead() && reader.peek() == '{') SNBTParser(reader).readCompound() else null

    fun parseItem() = ItemStackArgument(readItem(this.reader), readNBT(this.reader))

    fun parsePredicate(): ItemStackPredicate {
        var tag: String? = null
        var item: ItemType? = null
        var nbt: CompoundTag? = null

        if (reader.canRead() && reader.peek() == '#') tag = readTag(reader) else item = readItem(reader)
        if (reader.canRead() && reader.peek() == '{') nbt = readNBT(reader)

        return ItemStackPredicate {
            if (item != null) {
                if (nbt != null) nbt == (it.meta as KryptonMetaHolder).nbt.immutable() else it.type == item
            } else if (tag != null) {
                (ItemTags.tags[tag.toKey()] ?: throw UNKNOWN_ITEM_TAG.create(tag.toString())).contains(it.type)
            } else false
        }
    }

    private fun isCharValid(c: Char) = c in '0'..'9' || c in 'a'..'z' || c == '_' || c == ':' || c == '/' || c == '.' || c == '-'

    companion object {
        val ID_INVALID_EXCEPTION = DynamicCommandExceptionType { translatable("argument.item.id.invalid", it.toString().toComponent()).toMessage() }
        val TAG_DISALLOWED_EXCEPTION = SimpleCommandExceptionType(translatable("argument.item.tag.disallowed").toMessage())
        val UNKNOWN_ITEM_TAG = DynamicCommandExceptionType { translatable("arguments.item.tag.unknown", it.toString().toComponent()).toMessage() }
    }
}

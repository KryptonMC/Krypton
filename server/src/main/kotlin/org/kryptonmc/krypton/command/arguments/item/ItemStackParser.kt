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
package org.kryptonmc.krypton.command.arguments.item

import com.mojang.brigadier.StringReader
import net.kyori.adventure.key.Key
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.command.arguments.CommandExceptions
import org.kryptonmc.krypton.command.arguments.StringReading
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.nbt.SNBTParser
import org.kryptonmc.nbt.CompoundTag

/**
 * A parser that can parse both item stacks and item stack predicates.
 */
object ItemStackParser { // TODO: Tags for ItemStackPredicate etc.

    private val ID_INVALID_EXCEPTION = CommandExceptions.dynamic("argument.item.id.invalid")
    private val TAG_DISALLOWED_EXCEPTION = CommandExceptions.simple("argument.item.tag.disallowed")
    @Suppress("UnusedPrivateMember")
    private val UNKNOWN_ITEM_TAG = CommandExceptions.dynamic("argument.item.tag.unknown")

    @JvmStatic
    fun parseItem(reader: StringReader): ItemStackArgument = ItemStackArgument(readItem(reader), readNBT(reader))

    @JvmStatic
    fun parsePredicate(reader: StringReader, allowTags: Boolean): ItemStackPredicate {
        var tag: String? = null
        var item: ItemType? = null
        var data: CompoundTag? = null

        if (reader.canRead() && reader.peek() == '#') tag = readTag(reader, allowTags) else item = readItem(reader)
        if (reader.canRead() && reader.peek() == '{') data = readNBT(reader)

        return ItemStackPredicate {
            if (item != null) {
                if (data != null) return@ItemStackPredicate data == it.meta.data
                return@ItemStackPredicate it.type == item
            }
            /* FIXME: When we implement command build context and holder lookups, rewrite this
            if (tag != null) {
                val tags = KryptonTagManager.get(TagTypes.ITEMS, tag) ?: throw UNKNOWN_ITEM_TAG.create(tag.toString())
                return@ItemStackPredicate tags.contains(it.type)
            }
             */
            false
        }
    }

    @JvmStatic
    private fun readItem(reader: StringReader): KryptonItemType {
        val keyString = StringReading.readKeyString(reader)
        val item = KryptonRegistries.ITEM.get(Key.key(keyString))
        if (item === ItemTypes.AIR) throw ID_INVALID_EXCEPTION.createWithContext(reader, keyString)
        return item
    }

    @JvmStatic
    private fun readTag(reader: StringReader, allowTags: Boolean): String {
        if (allowTags) {
            reader.expect('#')
            return StringReading.readKeyString(reader)
        }
        throw TAG_DISALLOWED_EXCEPTION.createWithContext(reader)
    }

    @JvmStatic
    private fun readNBT(reader: StringReader): CompoundTag? {
        if (reader.canRead() && reader.peek() == '{') return SNBTParser(reader).readCompound()
        return null
    }
}

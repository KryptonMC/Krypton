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
package org.kryptonmc.krypton.adventure

import com.mojang.brigadier.Message
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.item.meta.KryptonWrittenBookMeta
import org.kryptonmc.krypton.util.gson.GsonHelper
import org.kryptonmc.krypton.util.Reflection

/**
 * Various things used by Krypton for supporting Adventure.
 */
object KryptonAdventure {

    // We need to do this because the only other solution, which is to use the NAMES index, doesn't have the guaranteed ordering
    // that we require to map the IDs properly. This internal list has the ordering we need.
    private val NAMED_TEXT_COLORS = Reflection.accessField<NamedTextColor, List<NamedTextColor>>("VALUES")
    private val NAMED_TEXT_COLOR_ID_MAP = Object2IntArrayMap<NamedTextColor>(NAMED_TEXT_COLORS.size).apply {
        for (i in NAMED_TEXT_COLORS.indices) {
            put(NAMED_TEXT_COLORS.get(i), i)
        }
    }

    @JvmStatic
    fun colors(): List<NamedTextColor> = NAMED_TEXT_COLORS

    @JvmStatic
    fun getColorId(color: NamedTextColor): Int = NAMED_TEXT_COLOR_ID_MAP.getInt(color)

    @JvmStatic
    fun getColorFromId(id: Int): NamedTextColor = NAMED_TEXT_COLORS.get(id)

    @JvmStatic
    fun toItemStack(book: Book): KryptonItemStack {
        if (book is KryptonWrittenBookMeta) return KryptonItemStack(ItemTypes.WRITTEN_BOOK.get().downcast(), 1, book)
        return KryptonItemStack.Builder()
            .type(ItemTypes.WRITTEN_BOOK.get())
            .amount(1)
            .meta<_, WrittenBookMeta> {
                title(book.title())
                author(book.author())
                pages(book.pages())
            }
            .build()
    }

    @JvmStatic
    fun toStableJson(component: Component): String = GsonHelper.toStableString(GsonComponentSerializer.gson().serializeToTree(component))

    @JvmStatic
    fun asMessage(component: Component): Message = KryptonAdventureMessage(component)

    @JvmStatic
    fun toPlainText(input: Component, maximumLength: Int): String {
        val result = StringBuilder()
        ComponentFlattener.basic().flatten(input) {
            val remaining = maximumLength - result.length
            if (remaining <= 0) return@flatten
            result.append(if (it.length <= remaining) it else it.substring(0, remaining))
        }
        return result.toString()
    }
}

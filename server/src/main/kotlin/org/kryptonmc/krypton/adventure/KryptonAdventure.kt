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
package org.kryptonmc.krypton.adventure

import com.mojang.brigadier.Message
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.item.meta.KryptonWrittenBookMeta
import org.kryptonmc.krypton.util.GsonHelper
import org.kryptonmc.krypton.util.Reflection
import org.kryptonmc.krypton.util.TranslationBootstrap

/**
 * Various things used by Krypton for supporting Adventure.
 */
object KryptonAdventure {

    /**
     * This flattener adds the use of the official Minecraft translations from en_us.json
     * to render translatable components when they are flattened.
     */
    @JvmField
    val FLATTENER: ComponentFlattener = ComponentFlattener.basic().toBuilder()
        .complexMapper(TranslatableComponent::class.java) { translatable, mapper -> mapper.accept(TranslationBootstrap.render(translatable)) }
        .build()
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
        if (book is KryptonWrittenBookMeta) return KryptonItemStack(ItemTypes.WRITTEN_BOOK.downcast(), 1, book)
        return KryptonItemStack.Builder()
            .type(ItemTypes.WRITTEN_BOOK)
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
}

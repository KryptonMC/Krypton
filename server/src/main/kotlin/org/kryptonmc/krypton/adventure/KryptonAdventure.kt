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
package org.kryptonmc.krypton.adventure

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.format.NamedTextColor
import org.kryptonmc.api.adventure.complexMapper
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonMetaHolder
import org.kryptonmc.krypton.util.TranslationBootstrap
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.mutableCompound
import java.util.Locale

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
        .complexMapper<TranslatableComponent> { translatable, consumer ->
            consumer(TranslationBootstrap.RENDERER.render(translatable, Locale.ENGLISH))
        }
        .build()
    private val NAMED_TEXT_COLOR_ID_MAP = Object2IntArrayMap<NamedTextColor>(16).apply {
        put(NamedTextColor.BLACK, 0)
        put(NamedTextColor.DARK_BLUE, 1)
        put(NamedTextColor.DARK_GREEN, 2)
        put(NamedTextColor.DARK_AQUA, 3)
        put(NamedTextColor.DARK_RED, 4)
        put(NamedTextColor.DARK_PURPLE, 5)
        put(NamedTextColor.GOLD, 6)
        put(NamedTextColor.GRAY, 7)
        put(NamedTextColor.DARK_GRAY, 8)
        put(NamedTextColor.BLUE, 9)
        put(NamedTextColor.GREEN, 10)
        put(NamedTextColor.AQUA, 11)
        put(NamedTextColor.RED, 12)
        put(NamedTextColor.LIGHT_PURPLE, 13)
        put(NamedTextColor.YELLOW, 14)
        put(NamedTextColor.WHITE, 15)
    }

    @JvmStatic
    fun id(color: NamedTextColor): Int = NAMED_TEXT_COLOR_ID_MAP.getInt(color)

    @JvmStatic
    fun toItemStack(book: Book): KryptonItemStack {
        val tag = mutableCompound {
            putString("title", book.title().toJsonString())
            putString("author", book.author().toJsonString())
            put("pages", MutableListTag(book.pages().mapTo(mutableListOf()) { StringTag.of(it.toJsonString()) }, StringTag.ID))
        }
        return KryptonItemStack(ItemTypes.WRITTEN_BOOK, 1, KryptonMetaHolder(tag))
    }
}

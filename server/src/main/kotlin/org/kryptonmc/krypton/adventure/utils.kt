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

import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.flattener.ComponentFlattener
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.item
import org.kryptonmc.api.item.meta
import org.kryptonmc.api.item.meta.WrittenBookMeta
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonWrittenBookMeta
import org.kryptonmc.krypton.util.GsonHelper
import java.util.Locale

inline fun <reified T : Component> ComponentFlattener.Builder.complexMapper(
    noinline converter: (T, (Component) -> Unit) -> Unit
): ComponentFlattener.Builder = complexMapper(T::class.java) { value, consumer -> converter(value) { consumer.accept(it) } }

fun Book.toItemStack(): KryptonItemStack {
    if (this is KryptonWrittenBookMeta) return KryptonItemStack(ItemTypes.WRITTEN_BOOK, 1, this)
    val book = this // Helps avoid ambiguity later
    return item {
        type(ItemTypes.WRITTEN_BOOK)
        amount(1)
        meta<WrittenBookMeta.Builder, WrittenBookMeta> {
            title(book.title())
            author(book.author())
            pages(book.pages())
        }
    } as KryptonItemStack
}

fun TextColor.serialize(): String {
    if (this is NamedTextColor) return NamedTextColor.NAMES.key(this)!!
    return String.format(Locale.ROOT, "#%06X", value())
}

fun Component.toStableJson(): String = GsonHelper.toStableString(GsonComponentSerializer.gson().serializeToTree(this))

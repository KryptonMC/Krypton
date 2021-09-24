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
package org.kryptonmc.krypton.item

import net.kyori.adventure.inventory.Book
import org.kryptonmc.api.adventure.toJsonString
import org.kryptonmc.api.item.ItemHandler
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.item.handler.DummyItemHandler
import org.kryptonmc.krypton.item.meta.KryptonMetaHolder
import org.kryptonmc.nbt.MutableListTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.mutableCompound

val ItemType.handler: ItemHandler
    get() = KryptonItemManager.handler(key.asString()) ?: DummyItemHandler

fun Book.toItemStack(): KryptonItemStack {
    val tag = mutableCompound {
        putString("title", title().toJsonString())
        putString("author", author().toJsonString())
        put("pages", MutableListTag(pages().mapTo(mutableListOf()) { StringTag.of(it.toJsonString()) }, StringTag.ID))
    }
    return KryptonItemStack(ItemTypes.WRITTEN_BOOK, 1, KryptonMetaHolder(tag))
}

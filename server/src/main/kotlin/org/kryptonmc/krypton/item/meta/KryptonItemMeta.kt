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
package org.kryptonmc.krypton.item.meta

import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.krypton.item.ItemFactory
import org.kryptonmc.nbt.CompoundTag

class KryptonItemMeta(data: CompoundTag) : AbstractItemMeta<KryptonItemMeta>(data), ItemMeta {

    override fun copy(data: CompoundTag): KryptonItemMeta = KryptonItemMeta(data)

    class Builder : KryptonItemMetaBuilder<ItemMeta.Builder, ItemMeta>(), ItemMeta.Builder {

        override fun build(): KryptonItemMeta = KryptonItemMeta(buildData().build())
    }

    object Factory : ItemMeta.Factory {

        override fun builder(): ItemMeta.Builder = Builder()

        override fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B = ItemFactory.builder(type)
    }

    companion object {

        @JvmField
        val DEFAULT: KryptonItemMeta = Builder().build()
    }
}

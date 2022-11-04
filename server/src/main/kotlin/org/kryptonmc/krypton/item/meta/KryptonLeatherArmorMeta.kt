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

import org.kryptonmc.api.item.meta.LeatherArmorMeta
import org.kryptonmc.api.util.Color
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag

class KryptonLeatherArmorMeta(data: CompoundTag) : AbstractItemMeta<KryptonLeatherArmorMeta>(data), LeatherArmorMeta {

    override val color: Color? = data.getDisplay<IntTag, _>("color", IntTag.ID, null) { Color.of(it.value()) }

    override fun copy(data: CompoundTag): KryptonLeatherArmorMeta = KryptonLeatherArmorMeta(data)

    override fun withColor(color: Color?): KryptonLeatherArmorMeta {
        val newData = if (color == null) data.remove("color") else data.putInt("color", color.value)
        return KryptonLeatherArmorMeta(newData)
    }

    override fun toBuilder(): LeatherArmorMeta.Builder = Builder(this)

    class Builder : KryptonItemMetaBuilder<LeatherArmorMeta.Builder, LeatherArmorMeta>, LeatherArmorMeta.Builder {

        private var color: Color? = null

        constructor() : super()

        constructor(meta: KryptonLeatherArmorMeta) : super(meta) {
            color = meta.color
        }

        override fun color(color: Color?): LeatherArmorMeta.Builder = apply { this.color = color }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            if (color != null) putInt("color", color!!.value)
        }

        override fun build(): KryptonLeatherArmorMeta = KryptonLeatherArmorMeta(buildData().build())
    }
}

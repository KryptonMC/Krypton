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
package org.kryptonmc.krypton.item.meta

import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.meta.LeatherArmorMeta
import java.awt.Color

class KryptonLeatherArmorMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val color: Color?
) : AbstractItemMeta<KryptonLeatherArmorMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn),
    LeatherArmorMeta {

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: List<Component>,
        hideFlags: Int,
        canDestroy: Set<Block>,
        canPlaceOn: Set<Block>
    ): KryptonLeatherArmorMeta = KryptonLeatherArmorMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, color)

    override fun withColor(color: Color?): KryptonLeatherArmorMeta = KryptonLeatherArmorMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        color
    )

    override fun toBuilder(): LeatherArmorMeta.Builder = Builder(this)

    class Builder() : KryptonItemMetaBuilder<LeatherArmorMeta.Builder, LeatherArmorMeta>(), LeatherArmorMeta.Builder {

        private var color: Color? = null

        constructor(meta: LeatherArmorMeta) : this() {
            copyFrom(meta)
            color = meta.color
        }

        override fun color(color: Color?): LeatherArmorMeta.Builder = apply { this.color = color }

        override fun build(): KryptonLeatherArmorMeta = KryptonLeatherArmorMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            color
        )
    }
}

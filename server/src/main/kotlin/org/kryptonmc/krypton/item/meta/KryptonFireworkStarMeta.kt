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
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.meta.FireworkStarMeta
import org.kryptonmc.krypton.item.data.KryptonFireworkEffect
import org.kryptonmc.krypton.item.data.save
import org.kryptonmc.nbt.CompoundTag

class KryptonFireworkStarMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val effect: FireworkEffect?
) : AbstractItemMeta<KryptonFireworkStarMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn),
    FireworkStarMeta {

    constructor(tag: CompoundTag) : this(
        tag.getInt("Damage"),
        tag.getBoolean("Unbreakable"),
        tag.getInt("CustomModelData"),
        tag.getName(),
        tag.getLore(),
        tag.getInt("HideFlags"),
        tag.getBlocks("CanDestroy"),
        tag.getBlocks("CanPlaceOn"),
        KryptonFireworkEffect(tag.getCompound("Explosion"))
    )

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: List<Component>,
        hideFlags: Int,
        canDestroy: Set<Block>,
        canPlaceOn: Set<Block>
    ): KryptonFireworkStarMeta = KryptonFireworkStarMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        effect
    )

    override fun saveData(): CompoundTag.Builder = super.saveData().apply {
        if (effect != null) put("Explosion", effect.save())
    }

    override fun withEffect(effect: FireworkEffect?): KryptonFireworkStarMeta = KryptonFireworkStarMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        effect
    )

    override fun toBuilder(): FireworkStarMeta.Builder = Builder(this)

    class Builder() : KryptonItemMetaBuilder<FireworkStarMeta.Builder, FireworkStarMeta>(), FireworkStarMeta.Builder {

        private var effect: FireworkEffect? = null

        constructor(meta: FireworkStarMeta) : this() {
            copyFrom(meta)
            effect = meta.effect
        }

        override fun effect(effect: FireworkEffect?): FireworkStarMeta.Builder = apply { this.effect = effect }

        override fun build(): KryptonFireworkStarMeta = KryptonFireworkStarMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            effect
        )
    }
}

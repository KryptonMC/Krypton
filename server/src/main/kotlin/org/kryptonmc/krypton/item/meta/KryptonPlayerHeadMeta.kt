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
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.item.meta.PlayerHeadMeta

class KryptonPlayerHeadMeta(
    damage: Int,
    isUnbreakable: Boolean,
    customModelData: Int,
    name: Component?,
    lore: List<Component>,
    hideFlags: Int,
    canDestroy: Set<Block>,
    canPlaceOn: Set<Block>,
    override val owner: GameProfile?
) : AbstractItemMeta<KryptonPlayerHeadMeta>(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn), PlayerHeadMeta {

    override fun copy(
        damage: Int,
        isUnbreakable: Boolean,
        customModelData: Int,
        name: Component?,
        lore: List<Component>,
        hideFlags: Int,
        canDestroy: Set<Block>,
        canPlaceOn: Set<Block>
    ): KryptonPlayerHeadMeta = KryptonPlayerHeadMeta(damage, isUnbreakable, customModelData, name, lore, hideFlags, canDestroy, canPlaceOn, owner)

    override fun withOwner(owner: GameProfile?): KryptonPlayerHeadMeta = KryptonPlayerHeadMeta(
        damage,
        isUnbreakable,
        customModelData,
        name,
        lore,
        hideFlags,
        canDestroy,
        canPlaceOn,
        owner
    )

    override fun toBuilder(): PlayerHeadMeta.Builder = Builder(this)

    class Builder() : KryptonItemMetaBuilder<PlayerHeadMeta.Builder, PlayerHeadMeta>(), PlayerHeadMeta.Builder {

        private var owner: GameProfile? = null

        constructor(meta: PlayerHeadMeta) : this() {
            copyFrom(meta)
            owner = meta.owner
        }

        override fun owner(owner: GameProfile?): PlayerHeadMeta.Builder = apply { this.owner = owner }

        override fun build(): KryptonPlayerHeadMeta = KryptonPlayerHeadMeta(
            damage,
            unbreakable,
            customModelData,
            name,
            lore,
            hideFlags,
            canDestroy,
            canPlaceOn,
            owner
        )
    }
}

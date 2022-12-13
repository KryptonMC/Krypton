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

import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.item.meta.PlayerHeadMeta
import org.kryptonmc.krypton.auth.GameProfileUtil
import org.kryptonmc.nbt.CompoundTag

class KryptonPlayerHeadMeta(data: CompoundTag) : AbstractItemMeta<KryptonPlayerHeadMeta>(data), PlayerHeadMeta {

    override val owner: GameProfile? = GameProfileUtil.getProfile(data, OWNER_TAG)

    override fun copy(data: CompoundTag): KryptonPlayerHeadMeta = KryptonPlayerHeadMeta(data)

    override fun withOwner(owner: GameProfile?): KryptonPlayerHeadMeta = copy(GameProfileUtil.putProfile(data, OWNER_TAG, owner))

    override fun toBuilder(): PlayerHeadMeta.Builder = Builder(this)

    class Builder : KryptonItemMetaBuilder<PlayerHeadMeta.Builder, PlayerHeadMeta>, PlayerHeadMeta.Builder {

        private var owner: GameProfile? = null

        constructor() : super()

        constructor(meta: KryptonPlayerHeadMeta) : super(meta) {
            owner = meta.owner
        }

        override fun owner(owner: GameProfile?): PlayerHeadMeta.Builder = apply { this.owner = owner }

        override fun buildData(): CompoundTag.Builder = GameProfileUtil.putProfile(super.buildData(), OWNER_TAG, owner)

        override fun build(): KryptonPlayerHeadMeta = KryptonPlayerHeadMeta(buildData().build())
    }

    companion object {

        private const val OWNER_TAG = "SkullOwner"
    }
}

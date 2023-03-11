/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

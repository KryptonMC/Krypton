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

import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.meta.FireworkStarMeta
import org.kryptonmc.krypton.item.data.KryptonFireworkEffect
import org.kryptonmc.nbt.CompoundTag

class KryptonFireworkStarMeta(data: CompoundTag) : AbstractItemMeta<KryptonFireworkStarMeta>(data), FireworkStarMeta {

    override val effect: FireworkEffect? = getEffect(data)

    override fun copy(data: CompoundTag): KryptonFireworkStarMeta = KryptonFireworkStarMeta(data)

    override fun withEffect(effect: FireworkEffect?): KryptonFireworkStarMeta {
        val newData = if (effect == null) data.remove(EFFECT_TAG) else data.put(EFFECT_TAG, KryptonFireworkEffect.save(effect))
        return KryptonFireworkStarMeta(newData)
    }

    override fun toBuilder(): FireworkStarMeta.Builder = Builder(this)

    class Builder : KryptonItemMetaBuilder<FireworkStarMeta.Builder, FireworkStarMeta>, FireworkStarMeta.Builder {

        private var effect: FireworkEffect? = null

        constructor() : super()

        constructor(meta: KryptonFireworkStarMeta) : super(meta) {
            effect = meta.effect
        }

        override fun effect(effect: FireworkEffect?): FireworkStarMeta.Builder = apply { this.effect = effect }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            if (effect != null) put(EFFECT_TAG, KryptonFireworkEffect.save(effect!!))
        }

        override fun build(): KryptonFireworkStarMeta = KryptonFireworkStarMeta(buildData().build())
    }

    companion object {

        private const val EFFECT_TAG = "Explosion"

        @JvmStatic
        private fun getEffect(data: CompoundTag): KryptonFireworkEffect? {
            if (data.contains(EFFECT_TAG, CompoundTag.ID)) return null
            return KryptonFireworkEffect.load(data.getCompound(EFFECT_TAG))
        }
    }
}

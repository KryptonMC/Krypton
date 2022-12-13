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

    override fun toString(): String = "KryptonFireworkStarMeta(${partialToString()}, effect=$effect)"

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

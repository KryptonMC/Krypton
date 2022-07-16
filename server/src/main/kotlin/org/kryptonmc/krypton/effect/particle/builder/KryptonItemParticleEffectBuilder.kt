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
package org.kryptonmc.krypton.effect.particle.builder

import org.kryptonmc.api.effect.particle.ItemParticleType
import org.kryptonmc.api.effect.particle.builder.ItemParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.effect.particle.data.KryptonItemParticleData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast

class KryptonItemParticleEffectBuilder(type: ItemParticleType) : AbstractParticleEffectBuilder<ItemParticleEffectBuilder>(type),
    ItemParticleEffectBuilder {

    private var item: KryptonItemStack = KryptonItemStack.EMPTY

    override fun item(item: ItemStack): ItemParticleEffectBuilder = apply { this.item = item.downcast() }

    override fun buildData(): ParticleData = KryptonItemParticleData(item)
}

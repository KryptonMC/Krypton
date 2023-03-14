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
package org.kryptonmc.krypton.effect.particle.builder

import org.kryptonmc.api.effect.particle.ItemParticleType
import org.kryptonmc.api.effect.particle.builder.ItemParticleEffectBuilder
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.effect.particle.data.KryptonItemParticleData
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.downcast

class KryptonItemParticleEffectBuilder(type: ItemParticleType) : AbstractParticleEffectBuilder<ApiItem>(type), ApiItem {

    private var item: KryptonItemStack = KryptonItemStack.EMPTY

    override fun item(item: ItemStack): ApiItem = apply { this.item = item.downcast() }

    override fun buildData(): ParticleData = KryptonItemParticleData(item)
}

private typealias ApiItem = ItemParticleEffectBuilder

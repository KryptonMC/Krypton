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
package org.kryptonmc.api.effect.particle.builder

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.internal.annotations.dsl.ParticleDsl

/**
 * A builder for building item particle effects.
 */
@ParticleDsl
public interface ItemParticleEffectBuilder : BaseParticleEffectBuilder<ItemParticleEffectBuilder> {

    /**
     * Sets the item data of the texture to be used.
     *
     * @param item the item type to use
     */
    @ParticleDsl
    @Contract("_ -> this", mutates = "this")
    public fun item(item: ItemStack): ItemParticleEffectBuilder
}

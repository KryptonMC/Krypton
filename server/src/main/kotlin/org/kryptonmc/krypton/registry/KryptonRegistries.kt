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
package org.kryptonmc.krypton.registry

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.hanging.PaintingVariant
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.dimension.DimensionType

/**
 * This class contains many registries from [org.kryptonmc.api.registry.Registries],
 * downcasted to [KryptonRegistry] types for use in the internals.
 */
object KryptonRegistries {

    // Vanilla-derived downcasted registries
    @JvmField
    val SOUND_EVENT: KryptonRegistry<SoundEvent> = Registries.SOUND_EVENT.downcastUnchecked()
    @JvmField
    val ENTITY_TYPE: KryptonRegistry<EntityType<*>> = Registries.ENTITY_TYPE.downcastUnchecked()
    @JvmField
    val PARTICLE_TYPE: KryptonRegistry<ParticleType> = Registries.PARTICLE_TYPE.downcastUnchecked()
    @JvmField
    val BLOCK: KryptonRegistry<Block> = Registries.BLOCK.downcastUnchecked()
    @JvmField
    val ITEM: KryptonRegistry<ItemType> = Registries.ITEM.downcastUnchecked()
    @JvmField
    val BIOME: KryptonRegistry<Biome> = Registries.BIOME.downcastUnchecked()
    @JvmField
    val STATISTIC_TYPE: KryptonRegistry<StatisticType<*>> = Registries.STATISTIC_TYPE.downcastUnchecked()
    @JvmField
    val FLUID: KryptonRegistry<Fluid> = Registries.FLUID.downcastUnchecked()
    @JvmField
    val DIMENSION_TYPE: KryptonRegistry<DimensionType> = Registries.DIMENSION_TYPE.downcastUnchecked()

    // Custom Krypton downcasted registries
    @JvmField
    val PICTURES: KryptonRegistry<PaintingVariant> = Registries.PAINTING_VARIANT.downcastUnchecked()
}

private fun <T : Any> Registry<T>.downcastUnchecked(): KryptonRegistry<T> = this as KryptonRegistry<T>

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
package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityDimensions
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.animal.type.AxolotlVariant
import org.kryptonmc.api.entity.animal.type.CatType
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.api.entity.animal.type.MooshroomType
import org.kryptonmc.api.entity.animal.type.PandaGene
import org.kryptonmc.api.entity.animal.type.RabbitType
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.meta.DyeColor
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.scoreboard.DisplaySlot
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.FactoryNotFoundException
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeCategory
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.command.meta.KryptonCommandMeta
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.krypton.effect.particle.KryptonParticleEffect
import org.kryptonmc.krypton.effect.particle.data.KryptonParticleDataFactory
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.entity.KryptonEntityDimensions
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.animal.type.KryptonAxolotlVariant
import org.kryptonmc.krypton.entity.animal.type.KryptonCatType
import org.kryptonmc.krypton.entity.animal.type.KryptonFoxType
import org.kryptonmc.krypton.entity.animal.type.KryptonMooshroomType
import org.kryptonmc.krypton.entity.animal.type.KryptonPandaGene
import org.kryptonmc.krypton.entity.animal.type.KryptonRabbitType
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeType
import org.kryptonmc.krypton.inventory.KryptonInventoryType
import org.kryptonmc.krypton.item.KryptonItemRarity
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.item.meta.KryptonDyeColor
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourcePack
import org.kryptonmc.krypton.world.KryptonGameMode
import org.kryptonmc.krypton.world.biome.KryptonAmbientAdditionsSettings
import org.kryptonmc.krypton.world.biome.KryptonAmbientMoodSettings
import org.kryptonmc.krypton.world.biome.KryptonAmbientParticleSettings
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.KryptonBiomeCategory
import org.kryptonmc.krypton.world.biome.KryptonBiomeEffects
import org.kryptonmc.krypton.world.biome.KryptonClimate
import org.kryptonmc.krypton.world.biome.KryptonGrassColorModifier
import org.kryptonmc.krypton.world.biome.KryptonPrecipitation
import org.kryptonmc.krypton.world.biome.KryptonTemperatureModifier
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.property.KryptonPropertyFactory
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.fluid.KryptonFluid
import org.kryptonmc.krypton.world.rule.KryptonGameRule
import org.kryptonmc.krypton.world.scoreboard.KryptonDisplaySlot
import org.kryptonmc.krypton.world.scoreboard.KryptonObjective
import org.kryptonmc.krypton.world.scoreboard.KryptonObjectiveRenderType
import org.kryptonmc.krypton.world.scoreboard.KryptonTeam

object KryptonFactoryProvider : FactoryProvider {

    private val factories = Object2ObjectOpenHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(type: Class<T>): T = factories[type] as? T ?: throw FactoryNotFoundException("Type $type has no factory registered!")

    fun <T> register(clazz: Class<T>, factory: T) {
        require(clazz !in factories) { "Duplicate registration for type $clazz!" }
        factories[clazz] = factory
    }

    fun bootstrap() {
        register<ResourceKey.Factory>(KryptonResourceKey.Factory)
        register<ParticleData.Factory>(KryptonParticleDataFactory)
        register<ParticleEffect.Factory>(KryptonParticleEffect.Factory)
        register<AttributeModifier.Factory>(KryptonAttributeModifier.Factory)
        register<AttributeType.Factory>(KryptonAttributeType.Factory)
        register<Property.Factory>(KryptonPropertyFactory)
        register<ItemStack.Factory>(KryptonItemStack.Factory)
        register<ItemType.Factory>(KryptonItemType.Factory)
        register<CommandMeta.Factory>(KryptonCommandMeta.Factory)
        register<GameProfile.Factory>(KryptonGameProfile.Factory)
        register<SoundEvent.Factory>(KryptonSoundEvent.Factory)
        register<EntityDimensions.Factory>(KryptonEntityDimensions.Factory)
        register<InventoryType.Factory>(KryptonInventoryType.Factory)
        register<Objective.Factory>(KryptonObjective.Factory)
        register<GameRule.Factory>(KryptonGameRule.Factory)
        register<GameMode.Factory>(KryptonGameMode.Factory)
        register<Music.Factory>(KryptonMusic.Factory)
        register<AmbientAdditionsSettings.Factory>(KryptonAmbientAdditionsSettings.Factory)
        register<AmbientMoodSettings.Factory>(KryptonAmbientMoodSettings.Factory)
        register<AmbientParticleSettings.Factory>(KryptonAmbientParticleSettings.Factory)
        register<Biome.Factory>(KryptonBiome.Factory)
        register<BiomeCategory.Factory>(KryptonBiomeCategory.Factory)
        register<BiomeEffects.Factory>(KryptonBiomeEffects.Factory)
        register<Climate.Factory>(KryptonClimate.Factory)
        register<GrassColorModifier.Factory>(KryptonGrassColorModifier.Factory)
        register<Precipitation.Factory>(KryptonPrecipitation.Factory)
        register<TemperatureModifier.Factory>(KryptonTemperatureModifier.Factory)
        register<BoundingBox.Factory>(KryptonBoundingBox.Factory)
        register<EntityType.Factory>(KryptonEntityType.Factory)
        register<ItemRarity.Factory>(KryptonItemRarity.Factory)
        register<DimensionType.Factory>(KryptonDimensionType.Factory)
        register<ResourcePack.Factory>(KryptonResourcePack.Factory)
        register<DisplaySlot.Factory>(KryptonDisplaySlot.Factory)
        register<ObjectiveRenderType.Factory>(KryptonObjectiveRenderType.Factory)
        register<Team.Factory>(KryptonTeam.Factory)
        register<Block.Factory>(KryptonBlock.Factory)
        register<Fluid.Factory>(KryptonFluid.Factory)
        register<DyeColor.Factory>(KryptonDyeColor.Factory)
        register<CatType.Factory>(KryptonCatType.Factory)
        register<MooshroomType.Factory>(KryptonMooshroomType.Factory)
        register<FoxType.Factory>(KryptonFoxType.Factory)
        register<PandaGene.Factory>(KryptonPandaGene.Factory)
        register<RabbitType.Factory>(KryptonRabbitType.Factory)
        register<AxolotlVariant.Factory>(KryptonAxolotlVariant.Factory)
    }
}

inline fun <reified T> KryptonFactoryProvider.register(factory: T) {
    register(T::class.java, factory)
}

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
package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.kryptonmc.api.adventure.AdventureMessage
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.hanging.Picture
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemAttribute
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.scoreboard.Objective
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Color
import org.kryptonmc.api.util.FactoryNotFoundException
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.register
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.damage.DamageSource
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.krypton.adventure.KryptonAdventureMessage
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.command.KryptonBrigadierCommand
import org.kryptonmc.krypton.command.meta.KryptonCommandMeta
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.krypton.effect.particle.KryptonParticleTypeFactory
import org.kryptonmc.krypton.effect.particle.data.KryptonParticleDataFactory
import org.kryptonmc.krypton.effect.sound.KryptonSoundEvent
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeType
import org.kryptonmc.krypton.entity.hanging.KryptonPicture
import org.kryptonmc.krypton.inventory.KryptonInventoryType
import org.kryptonmc.krypton.item.KryptonItemAttribute
import org.kryptonmc.krypton.item.KryptonItemRarity
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.item.data.KryptonDyeColor
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourcePack
import org.kryptonmc.krypton.statistic.KryptonStatisticType
import org.kryptonmc.krypton.world.biome.KryptonAmbientAdditionsSettings
import org.kryptonmc.krypton.world.biome.KryptonAmbientMoodSettings
import org.kryptonmc.krypton.world.biome.KryptonAmbientParticleSettings
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.KryptonBiomeEffects
import org.kryptonmc.krypton.world.biome.KryptonClimate
import org.kryptonmc.krypton.world.block.entity.banner.KryptonBannerPattern
import org.kryptonmc.krypton.world.block.entity.banner.KryptonBannerPatternType
import org.kryptonmc.krypton.world.block.property.KryptonPropertyFactory
import org.kryptonmc.krypton.world.damage.KryptonDamageSourceFactory
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.rule.KryptonGameRule
import org.kryptonmc.krypton.world.scoreboard.KryptonObjective
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard
import org.kryptonmc.krypton.world.scoreboard.KryptonTeam

object KryptonFactoryProvider : FactoryProvider {

    private val factories = Object2ObjectOpenHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(type: Class<T>): T = factories[type] as? T ?: throw FactoryNotFoundException("Type $type has no factory registered!")

    override fun <T> register(type: Class<T>, factory: T) {
        require(!factories.contains(type)) { "Duplicate registration for type $type!" }
        factories[type] = factory
    }

    fun bootstrap() {
        register<ResourceKey.Factory>(KryptonResourceKey.Factory)
        register<ParticleType.Factory>(KryptonParticleTypeFactory)
        register<ParticleData.Factory>(KryptonParticleDataFactory)
        register<AttributeModifier.Factory>(KryptonAttributeModifier.Factory)
        register<AttributeType.Factory>(KryptonAttributeType.Factory)
        register<Property.Factory>(KryptonPropertyFactory)
        register<ItemStack.Factory>(KryptonItemStack.Factory)
        register<ItemType.Factory>(KryptonItemType.Factory)
        register<CommandMeta.Factory>(KryptonCommandMeta.Factory)
        register<GameProfile.Factory>(KryptonGameProfile.Factory)
        register<ProfileProperty.Factory>(KryptonProfileProperty.Factory)
        register<SoundEvent.Factory>(KryptonSoundEvent.Factory)
        register<InventoryType.Factory>(KryptonInventoryType.Factory)
        register<Objective.Factory>(KryptonObjective.Factory)
        register<GameRule.Factory>(KryptonGameRule.Factory)
        register<Music.Factory>(KryptonMusic.Factory)
        register<AmbientAdditionsSettings.Factory>(KryptonAmbientAdditionsSettings.Factory)
        register<AmbientMoodSettings.Factory>(KryptonAmbientMoodSettings.Factory)
        register<AmbientParticleSettings.Factory>(KryptonAmbientParticleSettings.Factory)
        register<Biome.Factory>(KryptonBiome.Factory)
        register<BiomeEffects.Factory>(KryptonBiomeEffects.Factory)
        register<Climate.Factory>(KryptonClimate.Factory)
        register<BoundingBox.Factory>(KryptonBoundingBox.Factory)
        register<EntityType.Factory>(KryptonEntityType.Factory)
        register<ItemRarity.Factory>(KryptonItemRarity.Factory)
        register<DimensionType.Factory>(KryptonDimensionType.Factory)
        register<ResourcePack.Factory>(KryptonResourcePack.Factory)
        register<Team.Factory>(KryptonTeam.Factory)
        register<DyeColor.Factory>(KryptonDyeColor.Factory)
        register<Picture.Factory>(KryptonPicture.Factory)
        register<StatisticType.Factory>(KryptonStatisticType.Factory)
        register<ItemMeta.Factory>(KryptonItemMeta.Factory)
        register<DamageSource.Factory>(KryptonDamageSourceFactory)
        register<AdventureMessage.Factory>(KryptonAdventureMessage.Factory)
        register<BrigadierCommand.Factory>(KryptonBrigadierCommand.Factory)
        register<Scoreboard.Factory>(KryptonScoreboard.Factory)
        register<ItemAttribute.Factory>(KryptonItemAttribute.Factory)
        register<BannerPattern.Factory>(KryptonBannerPattern.Factory)
        register<BannerPatternType.Factory>(KryptonBannerPatternType.Factory)
        register<Color.Factory>(KryptonColor.Factory)
    }
}

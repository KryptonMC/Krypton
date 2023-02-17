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
import org.kryptonmc.api.state.Property
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.data.ParticleData
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.event.EventListener
import org.kryptonmc.api.event.EventNode
import org.kryptonmc.api.item.ItemAttributeModifier
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.data.FireworkEffect
import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.registry.DynamicRegistryReference
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourcePack
import org.kryptonmc.api.scheduling.TaskAction
import org.kryptonmc.api.scheduling.TaskTime
import org.kryptonmc.api.tags.TagKey
import org.kryptonmc.api.util.TypeNotFoundException
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.register
import org.kryptonmc.api.world.biome.AmbientAdditionsSettings
import org.kryptonmc.api.world.biome.AmbientMoodSettings
import org.kryptonmc.api.world.biome.AmbientParticleSettings
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeEffects
import org.kryptonmc.api.world.biome.Climate
import org.kryptonmc.api.world.chunk.BlockChangeFlags
import org.kryptonmc.api.world.damage.DamageSource
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.krypton.adventure.KryptonAdventureMessage
import org.kryptonmc.krypton.auth.KryptonGameProfile
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.command.KryptonBrigadierCommand
import org.kryptonmc.krypton.command.KryptonCommandMeta
import org.kryptonmc.krypton.effect.KryptonMusic
import org.kryptonmc.krypton.effect.particle.data.KryptonParticleDataFactory
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.event.KryptonEventListener
import org.kryptonmc.krypton.event.KryptonEventNode
import org.kryptonmc.krypton.item.data.KryptonItemAttributeModifier
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.data.KryptonFireworkEffect
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.registry.KryptonDynamicRegistryReference
import org.kryptonmc.krypton.registry.KryptonRegistryReference
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourcePack
import org.kryptonmc.krypton.scheduling.KryptonTaskAction
import org.kryptonmc.krypton.scheduling.KryptonTaskTime
import org.kryptonmc.krypton.world.biome.data.KryptonAmbientAdditionsSettings
import org.kryptonmc.krypton.world.biome.data.KryptonAmbientMoodSettings
import org.kryptonmc.krypton.world.biome.data.KryptonAmbientParticleSettings
import org.kryptonmc.krypton.world.biome.KryptonBiome
import org.kryptonmc.krypton.world.biome.data.KryptonBiomeEffects
import org.kryptonmc.krypton.world.biome.data.KryptonClimate
import org.kryptonmc.krypton.world.block.entity.banner.KryptonBannerPattern
import org.kryptonmc.krypton.state.property.KryptonPropertyFactory
import org.kryptonmc.krypton.tags.KryptonTagKey
import org.kryptonmc.krypton.world.chunk.flag.KryptonBlockChangeFlags
import org.kryptonmc.krypton.world.damage.KryptonDamageSourceFactory
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.rule.GameRuleKeys

object KryptonFactoryProvider : FactoryProvider {

    private val factories = Object2ObjectOpenHashMap<Class<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T> provide(type: Class<T>): T = factories.get(type) as? T ?: throw TypeNotFoundException("Type $type has no factory registered!")

    override fun <T> register(type: Class<T>, factory: T) {
        require(!factories.contains(type)) { "Duplicate registration for type $type!" }
        factories.put(type, factory)
    }

    @JvmStatic
    fun bootstrap() {
        register<AdventureMessage.Factory>(KryptonAdventureMessage.Factory)
        register<GameProfile.Factory>(KryptonGameProfile.Factory)
        register<ProfileProperty.Factory>(KryptonProfileProperty.Factory)
        register<BannerPattern.Factory>(KryptonBannerPattern.Factory)
        register<BrigadierCommand.Factory>(KryptonBrigadierCommand.Factory)
        register<CommandMeta.Factory>(KryptonCommandMeta.Factory)
        register<ParticleData.Factory>(KryptonParticleDataFactory)
        register<Music.Factory>(KryptonMusic.Factory)
        register<AttributeModifier.Factory>(KryptonAttributeModifier.Factory)
        register<ItemAttributeModifier.Factory>(KryptonItemAttributeModifier.Factory)
        register<ItemStack.Factory>(KryptonItemStack.Factory)
        register<FireworkEffect.Factory>(KryptonFireworkEffect.Factory)
        register<ItemMeta.Factory>(KryptonItemMeta.Factory)
        register<ResourceKey.Factory>(KryptonResourceKey.Factory)
        register<ResourcePack.Factory>(KryptonResourcePack.Factory)
        register<Property.Factory>(KryptonPropertyFactory)
        register<TagKey.Factory>(KryptonTagKey.Factory)
        register<AmbientAdditionsSettings.Factory>(KryptonAmbientAdditionsSettings.Factory)
        register<AmbientMoodSettings.Factory>(KryptonAmbientMoodSettings.Factory)
        register<AmbientParticleSettings.Factory>(KryptonAmbientParticleSettings.Factory)
        register<Biome.Factory>(KryptonBiome.Factory)
        register<BiomeEffects.Factory>(KryptonBiomeEffects.Factory)
        register<Climate.Factory>(KryptonClimate.Factory)
        register<DamageSource.Factory>(KryptonDamageSourceFactory)
        register<DimensionType.Factory>(KryptonDimensionType.Factory)
        register<GameRule.Factory>(GameRuleKeys.Factory)
        register<RegistryReference.Factory>(KryptonRegistryReference.Factory)
        register<DynamicRegistryReference.Factory>(KryptonDynamicRegistryReference.Factory)
        register<TaskTime.Factory>(KryptonTaskTime.Factory)
        register<TaskAction.Factory>(KryptonTaskAction.Factory)
        register<EventNode.Factory>(KryptonEventNode.Factory)
        register<EventListener.Factory>(KryptonEventListener.Factory)
        register<BlockChangeFlags.Factory>(KryptonBlockChangeFlags.Factory)
    }
}

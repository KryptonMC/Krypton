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

import com.mojang.brigadier.exceptions.CommandSyntaxException
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.entity.BlockEntityTypes
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityCategories
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.type.AxolotlVariants
import org.kryptonmc.api.entity.animal.type.CatTypes
import org.kryptonmc.api.entity.animal.type.FoxTypes
import org.kryptonmc.api.entity.animal.type.MooshroomTypes
import org.kryptonmc.api.entity.animal.type.PandaGenes
import org.kryptonmc.api.entity.animal.type.RabbitTypes
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.hanging.Pictures
import org.kryptonmc.api.fluid.Fluids
import org.kryptonmc.api.item.ItemRarities
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.MetaKeys
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.tags.EntityTypeTags
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.api.tags.TagTypes
import org.kryptonmc.api.world.GameModes
import org.kryptonmc.api.world.biome.BiomeCategories
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.api.world.biome.GrassColorModifiers
import org.kryptonmc.api.world.biome.Precipitations
import org.kryptonmc.api.world.biome.TemperatureModifiers
import org.kryptonmc.api.world.dimension.DimensionTypes
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.command.BrigadierExceptions
import org.kryptonmc.krypton.command.argument.ArgumentSerializers
import org.kryptonmc.krypton.effect.sound.SoundLoader
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.KryptonEntityCategories
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.AttributeLoader
import org.kryptonmc.krypton.entity.hanging.KryptonPictures
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.ItemLoader
import org.kryptonmc.krypton.item.KryptonItemManager
import org.kryptonmc.krypton.item.meta.KryptonMetaKeys
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.statistic.KryptonStatisticTypes
import org.kryptonmc.krypton.tags.GameEventTags
import org.kryptonmc.krypton.tags.KryptonTagManager
import org.kryptonmc.krypton.tags.KryptonTagTypes
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.block.KryptonBlockManager
import org.kryptonmc.krypton.world.block.entity.BlockEntityLoader
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import org.kryptonmc.krypton.world.event.GameEvents
import org.kryptonmc.krypton.world.fluid.FluidLoader
import java.util.TreeSet

object Bootstrap {

    private val LOGGER = logger<Bootstrap>()
    @Volatile
    private var bootstrapped = false

    // Might be better if you turn away from this now, unless you're making something that is registry-based,
    // in which, good luck. It probably needs to be placed somewhere with its dependencies before it and its
    // dependents after it.
    @JvmStatic
    fun preload() {
        if (bootstrapped) return
        bootstrapped = true
        TranslationBootstrap.init()

        // Preload the factory stuff
        // These are some kinda nasty hacks, but the tight coupling nature of Krypton requires it
        // The only 2 other alternatives here are to do away with the static singleton and revert back, or restructure
        // the entire project to use Guice's dependency inversion (something that should be looked in to at some point)
        Krypton::class.java.getDeclaredField("internalFactoryProvider").apply { isAccessible = true }.set(null, KryptonFactoryProvider)
        Krypton::class.java.getDeclaredField("internalRegistryManager").apply { isAccessible = true }.set(null, KryptonRegistryManager)
        Krypton::class.java.getDeclaredField("internalTagManager").apply { isAccessible = true }.set(null, KryptonTagManager)
        KryptonFactoryProvider.bootstrap()
        KryptonRegistryManager.parent // Force initialisation

        // Preload all the registry classes to ensure everything is properly registered
        InternalRegistries
        Registries
        KryptonTagTypes
        TagTypes
        GameModes
        SoundLoader.init()
        SoundEvents
        BlockLoader.init()
        Blocks
        BlockEntityLoader.init()
        BlockEntityTypes
        GameEvents
        ParticleTypes
        KryptonEntityCategories
        EntityCategories
        KryptonEntityTypes
        EntityTypes
        ItemRarities
        ItemLoader.init()
        ItemTypes
        FluidLoader.init()
        Fluids
        BiomeCategories
        GrassColorModifiers
        Precipitations
        TemperatureModifiers
        BiomeKeys
        KryptonBiomes
        Biomes
        KryptonTagManager.bootstrap()
        KryptonDimensionTypes
        DimensionTypes
        BlockTags
        EntityTypeTags
        FluidTags
        GameEventTags
        ItemTags
        GameRules
        AttributeLoader.init()
        AttributeTypes
        MetadataKeys
        KryptonStatisticTypes
        StatisticTypes
        CustomStatistics
        KryptonPictures
        Pictures
        KryptonMetaKeys
        MetaKeys // Not technically a registry, but quite close to one
        CatTypes
        FoxTypes
        MooshroomTypes
        PandaGenes
        RabbitTypes
        AxolotlVariants

        // Preload some other things that would otherwise load on first player join or some other time
        Encryption
        SessionService
        ArgumentSerializers
        EntityFactory
        KryptonItemManager
        KryptonBlockManager
        CommandSyntaxException.BUILT_IN_EXCEPTIONS = BrigadierExceptions
    }

    @JvmStatic
    fun validate() {
        require(bootstrapped) { "Bootstrap wasn't ran!" }
        missingTranslations().forEach { LOGGER.warn("Missing translation: $it") }
    }

    @JvmStatic
    private fun <T> Iterable<T>.checkTranslations(missing: MutableSet<String>, getter: (T) -> String) = forEach {
        val key = getter(it)
        if (!TranslationBootstrap.REGISTRY.contains(key)) missing.add(key)
    }

    @JvmStatic
    private fun missingTranslations(): Set<String> {
        val missing = TreeSet<String>()
        InternalRegistries.ATTRIBUTE.values.checkTranslations(missing) { it.translation.key() }
        InternalRegistries.ENTITY_TYPE.values.checkTranslations(missing) { it.translation.key() }
        InternalRegistries.BLOCK.values.checkTranslations(missing) { it.translation.key() }
        InternalRegistries.ITEM.values.checkTranslations(missing) { it.translation.key() }
        Registries.GAMERULES.values.checkTranslations(missing) { it.translation.key() }
        return missing
    }
}

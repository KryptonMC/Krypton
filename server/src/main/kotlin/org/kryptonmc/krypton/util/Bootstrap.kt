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
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.hanging.Canvases
import org.kryptonmc.api.fluid.Fluids
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.MetaKeys
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.command.BrigadierExceptions
import org.kryptonmc.krypton.command.argument.ArgumentSerializers
import org.kryptonmc.krypton.effect.sound.SoundLoader
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.entity.attribute.AttributeLoader
import org.kryptonmc.krypton.entity.hanging.KryptonCanvases
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemManager
import org.kryptonmc.krypton.item.meta.MetaFactory
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.statistic.KryptonStatisticTypes
import org.kryptonmc.krypton.tags.BlockTags
import org.kryptonmc.krypton.tags.EntityTypeTags
import org.kryptonmc.krypton.tags.FluidTags
import org.kryptonmc.krypton.tags.GameEventTags
import org.kryptonmc.krypton.tags.ItemTags
import org.kryptonmc.krypton.tags.TagManager
import org.kryptonmc.krypton.world.biome.BiomeKeys
import org.kryptonmc.krypton.world.biome.KryptonBiomes
import org.kryptonmc.krypton.world.block.KryptonBlockManager
import org.kryptonmc.krypton.world.block.palette.GlobalPalette
import org.kryptonmc.krypton.world.dimension.DimensionTypes
import org.kryptonmc.krypton.world.event.GameEvents
import org.kryptonmc.krypton.world.fluid.FluidLoader
import java.util.TreeSet

object Bootstrap {

    private val LOGGER = logger<Bootstrap>()
    @Volatile private var bootstrapped = false

    fun preload() {
        if (bootstrapped) return
        bootstrapped = true
        TranslationBootstrap.init()

        // Preload all the registry classes to ensure everything is properly registered
        InternalRegistries
        (FactoryProvider.INSTANCE as KryptonFactoryProvider).bootstrap()
        Registries
        Blocks
        GameEvents
        ParticleTypes
        SoundLoader.init()
        SoundEvents
        EntityTypes
        ItemTypes
        FluidLoader.init()
        Fluids
        KryptonBiomes
        BiomeKeys
        Biomes
        DimensionTypes
        GameRules
        AttributeLoader.init()
        AttributeTypes
        MetadataKeys
        KryptonStatisticTypes
        StatisticTypes
        CustomStatistics
        KryptonCanvases
        Canvases
        MetaKeys // Not technically a registry, but quite close to one

        // Preload tags (which use registries)
        TagManager
        BlockTags
        EntityTypeTags
        FluidTags
        GameEventTags
        ItemTags

        // Preload some other things that would otherwise load on first player join or some other time
        Encryption
        SessionService
        ArgumentSerializers
        EntityFactory
        KryptonItemManager
        MetaFactory
        KryptonBlockManager
        GlobalPalette
        CommandSyntaxException.BUILT_IN_EXCEPTIONS = BrigadierExceptions
    }

    fun validate() {
        require(bootstrapped) { "Bootstrap wasn't ran!" }
        missingTranslations.forEach { LOGGER.warn("Missing translation: $it") }
    }

    private fun <T> Iterable<T>.checkTranslations(missing: MutableSet<String>, getter: (T) -> String) = forEach {
        val key = getter(it)
        if (!TranslationBootstrap.REGISTRY.contains(key)) missing.add(key)
    }

    private val missingTranslations: Set<String>
        get() {
            val missing = TreeSet<String>()
            InternalRegistries.ATTRIBUTE.values.checkTranslations(missing) { it.translation.key() }
            InternalRegistries.ENTITY_TYPE.values.checkTranslations(missing) { it.name.key() }
            InternalRegistries.BLOCK.values.checkTranslations(missing) { it.translation.key() }
            InternalRegistries.ITEM.values.checkTranslations(missing) { it.translation.key() }
            Registries.GAMERULES.values.checkTranslations(missing) { it.translation.key() }
            return missing
        }
}

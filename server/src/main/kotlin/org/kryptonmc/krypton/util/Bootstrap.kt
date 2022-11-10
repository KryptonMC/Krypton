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

import com.mojang.brigadier.exceptions.CommandSyntaxException
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.tags.BannerPatternTags
import org.kryptonmc.api.tags.BiomeTags
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.tags.EntityTypeTags
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.krypton.auth.requests.SessionService
import org.kryptonmc.krypton.command.BrigadierExceptions
import org.kryptonmc.krypton.command.argument.ArgumentSerializers
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.item.ItemManager
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.tags.GameEventTags
import org.kryptonmc.krypton.util.crypto.Encryption
import java.util.TreeSet
import java.util.function.Function

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
        val kryptonClass = Class.forName("org.kryptonmc.api.Krypton")
        Reflection.modifyField(kryptonClass, "factoryProvider", KryptonFactoryProvider)
        Reflection.modifyField(kryptonClass, "registryManager", KryptonRegistries.ManagerImpl)
        KryptonFactoryProvider.bootstrap()

        // Preload all the registry classes to ensure everything is properly registered
        KryptonRegistries.bootstrap()
        Registries
        BlockTags
        EntityTypeTags
        FluidTags
        GameEventTags
        ItemTags
        BannerPatternTags
        BiomeTags

        // Preload some other things that would otherwise load on first player join or some other time
        Encryption
        SessionService
        ArgumentSerializers
        EntityFactory
        ItemManager
        CommandSyntaxException.BUILT_IN_EXCEPTIONS = BrigadierExceptions
    }

    @JvmStatic
    fun validate() {
        require(bootstrapped) { "Bootstrap wasn't ran!" }
        collectMissingTranslations().forEach { LOGGER.warn("Missing translation: $it") }
    }

    @JvmStatic
    private fun collectMissingTranslations(): Set<String> {
        val missing = TreeSet<String>()
        checkTranslations(KryptonRegistries.ATTRIBUTE, AttributeType::translationKey, missing)
        checkTranslations(KryptonRegistries.ENTITY_TYPE, EntityType<*>::translationKey, missing)
        checkTranslations(KryptonRegistries.ITEM, ItemType::translationKey, missing)
        checkTranslations(KryptonRegistries.BLOCK, Block::translationKey, missing)
        checkTranslations(KryptonRegistries.CUSTOM_STATISTIC, { "stat.${it.asString().replace(':', '.')}" }, missing)
        checkTranslations(KryptonRegistries.GAME_RULES, GameRule<*>::translationKey, missing)
        return missing
    }

    @JvmStatic
    private fun <T> checkTranslations(values: Iterable<T>, keyGetter: Function<T, String>, missing: MutableSet<String>) {
        values.forEach {
            val key = keyGetter.apply(it)
            if (!TranslationBootstrap.REGISTRY.contains(key)) missing.add(key)
        }
    }
}

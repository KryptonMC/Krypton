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
package org.kryptonmc.krypton.server

import com.mojang.brigadier.exceptions.CommandSyntaxException
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.VisibleForTesting
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.tags.BannerPatternTags
import org.kryptonmc.api.tags.BiomeTags
import org.kryptonmc.api.tags.BlockTags
import org.kryptonmc.api.tags.EntityTypeTags
import org.kryptonmc.api.tags.FluidTags
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.krypton.command.BrigadierExceptions
import org.kryptonmc.krypton.command.argument.ArgumentSerializers
import org.kryptonmc.krypton.entity.EntityFactory
import org.kryptonmc.krypton.item.ItemManager
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.tags.GameEventTags
import org.kryptonmc.krypton.util.KryptonFactoryProvider
import org.kryptonmc.krypton.util.Reflection
import org.kryptonmc.krypton.locale.MinecraftTranslationManager
import org.kryptonmc.krypton.registry.KryptonDynamicRegistries
import org.kryptonmc.krypton.util.crypto.Encryption
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.rule.GameRuleKeys
import org.kryptonmc.krypton.world.rule.WorldGameRules
import java.util.TreeSet
import java.util.function.Function

object Bootstrap {

    private val LOGGER = LogManager.getLogger()
    @Volatile
    private var bootstrapped = false
    private val kryptonClass by lazy { Class.forName("org.kryptonmc.api.Krypton") }

    // Might be better if you turn away from this now, unless you're making something that is registry-based,
    // in which, good luck. It probably needs to be placed somewhere with its dependencies before it and its
    // dependents after it.
    @JvmStatic
    fun preload() {
        if (bootstrapped) return
        bootstrapped = true
        preloadTranslations()
        preloadFactories()
        preloadRegistries()
        preloadOtherClasses()
    }

    @JvmStatic
    @VisibleForTesting
    fun preloadTranslations() {
        MinecraftTranslationManager.init()
    }

    // Preload the factory stuff
    // These are some kinda nasty hacks, but the tight coupling nature of Krypton requires it
    // The only 2 other alternatives here are to do away with the static singleton and revert back, or restructure
    // the entire project to use Guice's dependency inversion (something that should be looked in to at some point)
    @JvmStatic
    @VisibleForTesting
    fun preloadFactories() {
        Reflection.modifyStaticField(kryptonClass, "factoryProvider", KryptonFactoryProvider)
        KryptonFactoryProvider.bootstrap()
    }

    // Preload all the registry classes to ensure everything is properly registered
    @JvmStatic
    @VisibleForTesting
    fun preloadRegistries() {
        Reflection.modifyStaticField(kryptonClass, "staticRegistryHolder", KryptonRegistries.StaticHolder)
        KryptonRegistries.bootstrap()
        KryptonDynamicRegistries.bootstrap()
        KryptonBlocks.initializeStates()
        Registries
        BlockTags
        EntityTypeTags
        FluidTags
        GameEventTags
        ItemTags
        BannerPatternTags
        BiomeTags
    }

    // Preload some other things that would otherwise load on first player join or some other time
    @JvmStatic
    @VisibleForTesting
    fun preloadOtherClasses() {
        Encryption
        ArgumentSerializers.bootstrap()
        EntityFactory
        ItemManager.bootstrap()
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
        checkTranslations(KryptonRegistries.ATTRIBUTE, { it.translationKey() }, missing)
        checkTranslations(KryptonRegistries.ENTITY_TYPE, { it.translationKey() }, missing)
        checkTranslations(KryptonRegistries.ITEM, { it.translationKey() }, missing)
        checkTranslations(KryptonRegistries.BLOCK, { it.translationKey() }, missing)
        checkTranslations(KryptonRegistries.CUSTOM_STATISTIC, { "stat.${it.asString().replace(':', '.')}" }, missing)
        GameRuleKeys.visitTypes(object : WorldGameRules.TypeVisitor {
            override fun <T : WorldGameRules.Value<T>> visit(key: WorldGameRules.Key<T>, type: WorldGameRules.Type<T>) {
                if (!MinecraftTranslationManager.REGISTRY.contains(key.translationKey())) missing.add(key.id)
            }
        })
        return missing
    }

    @JvmStatic
    private fun <T> checkTranslations(values: Iterable<T>, keyGetter: Function<T, String>, missing: MutableSet<String>) {
        values.forEach {
            val key = keyGetter.apply(it)
            if (!MinecraftTranslationManager.REGISTRY.contains(key)) missing.add(key)
        }
    }
}

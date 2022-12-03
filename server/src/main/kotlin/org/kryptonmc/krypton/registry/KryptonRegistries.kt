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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.BlockEntityTypes
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.block.entity.banner.BannerPatternTypes
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityCategories
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.entity.hanging.PaintingVariant
import org.kryptonmc.api.entity.hanging.PaintingVariants
import org.kryptonmc.api.fluid.Fluids
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.inventory.InventoryTypes
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.registry.DefaultedRegistry
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.scoreboard.criteria.Criteria
import org.kryptonmc.api.scoreboard.criteria.KeyedCriterion
import org.kryptonmc.api.statistic.CustomStatistics
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.statistic.StatisticTypes
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.dimension.DimensionTypes
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.world.rule.GameRules
import org.kryptonmc.krypton.effect.sound.SoundLoader
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.memory.MemoryKey
import org.kryptonmc.krypton.entity.memory.MemoryKeys
import org.kryptonmc.krypton.item.Instrument
import org.kryptonmc.krypton.item.Instruments
import org.kryptonmc.krypton.item.ItemLoader
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.network.chat.ChatType
import org.kryptonmc.krypton.network.chat.ChatTypes
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.provider.IntProviderType
import org.kryptonmc.krypton.util.provider.IntProviderTypes
import org.kryptonmc.krypton.world.biome.KryptonBiomeRegistrar
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.dimension.KryptonDimensionTypes
import org.kryptonmc.krypton.world.event.GameEvent
import org.kryptonmc.krypton.world.event.GameEvents
import org.kryptonmc.krypton.world.fluid.KryptonFluid
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import java.util.function.Function
import java.util.function.Supplier

/**
 * This class contains all of the built-in registries for Krypton. These are required by the API to exist, and they exist in this class
 * because it is easier to not have to downcast the registries available in the API everywhere.
 */
object KryptonRegistries {

    private val LOGGER = logger<KryptonRegistries>()
    private val WRITABLE_PARENT: WritableRegistry<WritableRegistry<*>> = KryptonSimpleRegistry(ImplKeys.PARENT, null)
    private val LOADERS = LinkedHashMap<Key, Runnable>()

    /*
     * Built-in vanilla-derived registries
     */

    @JvmField
    val GAME_EVENT: Defaulted<GameEvent> = defaultedIntrusive(ImplKeys.GAME_EVENT, "step") { GameEvents }
    @JvmField
    val SOUND_EVENT: Simple<SoundEvent> = simple(ApiKeys.SOUND_EVENT, dataLoader(::SoundLoader) { SoundEvents })
    @JvmField
    val FLUID: Defaulted<KryptonFluid> = defaultedIntrusive(ImplKeys.FLUID, "empty") {
        KryptonFluids
        Fluids
    }
    @JvmField
    val BLOCK: Defaulted<KryptonBlock> = defaultedIntrusive(ImplKeys.BLOCK, "air") {
        KryptonBlocks
        Blocks
    }
    @JvmField
    val ENTITY_CATEGORIES: Simple<EntityCategory> = simple(ApiKeys.ENTITY_CATEGORIES, loader(Loaders.entityCategory()) { EntityCategories })
    @JvmField
    val ENTITY_TYPE: Defaulted<KryptonEntityType<*>> = defaultedIntrusive(ImplKeys.ENTITY_TYPE, "pig") {
        KryptonEntityTypes
        EntityTypes
    }
    @JvmField
    val ITEM: Defaulted<KryptonItemType> = defaultedIntrusive(ImplKeys.ITEM, "air", dataLoader(::ItemLoader) { ItemTypes })
    @JvmField
    val PARTICLE_TYPE: Simple<ParticleType> = simple(ApiKeys.PARTICLE_TYPE, loader(Loaders.particleType()) { ParticleTypes })
    @JvmField
    val BLOCK_ENTITY_TYPE: Simple<BlockEntityType<*>> = simple(ApiKeys.BLOCK_ENTITY_TYPE, loader(Loaders.blockEntityType()) { BlockEntityTypes })
    @JvmField
    val PAINTING_VARIANT: Defaulted<PaintingVariant> =
        defaulted(ApiKeys.PAINTING_VARIANT, "kebab", loader(Loaders.paintingVariant()) { PaintingVariants })
    @JvmField
    val INVENTORY_TYPE: Simple<InventoryType> =
        simple(ResourceKeys.INVENTORY_TYPE, loader(Loaders.inventoryType()) { InventoryTypes })
    @JvmField
    val ATTRIBUTE: Simple<AttributeType> = simple(ApiKeys.ATTRIBUTE) {
        KryptonAttributeTypes
        AttributeTypes
    }
    @JvmField
    val STATISTIC_TYPE: Simple<StatisticType<*>> = simple(ApiKeys.STATISTIC_TYPE, loader(Loaders.statisticType()) { StatisticTypes })
    @JvmField
    val CUSTOM_STATISTIC: Simple<Key> = simple(ApiKeys.CUSTOM_STATISTIC, loader(Loaders.customStatistic()) { CustomStatistics })
    @JvmField
    val MEMORIES: Simple<MemoryKey<*>> = simple(ImplKeys.MEMORIES) { MemoryKeys }
    @JvmField
    val INT_PROVIDER_TYPES: Simple<IntProviderType<*>> = simple(ImplKeys.INT_PROVIDER_TYPES) { IntProviderTypes }
    @JvmField
    val BANNER_PATTERN: Simple<BannerPatternType> = simple(ApiKeys.BANNER_PATTERN, loader(Loaders.bannerPatternType()) { BannerPatternTypes })
    @JvmField
    val INSTRUMENTS: Simple<Instrument> = simple(ImplKeys.INSTRUMENTS) { Instruments }
    @JvmField
    val DIMENSION_TYPE: Simple<DimensionType> = simple(ApiKeys.DIMENSION_TYPE) {
        KryptonDimensionTypes
        DimensionTypes
    }
    @JvmField
    val BIOME: Simple<Biome> = simple(ApiKeys.BIOME) {
        KryptonBiomeRegistrar.bootstrap()
        Biomes
    }
    @JvmField
    val CHAT_TYPE: Simple<ChatType> = simple(ImplKeys.CHAT_TYPE) { ChatTypes }

    /*
     * Custom built-in registries
     */

    @JvmField
    val GAME_RULES: Simple<GameRule<*>> = simple(ApiKeys.GAME_RULES, loader(Loaders.gameRule()) { GameRules })
    @JvmField
    val CRITERIA: Simple<KeyedCriterion> = simple(ApiKeys.CRITERIA, loader(Loaders.criterion()) { Criteria })
    @JvmField
    val DAMAGE_TYPES: Simple<DamageType> = simple(ApiKeys.DAMAGE_TYPES, loader(Loaders.damageType()) { DamageTypes })

    /*
     * Registry constructor functions
     */

    @JvmStatic
    private fun <T> simple(key: RegistryKey<T>, bootstrap: Bootstrap<T>): Simple<T> =
        internalRegister(key, KryptonSimpleRegistry(key, null), bootstrap)

    @JvmStatic
    private fun <T> defaulted(key: RegistryKey<T>, defaultName: String, bootstrap: Bootstrap<T>): Defaulted<T> =
        defaulted(key, defaultName, null, bootstrap)

    @JvmStatic
    private fun <T : IRO<T>> defaultedIntrusive(key: RegistryKey<T>, defaultName: String, bootstrap: Bootstrap<T>): Defaulted<T> =
        defaulted(key, defaultName, { it.builtInRegistryHolder }, bootstrap)

    @JvmStatic
    private fun <T> defaulted(key: RegistryKey<T>, defaultName: String, customHolderProvider: CHP<T>?, bootstrap: Bootstrap<T>): Defaulted<T> =
        internalRegister(key, KryptonDefaultedRegistry(Key.key(defaultName), key, customHolderProvider), bootstrap)

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T, R : WritableRegistry<T>> internalRegister(key: RegistryKey<T>, registry: R, loader: Bootstrap<T>): R {
        LOADERS.put(key.location) { loader.run(registry) }
        WRITABLE_PARENT.register(key as WritableKey, registry)
        return registry
    }

    /*
     * Registry registration functions. Designed to stop having to leak the WritableRegistry API in to other parts of the system,
     * as well as controlling how registry values are registered.
     */

    @JvmStatic
    fun <T, V : T> register(registry: Simple<T>, key: Key, value: V): V = register(registry, KryptonResourceKey.of(registry.key, key), value)

    @JvmStatic
    fun <T, V : T> register(registry: Simple<T>, key: ResourceKey<T>, value: V): V {
        (registry as WritableRegistry<T>).register(key, value)
        return value
    }

    /*
     * Bootstrap constructors. Used to create Bootstrap functions from registry loaders and data loaders.
     */

    @JvmStatic
    private inline fun <T> loader(loader: Supplier<RegistryLoader<T>>, crossinline initApiType: () -> Unit): Bootstrap<T> = Bootstrap {
        loader.get().forEach { key, value -> register(it, key, value) }
        initApiType()
    }

    @JvmStatic
    private inline fun <T> dataLoader(
        crossinline loader: (Simple<T>) -> KryptonDataLoader<T>,
        crossinline initApiType: () -> Unit
    ): Bootstrap<T> = Bootstrap {
        loader(it).init()
        initApiType()
    }

    /*
     * Bootstrapping function. Used to initialize all the registries at the right time from the Bootstrap class.
     */

    @JvmStatic
    fun bootstrap() {
        // Run all of the bootstrapping preload functions
        LOADERS.forEach { (key, action) ->
            try {
                action.run()
            } catch (exception: Exception) {
                LOGGER.error("Failed to bootstrap registry $key!", exception)
                throw RegistryInitializationException("Failed to bootstrap registry $key!", exception)
            }
        }
        // Check the parent to ensure every registry has values and all defaulted registries have their defaults
        // registered
        WRITABLE_PARENT.forEach { registry ->
            if (registry.keys.isEmpty()) LOGGER.error("Registry ${registry.key} was empty after loading!")
            if (registry is Defaulted<*>) {
                val defaultKey = registry.defaultKey
                checkNotNull(registry.get(defaultKey)) { "Default value for key $defaultKey in registry ${registry.key} was not loaded!" }
            }
        }
    }

    /**
     * A function used to bootstrap registries by preloading all the values in to them, available for use very early in the server
     * lifecycle, to avoid any failures in attempting to retrieve them.
     */
    private fun interface Bootstrap<T> {

        fun run(registry: Simple<T>)
    }

    private class RegistryInitializationException(message: String, cause: Throwable) : RuntimeException(message, cause)

    /**
     * The backend registry manager implementation. Moved inside KryptonRegistries as it needs access to internals that would rather
     * not be otherwise published and available to other components.
     */
    @Suppress("UNCHECKED_CAST")
    object ManagerImpl : RegistryManager {

        override fun <T> getRegistry(key: RegistryKey<T>): Registry<T>? = WRITABLE_PARENT.get(key as WritableKey) as? Registry<T>

        override fun <T> getDefaultedRegistry(key: RegistryKey<T>): DefaultedRegistry<T>? =
            WRITABLE_PARENT.get(key as WritableKey) as? DefaultedRegistry<T>

        override fun <T> create(key: RegistryKey<T>): Registry<T> = create(key) { KryptonSimpleRegistry(key, null) }

        override fun <T> createDefaulted(key: RegistryKey<T>, defaultKey: Key): DefaultedRegistry<T> =
            create(key) { KryptonDefaultedRegistry(defaultKey, key, null) }

        @JvmStatic
        private fun <T, R : WritableRegistry<T>> create(key: RegistryKey<T>, creator: () -> R): R {
            val registry = creator()
            WRITABLE_PARENT.register(key as WritableKey, registry)
            return registry
        }
    }
}

// Some private typealiases that are short and are used to make the types shorter and more readable and avoid wrapping often, which would
// decrease readability
private typealias Simple<T> = KryptonRegistry<T>
private typealias Defaulted<T> = KryptonDefaultedRegistry<T>
private typealias ApiKeys = ResourceKeys
private typealias ImplKeys = KryptonResourceKeys
private typealias Loaders = RegistryLoaders
private typealias IRO<T> = IntrusiveRegistryObject<T>
private typealias CHP<T> = Function<T, Holder.Reference<T>>
private typealias RegistryKey<T> = ResourceKey<out Registry<T>>
private typealias WritableKey = ResourceKey<WritableRegistry<*>>

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
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.hanging.PaintingVariant
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.registry.DefaultedRegistry
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.scoreboard.criteria.KeyedCriterion
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.krypton.effect.sound.SoundLoader
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.ai.memory.MemoryKey
import org.kryptonmc.krypton.entity.ai.memory.MemoryKeys
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.item.ItemLoader
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.item.data.Instrument
import org.kryptonmc.krypton.item.data.Instruments
import org.kryptonmc.krypton.registry.KryptonRegistries.Bootstrap
import org.kryptonmc.krypton.registry.loader.RegistryLoader
import org.kryptonmc.krypton.registry.loader.RegistryLoaders
import org.kryptonmc.krypton.resource.KryptonResourceKey
import org.kryptonmc.krypton.resource.KryptonResourceKeys
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.util.provider.IntProviderType
import org.kryptonmc.krypton.util.provider.IntProviderTypes
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.fluid.KryptonFluid
import org.kryptonmc.krypton.world.fluid.KryptonFluids
import org.kryptonmc.krypton.world.gameevent.GameEvent
import org.kryptonmc.krypton.world.gameevent.GameEvents
import java.util.function.Supplier

/**
 * This class contains all of the built-in registries for Krypton. These are required by the API to exist, and they exist in this class
 * because it is easier to not have to downcast the registries available in the API everywhere.
 */
object KryptonRegistries {

    private val LOGGER = LogManager.getLogger()
    private val LOADERS = LinkedHashMap<Key, Runnable>()
    private val WRITABLE_PARENT: WritableRegistry<WritableRegistry<*>> = KryptonSimpleRegistry.standard(KryptonResourceKeys.PARENT)
    @JvmField
    val PARENT: KryptonRegistry<out KryptonRegistry<*>> = WRITABLE_PARENT

    /*
     * Built-in vanilla-derived registries
     */

    @JvmField
    val GAME_EVENT: KryptonDefaultedRegistry<GameEvent> = defaultedIntrusive(KryptonResourceKeys.GAME_EVENT, "step") { GameEvents }
    @JvmField
    val SOUND_EVENT: KryptonRegistry<SoundEvent> = simple(ResourceKeys.SOUND_EVENT, dataLoader(::SoundLoader))
    @JvmField
    val FLUID: KryptonDefaultedRegistry<KryptonFluid> = defaultedIntrusive(KryptonResourceKeys.FLUID, "empty") { KryptonFluids }
    @JvmField
    val BLOCK: KryptonDefaultedRegistry<KryptonBlock> = defaultedIntrusive(KryptonResourceKeys.BLOCK, "air") { KryptonBlocks }
    @JvmField
    val ENTITY_CATEGORIES: KryptonRegistry<EntityCategory> = simple(ResourceKeys.ENTITY_CATEGORIES, loader(RegistryLoaders.entityCategory()))
    @JvmField
    val ENTITY_TYPE: KryptonDefaultedRegistry<KryptonEntityType<*>> =
        defaultedIntrusive(KryptonResourceKeys.ENTITY_TYPE, "pig") { KryptonEntityTypes }
    @JvmField
    val ITEM: KryptonDefaultedRegistry<KryptonItemType> = defaultedIntrusive(KryptonResourceKeys.ITEM, "air", dataLoader(::ItemLoader))
    @JvmField
    val PARTICLE_TYPE: KryptonRegistry<ParticleType> = simple(ResourceKeys.PARTICLE_TYPE, loader(RegistryLoaders.particleType()))
    @JvmField
    val BLOCK_ENTITY_TYPE: KryptonRegistry<BlockEntityType<*>> = simple(ResourceKeys.BLOCK_ENTITY_TYPE, loader(RegistryLoaders.blockEntityType()))
    @JvmField
    val PAINTING_VARIANT: KryptonDefaultedRegistry<PaintingVariant> =
        defaulted(ResourceKeys.PAINTING_VARIANT, "kebab", loader(RegistryLoaders.paintingVariant()))
    @JvmField
    val STATISTIC_TYPE: KryptonRegistry<StatisticType<*>> = simple(ResourceKeys.STATISTIC_TYPE, loader(RegistryLoaders.statisticType()))
    @JvmField
    val CUSTOM_STATISTIC: KryptonRegistry<Key> = simple(ResourceKeys.CUSTOM_STATISTIC, loader(RegistryLoaders.customStatistic()))
    @JvmField
    val INVENTORY_TYPE: KryptonRegistry<InventoryType> =
        simple(ResourceKeys.INVENTORY_TYPE, loader(RegistryLoaders.inventoryType()))
    @JvmField
    val ATTRIBUTE: KryptonRegistry<AttributeType> = simple(ResourceKeys.ATTRIBUTE) { KryptonAttributeTypes }
    @JvmField
    val MEMORY_KEY: KryptonRegistry<MemoryKey<*>> = simple(KryptonResourceKeys.MEMORIES) { MemoryKeys }
    @JvmField
    val INT_PROVIDER_TYPE: KryptonRegistry<IntProviderType<*>> = simple(KryptonResourceKeys.INT_PROVIDER_TYPES) { IntProviderTypes }
    @JvmField
    val BANNER_PATTERN: KryptonRegistry<BannerPatternType> = simple(ResourceKeys.BANNER_PATTERN, loader(RegistryLoaders.bannerPatternType()))
    @JvmField
    val INSTRUMENT: KryptonRegistry<Instrument> = simple(KryptonResourceKeys.INSTRUMENTS) { Instruments }

    /*
     * Custom built-in registries
     */

    @JvmField
    val CRITERIA: KryptonRegistry<KeyedCriterion> = simple(ResourceKeys.CRITERIA, loader(RegistryLoaders.criterion()))
    @JvmField
    val DAMAGE_TYPES: KryptonRegistry<DamageType> = simple(ResourceKeys.DAMAGE_TYPES, loader(RegistryLoaders.damageType()))

    /*
     * Registry constructor functions
     */

    @JvmStatic
    private fun <T> simple(key: ResourceKey<out Registry<T>>, bootstrap: Bootstrap<T>): KryptonRegistry<T> =
        internalRegister(key, KryptonSimpleRegistry.standard(key), bootstrap)

    @JvmStatic
    private fun <T> defaulted(key: ResourceKey<out Registry<T>>, defaultName: String, bootstrap: Bootstrap<T>): KryptonDefaultedRegistry<T> =
        internalRegister(key, KryptonDefaultedSimpleRegistry.standard(key, Key.key(defaultName)), bootstrap)

    @JvmStatic
    private fun <T> defaultedIntrusive(key: ResourceKey<out Registry<T>>, defaultName: String,
                                       bootstrap: Bootstrap<T>): KryptonDefaultedRegistry<T> {
        return internalRegister(key, KryptonDefaultedSimpleRegistry.intrusive(key, Key.key(defaultName)), bootstrap)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    private fun <T, R : WritableRegistry<T>> internalRegister(key: ResourceKey<out Registry<T>>, registry: R, loader: Bootstrap<T>): R {
        LOADERS.put(key.location) { loader.run(registry) }
        WRITABLE_PARENT.register(key as ResourceKey<WritableRegistry<*>>, registry)
        return registry
    }

    /*
     * Registry registration functions. Designed to stop having to leak the WritableRegistry API in to other parts of the system,
     * as well as controlling how registry values are registered.
     */

    @JvmStatic
    fun <T, V : T> register(registry: KryptonRegistry<T>, key: Key, value: V): V =
        register(registry, KryptonResourceKey.of(registry.key, key), value)

    @JvmStatic
    fun <T, V : T> register(registry: KryptonRegistry<T>, key: ResourceKey<T>, value: V): V {
        (registry as WritableRegistry<T>).register(key, value)
        return value
    }

    /*
     * Bootstrap constructors. Used to create Bootstrap functions from registry loaders and data loaders.
     */

    @JvmStatic
    private fun <T> loader(loader: Supplier<RegistryLoader<T>>): Bootstrap<T> =
        Bootstrap { loader.get().forEach { key, value -> register(it, key, value) } }

    @JvmStatic
    private inline fun <T> dataLoader(crossinline loader: (KryptonRegistry<T>) -> KryptonDataLoader<T>): Bootstrap<T> =
        Bootstrap { loader(it).init() }

    /*
     * Bootstrapping methods. Used to initialize all the registries at the right time from the Bootstrap class.
     */

    @JvmStatic
    fun bootstrap() {
        WRITABLE_PARENT.freeze()
        runLoaders()
        WRITABLE_PARENT.forEach { it.freeze() }
        validateAll(WRITABLE_PARENT)
    }

    @JvmStatic
    private fun runLoaders() {
        LOADERS.forEach { (key, action) ->
            try {
                action.run()
            } catch (exception: Exception) {
                throw RegistryInitializationException("Failed to bootstrap registry $key!", exception)
            }
            requireNotNull(WRITABLE_PARENT.get(key)) { "Cannot find registry for key $key in loading!" }.freeze()
        }
    }

    @JvmStatic
    fun <T : Registry<*>> validateAll(parent: Registry<T>) {
        parent.forEach { registry ->
            if (registry.keys.isEmpty()) LOGGER.error("Registry ${registry.key} was empty after loading!")
            if (registry is KryptonDefaultedRegistry<*>) {
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

        fun run(registry: KryptonRegistry<T>)
    }

    private class RegistryInitializationException(message: String, cause: Throwable) : RuntimeException(message, cause)

    /**
     * The backend registry manager implementation. Moved inside KryptonRegistries as it needs access to internals that would rather
     * not be otherwise published and available to other components.
     */
    object ManagerImpl : RegistryManager {

        @Suppress("UNCHECKED_CAST")
        override fun <T> getRegistry(key: ResourceKey<out Registry<T>>): Registry<T>? {
            var registry = WRITABLE_PARENT.get(key as ResourceKey<WritableRegistry<*>>) as? Registry<T>
            if (registry == null) registry = KryptonDynamicRegistries.getRegistry(key as ResourceKey<out Registry<T>>)
            return registry
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T> getDefaultedRegistry(key: ResourceKey<out Registry<T>>): DefaultedRegistry<T>? =
            WRITABLE_PARENT.get(key as ResourceKey<WritableRegistry<*>>) as? DefaultedRegistry<T>
    }
}

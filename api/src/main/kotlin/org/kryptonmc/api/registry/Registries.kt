/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.entity.hanging.Canvas
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.util.provide
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeCategory
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.api.world.dimension.DimensionEffect
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.world.scoreboard.CollisionRule
import org.kryptonmc.api.world.scoreboard.Visibility
import org.kryptonmc.api.world.scoreboard.criteria.Criterion

/**
 * Holder of all of the built-in registries.
 */
@Suppress("UndocumentedPublicProperty")
public object Registries {

    private val MANAGER = FactoryProvider.INSTANCE.provide<RegistryManager>()

    /**
     * The parent registry. All registries should be a child of this registry.
     */
    @JvmField public val PARENT: Registry<out Registry<out Any>> = MANAGER.parent

    /**
     * All built-in vanilla registries.
     */
    @JvmField public val SOUND_EVENT: Registry<SoundEvent> = get(ResourceKeys.SOUND_EVENT)!!
    @JvmField public val ENTITY_TYPE: DefaultedRegistry<EntityType<*>> = getDefaulted(ResourceKeys.ENTITY_TYPE)!!
    @JvmField public val PARTICLE_TYPE: Registry<ParticleType> = get(ResourceKeys.PARTICLE_TYPE)!!
    @JvmField public val BLOCK: Registry<Block> = get(ResourceKeys.BLOCK)!!
    @JvmField public val ITEM: DefaultedRegistry<ItemType> = getDefaulted(ResourceKeys.ITEM)!!
    @JvmField public val MENU: Registry<InventoryType> = get(ResourceKeys.MENU)!!
    @JvmField public val ATTRIBUTE: Registry<AttributeType> = get(ResourceKeys.ATTRIBUTE)!!
    @JvmField public val BIOME: Registry<Biome> = create(ResourceKeys.BIOME)
    @JvmField public val STATISTIC_TYPE: Registry<StatisticType<*>> = get(ResourceKeys.STATISTIC_TYPE)!!
    @JvmField public val CUSTOM_STATISTIC: Registry<Key> = create(ResourceKeys.CUSTOM_STATISTIC)
    @JvmField public val CANVAS: DefaultedRegistry<Canvas> = getDefaulted(ResourceKeys.CANVAS)!!
    @JvmField public val FLUID: Registry<Fluid> = get(ResourceKeys.FLUID)!!

    /**
     * Custom built-in registries.
     */
    @JvmField public val GAMERULES: Registry<GameRule<Any>> = create(ResourceKeys.GAMERULES)
    @JvmField public val MODIFIER_OPERATIONS: Registry<ModifierOperation> = create(ResourceKeys.MODIFIER_OPERATIONS)
    @JvmField public val CRITERIA: Registry<Criterion> = create(ResourceKeys.CRITERIA)
    @JvmField public val VISIBILITIES: Registry<Visibility> = create(ResourceKeys.VISIBILITIES)
    @JvmField public val COLLISION_RULES: Registry<CollisionRule> = create(ResourceKeys.COLLISION_RULES)
    @JvmField public val GAME_MODES: Registry<GameMode> = create(ResourceKeys.GAME_MODES)
    @JvmField public val DIMENSION_TYPES: Registry<DimensionType> = create(ResourceKeys.DIMENSION_TYPES)
    @JvmField public val DIMENSION_EFFECTS: Registry<DimensionEffect> = create(ResourceKeys.DIMENSION_EFFECTS)
    @JvmField public val PRECIPITATIONS: Registry<Precipitation> = create(ResourceKeys.PRECIPITATIONS)
    @JvmField public val TEMPERATURE_MODIFIERS: Registry<TemperatureModifier> = create(ResourceKeys.TEMPERATURE_MODIFIERS)
    @JvmField public val GRASS_COLOR_MODIFIERS: Registry<GrassColorModifier> = create(ResourceKeys.GRASS_COLOR_MODIFIERS)
    @JvmField public val MUSIC: Registry<Music> = create(ResourceKeys.MUSIC)
    @JvmField public val BIOME_CATEGORIES: Registry<BiomeCategory> = create(ResourceKeys.BIOME_CATEGORIES)

    /**
     * Gets the existing registry with the given resource [key], or returns null
     * if there is no existing registry with the given resource [key].
     *
     * @param key the key
     * @return the existing registry, or null if not present
     */
    @JvmStatic
    public operator fun <T : Any> get(key: ResourceKey<out Registry<T>>): Registry<T>? = MANAGER[key]

    /**
     * Gets the existing defaulted registry with the given resource [key], or
     * returns null if there is no existing defaulted registry with the given
     * resource [key].
     *
     * @param key the key
     * @return the existing defaulted registry, or null if not present
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    public fun <T : Any> getDefaulted(key: ResourceKey<out Registry<T>>): DefaultedRegistry<T>? =
        MANAGER.getDefaulted(key)

    /**
     * Registers a new entry to the given [registry], with the given [key]
     * mapped to the given [value].
     *
     * @param registry the registry to register to
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    public fun <T : Any> register(registry: Registry<T>, key: String, value: T): T = register(registry, key(key), value)

    /**
     * Registers a new entry to the given [registry], with the given [key]
     * mapped to the given [value].
     *
     * @param registry the registry to register to
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    public fun <T : Any> register(registry: Registry<T>, key: Key, value: T): T = MANAGER.register(registry, key, value)

    /**
     * Registers a new entry to the given [registry], with the given [key]
     * mapped to the given [value].
     *
     * @param registry the registry to register to
     * @param id the ID of the entry in the registry
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    public fun <T : Any> register(registry: Registry<T>, id: Int, key: String, value: T): T =
        register(registry, id, key, value)

    /**
     * Registers a new entry to the given [registry], with the given [key]
     * mapped to the given [value].
     *
     * @param registry the registry to register to
     * @param id the ID of the entry in the registry
     * @param key the key
     * @param value the value
     */
    @JvmStatic
    public fun <T : Any> register(registry: Registry<T>, id: Int, key: Key, value: T): T =
        MANAGER.register(registry, id, key, value)

    /**
     * Creates a new registry with the given registry [key].
     *
     * @param key the registry key
     * @return a registry for the given [key]
     */
    @JvmStatic
    public fun <T : Any> create(key: ResourceKey<out Registry<T>>): Registry<T> = MANAGER.create(key)

    /**
     * Creates a new registry with the given registry [key], with a
     * [defaultKey].
     *
     * The default value for this registry will be the first value registered
     * that has a key that matches the given [defaultKey].
     *
     * @param key the registry key
     * @param defaultKey the default key
     * @return a defaulted registry for the given [key]
     */
    @JvmStatic
    public fun <T : Any> createDefaulted(
        key: ResourceKey<out Registry<T>>,
        defaultKey: Key
    ): DefaultedRegistry<T> = MANAGER.createDefaulted(key, defaultKey)
}

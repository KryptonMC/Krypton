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
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.user.ban.BanType
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.animal.type.CatType
import org.kryptonmc.api.entity.animal.type.MooshroomType
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.api.entity.animal.type.PandaGene
import org.kryptonmc.api.entity.animal.type.RabbitType
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.entity.hanging.Picture
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.meta.DyeColor
import org.kryptonmc.api.item.meta.MetaKey
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.scoreboard.CollisionRule
import org.kryptonmc.api.scoreboard.DisplaySlot
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.Visibility
import org.kryptonmc.api.scoreboard.criteria.Criterion
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.tags.TagType
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeCategory
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.api.world.dimension.DimensionEffect
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRule

/**
 * Holder of all of the built-in registries.
 */
@Catalogue(Registry::class)
public object Registries {

    /**
     * The parent registry. All registries should be a child of this registry.
     */
    @JvmField public val PARENT: Registry<out Registry<out Any>> = Krypton.registryManager.parent

    /**
     * All built-in vanilla registries.
     */
    @JvmField public val SOUND_EVENT: Registry<SoundEvent> = get(ResourceKeys.SOUND_EVENT)!!
    @JvmField public val ENTITY_TYPE: DefaultedRegistry<EntityType<*>> = defaulted(ResourceKeys.ENTITY_TYPE)!!
    @JvmField public val PARTICLE_TYPE: Registry<ParticleType> = get(ResourceKeys.PARTICLE_TYPE)!!
    @JvmField public val BLOCK: Registry<Block> = get(ResourceKeys.BLOCK)!!
    @JvmField public val ITEM: DefaultedRegistry<ItemType> = defaulted(ResourceKeys.ITEM)!!
    @JvmField public val MENU: Registry<InventoryType> = get(ResourceKeys.MENU)!!
    @JvmField public val ATTRIBUTE: Registry<AttributeType> = get(ResourceKeys.ATTRIBUTE)!!
    @JvmField public val BIOME: Registry<Biome> = get(ResourceKeys.BIOME)!!
    @JvmField public val STATISTIC_TYPE: Registry<StatisticType<*>> = get(ResourceKeys.STATISTIC_TYPE)!!
    @JvmField public val CUSTOM_STATISTIC: Registry<Key> = create(ResourceKeys.CUSTOM_STATISTIC)
    @JvmField public val PICTURE: DefaultedRegistry<Picture> = defaulted(ResourceKeys.PICTURE)!!
    @JvmField public val FLUID: Registry<Fluid> = get(ResourceKeys.FLUID)!!
    @JvmField public val DIMENSION_TYPE: Registry<DimensionType> = create(ResourceKeys.DIMENSION_TYPE)
    @JvmField public val BLOCK_ENTITY_TYPE: Registry<BlockEntityType> = get(ResourceKeys.BLOCK_ENTITY_TYPE)!!

    /**
     * Custom built-in registries.
     */
    @JvmField public val GAMERULES: Registry<GameRule<Any>> = create(ResourceKeys.GAMERULES)
    @JvmField public val MODIFIER_OPERATIONS: Registry<ModifierOperation> = create(ResourceKeys.MODIFIER_OPERATIONS)
    @JvmField public val CRITERIA: Registry<Criterion> = create(ResourceKeys.CRITERIA)
    @JvmField public val VISIBILITIES: Registry<Visibility> = create(ResourceKeys.VISIBILITIES)
    @JvmField public val COLLISION_RULES: Registry<CollisionRule> = create(ResourceKeys.COLLISION_RULES)
    @JvmField public val GAME_MODES: Registry<GameMode> = create(ResourceKeys.GAME_MODES)
    @JvmField public val DIMENSION_EFFECTS: Registry<DimensionEffect> = create(ResourceKeys.DIMENSION_EFFECTS)
    @JvmField public val PRECIPITATIONS: Registry<Precipitation> = create(ResourceKeys.PRECIPITATIONS)
    @JvmField public val TEMPERATURE_MODIFIERS: Registry<TemperatureModifier> = create(ResourceKeys.TEMPERATURE_MODIFIERS)
    @JvmField public val GRASS_COLOR_MODIFIERS: Registry<GrassColorModifier> = create(ResourceKeys.GRASS_COLOR_MODIFIERS)
    @JvmField public val MUSIC: Registry<Music> = create(ResourceKeys.MUSIC)
    @JvmField public val BIOME_CATEGORIES: Registry<BiomeCategory> = create(ResourceKeys.BIOME_CATEGORIES)
    @JvmField public val META_KEYS: Registry<MetaKey<*>> = create(ResourceKeys.META_KEYS)
    @JvmField public val ITEM_RARITIES: Registry<ItemRarity> = create(ResourceKeys.ITEM_RARITIES)
    @JvmField public val DISPLAY_SLOTS: Registry<DisplaySlot> = create(ResourceKeys.DISPLAY_SLOTS)
    @JvmField public val OBJECTIVE_RENDER_TYPES: Registry<ObjectiveRenderType> = create(ResourceKeys.OBJECTIVE_RENDER_TYPES)
    @JvmField public val MOB_CATEGORIES: Registry<EntityCategory> = create(ResourceKeys.MOB_CATEGORIES)
    @JvmField public val DYE_COLORS: Registry<DyeColor> = create(ResourceKeys.DYE_COLORS)
    @JvmField public val TAG_TYPES: Registry<TagType<*>> = create(ResourceKeys.TAG_TYPES)
    @JvmField public val BAN_TYPES: Registry<BanType> = create(ResourceKeys.BAN_TYPES)
    @JvmField public val CAT_TYPES: Registry<CatType> = create(ResourceKeys.CAT_TYPES)
    @JvmField public val MOOSHROOM_TYPES: Registry<MooshroomType> = create(ResourceKeys.MOOSHROOM_TYPES)
    @JvmField public val FOX_TYPES: Registry<FoxType> = create(ResourceKeys.FOX_TYPES)
    @JvmField public val PANDA_GENES: Registry<PandaGene> = create(ResourceKeys.PANDA_GENES)
    @JvmField public val RABBIT_TYPES: Registry<RabbitType> = create(ResourceKeys.RABBIT_TYPES)

    /**
     * Gets the existing registry with the given resource [key], or returns null
     * if there is no existing registry with the given resource [key].
     *
     * @param key the key
     * @return the existing registry, or null if not present
     */
    @JvmStatic
    public operator fun <T : Any> get(key: ResourceKey<out Registry<T>>): Registry<T>? = Krypton.registryManager[key]

    /**
     * Gets the existing defaulted registry with the given resource [key], or
     * returns null if there is no existing defaulted registry with the given
     * resource [key].
     *
     * @param key the key
     * @return the existing defaulted registry, or null if not present
     */
    @JvmStatic
    public fun <T : Any> defaulted(key: ResourceKey<out Registry<T>>): DefaultedRegistry<T>? = Krypton.registryManager.defaulted(key)

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
    public fun <T : Any> register(registry: Registry<T>, key: Key, value: T): T = Krypton.registryManager.register(registry, key, value)

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
    public fun <T : Any> register(registry: Registry<T>, id: Int, key: String, value: T): T = register(registry, id, key, value)

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
    public fun <T : Any> register(registry: Registry<T>, id: Int, key: Key, value: T): T = Krypton.registryManager.register(registry, id, key, value)

    /**
     * Creates a new registry with the given registry [key].
     *
     * @param key the registry key
     * @return a registry for the given [key]
     */
    @JvmStatic
    public fun <T : Any> create(key: ResourceKey<out Registry<T>>): Registry<T> = Krypton.registryManager.create(key)

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
    ): DefaultedRegistry<T> = Krypton.registryManager.createDefaulted(key, defaultKey)
}

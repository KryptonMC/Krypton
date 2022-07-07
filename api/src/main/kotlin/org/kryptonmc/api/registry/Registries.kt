/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.registry

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.entity.hanging.Picture
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.api.scoreboard.criteria.Criterion
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.tags.TagType
import org.kryptonmc.api.user.ban.BanType
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.damage.type.DamageType
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
    @JvmField
    public val PARENT: Registry<out Registry<out Any>> = Krypton.registryManager.parent

    /**
     * All built-in vanilla registries.
     */
    @JvmField
    public val SOUND_EVENT: Registry<SoundEvent> = create(ResourceKeys.SOUND_EVENT)
    @JvmField
    public val ENTITY_TYPE: DefaultedRegistry<EntityType<*>> = createDefaulted(ResourceKeys.ENTITY_TYPE, Key.key("pig"))
    @JvmField
    public val PARTICLE_TYPE: Registry<ParticleType> = create(ResourceKeys.PARTICLE_TYPE)
    @JvmField
    public val BLOCK: Registry<Block> = create(ResourceKeys.BLOCK)
    @JvmField
    public val ITEM: DefaultedRegistry<ItemType> = createDefaulted(ResourceKeys.ITEM, Key.key("air"))
    @JvmField
    public val INVENTORY_TYPES: Registry<InventoryType> = create(ResourceKeys.INVENTORY_TYPES)
    @JvmField
    public val ATTRIBUTE: Registry<AttributeType> = create(ResourceKeys.ATTRIBUTE)
    @JvmField
    public val BIOME: Registry<Biome> = create(ResourceKeys.BIOME)
    @JvmField
    public val STATISTIC_TYPE: Registry<StatisticType<*>> = create(ResourceKeys.STATISTIC_TYPE)
    @JvmField
    public val CUSTOM_STATISTIC: Registry<Key> = create(ResourceKeys.CUSTOM_STATISTIC)
    @JvmField
    public val FLUID: DefaultedRegistry<Fluid> = createDefaulted(ResourceKeys.FLUID, Key.key("empty"))
    @JvmField
    public val DIMENSION_TYPE: Registry<DimensionType> = create(ResourceKeys.DIMENSION_TYPE)
    @JvmField
    public val BLOCK_ENTITY_TYPE: Registry<BlockEntityType> = create(ResourceKeys.BLOCK_ENTITY_TYPE)
    @JvmField
    public val BANNER_PATTERN: Registry<BannerPatternType> = create(ResourceKeys.BANNER_PATTERN)

    /**
     * Custom built-in registries.
     */
    @JvmField
    public val PICTURES: DefaultedRegistry<Picture> = createDefaulted(ResourceKeys.PICTURES, Key.key("kebab"))
    @JvmField
    public val GAME_RULES: Registry<GameRule<Any>> = create(ResourceKeys.GAME_RULES)
    @JvmField
    public val MODIFIER_OPERATIONS: Registry<ModifierOperation> = create(ResourceKeys.MODIFIER_OPERATIONS)
    @JvmField
    public val CRITERIA: Registry<Criterion> = create(ResourceKeys.CRITERIA)
    @JvmField
    public val DIMENSION_EFFECTS: Registry<DimensionEffect> = create(ResourceKeys.DIMENSION_EFFECTS)
    @JvmField
    public val ITEM_RARITIES: Registry<ItemRarity> = create(ResourceKeys.ITEM_RARITIES)
    @JvmField
    public val ENTITY_CATEGORIES: Registry<EntityCategory> = create(ResourceKeys.ENTITY_CATEGORIES)
    @JvmField
    public val DYE_COLORS: Registry<DyeColor> = create(ResourceKeys.DYE_COLORS)
    @JvmField
    public val TAG_TYPES: Registry<TagType<*>> = create(ResourceKeys.TAG_TYPES)
    @JvmField
    public val BAN_TYPES: Registry<BanType> = create(ResourceKeys.BAN_TYPES)
    @JvmField
    public val DAMAGE_TYPES: Registry<DamageType> = create(ResourceKeys.DAMAGE_TYPES)

    /**
     * Gets the existing registry with the given resource [key], or returns null
     * if there is no existing registry with the given resource [key].
     *
     * @param key the key
     * @return the existing registry, or null if not present
     */
    @JvmStatic
    public fun <T : Any> registry(key: ResourceKey<out Registry<T>>): Registry<T>? = Krypton.registryManager.registry(key)

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
     * Creates a new registry with the given registry [key].
     *
     * @param key the registry key
     * @return a registry for the given [key]
     */
    @JvmStatic
    @Contract("_ -> new", pure = true)
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
    @Contract("_ -> new", pure = true)
    public fun <T : Any> createDefaulted(key: ResourceKey<out Registry<T>>, defaultKey: Key): DefaultedRegistry<T> =
        Krypton.registryManager.createDefaulted(key, defaultKey)
}

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
package org.kryptonmc.api.resource

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.banner.BannerPatternType
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.hanging.PaintingVariant
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.scoreboard.criteria.KeyedCriterion
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.damage.type.DamageType
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All the built-in registry keys for various registries.
 */
@Catalogue(ResourceKey::class)
public object ResourceKeys {

    /**
     * Built-in vanilla registries.
     */
    @JvmField
    public val SOUND_EVENT: ResourceKey<out Registry<SoundEvent>> = minecraft("sound_event")
    @JvmField
    public val ENTITY_TYPE: ResourceKey<out Registry<EntityType<*>>> = minecraft("entity_type")
    @JvmField
    public val PARTICLE_TYPE: ResourceKey<out Registry<ParticleType>> = minecraft("particle_type")
    @JvmField
    public val BLOCK: ResourceKey<out Registry<Block>> = minecraft("block")
    @JvmField
    public val ITEM: ResourceKey<out Registry<ItemType>> = minecraft("item")
    @JvmField
    public val DIMENSION: ResourceKey<out Registry<World>> = minecraft("dimension")
    @JvmField
    public val ATTRIBUTE: ResourceKey<out Registry<AttributeType>> = minecraft("attribute")
    @JvmField
    public val BIOME: ResourceKey<out Registry<Biome>> = minecraft("worldgen/biome")
    @JvmField
    public val INVENTORY_TYPE: ResourceKey<out Registry<InventoryType>> = minecraft("menu")
    @JvmField
    public val STATISTIC_TYPE: ResourceKey<out Registry<StatisticType<*>>> = minecraft("stat_type")
    @JvmField
    public val CUSTOM_STATISTIC: ResourceKey<out Registry<Key>> = minecraft("custom_stat")
    @JvmField
    public val FLUID: ResourceKey<out Registry<Fluid>> = minecraft("fluid")
    @JvmField
    public val DIMENSION_TYPE: ResourceKey<out Registry<DimensionType>> = minecraft("dimension_type")
    @JvmField
    public val BLOCK_ENTITY_TYPE: ResourceKey<out Registry<BlockEntityType<*>>> = minecraft("block_entity_type")
    @JvmField
    public val BANNER_PATTERN: ResourceKey<out Registry<BannerPatternType>> = minecraft("banner_pattern")
    @JvmField
    public val PAINTING_VARIANT: ResourceKey<out Registry<PaintingVariant>> = minecraft("painting_variant")

    /**
     * Custom built-in registries.
     */
    @JvmField
    public val CRITERIA: ResourceKey<out Registry<KeyedCriterion>> = krypton("criteria")
    @JvmField
    public val ENTITY_CATEGORIES: ResourceKey<out Registry<EntityCategory>> = krypton("entity_categories")
    @JvmField
    public val DAMAGE_TYPES: ResourceKey<out Registry<DamageType>> = krypton("damage_types")

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.MINECRAFT] as its root.
     *
     * @param T the resource key type
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    @Contract("_ -> new", pure = true)
    public fun <T> minecraft(key: String): ResourceKey<out Registry<T>> = ResourceKey.of(RegistryRoots.MINECRAFT, Key.key(key))

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.KRYPTON] as its root.
     *
     * @param T the resource key type
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    @Contract("_ -> new", pure = true)
    public fun <T> krypton(key: String): ResourceKey<out Registry<T>> = ResourceKey.of(RegistryRoots.KRYPTON, Key.key("krypton", key))
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.resource

import net.kyori.adventure.key.Key
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.user.ban.BanType
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.effect.Music
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.animal.type.AxolotlVariant
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
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.BiomeCategory
import org.kryptonmc.api.world.biome.GrassColorModifier
import org.kryptonmc.api.world.biome.Precipitation
import org.kryptonmc.api.world.biome.TemperatureModifier
import org.kryptonmc.api.world.dimension.DimensionEffect
import org.kryptonmc.api.world.dimension.DimensionType
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.scoreboard.CollisionRule
import org.kryptonmc.api.scoreboard.DisplaySlot
import org.kryptonmc.api.scoreboard.ObjectiveRenderType
import org.kryptonmc.api.scoreboard.Visibility
import org.kryptonmc.api.scoreboard.criteria.Criterion
import org.kryptonmc.api.tags.TagType
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.api.world.damage.type.DamageType

/**
 * All the built-in registry keys for various registries.
 */
@Catalogue(ResourceKey::class)
public object ResourceKeys {

    /**
     * The key of the parent registry.
     */
    @JvmField public val PARENT: ResourceKey<out Registry<Registry<out Any>>> = minecraft("root")

    /**
     * Built-in vanilla registries.
     */
    @JvmField public val SOUND_EVENT: ResourceKey<out Registry<SoundEvent>> = minecraft("sound_event")
    @JvmField public val ENTITY_TYPE: ResourceKey<out Registry<EntityType<*>>> = minecraft("entity_type")
    @JvmField public val PARTICLE_TYPE: ResourceKey<out Registry<ParticleType>> = minecraft("particle_type")
    @JvmField public val BLOCK: ResourceKey<out Registry<Block>> = minecraft("block")
    @JvmField public val ITEM: ResourceKey<out Registry<ItemType>> = minecraft("item")
    @JvmField public val DIMENSION: ResourceKey<out Registry<World>> = minecraft("dimension")
    @JvmField public val ATTRIBUTE: ResourceKey<out Registry<AttributeType>> = minecraft("attribute")
    @JvmField public val BIOME: ResourceKey<out Registry<Biome>> = minecraft("worldgen/biome")
    @JvmField public val MENU: ResourceKey<out Registry<InventoryType>> = minecraft("menu")
    @JvmField public val STATISTIC_TYPE: ResourceKey<out Registry<StatisticType<*>>> = minecraft("stat_type")
    @JvmField public val CUSTOM_STATISTIC: ResourceKey<out Registry<Key>> = minecraft("custom_stat")
    @JvmField public val PICTURE: ResourceKey<out Registry<Picture>> = minecraft("motive")
    @JvmField public val FLUID: ResourceKey<out Registry<Fluid>> = minecraft("fluid")
    @JvmField public val DIMENSION_TYPE: ResourceKey<out Registry<DimensionType>> = minecraft("dimension_type")
    @JvmField public val BLOCK_ENTITY_TYPE: ResourceKey<out Registry<BlockEntityType>> = minecraft("block_entity")

    /**
     * Custom built-in registries.
     */
    @JvmField public val GAMERULES: ResourceKey<out Registry<GameRule<Any>>> = krypton("gamerules")
    @JvmField public val MODIFIER_OPERATIONS: ResourceKey<out Registry<ModifierOperation>> = krypton("attribute_modifier_operations")
    @JvmField public val CRITERIA: ResourceKey<out Registry<Criterion>> = krypton("criteria")
    @JvmField public val VISIBILITIES: ResourceKey<out Registry<Visibility>> = krypton("visibilities")
    @JvmField public val COLLISION_RULES: ResourceKey<out Registry<CollisionRule>> = krypton("collision_rules")
    @JvmField public val GAME_MODES: ResourceKey<out Registry<GameMode>> = krypton("game_modes")
    @JvmField public val DIMENSION_EFFECTS: ResourceKey<out Registry<DimensionEffect>> = krypton("dimension_effects")
    @JvmField public val PRECIPITATIONS: ResourceKey<out Registry<Precipitation>> = krypton("precipitations")
    @JvmField public val TEMPERATURE_MODIFIERS: ResourceKey<out Registry<TemperatureModifier>> = krypton("temperature_modifiers")
    @JvmField public val GRASS_COLOR_MODIFIERS: ResourceKey<out Registry<GrassColorModifier>> = krypton("grass_color_modifiers")
    @JvmField public val MUSIC: ResourceKey<out Registry<Music>> = krypton("music")
    @JvmField public val BIOME_CATEGORIES: ResourceKey<out Registry<BiomeCategory>> = krypton("biome_categories")
    @JvmField public val META_KEYS: ResourceKey<out Registry<MetaKey<*>>> = krypton("meta_keys")
    @JvmField public val ITEM_RARITIES: ResourceKey<out Registry<ItemRarity>> = krypton("item_rarities")
    @JvmField public val DISPLAY_SLOTS: ResourceKey<out Registry<DisplaySlot>> = krypton("display_slots")
    @JvmField public val OBJECTIVE_RENDER_TYPES: ResourceKey<out Registry<ObjectiveRenderType>> = krypton("objective_render_types")
    @JvmField public val MOB_CATEGORIES: ResourceKey<out Registry<EntityCategory>> = krypton("mob_categories")
    @JvmField public val DYE_COLORS: ResourceKey<out Registry<DyeColor>> = krypton("dye_colors")
    @JvmField public val TAG_TYPES: ResourceKey<out Registry<TagType<*>>> = krypton("tag_types")
    @JvmField public val BAN_TYPES: ResourceKey<out Registry<BanType>> = krypton("ban_types")
    @JvmField public val CAT_TYPES: ResourceKey<out Registry<CatType>> = krypton("cat_types")
    @JvmField public val MOOSHROOM_TYPES: ResourceKey<out Registry<MooshroomType>> = krypton("mooshroom_types")
    @JvmField public val FOX_TYPES: ResourceKey<out Registry<FoxType>> = krypton("fox_types")
    @JvmField public val PANDA_GENES: ResourceKey<out Registry<PandaGene>> = krypton("panda_genes")
    @JvmField public val RABBIT_TYPES: ResourceKey<out Registry<RabbitType>> = krypton("rabbit_types")
    @JvmField public val AXOLOTL_VARIANTS: ResourceKey<out Registry<AxolotlVariant>> = krypton("axolotl_variants")
    @JvmField public val DAMAGE_SOURCES: ResourceKey<out Registry<DamageType>> = krypton("damage_sources")

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.MINECRAFT] as its root.
     *
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    @Contract("_ -> new", pure = true)
    public fun <T : Any> minecraft(key: String): ResourceKey<out Registry<T>> = ResourceKey.of(RegistryRoots.MINECRAFT, Key.key(key))

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.KRYPTON] as its root.
     *
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    @Contract("_ -> new", pure = true)
    public fun <T : Any> krypton(key: String): ResourceKey<out Registry<T>> = ResourceKey.of(
        RegistryRoots.KRYPTON,
        Key.key("krypton", key)
    )
}

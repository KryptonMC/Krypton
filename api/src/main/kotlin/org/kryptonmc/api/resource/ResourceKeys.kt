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
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.particle.Particle
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.entity.hanging.Canvas
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.inventory.InventoryType
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.statistic.StatisticType
import org.kryptonmc.api.world.World
import org.kryptonmc.api.world.rule.GameRule
import org.kryptonmc.api.world.scoreboard.criteria.Criterion

/**
 * All the built-in registry keys for various registries.
 */
object ResourceKeys {

    // @formatter:off
    /**
     * The key of the parent registry.
     */
    @JvmField val PARENT = minecraft<Registry<out Any>>("root")

    /**
     * Built-in vanilla registries
     */
    @JvmField val SOUND_EVENT = minecraft<SoundEvent>("sound_event")
    @JvmField val ENTITY_TYPE = minecraft<EntityType<*>>("entity_type")
    @JvmField val PARTICLE_TYPE = minecraft<Particle>("particle_type")
    @JvmField val BLOCK = minecraft<Block>("block")
    @JvmField val ITEM = minecraft<ItemType>("item")
    @JvmField val DIMENSION = minecraft<World>("dimension")
    @JvmField val ATTRIBUTE = minecraft<AttributeType>("attribute")
    @JvmField val MENU = minecraft<InventoryType>("menu")
    @JvmField val STATISTIC_TYPE = minecraft<StatisticType<*>>("stat_type")
    @JvmField val CUSTOM_STATISTIC = minecraft<Key>("custom_stat")
    @JvmField val CANVAS = minecraft<Canvas>("motive")
    @JvmField val FLUID = minecraft<Fluid>("fluid")

    /**
     * Custom built-in registries
     */
    @JvmField val GAMERULES = krypton<GameRule<Any>>("gamerules")
    @JvmField val MODIFIER_OPERATIONS = krypton<ModifierOperation>("attribute_modifier_operations")
    @JvmField val CRITERIA = krypton<Criterion>("criteria")
    // @formatter:on

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.MINECRAFT] as its root.
     *
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    fun <T : Any> minecraft(key: String): ResourceKey<out Registry<T>> = ResourceKey.of(RegistryRoots.MINECRAFT, Key.key(key))

    /**
     * Creates a new registry key with the given [key] as its base key.
     *
     * This will use [RegistryRoots.KRYPTON] as its root.
     *
     * @param key the key
     * @return a new registry key
     */
    @JvmStatic
    fun <T : Any> krypton(key: String): ResourceKey<out Registry<T>> = ResourceKey.of(RegistryRoots.KRYPTON, Key.key("krypton", key))
}

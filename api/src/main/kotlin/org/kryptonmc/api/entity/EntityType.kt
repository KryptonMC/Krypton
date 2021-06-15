/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.key.Keyed

/**
 * An enum for every single entity in vanilla Minecraft.
 *
 * @param isLiving if this type of entity is a [LivingEntity]
 */
enum class EntityType(
    internalKey: Key? = null,
    val isLiving: Boolean = true
) : Keyed {

    AREA_EFFECT_CLOUD(isLiving = false),
    ARMOR_STAND(isLiving = false),
    ARROW(isLiving = false),
    AXOLOTL,
    BAT,
    BEE,
    BLAZE,
    BOAT(isLiving = false),
    CAT,
    CAVE_SPIDER,
    CHICKEN,
    COD,
    COW,
    CREEPER,
    DOLPHIN,
    DONKEY,
    DRAGON_FIREBALL(isLiving = false),
    DROWNED,
    ELDER_GUARDIAN,
    END_CRYSTAL(isLiving = false),
    ENDER_DRAGON,
    ENDERMAN,
    ENDERMTIE,
    EVOKER,
    EVOKER_FANGS(isLiving = false),
    EXPERIENCE_ORB,
    EYE_OF_ENDER(isLiving = false),
    FALLING_BLOCK(isLiving = false),
    FIREWORK_ROCKET(isLiving = false),
    FOX,
    GHAST,
    GIANT,
    GLOW_ITEM_FRAME(isLiving = false),
    GLOW_SQUID,
    GOAT,
    GUARDIAN,
    HOGLIN,
    HORSE,
    HUSK,
    ILLUSIONER,
    IRON_GOLEM,
    ITEM(isLiving = false),
    ITEM_FRAME(isLiving = false),
    FIREBALL(isLiving = false),
    LEASH_KNOT(isLiving = false),
    LIGHTNING_BOLT(isLiving = false),
    LLAMA,
    LLAMA_SPIT(isLiving = false),
    MAGMA_CUBE,
    MARKER,
    MINECART(isLiving = false),
    CHEST_MINECART(isLiving = false),
    COMMAND_BLOCK_MINECART(key("commandblock_minecart"), isLiving = false),
    FURNACE_MINECART(isLiving = false),
    HOPPER_MINECART(isLiving = false),
    SPAWNER_MINECART(isLiving = false),
    TNT_MINECART(isLiving = false),
    MULE,
    MOOSHROOM,
    OCELOT,
    PAINTING,
    PANDA,
    PARROT,
    PHANTOM,
    PIG,
    PIGLIN,
    PIGLIN_BRUTE,
    PILLAGER,
    POLAR_BEAR,
    PRIMED_TNT(key("tnt")),
    PUFFERFISH,
    RABBIT,
    RAVAGER,
    SALMON,
    SHEEP,
    SHULKER,
    SHULKER_BULLET(isLiving = false),
    SILVERFISH,
    SKELETON,
    SKELETON_HORSE,
    SLIME,
    SMALL_FIREBALL(isLiving = false),
    SNOW_GOLEM,
    SNOWBALL(isLiving = false),
    SPECTRAL_ARROW(isLiving = false),
    SPIDER,
    SQUID,
    STRAY,
    STRIDER,
    EGG(isLiving = false),
    ENDER_PEARL(isLiving = false),
    EXPERIENCE_BOTTLE(isLiving = false),
    POTION(isLiving = false),
    TRIDENT(isLiving = false),
    TRADER_LLAMA,
    TROPICAL_FISH,
    TURTLE,
    VEX,
    VILLAGER,
    VINDICATOR,
    WANDERING_TRADER,
    WITCH,
    WITHER,
    WITHER_SKELETON,
    WITHER_SKULL(isLiving = false),
    WOLF,
    ZOGLIN,
    ZOMBIE,
    ZOMBIE_HORSE,
    ZOMBIE_VILLAGER,
    ZOMBIFIED_PIGLIN,
    PLAYER,
    FISHING_HOOK(key("fishing_bobber"), isLiving = false);

    private val key = internalKey ?: key(name.lowercase())

    override fun key() = key
}

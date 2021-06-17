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
import org.kryptonmc.api.entity.monster.Zombie
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.registry.Registries

/**
 * All the types of entities in the game.
 */
@Suppress("UndocumentedPublicProperty")
object EntityTypes {

    // TODO: Make each of these be of their respective entity types when they exist
    // @formatter:off
    val AREA_EFFECT_CLOUD = register<Entity>("area_effect_cloud")
    val ARMOR_STAND = register<Entity>("armor_stand")
    val ARROW = register<Entity>("arrow")
    val AXOLOTL = register<Entity>("axolotl")
    val BAT = register<Entity>("bat")
    val BEE = register<Entity>("bee")
    val BLAZE = register<Entity>("blaze")
    val BOAT = register<Entity>("boat")
    val CAT = register<Entity>("cat")
    val CAVE_SPIDER = register<Entity>("cave_spider")
    val CHICKEN = register<Entity>("chicken")
    val COD = register<Entity>("cod")
    val COW = register<Entity>("cow")
    val CREEPER = register<Entity>("creeper")
    val DOLPHIN = register<Entity>("dolphin")
    val DONKEY = register<Entity>("donkey")
    val DRAGON_FIREBALL = register<Entity>("dragon_fireball")
    val DROWNED = register<Entity>("drowned")
    val ELDER_GUARDIAN = register<Entity>("elder_guardian")
    val END_CRYSTAL = register<Entity>("end_crystal")
    val ENDER_DRAGON = register<Entity>("ender_dragon")
    val ENDERMAN = register<Entity>("enderman")
    val ENDERMTIE = register<Entity>("endermite")
    val EVOKER = register<Entity>("evoker")
    val EVOKER_FANGS = register<Entity>("evoker_fangs")
    val EXPERIENCE_ORB = register<Entity>("experience_orb")
    val EYE_OF_ENDER = register<Entity>("eye_of_ender")
    val FALLING_BLOCK = register<Entity>("falling_block")
    val FIREWORK_ROCKET = register<Entity>("firework_rocket")
    val FOX = register<Entity>("fox")
    val GHAST = register<Entity>("ghast")
    val GIANT = register<Entity>("giant")
    val GLOW_ITEM_FRAME = register<Entity>("glow_item_frame")
    val GLOW_SQUID = register<Entity>("glow_squid")
    val GOAT = register<Entity>("goat")
    val GUARDIAN = register<Entity>("guardian")
    val HOGLIN = register<Entity>("hoglin")
    val HORSE = register<Entity>("horse")
    val HUSK = register<Entity>("husk")
    val ILLUSIONER = register<Entity>("illusioner")
    val IRON_GOLEM = register<Entity>("iron_golem")
    val ITEM = register<Entity>("item")
    val ITEM_FRAME = register<Entity>("item_frame")
    val FIREBALL = register<Entity>("fireball")
    val LEASH_KNOT = register<Entity>("leash_knot")
    val LIGHTNING_BOLT = register<Entity>("lightning_bolt")
    val LLAMA = register<Entity>("llama")
    val LLAMA_SPIT = register<Entity>("llama_spit")
    val MAGMA_CUBE = register<Entity>("magma_cube")
    val MARKER = register<Entity>("marker")
    val MINECART = register<Entity>("minecart")
    val CHEST_MINECART = register<Entity>("chest_minecart")
    val COMMAND_BLOCK_MINECART = register<Entity>("command_block_minecart")
    val FURNACE_MINECART = register<Entity>("furnace_minecart")
    val HOPPER_MINECART = register<Entity>("hopper_minecart")
    val SPAWNER_MINECART = register<Entity>("spawner_minecart")
    val TNT_MINECART = register<Entity>("tnt_minecart")
    val MULE = register<Entity>("mule")
    val MOOSHROOM = register<Entity>("mooshroom")
    val OCELOT = register<Entity>("ocelot")
    val PAINTING = register<Entity>("painting")
    val PANDA = register<Entity>("panda")
    val PARROT = register<Entity>("parrot")
    val PHANTOM = register<Entity>("phantom")
    val PIG = register<Entity>("pig")
    val PIGLIN = register<Entity>("piglin")
    val PIGLIN_BRUTE = register<Entity>("piglin_brute")
    val PILLAGER = register<Entity>("pillager")
    val POLAR_BEAR = register<Entity>("polar_bear")
    val PRIMED_TNT = register<Entity>("primed_tnt")
    val PUFFERFISH = register<Entity>("pufferfish")
    val RABBIT = register<Entity>("rabbit")
    val RAVAGER = register<Entity>("ravager")
    val SALMON = register<Entity>("salmon")
    val SHEEP = register<Entity>("sheep")
    val SHULKER = register<Entity>("shulker")
    val SHULKER_BULLET = register<Entity>("shulker_bullet")
    val SILVERFISH = register<Entity>("silverfish")
    val SKELETON = register<Entity>("skeleton")
    val SKELETON_HORSE = register<Entity>("skeleton_horse")
    val SLIME = register<Entity>("slime")
    val SMALL_FIREBALL = register<Entity>("small_fireball")
    val SNOW_GOLEM = register<Entity>("snow_golem")
    val SNOWBALL = register<Entity>("snowball")
    val SPECTRAL_ARROW = register<Entity>("spectral_arrow")
    val SPIDER = register<Entity>("spider")
    val SQUID = register<Entity>("squid")
    val STRAY = register<Entity>("stray")
    val STRIDER = register<Entity>("strider")
    val EGG = register<Entity>("egg")
    val ENDER_PEARL = register<Entity>("ender_pearl")
    val EXPERIENCE_BOTTLE = register<Entity>("experience_bottle")
    val POTION = register<Entity>("potion")
    val TRIDENT = register<Entity>("trident")
    val TRADER_LLAMA = register<Entity>("trader_llama")
    val TROPICAL_FISH = register<Entity>("tropical_fish")
    val TURTLE = register<Entity>("turtle")
    val VEX = register<Entity>("vex")
    val VILLAGER = register<Entity>("villager")
    val VINDICATOR = register<Entity>("vindicator")
    val WANDERING_TRADER = register<Entity>("wandering_trader")
    val WITCH = register<Entity>("witch")
    val WITHER = register<Entity>("wither")
    val WITHER_SKELETON = register<Entity>("wither_skeleton")
    val WITHER_SKULL = register<Entity>("wither_skull")
    val WOLF = register<Entity>("wolf")
    val ZOGLIN = register<Entity>("zoglin")
    val ZOMBIE = register<Zombie>("zombie")
    val ZOMBIE_HORSE = register<Entity>("zombie_horse")
    val ZOMBIE_VILLAGER = register<Entity>("zombie_villager")
    val ZOMBIFIED_PIGLIN = register<Entity>("zombified_piglin")
    val PLAYER = register<Player>("player")
    val FISHING_HOOK = register<Entity>("fishing_hook")
    // @formatter:on

    @Suppress("UNCHECKED_CAST") // This should never fail here
    private fun <T : Entity> register(key: String): EntityType<T> =
        Registries.register(Registries.ENTITY_TYPE, key, EntityType<T>(Key.key(key))) as EntityType<T>
}

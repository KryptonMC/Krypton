/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.ambient.Bat
import org.kryptonmc.api.entity.animal.Axolotl
import org.kryptonmc.api.entity.animal.Bee
import org.kryptonmc.api.entity.animal.Chicken
import org.kryptonmc.api.entity.animal.Cat
import org.kryptonmc.api.entity.animal.Fox
import org.kryptonmc.api.entity.animal.Ocelot
import org.kryptonmc.api.entity.animal.Cow
import org.kryptonmc.api.entity.animal.Goat
import org.kryptonmc.api.entity.animal.Mooshroom
import org.kryptonmc.api.entity.animal.Panda
import org.kryptonmc.api.entity.animal.Parrot
import org.kryptonmc.api.entity.animal.Pig
import org.kryptonmc.api.entity.animal.PolarBear
import org.kryptonmc.api.entity.animal.Rabbit
import org.kryptonmc.api.entity.animal.Sheep
import org.kryptonmc.api.entity.animal.Turtle
import org.kryptonmc.api.entity.animal.Wolf
import org.kryptonmc.api.entity.aquatic.Cod
import org.kryptonmc.api.entity.aquatic.Dolphin
import org.kryptonmc.api.entity.aquatic.GlowSquid
import org.kryptonmc.api.entity.aquatic.Pufferfish
import org.kryptonmc.api.entity.aquatic.Salmon
import org.kryptonmc.api.entity.aquatic.Squid
import org.kryptonmc.api.entity.aquatic.TropicalFish
import org.kryptonmc.api.entity.hanging.Painting
import org.kryptonmc.api.entity.monster.Creeper
import org.kryptonmc.api.entity.monster.Zombie
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.entity.projectile.Arrow
import org.kryptonmc.api.entity.projectile.DragonFireball
import org.kryptonmc.api.entity.projectile.Egg
import org.kryptonmc.api.entity.projectile.EnderPearl
import org.kryptonmc.api.entity.projectile.ExperienceBottle
import org.kryptonmc.api.entity.projectile.FireworkRocket
import org.kryptonmc.api.entity.projectile.FishingHook
import org.kryptonmc.api.entity.projectile.LargeFireball
import org.kryptonmc.api.entity.projectile.LlamaSpit
import org.kryptonmc.api.entity.projectile.ShulkerBullet
import org.kryptonmc.api.entity.projectile.SmallFireball
import org.kryptonmc.api.entity.projectile.Snowball
import org.kryptonmc.api.entity.projectile.SpectralArrow
import org.kryptonmc.api.entity.projectile.ThrownPotion
import org.kryptonmc.api.entity.projectile.Trident
import org.kryptonmc.api.entity.projectile.WitherSkull
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All the types of entities in the game.
 */
@Catalogue(EntityType::class)
public object EntityTypes {

    // TODO: Make each of these be of their respective entity types when they exist
    // @formatter:off
    @JvmField public val AREA_EFFECT_CLOUD: EntityType<AreaEffectCloud> = get("area_effect_cloud")
    @JvmField public val ARMOR_STAND: EntityType<ArmorStand> = get("armor_stand")
    @JvmField public val ARROW: EntityType<Arrow> = get("arrow")
    @JvmField public val AXOLOTL: EntityType<Axolotl> = get("axolotl")
    @JvmField public val BAT: EntityType<Bat> = get("bat")
    @JvmField public val BEE: EntityType<Bee> = get("bee")
    @JvmField public val BLAZE: EntityType<Entity> = get("blaze")
    @JvmField public val BOAT: EntityType<Entity> = get("boat")
    @JvmField public val CAT: EntityType<Cat> = get("cat")
    @JvmField public val CAVE_SPIDER: EntityType<Entity> = get("cave_spider")
    @JvmField public val CHICKEN: EntityType<Chicken> = get("chicken")
    @JvmField public val COD: EntityType<Cod> = get("cod")
    @JvmField public val COW: EntityType<Cow> = get("cow")
    @JvmField public val CREEPER: EntityType<Creeper> = get("creeper")
    @JvmField public val DOLPHIN: EntityType<Dolphin> = get("dolphin")
    @JvmField public val DONKEY: EntityType<Entity> = get("donkey")
    @JvmField public val DRAGON_FIREBALL: EntityType<DragonFireball> = get("dragon_fireball")
    @JvmField public val DROWNED: EntityType<Entity> = get("drowned")
    @JvmField public val ELDER_GUARDIAN: EntityType<Entity> = get("elder_guardian")
    @JvmField public val END_CRYSTAL: EntityType<Entity> = get("end_crystal")
    @JvmField public val ENDER_DRAGON: EntityType<Entity> = get("ender_dragon")
    @JvmField public val ENDERMAN: EntityType<Entity> = get("enderman")
    @JvmField public val ENDERMTIE: EntityType<Entity> = get("endermite")
    @JvmField public val EVOKER: EntityType<Entity> = get("evoker")
    @JvmField public val EVOKER_FANGS: EntityType<Entity> = get("evoker_fangs")
    @JvmField public val EXPERIENCE_ORB: EntityType<ExperienceOrb> = get("experience_orb")
    @JvmField public val EYE_OF_ENDER: EntityType<Entity> = get("eye_of_ender")
    @JvmField public val FALLING_BLOCK: EntityType<Entity> = get("falling_block")
    @JvmField public val FIREWORK_ROCKET: EntityType<FireworkRocket> = get("firework_rocket")
    @JvmField public val FOX: EntityType<Fox> = get("fox")
    @JvmField public val GHAST: EntityType<Entity> = get("ghast")
    @JvmField public val GIANT: EntityType<Entity> = get("giant")
    @JvmField public val GLOW_ITEM_FRAME: EntityType<Entity> = get("glow_item_frame")
    @JvmField public val GLOW_SQUID: EntityType<GlowSquid> = get("glow_squid")
    @JvmField public val GOAT: EntityType<Goat> = get("goat")
    @JvmField public val GUARDIAN: EntityType<Entity> = get("guardian")
    @JvmField public val HOGLIN: EntityType<Entity> = get("hoglin")
    @JvmField public val HORSE: EntityType<Entity> = get("horse")
    @JvmField public val HUSK: EntityType<Entity> = get("husk")
    @JvmField public val ILLUSIONER: EntityType<Entity> = get("illusioner")
    @JvmField public val IRON_GOLEM: EntityType<Entity> = get("iron_golem")
    @JvmField public val ITEM: EntityType<Entity> = get("item")
    @JvmField public val ITEM_FRAME: EntityType<Entity> = get("item_frame")
    @JvmField public val FIREBALL: EntityType<LargeFireball> = get("fireball")
    @JvmField public val LEASH_KNOT: EntityType<Entity> = get("leash_knot")
    @JvmField public val LIGHTNING_BOLT: EntityType<Entity> = get("lightning_bolt")
    @JvmField public val LLAMA: EntityType<Entity> = get("llama")
    @JvmField public val LLAMA_SPIT: EntityType<LlamaSpit> = get("llama_spit")
    @JvmField public val MAGMA_CUBE: EntityType<Entity> = get("magma_cube")
    @JvmField public val MARKER: EntityType<Entity> = get("marker")
    @JvmField public val MINECART: EntityType<Entity> = get("minecart")
    @JvmField public val CHEST_MINECART: EntityType<Entity> = get("chest_minecart")
    @JvmField public val COMMAND_BLOCK_MINECART: EntityType<Entity> = get("command_block_minecart")
    @JvmField public val FURNACE_MINECART: EntityType<Entity> = get("furnace_minecart")
    @JvmField public val HOPPER_MINECART: EntityType<Entity> = get("hopper_minecart")
    @JvmField public val SPAWNER_MINECART: EntityType<Entity> = get("spawner_minecart")
    @JvmField public val TNT_MINECART: EntityType<Entity> = get("tnt_minecart")
    @JvmField public val MULE: EntityType<Entity> = get("mule")
    @JvmField public val MOOSHROOM: EntityType<Mooshroom> = get("mooshroom")
    @JvmField public val OCELOT: EntityType<Ocelot> = get("ocelot")
    @JvmField public val PAINTING: EntityType<Painting> = get("painting")
    @JvmField public val PANDA: EntityType<Panda> = get("panda")
    @JvmField public val PARROT: EntityType<Parrot> = get("parrot")
    @JvmField public val PHANTOM: EntityType<Entity> = get("phantom")
    @JvmField public val PIG: EntityType<Pig> = get("pig")
    @JvmField public val PIGLIN: EntityType<Entity> = get("piglin")
    @JvmField public val PIGLIN_BRUTE: EntityType<Entity> = get("piglin_brute")
    @JvmField public val PILLAGER: EntityType<Entity> = get("pillager")
    @JvmField public val POLAR_BEAR: EntityType<PolarBear> = get("polar_bear")
    @JvmField public val PRIMED_TNT: EntityType<Entity> = get("tnt")
    @JvmField public val PUFFERFISH: EntityType<Pufferfish> = get("pufferfish")
    @JvmField public val RABBIT: EntityType<Rabbit> = get("rabbit")
    @JvmField public val RAVAGER: EntityType<Entity> = get("ravager")
    @JvmField public val SALMON: EntityType<Salmon> = get("salmon")
    @JvmField public val SHEEP: EntityType<Sheep> = get("sheep")
    @JvmField public val SHULKER: EntityType<Entity> = get("shulker")
    @JvmField public val SHULKER_BULLET: EntityType<ShulkerBullet> = get("shulker_bullet")
    @JvmField public val SILVERFISH: EntityType<Entity> = get("silverfish")
    @JvmField public val SKELETON: EntityType<Entity> = get("skeleton")
    @JvmField public val SKELETON_HORSE: EntityType<Entity> = get("skeleton_horse")
    @JvmField public val SLIME: EntityType<Entity> = get("slime")
    @JvmField public val SMALL_FIREBALL: EntityType<SmallFireball> = get("small_fireball")
    @JvmField public val SNOW_GOLEM: EntityType<Entity> = get("snow_golem")
    @JvmField public val SNOWBALL: EntityType<Snowball> = get("snowball")
    @JvmField public val SPECTRAL_ARROW: EntityType<SpectralArrow> = get("spectral_arrow")
    @JvmField public val SPIDER: EntityType<Entity> = get("spider")
    @JvmField public val SQUID: EntityType<Squid> = get("squid")
    @JvmField public val STRAY: EntityType<Entity> = get("stray")
    @JvmField public val STRIDER: EntityType<Entity> = get("strider")
    @JvmField public val EGG: EntityType<Egg> = get("egg")
    @JvmField public val ENDER_PEARL: EntityType<EnderPearl> = get("ender_pearl")
    @JvmField public val EXPERIENCE_BOTTLE: EntityType<ExperienceBottle> = get("experience_bottle")
    @JvmField public val POTION: EntityType<ThrownPotion> = get("potion")
    @JvmField public val TRIDENT: EntityType<Trident> = get("trident")
    @JvmField public val TRADER_LLAMA: EntityType<Entity> = get("trader_llama")
    @JvmField public val TROPICAL_FISH: EntityType<TropicalFish> = get("tropical_fish")
    @JvmField public val TURTLE: EntityType<Turtle> = get("turtle")
    @JvmField public val VEX: EntityType<Entity> = get("vex")
    @JvmField public val VILLAGER: EntityType<Entity> = get("villager")
    @JvmField public val VINDICATOR: EntityType<Entity> = get("vindicator")
    @JvmField public val WANDERING_TRADER: EntityType<Entity> = get("wandering_trader")
    @JvmField public val WITCH: EntityType<Entity> = get("witch")
    @JvmField public val WITHER: EntityType<Entity> = get("wither")
    @JvmField public val WITHER_SKELETON: EntityType<Entity> = get("wither_skeleton")
    @JvmField public val WITHER_SKULL: EntityType<WitherSkull> = get("wither_skull")
    @JvmField public val WOLF: EntityType<Wolf> = get("wolf")
    @JvmField public val ZOGLIN: EntityType<Entity> = get("zoglin")
    @JvmField public val ZOMBIE: EntityType<Zombie> = get("zombie")
    @JvmField public val ZOMBIE_HORSE: EntityType<Entity> = get("zombie_horse")
    @JvmField public val ZOMBIE_VILLAGER: EntityType<Entity> = get("zombie_villager")
    @JvmField public val ZOMBIFIED_PIGLIN: EntityType<Entity> = get("zombified_piglin")
    @JvmField public val PLAYER: EntityType<Player> = get("player")
    @JvmField public val FISHING_HOOK: EntityType<FishingHook> = get("fishing_bobber")
    // @formatter:on

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private fun <T : Entity> get(name: String): EntityType<T> = Registries.ENTITY_TYPE[Key.key(name)] as EntityType<T>
}

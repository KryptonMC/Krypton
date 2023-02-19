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
import org.kryptonmc.api.entity.vehicle.Boat
import org.kryptonmc.api.entity.vehicle.CommandBlockMinecart
import org.kryptonmc.api.entity.vehicle.FurnaceMinecart
import org.kryptonmc.api.entity.vehicle.Minecart
import org.kryptonmc.api.entity.vehicle.TNTMinecart
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * All the types of entities in the game.
 */
@Catalogue(EntityType::class)
public object EntityTypes {

    // TODO: Make each of these be of their respective entity types when they exist
    // @formatter:off
    @JvmField
    public val ALLAY: RegistryReference<EntityType<Entity>> = get("allay")
    @JvmField
    public val AREA_EFFECT_CLOUD: RegistryReference<EntityType<AreaEffectCloud>> = get("area_effect_cloud")
    @JvmField
    public val ARMOR_STAND: RegistryReference<EntityType<ArmorStand>> = get("armor_stand")
    @JvmField
    public val ARROW: RegistryReference<EntityType<Arrow>> = get("arrow")
    @JvmField
    public val AXOLOTL: RegistryReference<EntityType<Axolotl>> = get("axolotl")
    @JvmField
    public val BAT: RegistryReference<EntityType<Bat>> = get("bat")
    @JvmField
    public val BEE: RegistryReference<EntityType<Bee>> = get("bee")
    @JvmField
    public val BLAZE: RegistryReference<EntityType<Entity>> = get("blaze")
    @JvmField
    public val BOAT: RegistryReference<EntityType<Boat>> = get("boat")
    @JvmField
    public val CHEST_BOAT: RegistryReference<EntityType<Entity>> = get("chest_boat")
    @JvmField
    public val CAT: RegistryReference<EntityType<Cat>> = get("cat")
    @JvmField
    public val CAMEL: RegistryReference<EntityType<Entity>> = get("camel")
    @JvmField
    public val CAVE_SPIDER: RegistryReference<EntityType<Entity>> = get("cave_spider")
    @JvmField
    public val CHICKEN: RegistryReference<EntityType<Chicken>> = get("chicken")
    @JvmField
    public val COD: RegistryReference<EntityType<Cod>> = get("cod")
    @JvmField
    public val COW: RegistryReference<EntityType<Cow>> = get("cow")
    @JvmField
    public val CREEPER: RegistryReference<EntityType<Creeper>> = get("creeper")
    @JvmField
    public val DOLPHIN: RegistryReference<EntityType<Dolphin>> = get("dolphin")
    @JvmField
    public val DONKEY: RegistryReference<EntityType<Entity>> = get("donkey")
    @JvmField
    public val DRAGON_FIREBALL: RegistryReference<EntityType<DragonFireball>> = get("dragon_fireball")
    @JvmField
    public val DROWNED: RegistryReference<EntityType<Entity>> = get("drowned")
    @JvmField
    public val ELDER_GUARDIAN: RegistryReference<EntityType<Entity>> = get("elder_guardian")
    @JvmField
    public val END_CRYSTAL: RegistryReference<EntityType<Entity>> = get("end_crystal")
    @JvmField
    public val ENDER_DRAGON: RegistryReference<EntityType<Entity>> = get("ender_dragon")
    @JvmField
    public val ENDERMAN: RegistryReference<EntityType<Entity>> = get("enderman")
    @JvmField
    public val ENDERMTIE: RegistryReference<EntityType<Entity>> = get("endermite")
    @JvmField
    public val EVOKER: RegistryReference<EntityType<Entity>> = get("evoker")
    @JvmField
    public val EVOKER_FANGS: RegistryReference<EntityType<Entity>> = get("evoker_fangs")
    @JvmField
    public val EXPERIENCE_ORB: RegistryReference<EntityType<ExperienceOrb>> = get("experience_orb")
    @JvmField
    public val EYE_OF_ENDER: RegistryReference<EntityType<Entity>> = get("eye_of_ender")
    @JvmField
    public val FALLING_BLOCK: RegistryReference<EntityType<Entity>> = get("falling_block")
    @JvmField
    public val FIREWORK_ROCKET: RegistryReference<EntityType<FireworkRocket>> = get("firework_rocket")
    @JvmField
    public val FOX: RegistryReference<EntityType<Fox>> = get("fox")
    @JvmField
    public val FROG: RegistryReference<EntityType<Entity>> = get("frog")
    @JvmField
    public val GHAST: RegistryReference<EntityType<Entity>> = get("ghast")
    @JvmField
    public val GIANT: RegistryReference<EntityType<Entity>> = get("giant")
    @JvmField
    public val GLOW_ITEM_FRAME: RegistryReference<EntityType<Entity>> = get("glow_item_frame")
    @JvmField
    public val GLOW_SQUID: RegistryReference<EntityType<GlowSquid>> = get("glow_squid")
    @JvmField
    public val GOAT: RegistryReference<EntityType<Goat>> = get("goat")
    @JvmField
    public val GUARDIAN: RegistryReference<EntityType<Entity>> = get("guardian")
    @JvmField
    public val HOGLIN: RegistryReference<EntityType<Entity>> = get("hoglin")
    @JvmField
    public val HORSE: RegistryReference<EntityType<Entity>> = get("horse")
    @JvmField
    public val HUSK: RegistryReference<EntityType<Entity>> = get("husk")
    @JvmField
    public val ILLUSIONER: RegistryReference<EntityType<Entity>> = get("illusioner")
    @JvmField
    public val IRON_GOLEM: RegistryReference<EntityType<Entity>> = get("iron_golem")
    @JvmField
    public val ITEM: RegistryReference<EntityType<Entity>> = get("item")
    @JvmField
    public val ITEM_FRAME: RegistryReference<EntityType<Entity>> = get("item_frame")
    @JvmField
    public val FIREBALL: RegistryReference<EntityType<LargeFireball>> = get("fireball")
    @JvmField
    public val LEASH_KNOT: RegistryReference<EntityType<Entity>> = get("leash_knot")
    @JvmField
    public val LIGHTNING_BOLT: RegistryReference<EntityType<Entity>> = get("lightning_bolt")
    @JvmField
    public val LLAMA: RegistryReference<EntityType<Entity>> = get("llama")
    @JvmField
    public val LLAMA_SPIT: RegistryReference<EntityType<LlamaSpit>> = get("llama_spit")
    @JvmField
    public val MAGMA_CUBE: RegistryReference<EntityType<Entity>> = get("magma_cube")
    @JvmField
    public val MARKER: RegistryReference<EntityType<Entity>> = get("marker")
    @JvmField
    public val MINECART: RegistryReference<EntityType<Minecart>> = get("minecart")
    @JvmField
    public val CHEST_MINECART: RegistryReference<EntityType<Entity>> = get("chest_minecart")
    @JvmField
    public val COMMAND_BLOCK_MINECART: RegistryReference<EntityType<CommandBlockMinecart>> = get("command_block_minecart")
    @JvmField
    public val FURNACE_MINECART: RegistryReference<EntityType<FurnaceMinecart>> = get("furnace_minecart")
    @JvmField
    public val HOPPER_MINECART: RegistryReference<EntityType<Entity>> = get("hopper_minecart")
    @JvmField
    public val SPAWNER_MINECART: RegistryReference<EntityType<Entity>> = get("spawner_minecart")
    @JvmField
    public val TNT_MINECART: RegistryReference<EntityType<TNTMinecart>> = get("tnt_minecart")
    @JvmField
    public val MULE: RegistryReference<EntityType<Entity>> = get("mule")
    @JvmField
    public val MOOSHROOM: RegistryReference<EntityType<Mooshroom>> = get("mooshroom")
    @JvmField
    public val OCELOT: RegistryReference<EntityType<Ocelot>> = get("ocelot")
    @JvmField
    public val PAINTING: RegistryReference<EntityType<Painting>> = get("painting")
    @JvmField
    public val PANDA: RegistryReference<EntityType<Panda>> = get("panda")
    @JvmField
    public val PARROT: RegistryReference<EntityType<Parrot>> = get("parrot")
    @JvmField
    public val PHANTOM: RegistryReference<EntityType<Entity>> = get("phantom")
    @JvmField
    public val PIG: RegistryReference<EntityType<Pig>> = get("pig")
    @JvmField
    public val PIGLIN: RegistryReference<EntityType<Entity>> = get("piglin")
    @JvmField
    public val PIGLIN_BRUTE: RegistryReference<EntityType<Entity>> = get("piglin_brute")
    @JvmField
    public val PILLAGER: RegistryReference<EntityType<Entity>> = get("pillager")
    @JvmField
    public val POLAR_BEAR: RegistryReference<EntityType<PolarBear>> = get("polar_bear")
    @JvmField
    public val PRIMED_TNT: RegistryReference<EntityType<Entity>> = get("tnt")
    @JvmField
    public val PUFFERFISH: RegistryReference<EntityType<Pufferfish>> = get("pufferfish")
    @JvmField
    public val RABBIT: RegistryReference<EntityType<Rabbit>> = get("rabbit")
    @JvmField
    public val RAVAGER: RegistryReference<EntityType<Entity>> = get("ravager")
    @JvmField
    public val SALMON: RegistryReference<EntityType<Salmon>> = get("salmon")
    @JvmField
    public val SHEEP: RegistryReference<EntityType<Sheep>> = get("sheep")
    @JvmField
    public val SHULKER: RegistryReference<EntityType<Entity>> = get("shulker")
    @JvmField
    public val SHULKER_BULLET: RegistryReference<EntityType<ShulkerBullet>> = get("shulker_bullet")
    @JvmField
    public val SILVERFISH: RegistryReference<EntityType<Entity>> = get("silverfish")
    @JvmField
    public val SKELETON: RegistryReference<EntityType<Entity>> = get("skeleton")
    @JvmField
    public val SKELETON_HORSE: RegistryReference<EntityType<Entity>> = get("skeleton_horse")
    @JvmField
    public val SLIME: RegistryReference<EntityType<Entity>> = get("slime")
    @JvmField
    public val SMALL_FIREBALL: RegistryReference<EntityType<SmallFireball>> = get("small_fireball")
    @JvmField
    public val SNOW_GOLEM: RegistryReference<EntityType<Entity>> = get("snow_golem")
    @JvmField
    public val SNOWBALL: RegistryReference<EntityType<Snowball>> = get("snowball")
    @JvmField
    public val SPECTRAL_ARROW: RegistryReference<EntityType<SpectralArrow>> = get("spectral_arrow")
    @JvmField
    public val SPIDER: RegistryReference<EntityType<Entity>> = get("spider")
    @JvmField
    public val SQUID: RegistryReference<EntityType<Squid>> = get("squid")
    @JvmField
    public val STRAY: RegistryReference<EntityType<Entity>> = get("stray")
    @JvmField
    public val STRIDER: RegistryReference<EntityType<Entity>> = get("strider")
    @JvmField
    public val TADPOLE: RegistryReference<EntityType<Entity>> = get("tadpole")
    @JvmField
    public val EGG: RegistryReference<EntityType<Egg>> = get("egg")
    @JvmField
    public val ENDER_PEARL: RegistryReference<EntityType<EnderPearl>> = get("ender_pearl")
    @JvmField
    public val EXPERIENCE_BOTTLE: RegistryReference<EntityType<ExperienceBottle>> = get("experience_bottle")
    @JvmField
    public val POTION: RegistryReference<EntityType<ThrownPotion>> = get("potion")
    @JvmField
    public val TRIDENT: RegistryReference<EntityType<Trident>> = get("trident")
    @JvmField
    public val TRADER_LLAMA: RegistryReference<EntityType<Entity>> = get("trader_llama")
    @JvmField
    public val TROPICAL_FISH: RegistryReference<EntityType<TropicalFish>> = get("tropical_fish")
    @JvmField
    public val TURTLE: RegistryReference<EntityType<Turtle>> = get("turtle")
    @JvmField
    public val VEX: RegistryReference<EntityType<Entity>> = get("vex")
    @JvmField
    public val VILLAGER: RegistryReference<EntityType<Entity>> = get("villager")
    @JvmField
    public val VINDICATOR: RegistryReference<EntityType<Entity>> = get("vindicator")
    @JvmField
    public val WANDERING_TRADER: RegistryReference<EntityType<Entity>> = get("wandering_trader")
    @JvmField
    public val WARDEN: RegistryReference<EntityType<Entity>> = get("warden")
    @JvmField
    public val WITCH: RegistryReference<EntityType<Entity>> = get("witch")
    @JvmField
    public val WITHER: RegistryReference<EntityType<Entity>> = get("wither")
    @JvmField
    public val WITHER_SKELETON: RegistryReference<EntityType<Entity>> = get("wither_skeleton")
    @JvmField
    public val WITHER_SKULL: RegistryReference<EntityType<WitherSkull>> = get("wither_skull")
    @JvmField
    public val WOLF: RegistryReference<EntityType<Wolf>> = get("wolf")
    @JvmField
    public val ZOGLIN: RegistryReference<EntityType<Entity>> = get("zoglin")
    @JvmField
    public val ZOMBIE: RegistryReference<EntityType<Zombie>> = get("zombie")
    @JvmField
    public val ZOMBIE_HORSE: RegistryReference<EntityType<Entity>> = get("zombie_horse")
    @JvmField
    public val ZOMBIE_VILLAGER: RegistryReference<EntityType<Entity>> = get("zombie_villager")
    @JvmField
    public val ZOMBIFIED_PIGLIN: RegistryReference<EntityType<Entity>> = get("zombified_piglin")
    @JvmField
    public val PLAYER: RegistryReference<EntityType<Player>> = get("player")
    @JvmField
    public val FISHING_HOOK: RegistryReference<EntityType<FishingHook>> = get("fishing_bobber")
    // @formatter:on

    @JvmStatic
    private fun <T : Entity> get(name: String): RegistryReference<EntityType<T>> = RegistryReference.of(Registries.ENTITY_TYPE, Key.key(name))
}

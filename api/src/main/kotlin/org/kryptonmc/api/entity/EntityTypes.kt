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
import org.kryptonmc.api.entity.ambient.Bat
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
@Suppress("UndocumentedPublicProperty")
@Catalogue(EntityType::class)
public object EntityTypes {

    // TODO: Make each of these be of their respective entity types when they exist
    // @formatter:off
    @JvmField public val AREA_EFFECT_CLOUD: EntityType<AreaEffectCloud> = register("area_effect_cloud")
    @JvmField public val ARMOR_STAND: EntityType<ArmorStand> = register("armor_stand")
    @JvmField public val ARROW: EntityType<Arrow> = register("arrow")
    @JvmField public val AXOLOTL: EntityType<Entity> = register("axolotl")
    @JvmField public val BAT: EntityType<Bat> = register("bat")
    @JvmField public val BEE: EntityType<Entity> = register("bee")
    @JvmField public val BLAZE: EntityType<Entity> = register("blaze")
    @JvmField public val BOAT: EntityType<Entity> = register("boat")
    @JvmField public val CAT: EntityType<Entity> = register("cat")
    @JvmField public val CAVE_SPIDER: EntityType<Entity> = register("cave_spider")
    @JvmField public val CHICKEN: EntityType<Entity> = register("chicken")
    @JvmField public val COD: EntityType<Entity> = register("cod")
    @JvmField public val COW: EntityType<Entity> = register("cow")
    @JvmField public val CREEPER: EntityType<Creeper> = register("creeper")
    @JvmField public val DOLPHIN: EntityType<Entity> = register("dolphin")
    @JvmField public val DONKEY: EntityType<Entity> = register("donkey")
    @JvmField public val DRAGON_FIREBALL: EntityType<DragonFireball> = register("dragon_fireball")
    @JvmField public val DROWNED: EntityType<Entity> = register("drowned")
    @JvmField public val ELDER_GUARDIAN: EntityType<Entity> = register("elder_guardian")
    @JvmField public val END_CRYSTAL: EntityType<Entity> = register("end_crystal")
    @JvmField public val ENDER_DRAGON: EntityType<Entity> = register("ender_dragon")
    @JvmField public val ENDERMAN: EntityType<Entity> = register("enderman")
    @JvmField public val ENDERMTIE: EntityType<Entity> = register("endermite")
    @JvmField public val EVOKER: EntityType<Entity> = register("evoker")
    @JvmField public val EVOKER_FANGS: EntityType<Entity> = register("evoker_fangs")
    @JvmField public val EXPERIENCE_ORB: EntityType<ExperienceOrb> = register("experience_orb")
    @JvmField public val EYE_OF_ENDER: EntityType<Entity> = register("eye_of_ender")
    @JvmField public val FALLING_BLOCK: EntityType<Entity> = register("falling_block")
    @JvmField public val FIREWORK_ROCKET: EntityType<FireworkRocket> = register("firework_rocket")
    @JvmField public val FOX: EntityType<Entity> = register("fox")
    @JvmField public val GHAST: EntityType<Entity> = register("ghast")
    @JvmField public val GIANT: EntityType<Entity> = register("giant")
    @JvmField public val GLOW_ITEM_FRAME: EntityType<Entity> = register("glow_item_frame")
    @JvmField public val GLOW_SQUID: EntityType<Entity> = register("glow_squid")
    @JvmField public val GOAT: EntityType<Entity> = register("goat")
    @JvmField public val GUARDIAN: EntityType<Entity> = register("guardian")
    @JvmField public val HOGLIN: EntityType<Entity> = register("hoglin")
    @JvmField public val HORSE: EntityType<Entity> = register("horse")
    @JvmField public val HUSK: EntityType<Entity> = register("husk")
    @JvmField public val ILLUSIONER: EntityType<Entity> = register("illusioner")
    @JvmField public val IRON_GOLEM: EntityType<Entity> = register("iron_golem")
    @JvmField public val ITEM: EntityType<Entity> = register("item")
    @JvmField public val ITEM_FRAME: EntityType<Entity> = register("item_frame")
    @JvmField public val FIREBALL: EntityType<LargeFireball> = register("fireball")
    @JvmField public val LEASH_KNOT: EntityType<Entity> = register("leash_knot")
    @JvmField public val LIGHTNING_BOLT: EntityType<Entity> = register("lightning_bolt")
    @JvmField public val LLAMA: EntityType<Entity> = register("llama")
    @JvmField public val LLAMA_SPIT: EntityType<LlamaSpit> = register("llama_spit")
    @JvmField public val MAGMA_CUBE: EntityType<Entity> = register("magma_cube")
    @JvmField public val MARKER: EntityType<Entity> = register("marker", false)
    @JvmField public val MINECART: EntityType<Entity> = register("minecart")
    @JvmField public val CHEST_MINECART: EntityType<Entity> = register("chest_minecart")
    @JvmField public val COMMAND_BLOCK_MINECART: EntityType<Entity> = register("command_block_minecart")
    @JvmField public val FURNACE_MINECART: EntityType<Entity> = register("furnace_minecart")
    @JvmField public val HOPPER_MINECART: EntityType<Entity> = register("hopper_minecart")
    @JvmField public val SPAWNER_MINECART: EntityType<Entity> = register("spawner_minecart")
    @JvmField public val TNT_MINECART: EntityType<Entity> = register("tnt_minecart")
    @JvmField public val MULE: EntityType<Entity> = register("mule")
    @JvmField public val MOOSHROOM: EntityType<Entity> = register("mooshroom")
    @JvmField public val OCELOT: EntityType<Entity> = register("ocelot")
    @JvmField public val PAINTING: EntityType<Painting> = register("painting")
    @JvmField public val PANDA: EntityType<Entity> = register("panda")
    @JvmField public val PARROT: EntityType<Entity> = register("parrot")
    @JvmField public val PHANTOM: EntityType<Entity> = register("phantom")
    @JvmField public val PIG: EntityType<Entity> = register("pig")
    @JvmField public val PIGLIN: EntityType<Entity> = register("piglin")
    @JvmField public val PIGLIN_BRUTE: EntityType<Entity> = register("piglin_brute")
    @JvmField public val PILLAGER: EntityType<Entity> = register("pillager")
    @JvmField public val POLAR_BEAR: EntityType<Entity> = register("polar_bear")
    @JvmField public val PRIMED_TNT: EntityType<Entity> = register("tnt")
    @JvmField public val PUFFERFISH: EntityType<Entity> = register("pufferfish")
    @JvmField public val RABBIT: EntityType<Entity> = register("rabbit")
    @JvmField public val RAVAGER: EntityType<Entity> = register("ravager")
    @JvmField public val SALMON: EntityType<Entity> = register("salmon")
    @JvmField public val SHEEP: EntityType<Entity> = register("sheep")
    @JvmField public val SHULKER: EntityType<Entity> = register("shulker")
    @JvmField public val SHULKER_BULLET: EntityType<ShulkerBullet> = register("shulker_bullet")
    @JvmField public val SILVERFISH: EntityType<Entity> = register("silverfish")
    @JvmField public val SKELETON: EntityType<Entity> = register("skeleton")
    @JvmField public val SKELETON_HORSE: EntityType<Entity> = register("skeleton_horse")
    @JvmField public val SLIME: EntityType<Entity> = register("slime")
    @JvmField public val SMALL_FIREBALL: EntityType<SmallFireball> = register("small_fireball")
    @JvmField public val SNOW_GOLEM: EntityType<Entity> = register("snow_golem")
    @JvmField public val SNOWBALL: EntityType<Snowball> = register("snowball")
    @JvmField public val SPECTRAL_ARROW: EntityType<SpectralArrow> = register("spectral_arrow")
    @JvmField public val SPIDER: EntityType<Entity> = register("spider")
    @JvmField public val SQUID: EntityType<Entity> = register("squid")
    @JvmField public val STRAY: EntityType<Entity> = register("stray")
    @JvmField public val STRIDER: EntityType<Entity> = register("strider")
    @JvmField public val EGG: EntityType<Egg> = register("egg")
    @JvmField public val ENDER_PEARL: EntityType<EnderPearl> = register("ender_pearl")
    @JvmField public val EXPERIENCE_BOTTLE: EntityType<ExperienceBottle> = register("experience_bottle")
    @JvmField public val POTION: EntityType<ThrownPotion> = register("potion")
    @JvmField public val TRIDENT: EntityType<Trident> = register("trident")
    @JvmField public val TRADER_LLAMA: EntityType<Entity> = register("trader_llama")
    @JvmField public val TROPICAL_FISH: EntityType<Entity> = register("tropical_fish")
    @JvmField public val TURTLE: EntityType<Entity> = register("turtle")
    @JvmField public val VEX: EntityType<Entity> = register("vex")
    @JvmField public val VILLAGER: EntityType<Entity> = register("villager")
    @JvmField public val VINDICATOR: EntityType<Entity> = register("vindicator")
    @JvmField public val WANDERING_TRADER: EntityType<Entity> = register("wandering_trader")
    @JvmField public val WITCH: EntityType<Entity> = register("witch")
    @JvmField public val WITHER: EntityType<Entity> = register("wither")
    @JvmField public val WITHER_SKELETON: EntityType<Entity> = register("wither_skeleton")
    @JvmField public val WITHER_SKULL: EntityType<WitherSkull> = register("wither_skull")
    @JvmField public val WOLF: EntityType<Entity> = register("wolf")
    @JvmField public val ZOGLIN: EntityType<Entity> = register("zoglin")
    @JvmField public val ZOMBIE: EntityType<Zombie> = register("zombie")
    @JvmField public val ZOMBIE_HORSE: EntityType<Entity> = register("zombie_horse")
    @JvmField public val ZOMBIE_VILLAGER: EntityType<Entity> = register("zombie_villager")
    @JvmField public val ZOMBIFIED_PIGLIN: EntityType<Entity> = register("zombified_piglin")
    @JvmField public val PLAYER: EntityType<Player> = register("player", false)
    @JvmField public val FISHING_HOOK: EntityType<FishingHook> = register("fishing_bobber", false)
    // @formatter:on

    @Suppress("UNCHECKED_CAST") // This should never fail here
    private fun <T : Entity> register(name: String, isSummonable: Boolean = true): EntityType<T> =
        Registries.register(Registries.ENTITY_TYPE, name, EntityType.of<T>(Key.key(name), isSummonable)) as EntityType<T>
}

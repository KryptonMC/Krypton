/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.AreaEffectCloud
import org.kryptonmc.api.entity.ArmorStand
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityCategories
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.entity.EntityDimensions
import org.kryptonmc.api.entity.ExperienceOrb
import org.kryptonmc.api.entity.ambient.Bat
import org.kryptonmc.api.entity.animal.Axolotl
import org.kryptonmc.api.entity.animal.Bee
import org.kryptonmc.api.entity.animal.Cat
import org.kryptonmc.api.entity.animal.Chicken
import org.kryptonmc.api.entity.animal.Cow
import org.kryptonmc.api.entity.animal.Fox
import org.kryptonmc.api.entity.animal.Goat
import org.kryptonmc.api.entity.animal.Ocelot
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

object KryptonEntityTypes {

    private const val MAGIC_HORSE_WIDTH = 1.3964844F
    private val HUMAN_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.95F)

    // TODO: Make each of these be of their respective entity types when they exist
    @JvmField
    val AREA_EFFECT_CLOUD: KryptonEntityType<AreaEffectCloud> = register("area_effect_cloud", EntityCategories.MISC) {
        fireImmune()
        dimensions(6F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val ARMOR_STAND: KryptonEntityType<ArmorStand> = register("armor_stand", EntityCategories.MISC) {
        dimensions(0.5F, 1.975F)
        clientTrackingRange(10)
    }
    @JvmField
    val ARROW: KryptonEntityType<Arrow> = register("arrow", EntityCategories.MISC) {
        clientTrackingRange(4)
        updateInterval(20)
    }
    @JvmField
    val AXOLOTL: KryptonEntityType<Axolotl> = register("axolotl", EntityCategories.UNDERGROUND_WATER_CREATURE) {
        dimensions(0.75F, 0.42F)
        clientTrackingRange(10)
    }
    @JvmField
    val BAT: KryptonEntityType<Bat> = register("bat", EntityCategories.AMBIENT) {
        dimensions(0.5F, 0.9F)
        clientTrackingRange(5)
    }
    @JvmField
    val BEE: KryptonEntityType<Bee> = register("bee", EntityCategories.CREATURE) {
        dimensions(0.7F, 0.6F)
        clientTrackingRange(8)
    }
    @JvmField
    val BLAZE: KryptonEntityType<Entity> = register("blaze", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(0.6F, 1.8F)
        clientTrackingRange(8)
    }
    @JvmField
    val BOAT: KryptonEntityType<Entity> = register("boat", EntityCategories.MISC) {
        dimensions(1.375F, 0.5625F)
        clientTrackingRange(10)
    }
    @JvmField
    val CAT: KryptonEntityType<Cat> = register("cat", EntityCategories.CREATURE) {
        dimensions(0.6F, 0.7F)
        clientTrackingRange(8)
    }
    @JvmField
    val CAVE_SPIDER: KryptonEntityType<Entity> = register("cave_spider", EntityCategories.MONSTER) {
        dimensions(0.7F, 0.5F)
        clientTrackingRange(8)
    }
    @JvmField
    val CHICKEN: KryptonEntityType<Chicken> = register("chicken", EntityCategories.CREATURE) {
        dimensions(0.4F, 0.7F)
        clientTrackingRange(10)
    }
    @JvmField
    val COD: KryptonEntityType<Cod> = register("cod", EntityCategories.WATER_AMBIENT) {
        dimensions(0.5F, 0.3F)
        clientTrackingRange(4)
    }
    @JvmField
    val COW: KryptonEntityType<Cow> = register("cow", EntityCategories.CREATURE) {
        dimensions(0.9F, 1.4F)
        clientTrackingRange(10)
    }
    @JvmField
    val CREEPER: KryptonEntityType<Creeper> = register("creeper", EntityCategories.MONSTER) {
        dimensions(0.6F, 1.7F)
        clientTrackingRange(8)
    }
    @JvmField
    val DOLPHIN: KryptonEntityType<Dolphin> = register("dolphin", EntityCategories.WATER_CREATURE) {
        dimensions(0.9F, 0.6F)
        rideable()
    }
    @JvmField
    val DONKEY: KryptonEntityType<Entity> = register("donkey", EntityCategories.CREATURE) {
        dimensions(MAGIC_HORSE_WIDTH, 1.5F)
        clientTrackingRange(10)
    }
    @JvmField
    val DRAGON_FIREBALL: KryptonEntityType<DragonFireball> = register("dragon_fireball", EntityCategories.MISC) {
        dimensions(1F, 1F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val DROWNED: KryptonEntityType<Entity> = register("drowned", EntityCategories.MONSTER) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val ELDER_GUARDIAN: KryptonEntityType<Entity> = register("elder_guardian", EntityCategories.MONSTER) {
        dimensions(1.9975F, 1.9975F)
        clientTrackingRange(10)
    }
    @JvmField
    val END_CRYSTAL: KryptonEntityType<Entity> = register("end_crystal", EntityCategories.MISC) {
        dimensions(2F, 2F)
        clientTrackingRange(16)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val ENDER_DRAGON: KryptonEntityType<Entity> = register("ender_dragon", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(16F, 8F)
        clientTrackingRange(10)
    }
    @JvmField
    val ENDERMAN: KryptonEntityType<Entity> = register("enderman", EntityCategories.MONSTER) {
        dimensions(0.6F, 2.9F)
        clientTrackingRange(8)
    }
    @JvmField
    val ENDERMTIE: KryptonEntityType<Entity> = register("endermite", EntityCategories.MONSTER) {
        dimensions(0.4F, 0.3F)
        clientTrackingRange(8)
    }
    @JvmField
    val EVOKER: KryptonEntityType<Entity> = register("evoker", EntityCategories.MONSTER) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val EVOKER_FANGS: KryptonEntityType<Entity> = register("evoker_fangs", EntityCategories.MISC) {
        dimensions(0.5F, 0.8F)
        clientTrackingRange(6)
        updateInterval(2)
    }
    @JvmField
    val EXPERIENCE_ORB: KryptonEntityType<ExperienceOrb> = register("experience_orb", EntityCategories.MISC) {
        dimensions(0.5F, 0.5F)
        clientTrackingRange(6)
        updateInterval(20)
    }
    @JvmField
    val EYE_OF_ENDER: KryptonEntityType<Entity> = register("eye_of_ender", EntityCategories.MISC) {
        dimensions(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(4)
    }
    @JvmField
    val FALLING_BLOCK: KryptonEntityType<Entity> = register("falling_block", EntityCategories.MISC) {
        dimensions(0.98F, 0.98F)
        clientTrackingRange(10)
        updateInterval(20)
    }
    @JvmField
    val FIREWORK_ROCKET: KryptonEntityType<FireworkRocket> = register("firework_rocket", EntityCategories.MISC) {
        dimensions(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val FOX: KryptonEntityType<Fox> = register("fox", EntityCategories.CREATURE) {
        dimensions(0.6F, 0.7F)
        clientTrackingRange(8)
        immuneTo(Blocks.SWEET_BERRY_BUSH)
    }
    @JvmField
    val GHAST: KryptonEntityType<Entity> = register("ghast", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(4F, 4F)
        clientTrackingRange(10)
    }
    @JvmField
    val GIANT: KryptonEntityType<Entity> = register("giant", EntityCategories.MONSTER) {
        dimensions(3.6F, 12F)
        clientTrackingRange(10)
    }
    @JvmField
    val GLOW_ITEM_FRAME: KryptonEntityType<Entity> = register("glow_item_frame", EntityCategories.MISC) {
        dimensions(0.5F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val GLOW_SQUID: KryptonEntityType<GlowSquid> = register("glow_squid", EntityCategories.UNDERGROUND_WATER_CREATURE) {
        dimensions(0.8F, 0.8F)
        clientTrackingRange(10)
    }
    @JvmField
    val GOAT: KryptonEntityType<Goat> = register("goat", EntityCategories.CREATURE) {
        dimensions(0.9F, 1.3F)
        clientTrackingRange(10)
    }
    @JvmField
    val GUARDIAN: KryptonEntityType<Entity> = register("guardian", EntityCategories.MONSTER) {
        dimensions(0.85F, 0.85F)
        clientTrackingRange(8)
    }
    @JvmField
    val HOGLIN: KryptonEntityType<Entity> = register("hoglin", EntityCategories.MONSTER) {
        dimensions(MAGIC_HORSE_WIDTH, 1.4F)
        clientTrackingRange(8)
    }
    @JvmField
    val HORSE: KryptonEntityType<Entity> = register("horse", EntityCategories.CREATURE) {
        dimensions(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(10)
    }
    @JvmField
    val HUSK: KryptonEntityType<Entity> = register("husk", EntityCategories.MONSTER) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val ILLUSIONER: KryptonEntityType<Entity> = register("illusioner", EntityCategories.MONSTER) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val IRON_GOLEM: KryptonEntityType<Entity> = register("iron_golem", EntityCategories.MISC) {
        dimensions(1.4F, 2.7F)
        clientTrackingRange(10)
    }
    @JvmField
    val ITEM: KryptonEntityType<Entity> = register("item", EntityCategories.MISC) {
        dimensions(0.25F, 0.25F)
        clientTrackingRange(6)
        updateInterval(20)
    }
    @JvmField
    val ITEM_FRAME: KryptonEntityType<Entity> = register("item_frame", EntityCategories.MISC) {
        dimensions(0.5F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val FIREBALL: KryptonEntityType<LargeFireball> = register("fireball", EntityCategories.MISC) {
        dimensions(1F, 1F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val LEASH_KNOT: KryptonEntityType<Entity> = register("leash_knot", EntityCategories.MISC) {
        dimensions(0.375F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val LIGHTNING_BOLT: KryptonEntityType<Entity> = register("lightning_bolt", EntityCategories.MISC) {
        dimensions(0F, 0F)
        clientTrackingRange(16)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val LLAMA: KryptonEntityType<Entity> = register("llama", EntityCategories.CREATURE) {
        dimensions(0.9F, 1.87F)
        clientTrackingRange(10)
    }
    @JvmField
    val LLAMA_SPIT: KryptonEntityType<LlamaSpit> = register("llama_spit", EntityCategories.MISC) {
        dimensions(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val MAGMA_CUBE: KryptonEntityType<Entity> = register("magma_cube", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(2.04F, 2.04F)
        clientTrackingRange(8)
    }
    @JvmField
    val MARKER: KryptonEntityType<Entity> = register("marker", EntityCategories.MISC) {
        dimensions(0F, 0F)
        clientTrackingRange(0)
    }
    @JvmField
    val MINECART: KryptonEntityType<Entity> = minecart("minecart")
    @JvmField
    val CHEST_MINECART: KryptonEntityType<Entity> = minecart("chest_minecart")
    @JvmField
    val COMMAND_BLOCK_MINECART: KryptonEntityType<Entity> = minecart("command_block_minecart")
    @JvmField
    val FURNACE_MINECART: KryptonEntityType<Entity> = minecart("furnace_minecart")
    @JvmField
    val HOPPER_MINECART: KryptonEntityType<Entity> = minecart("hopper_minecart")
    @JvmField
    val SPAWNER_MINECART: KryptonEntityType<Entity> = minecart("spawner_minecart")
    @JvmField
    val TNT_MINECART: KryptonEntityType<Entity> = minecart("tnt_minecart")
    @JvmField
    val MULE: KryptonEntityType<Entity> = register("mule", EntityCategories.CREATURE) {
        dimensions(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(8)
    }
    @JvmField
    val MOOSHROOM: KryptonEntityType<Entity> = register("mooshroom", EntityCategories.CREATURE) {
        dimensions(0.9F, 1.4F)
        clientTrackingRange(10)
    }
    @JvmField
    val OCELOT: KryptonEntityType<Ocelot> = register("ocelot", EntityCategories.CREATURE) {
        dimensions(0.6F, 0.7F)
        clientTrackingRange(10)
    }
    @JvmField
    val PAINTING: KryptonEntityType<Painting> = register("painting", EntityCategories.MISC) {
        dimensions(0.5F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val PANDA: KryptonEntityType<Panda> = register("panda", EntityCategories.CREATURE) {
        dimensions(1.3F, 1.25F)
        clientTrackingRange(10)
    }
    @JvmField
    val PARROT: KryptonEntityType<Parrot> = register("parrot", EntityCategories.CREATURE) {
        dimensions(0.5F, 0.9F)
        clientTrackingRange(8)
    }
    @JvmField
    val PHANTOM: KryptonEntityType<Entity> = register("phantom", EntityCategories.MONSTER) {
        dimensions(0.9F, 0.5F)
        clientTrackingRange(8)
    }
    @JvmField
    val PIG: KryptonEntityType<Pig> = register("pig", EntityCategories.CREATURE) {
        dimensions(0.9F, 0.9F)
        clientTrackingRange(10)
    }
    @JvmField
    val PIGLIN: KryptonEntityType<Entity> = register("piglin", EntityCategories.MONSTER) {
        dimensions(0.6F, 1.95F)
        clientTrackingRange(8)
    }
    @JvmField
    val PIGLIN_BRUTE: KryptonEntityType<Entity> = register("piglin_brute", EntityCategories.MONSTER) {
        dimensions(0.6F, 1.95F)
        clientTrackingRange(8)
    }
    @JvmField
    val PILLAGER: KryptonEntityType<Entity> = register("pillager", EntityCategories.MONSTER) {
        dimensions(0.6F, 1.95F)
        clientTrackingRange(8)
    }
    @JvmField
    val POLAR_BEAR: KryptonEntityType<PolarBear> = register("polar_bear", EntityCategories.CREATURE) {
        dimensions(1.4F, 1.4F)
        clientTrackingRange(10)
        immuneTo(Blocks.POWDER_SNOW)
    }
    @JvmField
    val PRIMED_TNT: KryptonEntityType<Entity> = register("tnt", EntityCategories.MISC) {
        fireImmune()
        dimensions(0.98F, 0.98F)
        clientTrackingRange(10)
        updateInterval(10)
    }
    @JvmField
    val PUFFERFISH: KryptonEntityType<Pufferfish> = register("pufferfish", EntityCategories.WATER_AMBIENT) {
        dimensions(0.7F, 0.7F)
        clientTrackingRange(4)
    }
    @JvmField
    val RABBIT: KryptonEntityType<Rabbit> = register("rabbit", EntityCategories.CREATURE) {
        dimensions(0.4F, 0.5F)
        clientTrackingRange(8)
    }
    @JvmField
    val RAVAGER: KryptonEntityType<Entity> = register("ravager", EntityCategories.MONSTER) {
        dimensions(1.95F, 2.2F)
        clientTrackingRange(10)
    }
    @JvmField
    val SALMON: KryptonEntityType<Salmon> = register("salmon", EntityCategories.WATER_AMBIENT) {
        dimensions(0.7F, 0.4F)
        clientTrackingRange(4)
    }
    @JvmField
    val SHEEP: KryptonEntityType<Sheep> = register("sheep", EntityCategories.CREATURE) {
        dimensions(0.9F, 1.3F)
        clientTrackingRange(10)
    }
    @JvmField
    val SHULKER: KryptonEntityType<Entity> = register("shulker", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(1F, 1F)
        clientTrackingRange(10)
    }
    @JvmField
    val SHULKER_BULLET: KryptonEntityType<ShulkerBullet> = register("shulker_bullet", EntityCategories.MISC) {
        dimensions(0.3125F, 0.3125F)
        clientTrackingRange(8)
    }
    @JvmField
    val SILVERFISH: KryptonEntityType<Entity> = register("silverfish", EntityCategories.MONSTER) {
        dimensions(0.4F, 0.3F)
        clientTrackingRange(8)
    }
    @JvmField
    val SKELETON: KryptonEntityType<Entity> = register("skeleton", EntityCategories.MONSTER) {
        dimensions(0.6F, 1.99F)
        clientTrackingRange(8)
    }
    @JvmField
    val SKELETON_HORSE: KryptonEntityType<Entity> = register("skeleton_horse", EntityCategories.CREATURE) {
        dimensions(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(10)
    }
    @JvmField
    val SLIME: KryptonEntityType<Entity> = register("slime", EntityCategories.MONSTER) {
        dimensions(2.04F, 2.04F)
        clientTrackingRange(10)
    }
    @JvmField
    val SMALL_FIREBALL: KryptonEntityType<SmallFireball> = register("small_fireball", EntityCategories.MISC) {
        dimensions(0.3125F, 0.3125F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val SNOW_GOLEM: KryptonEntityType<Entity> = register("snow_golem", EntityCategories.MISC) {
        dimensions(0.7F, 1.9F)
        clientTrackingRange(8)
        immuneTo(Blocks.POWDER_SNOW)
    }
    @JvmField
    val SNOWBALL: KryptonEntityType<Snowball> = register("snowball", EntityCategories.MISC) {
        dimensions(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val SPECTRAL_ARROW: KryptonEntityType<SpectralArrow> = register("spectral_arrow", EntityCategories.MISC) {
        dimensions(0.5F, 0.5F)
        clientTrackingRange(4)
        updateInterval(20)
    }
    @JvmField
    val SPIDER: KryptonEntityType<Entity> = register("spider", EntityCategories.MONSTER) {
        dimensions(1.4F, 0.9F)
        clientTrackingRange(8)
    }
    @JvmField
    val SQUID: KryptonEntityType<Squid> = register("squid", EntityCategories.WATER_CREATURE) {
        dimensions(0.8F, 0.8F)
        clientTrackingRange(8)
    }
    @JvmField
    val STRAY: KryptonEntityType<Entity> = register("stray", EntityCategories.MONSTER) {
        dimensions(0.6F, 1.99F)
        clientTrackingRange(8)
        immuneTo(Blocks.POWDER_SNOW)
    }
    @JvmField
    val STRIDER: KryptonEntityType<Entity> = register("strider", EntityCategories.CREATURE) {
        fireImmune()
        dimensions(0.9F, 1.7F)
        clientTrackingRange(10)
    }
    @JvmField
    val EGG: KryptonEntityType<Egg> = thrownItem("egg")
    @JvmField
    val ENDER_PEARL: KryptonEntityType<EnderPearl> = thrownItem("ender_pearl")
    @JvmField
    val EXPERIENCE_BOTTLE: KryptonEntityType<ExperienceBottle> = thrownItem("experience_bottle")
    @JvmField
    val POTION: KryptonEntityType<ThrownPotion> = thrownItem("potion")
    @JvmField
    val TRIDENT: KryptonEntityType<Trident> = register("trident", EntityCategories.MISC) {
        dimensions(0.5F, 0.5F)
        clientTrackingRange(4)
        updateInterval(20)
    }
    @JvmField
    val TRADER_LLAMA: KryptonEntityType<Entity> = register("trader_llama", EntityCategories.CREATURE) {
        dimensions(0.9F, 1.87F)
        clientTrackingRange(10)
    }
    @JvmField
    val TROPICAL_FISH: KryptonEntityType<TropicalFish> = register("tropical_fish", EntityCategories.WATER_AMBIENT) {
        dimensions(0.5F, 0.4F)
        clientTrackingRange(4)
    }
    @JvmField
    val TURTLE: KryptonEntityType<Turtle> = register("turtle", EntityCategories.CREATURE) {
        dimensions(1.2F, 0.4F)
        clientTrackingRange(10)
    }
    @JvmField
    val VEX: KryptonEntityType<Entity> = register("vex", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(0.4F, 0.8F)
        clientTrackingRange(8)
    }
    @JvmField
    val VILLAGER: KryptonEntityType<Entity> = register("villager", EntityCategories.MISC) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(10)
    }
    @JvmField
    val VINDICATOR: KryptonEntityType<Entity> = register("vindicator", EntityCategories.MONSTER) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val WANDERING_TRADER: KryptonEntityType<Entity> = register("wandering_trader", EntityCategories.CREATURE) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(10)
    }
    @JvmField
    val WITCH: KryptonEntityType<Entity> = register("witch", EntityCategories.MONSTER) {
        dimensions(0.6F, 1.95F)
        clientTrackingRange(8)
    }
    @JvmField
    val WITHER: KryptonEntityType<Entity> = register("wither", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(0.9F, 3.5F)
        clientTrackingRange(10)
        immuneTo(Blocks.WITHER_ROSE)
    }
    @JvmField
    val WITHER_SKELETON: KryptonEntityType<Entity> = register("wither_skeleton", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(0.7F, 2.4F)
        clientTrackingRange(8)
        immuneTo(Blocks.WITHER_ROSE)
    }
    @JvmField
    val WITHER_SKULL: KryptonEntityType<WitherSkull> = register("wither_skull", EntityCategories.MISC) {
        dimensions(0.3125F, 0.3125F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val WOLF: KryptonEntityType<Wolf> = register("wolf", EntityCategories.CREATURE) {
        dimensions(0.6F, 0.85F)
        clientTrackingRange(10)
    }
    @JvmField
    val ZOGLIN: KryptonEntityType<Entity> = register("zoglin", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(MAGIC_HORSE_WIDTH, 1.4F)
        clientTrackingRange(8)
    }
    @JvmField
    val ZOMBIE: KryptonEntityType<Zombie> = register("zombie", EntityCategories.MONSTER) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val ZOMBIE_HORSE: KryptonEntityType<Entity> = register("zombie_horse", EntityCategories.CREATURE) {
        dimensions(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(10)
    }
    @JvmField
    val ZOMBIE_VILLAGER: KryptonEntityType<Entity> = register("zombie_villager", EntityCategories.MONSTER) {
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val ZOMBIFIED_PIGLIN: KryptonEntityType<Entity> = register("zombified_piglin", EntityCategories.MONSTER) {
        fireImmune()
        dimensions(HUMAN_DIMENSIONS)
        clientTrackingRange(8)
    }
    @JvmField
    val PLAYER: KryptonEntityType<Player> = register("player", EntityCategories.MISC) {
        notSummonable()
        dimensions(0.6F, 1.8F)
        clientTrackingRange(32)
        updateInterval(2)
    }
    @JvmField
    val FISHING_HOOK: KryptonEntityType<FishingHook> = register("fishing_bobber", EntityCategories.MISC) {
        notSummonable()
        dimensions(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(5)
    }

    @JvmStatic
    private fun <T : Entity> minecart(name: String): KryptonEntityType<T> = register(name, EntityCategories.MISC) {
        dimensions(0.98F, 0.7F)
        clientTrackingRange(8)
    }

    @JvmStatic
    private fun <T : Entity> thrownItem(name: String): KryptonEntityType<T> = register(name, EntityCategories.MISC) {
        dimensions(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }

    @JvmStatic
    private fun <T : Entity> register(
        name: String,
        category: EntityCategory,
        builder: KryptonEntityType.Builder<T>.() -> Unit = {}
    ): KryptonEntityType<T> {
        val key = Key.key(name)
        return Registries.ENTITY_TYPE.register(key, KryptonEntityType.Builder<T>(key, category).apply(builder).build())
    }
}

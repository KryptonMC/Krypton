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
import org.kryptonmc.api.entity.EntityCategory
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.ambient.KryptonBat
import org.kryptonmc.krypton.entity.animal.KryptonAxolotl
import org.kryptonmc.krypton.entity.animal.KryptonBee
import org.kryptonmc.krypton.entity.animal.KryptonCat
import org.kryptonmc.krypton.entity.animal.KryptonChicken
import org.kryptonmc.krypton.entity.animal.KryptonCow
import org.kryptonmc.krypton.entity.animal.KryptonFox
import org.kryptonmc.krypton.entity.animal.KryptonGoat
import org.kryptonmc.krypton.entity.animal.KryptonMooshroom
import org.kryptonmc.krypton.entity.animal.KryptonOcelot
import org.kryptonmc.krypton.entity.animal.KryptonPanda
import org.kryptonmc.krypton.entity.animal.KryptonParrot
import org.kryptonmc.krypton.entity.animal.KryptonPig
import org.kryptonmc.krypton.entity.animal.KryptonPolarBear
import org.kryptonmc.krypton.entity.animal.KryptonRabbit
import org.kryptonmc.krypton.entity.animal.KryptonSheep
import org.kryptonmc.krypton.entity.animal.KryptonTurtle
import org.kryptonmc.krypton.entity.animal.KryptonWolf
import org.kryptonmc.krypton.entity.aquatic.KryptonCod
import org.kryptonmc.krypton.entity.aquatic.KryptonDolphin
import org.kryptonmc.krypton.entity.aquatic.KryptonGlowSquid
import org.kryptonmc.krypton.entity.aquatic.KryptonPufferfish
import org.kryptonmc.krypton.entity.aquatic.KryptonSalmon
import org.kryptonmc.krypton.entity.aquatic.KryptonSquid
import org.kryptonmc.krypton.entity.aquatic.KryptonTropicalFish
import org.kryptonmc.krypton.entity.hanging.KryptonPainting
import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.monster.KryptonZombie
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.projectile.KryptonArrow
import org.kryptonmc.krypton.entity.projectile.KryptonDragonFireball
import org.kryptonmc.krypton.entity.projectile.KryptonEgg
import org.kryptonmc.krypton.entity.projectile.KryptonEnderPearl
import org.kryptonmc.krypton.entity.projectile.KryptonExperienceBottle
import org.kryptonmc.krypton.entity.projectile.KryptonFireworkRocket
import org.kryptonmc.krypton.entity.projectile.KryptonFishingHook
import org.kryptonmc.krypton.entity.projectile.KryptonLargeFireball
import org.kryptonmc.krypton.entity.projectile.KryptonLlamaSpit
import org.kryptonmc.krypton.entity.projectile.KryptonShulkerBullet
import org.kryptonmc.krypton.entity.projectile.KryptonSmallFireball
import org.kryptonmc.krypton.entity.projectile.KryptonSnowball
import org.kryptonmc.krypton.entity.projectile.KryptonSpectralArrow
import org.kryptonmc.krypton.entity.projectile.KryptonThrownPotion
import org.kryptonmc.krypton.entity.projectile.KryptonTrident
import org.kryptonmc.krypton.entity.projectile.KryptonWitherSkull
import org.kryptonmc.krypton.entity.vehicle.KryptonBoat
import org.kryptonmc.krypton.entity.vehicle.KryptonCommandBlockMinecart
import org.kryptonmc.krypton.entity.vehicle.KryptonFurnaceMinecart
import org.kryptonmc.krypton.entity.vehicle.KryptonMinecart
import org.kryptonmc.krypton.entity.vehicle.KryptonTNTMinecart

@Suppress("MagicNumber")
object KryptonEntityTypes {

    private const val MAGIC_HORSE_WIDTH = 1.3964844F
    private const val HUMAN_HEIGHT = 1.95F

    // TODO: Make each of these be of their respective entity types when they exist
    @JvmField
    val AREA_EFFECT_CLOUD: KryptonEntityType<KryptonAreaEffectCloud> = register("area_effect_cloud", KryptonEntityCategories.MISC) {
        fireImmune()
        size(6F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val ARMOR_STAND: KryptonEntityType<KryptonArmorStand> = register("armor_stand", KryptonEntityCategories.MISC) {
        size(0.5F, 1.975F)
        clientTrackingRange(10)
    }
    @JvmField
    val ARROW: KryptonEntityType<KryptonArrow> = register("arrow", KryptonEntityCategories.MISC) {
        clientTrackingRange(4)
        updateInterval(20)
    }
    @JvmField
    val AXOLOTL: KryptonEntityType<KryptonAxolotl> = register("axolotl", KryptonEntityCategories.UNDERGROUND_WATER_CREATURE) {
        size(0.75F, 0.42F)
        clientTrackingRange(10)
    }
    @JvmField
    val BAT: KryptonEntityType<KryptonBat> = register("bat", KryptonEntityCategories.AMBIENT) {
        size(0.5F, 0.9F)
        clientTrackingRange(5)
    }
    @JvmField
    val BEE: KryptonEntityType<KryptonBee> = register("bee", KryptonEntityCategories.CREATURE) {
        size(0.7F, 0.6F)
        clientTrackingRange(8)
    }
    @JvmField
    val BLAZE: KryptonEntityType<KryptonEntity> = register("blaze", KryptonEntityCategories.MONSTER) {
        fireImmune()
        clientTrackingRange(8)
    }
    @JvmField
    val BOAT: KryptonEntityType<KryptonBoat> = register("boat", KryptonEntityCategories.MISC) {
        size(1.375F, 0.5625F)
        clientTrackingRange(10)
    }
    @JvmField
    val CAT: KryptonEntityType<KryptonCat> = register("cat", KryptonEntityCategories.CREATURE) {
        size(0.6F, 0.7F)
        clientTrackingRange(8)
    }
    @JvmField
    val CAVE_SPIDER: KryptonEntityType<KryptonEntity> = register("cave_spider", KryptonEntityCategories.MONSTER) {
        size(0.7F, 0.5F)
        clientTrackingRange(8)
    }
    @JvmField
    val CHICKEN: KryptonEntityType<KryptonChicken> = register("chicken", KryptonEntityCategories.CREATURE) {
        size(0.4F, 0.7F)
        clientTrackingRange(10)
    }
    @JvmField
    val COD: KryptonEntityType<KryptonCod> = register("cod", KryptonEntityCategories.WATER_AMBIENT) {
        size(0.5F, 0.3F)
        clientTrackingRange(4)
    }
    @JvmField
    val COW: KryptonEntityType<KryptonCow> = register("cow", KryptonEntityCategories.CREATURE) {
        size(0.9F, 1.4F)
        clientTrackingRange(10)
    }
    @JvmField
    val CREEPER: KryptonEntityType<KryptonCreeper> = register("creeper", KryptonEntityCategories.MONSTER) {
        size(0.6F, 1.7F)
        clientTrackingRange(8)
    }
    @JvmField
    val DOLPHIN: KryptonEntityType<KryptonDolphin> = register("dolphin", KryptonEntityCategories.WATER_CREATURE) { size(0.9F, 0.6F) }
    @JvmField
    val DONKEY: KryptonEntityType<KryptonEntity> = register("donkey", KryptonEntityCategories.CREATURE) {
        size(MAGIC_HORSE_WIDTH, 1.5F)
        clientTrackingRange(10)
    }
    @JvmField
    val DRAGON_FIREBALL: KryptonEntityType<KryptonDragonFireball> = register("dragon_fireball", KryptonEntityCategories.MISC) {
        size(1F, 1F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val DROWNED: KryptonEntityType<KryptonEntity> = register("drowned", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val ELDER_GUARDIAN: KryptonEntityType<KryptonEntity> = register("elder_guardian", KryptonEntityCategories.MONSTER) {
        size(1.9975F, 1.9975F)
        clientTrackingRange(10)
    }
    @JvmField
    val END_CRYSTAL: KryptonEntityType<KryptonEntity> = register("end_crystal", KryptonEntityCategories.MISC) {
        size(2F, 2F)
        clientTrackingRange(16)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val ENDER_DRAGON: KryptonEntityType<KryptonEntity> = register("ender_dragon", KryptonEntityCategories.MONSTER) {
        fireImmune()
        size(16F, 8F)
        clientTrackingRange(10)
    }
    @JvmField
    val ENDERMAN: KryptonEntityType<KryptonEntity> = register("enderman", KryptonEntityCategories.MONSTER) {
        size(0.6F, 2.9F)
        clientTrackingRange(8)
    }
    @JvmField
    val ENDERMTIE: KryptonEntityType<KryptonEntity> = register("endermite", KryptonEntityCategories.MONSTER) {
        size(0.4F, 0.3F)
        clientTrackingRange(8)
    }
    @JvmField
    val EVOKER: KryptonEntityType<KryptonEntity> = register("evoker", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val EVOKER_FANGS: KryptonEntityType<KryptonEntity> = register("evoker_fangs", KryptonEntityCategories.MISC) {
        size(0.5F, 0.8F)
        clientTrackingRange(6)
        updateInterval(2)
    }
    @JvmField
    val EXPERIENCE_ORB: KryptonEntityType<KryptonExperienceOrb> = register("experience_orb", KryptonEntityCategories.MISC) {
        size(0.5F, 0.5F)
        clientTrackingRange(6)
        updateInterval(20)
    }
    @JvmField
    val EYE_OF_ENDER: KryptonEntityType<KryptonEntity> = register("eye_of_ender", KryptonEntityCategories.MISC) {
        size(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(4)
    }
    @JvmField
    val FALLING_BLOCK: KryptonEntityType<KryptonEntity> = register("falling_block", KryptonEntityCategories.MISC) {
        size(0.98F, 0.98F)
        clientTrackingRange(10)
        updateInterval(20)
    }
    @JvmField
    val FIREWORK_ROCKET: KryptonEntityType<KryptonFireworkRocket> = register("firework_rocket", KryptonEntityCategories.MISC) {
        size(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val FOX: KryptonEntityType<KryptonFox> = register("fox", KryptonEntityCategories.CREATURE) {
        size(0.6F, 0.7F)
        clientTrackingRange(8)
        immuneTo(Blocks.SWEET_BERRY_BUSH)
    }
    @JvmField
    val GHAST: KryptonEntityType<KryptonEntity> = register("ghast", KryptonEntityCategories.MONSTER) {
        size(4F, 4F)
        fireImmune()
        clientTrackingRange(10)
    }
    @JvmField
    val GIANT: KryptonEntityType<KryptonEntity> = register("giant", KryptonEntityCategories.MONSTER) {
        size(3.6F, 12F)
        clientTrackingRange(10)
    }
    @JvmField
    val GLOW_ITEM_FRAME: KryptonEntityType<KryptonEntity> = register("glow_item_frame", KryptonEntityCategories.MISC) {
        size(0.5F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val GLOW_SQUID: KryptonEntityType<KryptonGlowSquid> = register("glow_squid", KryptonEntityCategories.UNDERGROUND_WATER_CREATURE) {
        size(0.8F, 0.8F)
        clientTrackingRange(10)
    }
    @JvmField
    val GOAT: KryptonEntityType<KryptonGoat> = register("goat", KryptonEntityCategories.CREATURE) {
        size(0.9F, 1.3F)
        clientTrackingRange(10)
    }
    @JvmField
    val GUARDIAN: KryptonEntityType<KryptonEntity> = register("guardian", KryptonEntityCategories.MONSTER) {
        size(0.85F, 0.85F)
        clientTrackingRange(8)
    }
    @JvmField
    val HOGLIN: KryptonEntityType<KryptonEntity> = register("hoglin", KryptonEntityCategories.MONSTER) {
        size(MAGIC_HORSE_WIDTH, 1.4F)
        clientTrackingRange(8)
    }
    @JvmField
    val HORSE: KryptonEntityType<KryptonEntity> = register("horse", KryptonEntityCategories.CREATURE) {
        size(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(10)
    }
    @JvmField
    val HUSK: KryptonEntityType<KryptonEntity> = register("husk", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val ILLUSIONER: KryptonEntityType<KryptonEntity> = register("illusioner", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val IRON_GOLEM: KryptonEntityType<KryptonEntity> = register("iron_golem", KryptonEntityCategories.MISC) {
        size(1.4F, 2.7F)
        clientTrackingRange(10)
    }
    @JvmField
    val ITEM: KryptonEntityType<KryptonEntity> = register("item", KryptonEntityCategories.MISC) {
        size(0.25F, 0.25F)
        clientTrackingRange(6)
        updateInterval(20)
    }
    @JvmField
    val ITEM_FRAME: KryptonEntityType<KryptonEntity> = register("item_frame", KryptonEntityCategories.MISC) {
        size(0.5F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val FIREBALL: KryptonEntityType<KryptonLargeFireball> = register("fireball", KryptonEntityCategories.MISC) {
        size(1F, 1F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val LEASH_KNOT: KryptonEntityType<KryptonEntity> = register("leash_knot", KryptonEntityCategories.MISC) {
        size(0.375F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val LIGHTNING_BOLT: KryptonEntityType<KryptonEntity> = register("lightning_bolt", KryptonEntityCategories.MISC) {
        size(0F, 0F)
        clientTrackingRange(16)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val LLAMA: KryptonEntityType<KryptonEntity> = register("llama", KryptonEntityCategories.CREATURE) {
        size(0.9F, 1.87F)
        clientTrackingRange(10)
    }
    @JvmField
    val LLAMA_SPIT: KryptonEntityType<KryptonLlamaSpit> = register("llama_spit", KryptonEntityCategories.MISC) {
        size(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val MAGMA_CUBE: KryptonEntityType<KryptonEntity> = register("magma_cube", KryptonEntityCategories.MONSTER) {
        size(2.04F, 2.04F)
        fireImmune()
        clientTrackingRange(8)
    }
    @JvmField
    val MARKER: KryptonEntityType<KryptonEntity> = register("marker", KryptonEntityCategories.MISC) {
        size(0F, 0F)
        clientTrackingRange(0)
    }
    @JvmField
    val MINECART: KryptonEntityType<KryptonMinecart> = minecart("minecart")
    @JvmField
    val CHEST_MINECART: KryptonEntityType<KryptonEntity> = minecart("chest_minecart")
    @JvmField
    val COMMAND_BLOCK_MINECART: KryptonEntityType<KryptonCommandBlockMinecart> = minecart("command_block_minecart")
    @JvmField
    val FURNACE_MINECART: KryptonEntityType<KryptonFurnaceMinecart> = minecart("furnace_minecart")
    @JvmField
    val HOPPER_MINECART: KryptonEntityType<KryptonEntity> = minecart("hopper_minecart")
    @JvmField
    val SPAWNER_MINECART: KryptonEntityType<KryptonEntity> = minecart("spawner_minecart")
    @JvmField
    val TNT_MINECART: KryptonEntityType<KryptonTNTMinecart> = minecart("tnt_minecart")
    @JvmField
    val MULE: KryptonEntityType<KryptonEntity> = register("mule", KryptonEntityCategories.CREATURE) {
        size(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(8)
    }
    @JvmField
    val MOOSHROOM: KryptonEntityType<KryptonMooshroom> = register("mooshroom", KryptonEntityCategories.CREATURE) {
        size(0.9F, 1.4F)
        clientTrackingRange(10)
    }
    @JvmField
    val OCELOT: KryptonEntityType<KryptonOcelot> = register("ocelot", KryptonEntityCategories.CREATURE) {
        size(0.6F, 0.7F)
        clientTrackingRange(10)
    }
    @JvmField
    val PAINTING: KryptonEntityType<KryptonPainting> = register("painting", KryptonEntityCategories.MISC) {
        size(0.5F, 0.5F)
        clientTrackingRange(10)
        updateInterval(Int.MAX_VALUE)
    }
    @JvmField
    val PANDA: KryptonEntityType<KryptonPanda> = register("panda", KryptonEntityCategories.CREATURE) {
        size(1.3F, 1.25F)
        clientTrackingRange(10)
    }
    @JvmField
    val PARROT: KryptonEntityType<KryptonParrot> = register("parrot", KryptonEntityCategories.CREATURE) {
        size(0.5F, 0.9F)
        clientTrackingRange(8)
    }
    @JvmField
    val PHANTOM: KryptonEntityType<KryptonEntity> = register("phantom", KryptonEntityCategories.MONSTER) {
        size(0.9F, 0.5F)
        clientTrackingRange(8)
    }
    @JvmField
    val PIG: KryptonEntityType<KryptonPig> = register("pig", KryptonEntityCategories.CREATURE) {
        size(0.9F, 0.9F)
        clientTrackingRange(10)
    }
    @JvmField
    val PIGLIN: KryptonEntityType<KryptonEntity> = register("piglin", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val PIGLIN_BRUTE: KryptonEntityType<KryptonEntity> = register("piglin_brute", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val PILLAGER: KryptonEntityType<KryptonEntity> = register("pillager", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val POLAR_BEAR: KryptonEntityType<KryptonPolarBear> = register("polar_bear", KryptonEntityCategories.CREATURE) {
        size(1.4F, 1.4F)
        clientTrackingRange(10)
        immuneTo(Blocks.POWDER_SNOW)
    }
    @JvmField
    val PRIMED_TNT: KryptonEntityType<KryptonEntity> = register("tnt", KryptonEntityCategories.MISC) {
        size(0.98F, 0.98F)
        fireImmune()
        clientTrackingRange(10)
        updateInterval(10)
    }
    @JvmField
    val PUFFERFISH: KryptonEntityType<KryptonPufferfish> = register("pufferfish", KryptonEntityCategories.WATER_AMBIENT) {
        size(0.7F, 0.7F)
        clientTrackingRange(4)
    }
    @JvmField
    val RABBIT: KryptonEntityType<KryptonRabbit> = register("rabbit", KryptonEntityCategories.CREATURE) {
        size(0.4F, 0.5F)
        clientTrackingRange(8)
    }
    @JvmField
    val RAVAGER: KryptonEntityType<KryptonEntity> = register("ravager", KryptonEntityCategories.MONSTER) {
        size(1.95F, 2.2F)
        clientTrackingRange(10)
    }
    @JvmField
    val SALMON: KryptonEntityType<KryptonSalmon> = register("salmon", KryptonEntityCategories.WATER_AMBIENT) {
        size(0.7F, 0.4F)
        clientTrackingRange(4)
    }
    @JvmField
    val SHEEP: KryptonEntityType<KryptonSheep> = register("sheep", KryptonEntityCategories.CREATURE) {
        size(0.9F, 1.3F)
        clientTrackingRange(10)
    }
    @JvmField
    val SHULKER: KryptonEntityType<KryptonEntity> = register("shulker", KryptonEntityCategories.MONSTER) {
        size(1F, 1F)
        fireImmune()
        clientTrackingRange(10)
    }
    @JvmField
    val SHULKER_BULLET: KryptonEntityType<KryptonShulkerBullet> = register("shulker_bullet", KryptonEntityCategories.MISC) {
        size(0.3125F, 0.3125F)
        clientTrackingRange(8)
    }
    @JvmField
    val SILVERFISH: KryptonEntityType<KryptonEntity> = register("silverfish", KryptonEntityCategories.MONSTER) {
        size(0.4F, 0.3F)
        clientTrackingRange(8)
    }
    @JvmField
    val SKELETON: KryptonEntityType<KryptonEntity> = register("skeleton", KryptonEntityCategories.MONSTER) {
        size(0.6F, 1.99F)
        clientTrackingRange(8)
    }
    @JvmField
    val SKELETON_HORSE: KryptonEntityType<KryptonEntity> = register("skeleton_horse", KryptonEntityCategories.CREATURE) {
        size(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(10)
    }
    @JvmField
    val SLIME: KryptonEntityType<KryptonEntity> = register("slime", KryptonEntityCategories.MONSTER) {
        size(2.04F, 2.04F)
        clientTrackingRange(10)
    }
    @JvmField
    val SMALL_FIREBALL: KryptonEntityType<KryptonSmallFireball> = register("small_fireball", KryptonEntityCategories.MISC) {
        size(0.3125F, 0.3125F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val SNOW_GOLEM: KryptonEntityType<KryptonEntity> = register("snow_golem", KryptonEntityCategories.MISC) {
        size(0.7F, 1.9F)
        clientTrackingRange(8)
        immuneTo(Blocks.POWDER_SNOW)
    }
    @JvmField
    val SNOWBALL: KryptonEntityType<KryptonSnowball> = register("snowball", KryptonEntityCategories.MISC) {
        size(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val SPECTRAL_ARROW: KryptonEntityType<KryptonSpectralArrow> = register("spectral_arrow", KryptonEntityCategories.MISC) {
        size(0.5F, 0.5F)
        clientTrackingRange(4)
        updateInterval(20)
    }
    @JvmField
    val SPIDER: KryptonEntityType<KryptonEntity> = register("spider", KryptonEntityCategories.MONSTER) {
        size(1.4F, 0.9F)
        clientTrackingRange(8)
    }
    @JvmField
    val SQUID: KryptonEntityType<KryptonSquid> = register("squid", KryptonEntityCategories.WATER_CREATURE) {
        size(0.8F, 0.8F)
        clientTrackingRange(8)
    }
    @JvmField
    val STRAY: KryptonEntityType<KryptonEntity> = register("stray", KryptonEntityCategories.MONSTER) {
        size(0.6F, 1.99F)
        clientTrackingRange(8)
        immuneTo(Blocks.POWDER_SNOW)
    }
    @JvmField
    val STRIDER: KryptonEntityType<KryptonEntity> = register("strider", KryptonEntityCategories.CREATURE) {
        size(0.9F, 1.7F)
        fireImmune()
        clientTrackingRange(10)
    }
    @JvmField
    val EGG: KryptonEntityType<KryptonEgg> = thrownItem("egg")
    @JvmField
    val ENDER_PEARL: KryptonEntityType<KryptonEnderPearl> = thrownItem("ender_pearl")
    @JvmField
    val EXPERIENCE_BOTTLE: KryptonEntityType<KryptonExperienceBottle> = thrownItem("experience_bottle")
    @JvmField
    val POTION: KryptonEntityType<KryptonThrownPotion> = thrownItem("potion")
    @JvmField
    val TRIDENT: KryptonEntityType<KryptonTrident> = register("trident", KryptonEntityCategories.MISC) {
        size(0.5F, 0.5F)
        clientTrackingRange(4)
        updateInterval(20)
    }
    @JvmField
    val TRADER_LLAMA: KryptonEntityType<KryptonEntity> = register("trader_llama", KryptonEntityCategories.CREATURE) {
        size(0.9F, 1.87F)
        clientTrackingRange(10)
    }
    @JvmField
    val TROPICAL_FISH: KryptonEntityType<KryptonTropicalFish> = register("tropical_fish", KryptonEntityCategories.WATER_AMBIENT) {
        size(0.5F, 0.4F)
        clientTrackingRange(4)
    }
    @JvmField
    val TURTLE: KryptonEntityType<KryptonTurtle> = register("turtle", KryptonEntityCategories.CREATURE) {
        size(1.2F, 0.4F)
        clientTrackingRange(10)
    }
    @JvmField
    val VEX: KryptonEntityType<KryptonEntity> = register("vex", KryptonEntityCategories.MONSTER) {
        size(0.4F, 0.8F)
        fireImmune()
        clientTrackingRange(8)
    }
    @JvmField
    val VILLAGER: KryptonEntityType<KryptonEntity> = register("villager", KryptonEntityCategories.MISC) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(10)
    }
    @JvmField
    val VINDICATOR: KryptonEntityType<KryptonEntity> = register("vindicator", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val WANDERING_TRADER: KryptonEntityType<KryptonEntity> = register("wandering_trader", KryptonEntityCategories.CREATURE) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(10)
    }
    @JvmField
    val WITCH: KryptonEntityType<KryptonEntity> = register("witch", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val WITHER: KryptonEntityType<KryptonEntity> = register("wither", KryptonEntityCategories.MONSTER) {
        size(0.9F, 3.5F)
        fireImmune()
        clientTrackingRange(10)
        immuneTo(Blocks.WITHER_ROSE)
    }
    @JvmField
    val WITHER_SKELETON: KryptonEntityType<KryptonEntity> = register("wither_skeleton", KryptonEntityCategories.MONSTER) {
        size(0.7F, 2.4F)
        fireImmune()
        clientTrackingRange(8)
        immuneTo(Blocks.WITHER_ROSE)
    }
    @JvmField
    val WITHER_SKULL: KryptonEntityType<KryptonWitherSkull> = register("wither_skull", KryptonEntityCategories.MISC) {
        size(0.3125F, 0.3125F)
        clientTrackingRange(4)
        updateInterval(10)
    }
    @JvmField
    val WOLF: KryptonEntityType<KryptonWolf> = register("wolf", KryptonEntityCategories.CREATURE) {
        size(0.6F, 0.85F)
        clientTrackingRange(10)
    }
    @JvmField
    val ZOGLIN: KryptonEntityType<KryptonEntity> = register("zoglin", KryptonEntityCategories.MONSTER) {
        size(MAGIC_HORSE_WIDTH, 1.4F)
        fireImmune()
        clientTrackingRange(8)
    }
    @JvmField
    val ZOMBIE: KryptonEntityType<KryptonZombie> = register("zombie", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val ZOMBIE_HORSE: KryptonEntityType<KryptonEntity> = register("zombie_horse", KryptonEntityCategories.CREATURE) {
        size(MAGIC_HORSE_WIDTH, 1.6F)
        clientTrackingRange(10)
    }
    @JvmField
    val ZOMBIE_VILLAGER: KryptonEntityType<KryptonEntity> = register("zombie_villager", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        clientTrackingRange(8)
    }
    @JvmField
    val ZOMBIFIED_PIGLIN: KryptonEntityType<KryptonEntity> = register("zombified_piglin", KryptonEntityCategories.MONSTER) {
        size(0.6F, HUMAN_HEIGHT)
        fireImmune()
        clientTrackingRange(8)
    }
    @JvmField
    val PLAYER: KryptonEntityType<KryptonPlayer> = register("player", KryptonEntityCategories.MISC) {
        notSummonable()
        clientTrackingRange(32)
        updateInterval(2)
    }
    @JvmField
    val FISHING_HOOK: KryptonEntityType<KryptonFishingHook> = register("fishing_bobber", KryptonEntityCategories.MISC) {
        size(0.25F, 0.25F)
        notSummonable()
        clientTrackingRange(4)
        updateInterval(5)
    }

    @JvmStatic
    private fun <T : KryptonEntity> minecart(name: String): KryptonEntityType<T> = register(name, KryptonEntityCategories.MISC) {
        size(0.98F, 0.7F)
        clientTrackingRange(8)
    }

    @JvmStatic
    private fun <T : KryptonEntity> thrownItem(name: String): KryptonEntityType<T> = register(name, KryptonEntityCategories.MISC) {
        size(0.25F, 0.25F)
        clientTrackingRange(4)
        updateInterval(10)
    }

    @JvmStatic
    private inline fun <T : KryptonEntity> register(
        name: String,
        category: EntityCategory,
        builder: KryptonEntityType.Builder<T>.() -> Unit = {}
    ): KryptonEntityType<T> = register(name, KryptonEntityType.Builder<T>(category).apply(builder).build())

    @JvmStatic
    private fun <T : KryptonEntity> register(name: String, type: KryptonEntityType<T>): KryptonEntityType<T> =
        Registries.ENTITY_TYPE.register(Key.key(name), type)
}

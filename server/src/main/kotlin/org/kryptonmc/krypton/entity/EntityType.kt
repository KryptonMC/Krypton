/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.api.util.minecraftKey

// commented out entries in here are ones that are spawned in their own special ways
// TODO: Use this for something
enum class EntityType(val type: Int, val id: Key) {

    AREA_EFFECT_CLOUD(0, minecraftKey("area_effect_cloud")),
    ARMOR_STAND(1, minecraftKey("armor_stand")),
    ARROW(2, minecraftKey("arrow")),
    BAT(3, minecraftKey("bat")),
    BEE(4, minecraftKey("bee")),
    BLAZE(5, minecraftKey("blaze")),
    BOAT(6, minecraftKey("boat")),
    CAT(7, minecraftKey("cat")),
    CAVE_SPIDER(8, minecraftKey("cave_spider")),
    CHICKEN(9, minecraftKey("chicken")),
    COD(10, minecraftKey("cod")),
    COW(11, minecraftKey("cow")),
    CREEPER(12, minecraftKey("creeper")),
    DOLPHIN(13, minecraftKey("dolphin")),
    DONKEY(14, minecraftKey("donkey")),
    DRAGON_FIREBALL(15, minecraftKey("dragon_fireball")),
    DROWNED(16, minecraftKey("drowned")),
    ELDER_GUARDIAN(17, minecraftKey("elder_guardian")),
    END_CRYSTAL(18, minecraftKey("end_crystal")),
    ENDER_DRAGON(19, minecraftKey("ender_dragon")),
    ENDERMAN(20, minecraftKey("enderman")),
    ENDERMTIE(21, minecraftKey("endermite")),
    EVOKER(22, minecraftKey("evoker")),
    EVOKER_FANGS(23, minecraftKey("evoker_fangs")),
    //EXPERIENCE_ORB(24, minecraftKey("experience_orb")),
    EYE_OF_ENDER(24, minecraftKey("eye_of_ender")),
    FALLING_BLOCK(26, minecraftKey("falling_block")),
    FIREWORK_ROCKET(27, minecraftKey("firework_rocket")),
    FOX(28, minecraftKey("fox")),
    GHAST(29, minecraftKey("ghast")),
    GIANT(30, minecraftKey("giant")),
    GUARDIAN(31, minecraftKey("guardian")),
    HOGLIN(32, minecraftKey("hoglin")),
    HORSE(33, minecraftKey("horse")),
    HUSK(34, minecraftKey("husk")),
    ILLUSIONER(35, minecraftKey("illusioner")),
    IRON_GOLEM(36, minecraftKey("iron_golem")),
    ITEM(37, minecraftKey("item")),
    ITEM_FRAME(38, minecraftKey("item_frame")),
    FIREBALL(39, minecraftKey("fireball")),
    LEASH_KNOT(40, minecraftKey("leash_knot")),
    LIGHTNING_BOLT(41, minecraftKey("lightning_bolt")),
    LLAMA(42, minecraftKey("llama")),
    LLAMA_SPIT(43, minecraftKey("llama_spit")),
    MAGMA_CUBE(44, minecraftKey("magma_cube")),
    MINECART(45, minecraftKey("minecart")),
    CHEST_MINECART(46, minecraftKey("chest_minecart")),
    COMMAND_BLOCK_MINECART(47, minecraftKey("commandblock_minecart")),
    FURNACE_MINECART(48, minecraftKey("furnace_minecart")),
    HOPPER_MINECART(49, minecraftKey("hopper_minecart")),
    SPAWNER_MINECART(50, minecraftKey("spawner_minecart")),
    TNT_MINECART(51, minecraftKey("tnt_minecart")),
    MULE(52, minecraftKey("mule")),
    MOOSHROOM(53, minecraftKey("mooshroom")),
    OCELOT(54, minecraftKey("ocelot")),
    //PAINTING(55, minecraftKey("painting")),
    PANDA(56, minecraftKey("panda")),
    PARROT(57, minecraftKey("parrot")),
    PHANTOM(58, minecraftKey("phantom")),
    PIG(59, minecraftKey("pig")),
    PIGLIN(60, minecraftKey("piglin")),
    PIGLIN_BRUTE(61, minecraftKey("piglin_brute")),
    PILLAGER(62, minecraftKey("pillager")),
    POLAR_BEAR(63, minecraftKey("polar_bear")),
    //PRIMED_TNT(64, minecraftKey("tnt")),
    PUFFERFISH(65, minecraftKey("pufferfish")),
    RABBIT(66, minecraftKey("rabbit")),
    RAVAGER(67, minecraftKey("ravager")),
    SALMON(68, minecraftKey("salmon")),
    SHEEP(69, minecraftKey("sheep")),
    SHULKER(70, minecraftKey("shulker")),
    SHULKER_BULLET(71, minecraftKey("shulker_bullet")),
    SILVERFISH(72, minecraftKey("silverfish")),
    SKELETON(73, minecraftKey("skeleton")),
    SKELETON_HORSE(74, minecraftKey("skeleton_horse")),
    SLIME(75, minecraftKey("slime")),
    SMALL_FIREBALL(76, minecraftKey("small_fireball")),
    SNOW_GOLEM(77, minecraftKey("snow_golem")),
    SNOWBALL(78, minecraftKey("snowball")),
    SPECTRAL_ARROW(79, minecraftKey("spectral_arrow")),
    SPIDER(80, minecraftKey("spider")),
    SQUID(81, minecraftKey("squid")),
    STRAY(82, minecraftKey("stray")),
    STRIDER(83, minecraftKey("strider")),
    THROWN_EGG(84, minecraftKey("egg")),
    THROWN_ENDER_PEARL(85, minecraftKey("ender_pearl")),
    THROWN_EXPERIENCE_BOTTLE(86, minecraftKey("experience_bottle")),
    THROWN_POTION(87, minecraftKey("potion")),
    THROWN_TRIDENT(88, minecraftKey("trident")),
    TRADER_LLAMA(89, minecraftKey("trader_llama")),
    TROPICAL_FISH(90, minecraftKey("tropical_fish")),
    TURTLE(91, minecraftKey("turtle")),
    VEX(92, minecraftKey("vex")),
    VILLAGER(93, minecraftKey("villager")),
    VINDICATOR(94, minecraftKey("vindicator")),
    WANDERING_TRADER(95, minecraftKey("wandering_trader")),
    WITCH(96, minecraftKey("witch")),
    WITHER(97, minecraftKey("wither")),
    WITHER_SKELETON(98, minecraftKey("wither_skeleton")),
    WITHER_SKULL(99, minecraftKey("wither_skull")),
    WOLF(100, minecraftKey("wolf")),
    ZOGLIN(101, minecraftKey("zoglin")),
    ZOMBIE(102, minecraftKey("zombie")),
    ZOMBIE_HORSE(103, minecraftKey("zombie_horse")),
    ZOMBIE_VILLAGER(104, minecraftKey("zombie_villager")),
    ZOMBIFIED_PIGLIN(105, minecraftKey("zombified_piglin")),
    //PLAYER(106, minecraftKey("player")),
    FISHING_HOOK(107, minecraftKey("fishing_bobber"))
}

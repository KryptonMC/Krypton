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
import net.kyori.adventure.key.Key.key

// commented out entries in here are ones that are spawned in their own special ways
// TODO: Use this for something
enum class EntityType(val type: Int, val id: Key) {

    AREA_EFFECT_CLOUD(0, key("area_effect_cloud")),
    ARMOR_STAND(1, key("armor_stand")),
    ARROW(2, key("arrow")),
    BAT(3, key("bat")),
    BEE(4, key("bee")),
    BLAZE(5, key("blaze")),
    BOAT(6, key("boat")),
    CAT(7, key("cat")),
    CAVE_SPIDER(8, key("cave_spider")),
    CHICKEN(9, key("chicken")),
    COD(10, key("cod")),
    COW(11, key("cow")),
    CREEPER(12, key("creeper")),
    DOLPHIN(13, key("dolphin")),
    DONKEY(14, key("donkey")),
    DRAGON_FIREBALL(15, key("dragon_fireball")),
    DROWNED(16, key("drowned")),
    ELDER_GUARDIAN(17, key("elder_guardian")),
    END_CRYSTAL(18, key("end_crystal")),
    ENDER_DRAGON(19, key("ender_dragon")),
    ENDERMAN(20, key("enderman")),
    ENDERMTIE(21, key("endermite")),
    EVOKER(22, key("evoker")),
    EVOKER_FANGS(23, key("evoker_fangs")),
    //EXPERIENCE_ORB(24, Key.key("experience_orb")),
    EYE_OF_ENDER(24, key("eye_of_ender")),
    FALLING_BLOCK(26, key("falling_block")),
    FIREWORK_ROCKET(27, key("firework_rocket")),
    FOX(28, key("fox")),
    GHAST(29, key("ghast")),
    GIANT(30, key("giant")),
    GUARDIAN(31, key("guardian")),
    HOGLIN(32, key("hoglin")),
    HORSE(33, key("horse")),
    HUSK(34, key("husk")),
    ILLUSIONER(35, key("illusioner")),
    IRON_GOLEM(36, key("iron_golem")),
    ITEM(37, key("item")),
    ITEM_FRAME(38, key("item_frame")),
    FIREBALL(39, key("fireball")),
    LEASH_KNOT(40, key("leash_knot")),
    LIGHTNING_BOLT(41, key("lightning_bolt")),
    LLAMA(42, key("llama")),
    LLAMA_SPIT(43, key("llama_spit")),
    MAGMA_CUBE(44, key("magma_cube")),
    MINECART(45, key("minecart")),
    CHEST_MINECART(46, key("chest_minecart")),
    COMMAND_BLOCK_MINECART(47, key("commandblock_minecart")),
    FURNACE_MINECART(48, key("furnace_minecart")),
    HOPPER_MINECART(49, key("hopper_minecart")),
    SPAWNER_MINECART(50, key("spawner_minecart")),
    TNT_MINECART(51, key("tnt_minecart")),
    MULE(52, key("mule")),
    MOOSHROOM(53, key("mooshroom")),
    OCELOT(54, key("ocelot")),
    //PAINTING(55, Key.key("painting")),
    PANDA(56, key("panda")),
    PARROT(57, key("parrot")),
    PHANTOM(58, key("phantom")),
    PIG(59, key("pig")),
    PIGLIN(60, key("piglin")),
    PIGLIN_BRUTE(61, key("piglin_brute")),
    PILLAGER(62, key("pillager")),
    POLAR_BEAR(63, key("polar_bear")),
    //PRIMED_TNT(64, Key.key("tnt")),
    PUFFERFISH(65, key("pufferfish")),
    RABBIT(66, key("rabbit")),
    RAVAGER(67, key("ravager")),
    SALMON(68, key("salmon")),
    SHEEP(69, key("sheep")),
    SHULKER(70, key("shulker")),
    SHULKER_BULLET(71, key("shulker_bullet")),
    SILVERFISH(72, key("silverfish")),
    SKELETON(73, key("skeleton")),
    SKELETON_HORSE(74, key("skeleton_horse")),
    SLIME(75, key("slime")),
    SMALL_FIREBALL(76, key("small_fireball")),
    SNOW_GOLEM(77, key("snow_golem")),
    SNOWBALL(78, key("snowball")),
    SPECTRAL_ARROW(79, key("spectral_arrow")),
    SPIDER(80, key("spider")),
    SQUID(81, key("squid")),
    STRAY(82, key("stray")),
    STRIDER(83, key("strider")),
    THROWN_EGG(84, key("egg")),
    THROWN_ENDER_PEARL(85, key("ender_pearl")),
    THROWN_EXPERIENCE_BOTTLE(86, key("experience_bottle")),
    THROWN_POTION(87, key("potion")),
    THROWN_TRIDENT(88, key("trident")),
    TRADER_LLAMA(89, key("trader_llama")),
    TROPICAL_FISH(90, key("tropical_fish")),
    TURTLE(91, key("turtle")),
    VEX(92, key("vex")),
    VILLAGER(93, key("villager")),
    VINDICATOR(94, key("vindicator")),
    WANDERING_TRADER(95, key("wandering_trader")),
    WITCH(96, key("witch")),
    WITHER(97, key("wither")),
    WITHER_SKELETON(98, key("wither_skeleton")),
    WITHER_SKULL(99, key("wither_skull")),
    WOLF(100, key("wolf")),
    ZOGLIN(101, key("zoglin")),
    ZOMBIE(102, key("zombie")),
    ZOMBIE_HORSE(103, key("zombie_horse")),
    ZOMBIE_VILLAGER(104, key("zombie_villager")),
    ZOMBIFIED_PIGLIN(105, key("zombified_piglin")),
    //PLAYER(106, Key.key("player")),
    FISHING_HOOK(107, key("fishing_bobber"))
}

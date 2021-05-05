/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity

import org.kryptonmc.krypton.api.registry.NamespacedKey

// commented out entries in here are ones that are spawned in their own special ways
// TODO: Use this for something
enum class EntityType(val type: Int, val id: NamespacedKey) {

    AREA_EFFECT_CLOUD(0, NamespacedKey(value = "area_effect_cloud")),
    ARMOR_STAND(1, NamespacedKey(value = "armor_stand")),
    ARROW(2, NamespacedKey(value = "arrow")),
    BAT(3, NamespacedKey(value = "bat")),
    BEE(4, NamespacedKey(value = "bee")),
    BLAZE(5, NamespacedKey(value = "blaze")),
    BOAT(6, NamespacedKey(value = "boat")),
    CAT(7, NamespacedKey(value = "cat")),
    CAVE_SPIDER(8, NamespacedKey(value = "cave_spider")),
    CHICKEN(9, NamespacedKey(value = "chicken")),
    COD(10, NamespacedKey(value = "cod")),
    COW(11, NamespacedKey(value = "cow")),
    CREEPER(12, NamespacedKey(value = "creeper")),
    DOLPHIN(13, NamespacedKey(value = "dolphin")),
    DONKEY(14, NamespacedKey(value = "donkey")),
    DRAGON_FIREBALL(15, NamespacedKey(value = "dragon_fireball")),
    DROWNED(16, NamespacedKey(value = "drowned")),
    ELDER_GUARDIAN(17, NamespacedKey(value = "elder_guardian")),
    END_CRYSTAL(18, NamespacedKey(value = "end_crystal")),
    ENDER_DRAGON(19, NamespacedKey(value = "ender_dragon")),
    ENDERMAN(20, NamespacedKey(value = "enderman")),
    ENDERMTIE(21, NamespacedKey(value = "endermite")),
    EVOKER(22, NamespacedKey(value = "evoker")),
    EVOKER_FANGS(23, NamespacedKey(value = "evoker_fangs")),
    //EXPERIENCE_ORB(24, NamespacedKey(value = "experience_orb")),
    EYE_OF_ENDER(24, NamespacedKey(value = "eye_of_ender")),
    FALLING_BLOCK(26, NamespacedKey(value = "falling_block")),
    FIREWORK_ROCKET(27, NamespacedKey(value = "firework_rocket")),
    FOX(28, NamespacedKey(value = "fox")),
    GHAST(29, NamespacedKey(value = "ghast")),
    GIANT(30, NamespacedKey(value = "giant")),
    GUARDIAN(31, NamespacedKey(value = "guardian")),
    HOGLIN(32, NamespacedKey(value = "hoglin")),
    HORSE(33, NamespacedKey(value = "horse")),
    HUSK(34, NamespacedKey(value = "husk")),
    ILLUSIONER(35, NamespacedKey(value = "illusioner")),
    IRON_GOLEM(36, NamespacedKey(value = "iron_golem")),
    ITEM(37, NamespacedKey(value = "item")),
    ITEM_FRAME(38, NamespacedKey(value = "item_frame")),
    FIREBALL(39, NamespacedKey(value = "fireball")),
    LEASH_KNOT(40, NamespacedKey(value = "leash_knot")),
    LIGHTNING_BOLT(41, NamespacedKey(value = "lightning_bolt")),
    LLAMA(42, NamespacedKey(value = "llama")),
    LLAMA_SPIT(43, NamespacedKey(value = "llama_spit")),
    MAGMA_CUBE(44, NamespacedKey(value = "magma_cube")),
    MINECART(45, NamespacedKey(value = "minecart")),
    CHEST_MINECART(46, NamespacedKey(value = "chest_minecart")),
    COMMAND_BLOCK_MINECART(47, NamespacedKey(value = "commandblock_minecart")),
    FURNACE_MINECART(48, NamespacedKey(value = "furnace_minecart")),
    HOPPER_MINECART(49, NamespacedKey(value = "hopper_minecart")),
    SPAWNER_MINECART(50, NamespacedKey(value = "spawner_minecart")),
    TNT_MINECART(51, NamespacedKey(value = "tnt_minecart")),
    MULE(52, NamespacedKey(value = "mule")),
    MOOSHROOM(53, NamespacedKey(value = "mooshroom")),
    OCELOT(54, NamespacedKey(value = "ocelot")),
    //PAINTING(55, NamespacedKey(value = "painting")),
    PANDA(56, NamespacedKey(value = "panda")),
    PARROT(57, NamespacedKey(value = "parrot")),
    PHANTOM(58, NamespacedKey(value = "phantom")),
    PIG(59, NamespacedKey(value = "pig")),
    PIGLIN(60, NamespacedKey(value = "piglin")),
    PIGLIN_BRUTE(61, NamespacedKey(value = "piglin_brute")),
    PILLAGER(62, NamespacedKey(value = "pillager")),
    POLAR_BEAR(63, NamespacedKey(value = "polar_bear")),
    //PRIMED_TNT(64, NamespacedKey(value = "tnt")),
    PUFFERFISH(65, NamespacedKey(value = "pufferfish")),
    RABBIT(66, NamespacedKey(value = "rabbit")),
    RAVAGER(67, NamespacedKey(value = "ravager")),
    SALMON(68, NamespacedKey(value = "salmon")),
    SHEEP(69, NamespacedKey(value = "sheep")),
    SHULKER(70, NamespacedKey(value = "shulker")),
    SHULKER_BULLET(71, NamespacedKey(value = "shulker_bullet")),
    SILVERFISH(72, NamespacedKey(value = "silverfish")),
    SKELETON(73, NamespacedKey(value = "skeleton")),
    SKELETON_HORSE(74, NamespacedKey(value = "skeleton_horse")),
    SLIME(75, NamespacedKey(value = "slime")),
    SMALL_FIREBALL(76, NamespacedKey(value = "small_fireball")),
    SNOW_GOLEM(77, NamespacedKey(value = "snow_golem")),
    SNOWBALL(78, NamespacedKey(value = "snowball")),
    SPECTRAL_ARROW(79, NamespacedKey(value = "spectral_arrow")),
    SPIDER(80, NamespacedKey(value = "spider")),
    SQUID(81, NamespacedKey(value = "squid")),
    STRAY(82, NamespacedKey(value = "stray")),
    STRIDER(83, NamespacedKey(value = "strider")),
    THROWN_EGG(84, NamespacedKey(value = "egg")),
    THROWN_ENDER_PEARL(85, NamespacedKey(value = "ender_pearl")),
    THROWN_EXPERIENCE_BOTTLE(86, NamespacedKey(value = "experience_bottle")),
    THROWN_POTION(87, NamespacedKey(value = "potion")),
    THROWN_TRIDENT(88, NamespacedKey(value = "trident")),
    TRADER_LLAMA(89, NamespacedKey(value = "trader_llama")),
    TROPICAL_FISH(90, NamespacedKey(value = "tropical_fish")),
    TURTLE(91, NamespacedKey(value = "turtle")),
    VEX(92, NamespacedKey(value = "vex")),
    VILLAGER(93, NamespacedKey(value = "villager")),
    VINDICATOR(94, NamespacedKey(value = "vindicator")),
    WANDERING_TRADER(95, NamespacedKey(value = "wandering_trader")),
    WITCH(96, NamespacedKey(value = "witch")),
    WITHER(97, NamespacedKey(value = "wither")),
    WITHER_SKELETON(98, NamespacedKey(value = "wither_skeleton")),
    WITHER_SKULL(99, NamespacedKey(value = "wither_skull")),
    WOLF(100, NamespacedKey(value = "wolf")),
    ZOGLIN(101, NamespacedKey(value = "zoglin")),
    ZOMBIE(102, NamespacedKey(value = "zombie")),
    ZOMBIE_HORSE(103, NamespacedKey(value = "zombie_horse")),
    ZOMBIE_VILLAGER(104, NamespacedKey(value = "zombie_villager")),
    ZOMBIFIED_PIGLIN(105, NamespacedKey(value = "zombified_piglin")),
    //PLAYER(106, NamespacedKey(value = "player")),
    FISHING_HOOK(107, NamespacedKey(value = "fishing_bobber"))
}

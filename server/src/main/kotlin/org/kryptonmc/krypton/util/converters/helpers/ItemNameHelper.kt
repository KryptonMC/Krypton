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
package org.kryptonmc.krypton.util.converters.helpers

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

object ItemNameHelper {

    private val ITEM_NAMES = Int2ObjectOpenHashMap<String>().apply {
        put(0, "minecraft:air")
        put(1, "minecraft:stone")
        put(2, "minecraft:grass")
        put(3, "minecraft:dirt")
        put(4, "minecraft:cobblestone")
        put(5, "minecraft:planks")
        put(6, "minecraft:sapling")
        put(7, "minecraft:bedrock")
        put(8, "minecraft:flowing_water")
        put(9, "minecraft:water")
        put(10, "minecraft:flowing_lava")
        put(11, "minecraft:lava")
        put(12, "minecraft:sand")
        put(13, "minecraft:gravel")
        put(14, "minecraft:gold_ore")
        put(15, "minecraft:iron_ore")
        put(16, "minecraft:coal_ore")
        put(17, "minecraft:log")
        put(18, "minecraft:leaves")
        put(19, "minecraft:sponge")
        put(20, "minecraft:glass")
        put(21, "minecraft:lapis_ore")
        put(22, "minecraft:lapis_block")
        put(23, "minecraft:dispenser")
        put(24, "minecraft:sandstone")
        put(25, "minecraft:noteblock")
        put(27, "minecraft:golden_rail")
        put(28, "minecraft:detector_rail")
        put(29, "minecraft:sticky_piston")
        put(30, "minecraft:web")
        put(31, "minecraft:tallgrass")
        put(32, "minecraft:deadbush")
        put(33, "minecraft:piston")
        put(35, "minecraft:wool")
        put(37, "minecraft:yellow_flower")
        put(38, "minecraft:red_flower")
        put(39, "minecraft:brown_mushroom")
        put(40, "minecraft:red_mushroom")
        put(41, "minecraft:gold_block")
        put(42, "minecraft:iron_block")
        put(43, "minecraft:double_stone_slab")
        put(44, "minecraft:stone_slab")
        put(45, "minecraft:brick_block")
        put(46, "minecraft:tnt")
        put(47, "minecraft:bookshelf")
        put(48, "minecraft:mossy_cobblestone")
        put(49, "minecraft:obsidian")
        put(50, "minecraft:torch")
        put(51, "minecraft:fire")
        put(52, "minecraft:mob_spawner")
        put(53, "minecraft:oak_stairs")
        put(54, "minecraft:chest")
        put(56, "minecraft:diamond_ore")
        put(57, "minecraft:diamond_block")
        put(58, "minecraft:crafting_table")
        put(60, "minecraft:farmland")
        put(61, "minecraft:furnace")
        put(62, "minecraft:lit_furnace")
        put(65, "minecraft:ladder")
        put(66, "minecraft:rail")
        put(67, "minecraft:stone_stairs")
        put(69, "minecraft:lever")
        put(70, "minecraft:stone_pressure_plate")
        put(72, "minecraft:wooden_pressure_plate")
        put(73, "minecraft:redstone_ore")
        put(76, "minecraft:redstone_torch")
        put(77, "minecraft:stone_button")
        put(78, "minecraft:snow_layer")
        put(79, "minecraft:ice")
        put(80, "minecraft:snow")
        put(81, "minecraft:cactus")
        put(82, "minecraft:clay")
        put(84, "minecraft:jukebox")
        put(85, "minecraft:fence")
        put(86, "minecraft:pumpkin")
        put(87, "minecraft:netherrack")
        put(88, "minecraft:soul_sand")
        put(89, "minecraft:glowstone")
        put(90, "minecraft:portal")
        put(91, "minecraft:lit_pumpkin")
        put(95, "minecraft:stained_glass")
        put(96, "minecraft:trapdoor")
        put(97, "minecraft:monster_egg")
        put(98, "minecraft:stonebrick")
        put(99, "minecraft:brown_mushroom_block")
        put(100, "minecraft:red_mushroom_block")
        put(101, "minecraft:iron_bars")
        put(102, "minecraft:glass_pane")
        put(103, "minecraft:melon_block")
        put(106, "minecraft:vine")
        put(107, "minecraft:fence_gate")
        put(108, "minecraft:brick_stairs")
        put(109, "minecraft:stone_brick_stairs")
        put(110, "minecraft:mycelium")
        put(111, "minecraft:waterlily")
        put(112, "minecraft:nether_brick")
        put(113, "minecraft:nether_brick_fence")
        put(114, "minecraft:nether_brick_stairs")
        put(116, "minecraft:enchanting_table")
        put(119, "minecraft:end_portal")
        put(120, "minecraft:end_portal_frame")
        put(121, "minecraft:end_stone")
        put(122, "minecraft:dragon_egg")
        put(123, "minecraft:redstone_lamp")
        put(125, "minecraft:double_wooden_slab")
        put(126, "minecraft:wooden_slab")
        put(127, "minecraft:cocoa")
        put(128, "minecraft:sandstone_stairs")
        put(129, "minecraft:emerald_ore")
        put(130, "minecraft:ender_chest")
        put(131, "minecraft:tripwire_hook")
        put(133, "minecraft:emerald_block")
        put(134, "minecraft:spruce_stairs")
        put(135, "minecraft:birch_stairs")
        put(136, "minecraft:jungle_stairs")
        put(137, "minecraft:command_block")
        put(138, "minecraft:beacon")
        put(139, "minecraft:cobblestone_wall")
        put(141, "minecraft:carrots")
        put(142, "minecraft:potatoes")
        put(143, "minecraft:wooden_button")
        put(145, "minecraft:anvil")
        put(146, "minecraft:trapped_chest")
        put(147, "minecraft:light_weighted_pressure_plate")
        put(148, "minecraft:heavy_weighted_pressure_plate")
        put(151, "minecraft:daylight_detector")
        put(152, "minecraft:redstone_block")
        put(153, "minecraft:quartz_ore")
        put(154, "minecraft:hopper")
        put(155, "minecraft:quartz_block")
        put(156, "minecraft:quartz_stairs")
        put(157, "minecraft:activator_rail")
        put(158, "minecraft:dropper")
        put(159, "minecraft:stained_hardened_clay")
        put(160, "minecraft:stained_glass_pane")
        put(161, "minecraft:leaves2")
        put(162, "minecraft:log2")
        put(163, "minecraft:acacia_stairs")
        put(164, "minecraft:dark_oak_stairs")
        put(170, "minecraft:hay_block")
        put(171, "minecraft:carpet")
        put(172, "minecraft:hardened_clay")
        put(173, "minecraft:coal_block")
        put(174, "minecraft:packed_ice")
        put(175, "minecraft:double_plant")
        put(256, "minecraft:iron_shovel")
        put(257, "minecraft:iron_pickaxe")
        put(258, "minecraft:iron_axe")
        put(259, "minecraft:flint_and_steel")
        put(260, "minecraft:apple")
        put(261, "minecraft:bow")
        put(262, "minecraft:arrow")
        put(263, "minecraft:coal")
        put(264, "minecraft:diamond")
        put(265, "minecraft:iron_ingot")
        put(266, "minecraft:gold_ingot")
        put(267, "minecraft:iron_sword")
        put(268, "minecraft:wooden_sword")
        put(269, "minecraft:wooden_shovel")
        put(270, "minecraft:wooden_pickaxe")
        put(271, "minecraft:wooden_axe")
        put(272, "minecraft:stone_sword")
        put(273, "minecraft:stone_shovel")
        put(274, "minecraft:stone_pickaxe")
        put(275, "minecraft:stone_axe")
        put(276, "minecraft:diamond_sword")
        put(277, "minecraft:diamond_shovel")
        put(278, "minecraft:diamond_pickaxe")
        put(279, "minecraft:diamond_axe")
        put(280, "minecraft:stick")
        put(281, "minecraft:bowl")
        put(282, "minecraft:mushroom_stew")
        put(283, "minecraft:golden_sword")
        put(284, "minecraft:golden_shovel")
        put(285, "minecraft:golden_pickaxe")
        put(286, "minecraft:golden_axe")
        put(287, "minecraft:string")
        put(288, "minecraft:feather")
        put(289, "minecraft:gunpowder")
        put(290, "minecraft:wooden_hoe")
        put(291, "minecraft:stone_hoe")
        put(292, "minecraft:iron_hoe")
        put(293, "minecraft:diamond_hoe")
        put(294, "minecraft:golden_hoe")
        put(295, "minecraft:wheat_seeds")
        put(296, "minecraft:wheat")
        put(297, "minecraft:bread")
        put(298, "minecraft:leather_helmet")
        put(299, "minecraft:leather_chestplate")
        put(300, "minecraft:leather_leggings")
        put(301, "minecraft:leather_boots")
        put(302, "minecraft:chainmail_helmet")
        put(303, "minecraft:chainmail_chestplate")
        put(304, "minecraft:chainmail_leggings")
        put(305, "minecraft:chainmail_boots")
        put(306, "minecraft:iron_helmet")
        put(307, "minecraft:iron_chestplate")
        put(308, "minecraft:iron_leggings")
        put(309, "minecraft:iron_boots")
        put(310, "minecraft:diamond_helmet")
        put(311, "minecraft:diamond_chestplate")
        put(312, "minecraft:diamond_leggings")
        put(313, "minecraft:diamond_boots")
        put(314, "minecraft:golden_helmet")
        put(315, "minecraft:golden_chestplate")
        put(316, "minecraft:golden_leggings")
        put(317, "minecraft:golden_boots")
        put(318, "minecraft:flint")
        put(319, "minecraft:porkchop")
        put(320, "minecraft:cooked_porkchop")
        put(321, "minecraft:painting")
        put(322, "minecraft:golden_apple")
        put(323, "minecraft:sign")
        put(324, "minecraft:wooden_door")
        put(325, "minecraft:bucket")
        put(326, "minecraft:water_bucket")
        put(327, "minecraft:lava_bucket")
        put(328, "minecraft:minecart")
        put(329, "minecraft:saddle")
        put(330, "minecraft:iron_door")
        put(331, "minecraft:redstone")
        put(332, "minecraft:snowball")
        put(333, "minecraft:boat")
        put(334, "minecraft:leather")
        put(335, "minecraft:milk_bucket")
        put(336, "minecraft:brick")
        put(337, "minecraft:clay_ball")
        put(338, "minecraft:reeds")
        put(339, "minecraft:paper")
        put(340, "minecraft:book")
        put(341, "minecraft:slime_ball")
        put(342, "minecraft:chest_minecart")
        put(343, "minecraft:furnace_minecart")
        put(344, "minecraft:egg")
        put(345, "minecraft:compass")
        put(346, "minecraft:fishing_rod")
        put(347, "minecraft:clock")
        put(348, "minecraft:glowstone_dust")
        put(349, "minecraft:fish")
        put(350, "minecraft:cooked_fish") // Fix typo, the game never recognized cooked_fished
        put(351, "minecraft:dye")
        put(352, "minecraft:bone")
        put(353, "minecraft:sugar")
        put(354, "minecraft:cake")
        put(355, "minecraft:bed")
        put(356, "minecraft:repeater")
        put(357, "minecraft:cookie")
        put(358, "minecraft:filled_map")
        put(359, "minecraft:shears")
        put(360, "minecraft:melon")
        put(361, "minecraft:pumpkin_seeds")
        put(362, "minecraft:melon_seeds")
        put(363, "minecraft:beef")
        put(364, "minecraft:cooked_beef")
        put(365, "minecraft:chicken")
        put(366, "minecraft:cooked_chicken")
        put(367, "minecraft:rotten_flesh")
        put(368, "minecraft:ender_pearl")
        put(369, "minecraft:blaze_rod")
        put(370, "minecraft:ghast_tear")
        put(371, "minecraft:gold_nugget")
        put(372, "minecraft:nether_wart")
        put(373, "minecraft:potion")
        put(374, "minecraft:glass_bottle")
        put(375, "minecraft:spider_eye")
        put(376, "minecraft:fermented_spider_eye")
        put(377, "minecraft:blaze_powder")
        put(378, "minecraft:magma_cream")
        put(379, "minecraft:brewing_stand")
        put(380, "minecraft:cauldron")
        put(381, "minecraft:ender_eye")
        put(382, "minecraft:speckled_melon")
        put(383, "minecraft:spawn_egg")
        put(384, "minecraft:experience_bottle")
        put(385, "minecraft:fire_charge")
        put(386, "minecraft:writable_book")
        put(387, "minecraft:written_book")
        put(388, "minecraft:emerald")
        put(389, "minecraft:item_frame")
        put(390, "minecraft:flower_pot")
        put(391, "minecraft:carrot")
        put(392, "minecraft:potato")
        put(393, "minecraft:baked_potato")
        put(394, "minecraft:poisonous_potato")
        put(395, "minecraft:map")
        put(396, "minecraft:golden_carrot")
        put(397, "minecraft:skull")
        put(398, "minecraft:carrot_on_a_stick")
        put(399, "minecraft:nether_star")
        put(400, "minecraft:pumpkin_pie")
        put(401, "minecraft:fireworks")
        put(402, "minecraft:firework_charge")
        put(403, "minecraft:enchanted_book")
        put(404, "minecraft:comparator")
        put(405, "minecraft:netherbrick")
        put(406, "minecraft:quartz")
        put(407, "minecraft:tnt_minecart")
        put(408, "minecraft:hopper_minecart")
        put(417, "minecraft:iron_horse_armor")
        put(418, "minecraft:golden_horse_armor")
        put(419, "minecraft:diamond_horse_armor")
        put(420, "minecraft:lead")
        put(421, "minecraft:name_tag")
        put(422, "minecraft:command_block_minecart")
        put(2256, "minecraft:record_13")
        put(2257, "minecraft:record_cat")
        put(2258, "minecraft:record_blocks")
        put(2259, "minecraft:record_chirp")
        put(2260, "minecraft:record_far")
        put(2261, "minecraft:record_mall")
        put(2262, "minecraft:record_mellohi")
        put(2263, "minecraft:record_stal")
        put(2264, "minecraft:record_strad")
        put(2265, "minecraft:record_ward")
        put(2266, "minecraft:record_11")
        put(2267, "minecraft:record_wait")

        // Add block ids into conversion as well
        // Very old versions of the game handled them, but it seems 1.8.8 did not parse them at all, so no conversion
        // was written.
        // block ids are only skipped (set to AIR) if there is no 1-1 replacement item.
        put(26, "minecraft:bed") // bed block
        put(34, get(0)) // skip (piston head block)
        put(55, "minecraft:redstone") // redstone wire block
        put(59, get(0)) // skip (wheat crop block)
        put(63, "minecraft:sign") // standing sign
        put(64, "minecraft:wooden_door") // wooden door block
        put(68, "minecraft:sign") // wall sign
        put(71, "minecraft:iron_door") // iron door block
        put(74, "minecraft:redstone_ore") // lit redstone ore block
        put(75, "minecraft:redstone_torch") // unlit redstone torch
        put(83, "minecraft:reeds") // sugar cane block
        put(92, "minecraft:cake") // cake block
        put(93, "minecraft:repeater") // unpowered repeater block
        put(94, "minecraft:repeater") // powered repeater block
        put(104, get(0)) // skip (pumpkin stem)
        put(105, get(0)) // skip (melon stem)
        put(115, "minecraft:nether_wart") // nether wart block
        put(117, "minecraft:brewing_stand") // brewing stand block
        put(118, "minecraft:cauldron") // cauldron block
        put(124, "minecraft:redstone_lamp") // lit redstone lamp block
        put(132, get(0)) // skip (tripwire wire block)
        put(140, "minecraft:flower_pot") // flower pot block
        put(144, "minecraft:skull") // skull block
        put(149, "minecraft:comparator") // unpowered comparator block
        put(150, "minecraft:comparator") // powered comparator block
        // there are technically more, but at some point even older versions pre id -> name conversion didn't even load them.
        // (all I know is 1.7.10 does not load them)
        // and so given even the vanilla game wouldn't load them, there's no conversion path for them - they were never valid.
    }
    private val POTION_NAMES = arrayOf(
        "minecraft:water",
        "minecraft:regeneration",
        "minecraft:swiftness",
        "minecraft:fire_resistance",
        "minecraft:poison",
        "minecraft:healing",
        "minecraft:night_vision",
        null,
        "minecraft:weakness",
        "minecraft:strength",
        "minecraft:slowness",
        "minecraft:leaping",
        "minecraft:harming",
        "minecraft:water_breathing",
        "minecraft:invisibility",
        null,
        "minecraft:awkward",
        "minecraft:regeneration",
        "minecraft:swiftness",
        "minecraft:fire_resistance",
        "minecraft:poison",
        "minecraft:healing",
        "minecraft:night_vision",
        null,
        "minecraft:weakness",
        "minecraft:strength",
        "minecraft:slowness",
        "minecraft:leaping",
        "minecraft:harming",
        "minecraft:water_breathing",
        "minecraft:invisibility",
        null,
        "minecraft:thick",
        "minecraft:strong_regeneration",
        "minecraft:strong_swiftness",
        "minecraft:fire_resistance",
        "minecraft:strong_poison",
        "minecraft:strong_healing",
        "minecraft:night_vision",
        null,
        "minecraft:weakness",
        "minecraft:strong_strength",
        "minecraft:slowness",
        "minecraft:strong_leaping",
        "minecraft:strong_harming",
        "minecraft:water_breathing",
        "minecraft:invisibility",
        null,
        null,
        "minecraft:strong_regeneration",
        "minecraft:strong_swiftness",
        "minecraft:fire_resistance",
        "minecraft:strong_poison",
        "minecraft:strong_healing",
        "minecraft:night_vision",
        null,
        "minecraft:weakness",
        "minecraft:strong_strength",
        "minecraft:slowness",
        "minecraft:strong_leaping",
        "minecraft:strong_harming",
        "minecraft:water_breathing",
        "minecraft:invisibility",
        null,
        "minecraft:mundane",
        "minecraft:long_regeneration",
        "minecraft:long_swiftness",
        "minecraft:long_fire_resistance",
        "minecraft:long_poison",
        "minecraft:healing",
        "minecraft:long_night_vision",
        null,
        "minecraft:long_weakness",
        "minecraft:long_strength",
        "minecraft:long_slowness",
        "minecraft:long_leaping",
        "minecraft:harming",
        "minecraft:long_water_breathing",
        "minecraft:long_invisibility",
        null,
        "minecraft:awkward",
        "minecraft:long_regeneration",
        "minecraft:long_swiftness",
        "minecraft:long_fire_resistance",
        "minecraft:long_poison",
        "minecraft:healing",
        "minecraft:long_night_vision",
        null,
        "minecraft:long_weakness",
        "minecraft:long_strength",
        "minecraft:long_slowness",
        "minecraft:long_leaping",
        "minecraft:harming",
        "minecraft:long_water_breathing",
        "minecraft:long_invisibility",
        null,
        "minecraft:thick",
        "minecraft:regeneration",
        "minecraft:swiftness",
        "minecraft:long_fire_resistance",
        "minecraft:poison",
        "minecraft:strong_healing",
        "minecraft:long_night_vision",
        null,
        "minecraft:long_weakness",
        "minecraft:strength",
        "minecraft:long_slowness",
        "minecraft:leaping",
        "minecraft:strong_harming",
        "minecraft:long_water_breathing",
        "minecraft:long_invisibility",
        null,
        null,
        "minecraft:regeneration",
        "minecraft:swiftness",
        "minecraft:long_fire_resistance",
        "minecraft:poison",
        "minecraft:strong_healing",
        "minecraft:long_night_vision",
        null,
        "minecraft:long_weakness",
        "minecraft:strength",
        "minecraft:long_slowness",
        "minecraft:leaping",
        "minecraft:strong_harming",
        "minecraft:long_water_breathing",
        "minecraft:long_invisibility",
        null,
    )

    fun getNameFromId(id: Int): String? = ITEM_NAMES[id]

    fun getPotionNameFromId(id: Short) = POTION_NAMES[id.toInt() and 127]
}

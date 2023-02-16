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
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.key.Key
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.krypton.registry.KryptonRegistries

/*
 * The formatting for this class is very weird, but it's this way to reduce the amount of lines,
 * so analysis of it will finish this side of christmas.
 */
@Suppress("CascadingCallWrapping")
@Catalogue(KryptonBlock::class)
object KryptonBlocks {

    @JvmField
    val AIR: KryptonBlock = get("air")
    @JvmField
    val STONE: KryptonBlock = get("stone")
    @JvmField
    val GRANITE: KryptonBlock = get("granite")
    @JvmField
    val POLISHED_GRANITE: KryptonBlock = get("polished_granite")
    @JvmField
    val DIORITE: KryptonBlock = get("diorite")
    @JvmField
    val POLISHED_DIORITE: KryptonBlock = get("polished_diorite")
    @JvmField
    val ANDESITE: KryptonBlock = get("andesite")
    @JvmField
    val POLISHED_ANDESITE: KryptonBlock = get("polished_andesite")
    @JvmField
    val GRASS_BLOCK: KryptonBlock = get("grass_block")
    @JvmField
    val DIRT: KryptonBlock = get("dirt")
    @JvmField
    val COARSE_DIRT: KryptonBlock = get("coarse_dirt")
    @JvmField
    val PODZOL: KryptonBlock = get("podzol")
    @JvmField
    val COBBLESTONE: KryptonBlock = get("cobblestone")
    @JvmField
    val OAK_PLANKS: KryptonBlock = get("oak_planks")
    @JvmField
    val SPRUCE_PLANKS: KryptonBlock = get("spruce_planks")
    @JvmField
    val BIRCH_PLANKS: KryptonBlock = get("birch_planks")
    @JvmField
    val JUNGLE_PLANKS: KryptonBlock = get("jungle_planks")
    @JvmField
    val ACACIA_PLANKS: KryptonBlock = get("acacia_planks")
    @JvmField
    val DARK_OAK_PLANKS: KryptonBlock = get("dark_oak_planks")
    @JvmField
    val MANGROVE_PLANKS: KryptonBlock = get("mangrove_planks")
    @JvmField
    val OAK_SAPLING: KryptonBlock = get("oak_sapling")
    @JvmField
    val SPRUCE_SAPLING: KryptonBlock = get("spruce_sapling")
    @JvmField
    val BIRCH_SAPLING: KryptonBlock = get("birch_sapling")
    @JvmField
    val JUNGLE_SAPLING: KryptonBlock = get("jungle_sapling")
    @JvmField
    val ACACIA_SAPLING: KryptonBlock = get("acacia_sapling")
    @JvmField
    val DARK_OAK_SAPLING: KryptonBlock = get("dark_oak_sapling")
    @JvmField
    val MANGROVE_PROPAGULE: KryptonBlock = get("mangrove_propagule")
    @JvmField
    val BEDROCK: KryptonBlock = get("bedrock")
    @JvmField
    val WATER: KryptonBlock = get("water")
    @JvmField
    val LAVA: KryptonBlock = get("lava")
    @JvmField
    val SAND: KryptonBlock = get("sand")
    @JvmField
    val RED_SAND: KryptonBlock = get("red_sand")
    @JvmField
    val GRAVEL: KryptonBlock = get("gravel")
    @JvmField
    val GOLD_ORE: KryptonBlock = get("gold_ore")
    @JvmField
    val DEEPSLATE_GOLD_ORE: KryptonBlock = get("deepslate_gold_ore")
    @JvmField
    val IRON_ORE: KryptonBlock = get("iron_ore")
    @JvmField
    val DEEPSLATE_IRON_ORE: KryptonBlock = get("deepslate_iron_ore")
    @JvmField
    val COAL_ORE: KryptonBlock = get("coal_ore")
    @JvmField
    val DEEPSLATE_COAL_ORE: KryptonBlock = get("deepslate_coal_ore")
    @JvmField
    val NETHER_GOLD_ORE: KryptonBlock = get("nether_gold_ore")
    @JvmField
    val OAK_LOG: KryptonBlock = get("oak_log")
    @JvmField
    val SPRUCE_LOG: KryptonBlock = get("spruce_log")
    @JvmField
    val BIRCH_LOG: KryptonBlock = get("birch_log")
    @JvmField
    val JUNGLE_LOG: KryptonBlock = get("jungle_log")
    @JvmField
    val ACACIA_LOG: KryptonBlock = get("acacia_log")
    @JvmField
    val DARK_OAK_LOG: KryptonBlock = get("dark_oak_log")
    @JvmField
    val MANGROVE_LOG: KryptonBlock = get("mangrove_log")
    @JvmField
    val MANGROVE_ROOTS: KryptonBlock = get("mangrove_roots")
    @JvmField
    val MUDDY_MANGROVE_ROOTS: KryptonBlock = get("muddy_mangrove_roots")
    @JvmField
    val STRIPPED_SPRUCE_LOG: KryptonBlock = get("stripped_spruce_log")
    @JvmField
    val STRIPPED_BIRCH_LOG: KryptonBlock = get("stripped_birch_log")
    @JvmField
    val STRIPPED_JUNGLE_LOG: KryptonBlock = get("stripped_jungle_log")
    @JvmField
    val STRIPPED_ACACIA_LOG: KryptonBlock = get("stripped_acacia_log")
    @JvmField
    val STRIPPED_DARK_OAK_LOG: KryptonBlock = get("stripped_dark_oak_log")
    @JvmField
    val STRIPPED_OAK_LOG: KryptonBlock = get("stripped_oak_log")
    @JvmField
    val STRIPPED_MANGROVE_LOG: KryptonBlock = get("stripped_mangrove_log")
    @JvmField
    val OAK_WOOD: KryptonBlock = get("oak_wood")
    @JvmField
    val SPRUCE_WOOD: KryptonBlock = get("spruce_wood")
    @JvmField
    val BIRCH_WOOD: KryptonBlock = get("birch_wood")
    @JvmField
    val JUNGLE_WOOD: KryptonBlock = get("jungle_wood")
    @JvmField
    val ACACIA_WOOD: KryptonBlock = get("acacia_wood")
    @JvmField
    val DARK_OAK_WOOD: KryptonBlock = get("dark_oak_wood")
    @JvmField
    val MANGROVE_WOOD: KryptonBlock = get("mangrove_wood")
    @JvmField
    val STRIPPED_OAK_WOOD: KryptonBlock = get("stripped_oak_wood")
    @JvmField
    val STRIPPED_SPRUCE_WOOD: KryptonBlock = get("stripped_spruce_wood")
    @JvmField
    val STRIPPED_BIRCH_WOOD: KryptonBlock = get("stripped_birch_wood")
    @JvmField
    val STRIPPED_JUNGLE_WOOD: KryptonBlock = get("stripped_jungle_wood")
    @JvmField
    val STRIPPED_ACACIA_WOOD: KryptonBlock = get("stripped_acacia_wood")
    @JvmField
    val STRIPPED_DARK_OAK_WOOD: KryptonBlock = get("stripped_dark_oak_wood")
    @JvmField
    val STRIPPED_MANGROVE_WOOD: KryptonBlock = get("stripped_mangrove_wood")
    @JvmField
    val OAK_LEAVES: KryptonBlock = get("oak_leaves")
    @JvmField
    val SPRUCE_LEAVES: KryptonBlock = get("spruce_leaves")
    @JvmField
    val BIRCH_LEAVES: KryptonBlock = get("birch_leaves")
    @JvmField
    val JUNGLE_LEAVES: KryptonBlock = get("jungle_leaves")
    @JvmField
    val ACACIA_LEAVES: KryptonBlock = get("acacia_leaves")
    @JvmField
    val DARK_OAK_LEAVES: KryptonBlock = get("dark_oak_leaves")
    @JvmField
    val MANGROVE_LEAVES: KryptonBlock = get("mangrove_leaves")
    @JvmField
    val AZALEA_LEAVES: KryptonBlock = get("azalea_leaves")
    @JvmField
    val FLOWERING_AZALEA_LEAVES: KryptonBlock = get("flowering_azalea_leaves")
    @JvmField
    val SPONGE: KryptonBlock = get("sponge")
    @JvmField
    val WET_SPONGE: KryptonBlock = get("wet_sponge")
    @JvmField
    val GLASS: KryptonBlock = get("glass")
    @JvmField
    val LAPIS_ORE: KryptonBlock = get("lapis_ore")
    @JvmField
    val DEEPSLATE_LAPIS_ORE: KryptonBlock = get("deepslate_lapis_ore")
    @JvmField
    val LAPIS_BLOCK: KryptonBlock = get("lapis_block")
    @JvmField
    val DISPENSER: KryptonBlock = get("dispenser")
    @JvmField
    val SANDSTONE: KryptonBlock = get("sandstone")
    @JvmField
    val CHISELED_SANDSTONE: KryptonBlock = get("chiseled_sandstone")
    @JvmField
    val CUT_SANDSTONE: KryptonBlock = get("cut_sandstone")
    @JvmField
    val NOTE_BLOCK: KryptonBlock = get("note_block")
    @JvmField
    val WHITE_BED: KryptonBlock = get("white_bed")
    @JvmField
    val ORANGE_BED: KryptonBlock = get("orange_bed")
    @JvmField
    val MAGENTA_BED: KryptonBlock = get("magenta_bed")
    @JvmField
    val LIGHT_BLUE_BED: KryptonBlock = get("light_blue_bed")
    @JvmField
    val YELLOW_BED: KryptonBlock = get("yellow_bed")
    @JvmField
    val LIME_BED: KryptonBlock = get("lime_bed")
    @JvmField
    val PINK_BED: KryptonBlock = get("pink_bed")
    @JvmField
    val GRAY_BED: KryptonBlock = get("gray_bed")
    @JvmField
    val LIGHT_GRAY_BED: KryptonBlock = get("light_gray_bed")
    @JvmField
    val CYAN_BED: KryptonBlock = get("cyan_bed")
    @JvmField
    val PURPLE_BED: KryptonBlock = get("purple_bed")
    @JvmField
    val BLUE_BED: KryptonBlock = get("blue_bed")
    @JvmField
    val BROWN_BED: KryptonBlock = get("brown_bed")
    @JvmField
    val GREEN_BED: KryptonBlock = get("green_bed")
    @JvmField
    val RED_BED: KryptonBlock = get("red_bed")
    @JvmField
    val BLACK_BED: KryptonBlock = get("black_bed")
    @JvmField
    val POWERED_RAIL: KryptonBlock = get("powered_rail")
    @JvmField
    val DETECTOR_RAIL: KryptonBlock = get("detector_rail")
    @JvmField
    val STICKY_PISTON: KryptonBlock = get("sticky_piston")
    @JvmField
    val COBWEB: KryptonBlock = get("cobweb")
    @JvmField
    val GRASS: KryptonBlock = get("grass")
    @JvmField
    val FERN: KryptonBlock = get("fern")
    @JvmField
    val DEAD_BUSH: KryptonBlock = get("dead_bush")
    @JvmField
    val SEAGRASS: KryptonBlock = get("seagrass")
    @JvmField
    val TALL_SEAGRASS: KryptonBlock = get("tall_seagrass")
    @JvmField
    val PISTON: KryptonBlock = get("piston")
    @JvmField
    val PISTON_HEAD: KryptonBlock = get("piston_head")
    @JvmField
    val WHITE_WOOL: KryptonBlock = get("white_wool")
    @JvmField
    val ORANGE_WOOL: KryptonBlock = get("orange_wool")
    @JvmField
    val MAGENTA_WOOL: KryptonBlock = get("magenta_wool")
    @JvmField
    val LIGHT_BLUE_WOOL: KryptonBlock = get("light_blue_wool")
    @JvmField
    val YELLOW_WOOL: KryptonBlock = get("yellow_wool")
    @JvmField
    val LIME_WOOL: KryptonBlock = get("lime_wool")
    @JvmField
    val PINK_WOOL: KryptonBlock = get("pink_wool")
    @JvmField
    val GRAY_WOOL: KryptonBlock = get("gray_wool")
    @JvmField
    val LIGHT_GRAY_WOOL: KryptonBlock = get("light_gray_wool")
    @JvmField
    val CYAN_WOOL: KryptonBlock = get("cyan_wool")
    @JvmField
    val PURPLE_WOOL: KryptonBlock = get("purple_wool")
    @JvmField
    val BLUE_WOOL: KryptonBlock = get("blue_wool")
    @JvmField
    val BROWN_WOOL: KryptonBlock = get("brown_wool")
    @JvmField
    val GREEN_WOOL: KryptonBlock = get("green_wool")
    @JvmField
    val RED_WOOL: KryptonBlock = get("red_wool")
    @JvmField
    val BLACK_WOOL: KryptonBlock = get("black_wool")
    @JvmField
    val MOVING_PISTON: KryptonBlock = get("moving_piston")
    @JvmField
    val DANDELION: KryptonBlock = get("dandelion")
    @JvmField
    val POPPY: KryptonBlock = get("poppy")
    @JvmField
    val BLUE_ORCHID: KryptonBlock = get("blue_orchid")
    @JvmField
    val ALLIUM: KryptonBlock = get("allium")
    @JvmField
    val AZURE_BLUET: KryptonBlock = get("azure_bluet")
    @JvmField
    val RED_TULIP: KryptonBlock = get("red_tulip")
    @JvmField
    val ORANGE_TULIP: KryptonBlock = get("orange_tulip")
    @JvmField
    val WHITE_TULIP: KryptonBlock = get("white_tulip")
    @JvmField
    val PINK_TULIP: KryptonBlock = get("pink_tulip")
    @JvmField
    val OXEYE_DAISY: KryptonBlock = get("oxeye_daisy")
    @JvmField
    val CORNFLOWER: KryptonBlock = get("cornflower")
    @JvmField
    val WITHER_ROSE: KryptonBlock = get("wither_rose")
    @JvmField
    val LILY_OF_THE_VALLEY: KryptonBlock = get("lily_of_the_valley")
    @JvmField
    val BROWN_MUSHROOM: KryptonBlock = get("brown_mushroom")
    @JvmField
    val RED_MUSHROOM: KryptonBlock = get("red_mushroom")
    @JvmField
    val GOLD_BLOCK: KryptonBlock = get("gold_block")
    @JvmField
    val IRON_BLOCK: KryptonBlock = get("iron_block")
    @JvmField
    val BRICKS: KryptonBlock = get("bricks")
    @JvmField
    val TNT: KryptonBlock = get("tnt")
    @JvmField
    val BOOKSHELF: KryptonBlock = get("bookshelf")
    @JvmField
    val MOSSY_COBBLESTONE: KryptonBlock = get("mossy_cobblestone")
    @JvmField
    val OBSIDIAN: KryptonBlock = get("obsidian")
    @JvmField
    val TORCH: KryptonBlock = get("torch")
    @JvmField
    val WALL_TORCH: KryptonBlock = get("wall_torch")
    @JvmField
    val FIRE: KryptonBlock = get("fire")
    @JvmField
    val SOUL_FIRE: KryptonBlock = get("soul_fire")
    @JvmField
    val SPAWNER: KryptonBlock = get("spawner")
    @JvmField
    val OAK_STAIRS: KryptonBlock = get("oak_stairs")
    @JvmField
    val CHEST: KryptonBlock = get("chest")
    @JvmField
    val REDSTONE_WIRE: KryptonBlock = get("redstone_wire")
    @JvmField
    val DIAMOND_ORE: KryptonBlock = get("diamond_ore")
    @JvmField
    val DEEPSLATE_DIAMOND_ORE: KryptonBlock = get("deepslate_diamond_ore")
    @JvmField
    val DIAMOND_BLOCK: KryptonBlock = get("diamond_block")
    @JvmField
    val CRAFTING_TABLE: KryptonBlock = get("crafting_table")
    @JvmField
    val WHEAT: KryptonBlock = get("wheat")
    @JvmField
    val FARMLAND: KryptonBlock = get("farmland")
    @JvmField
    val FURNACE: KryptonBlock = get("furnace")
    @JvmField
    val OAK_SIGN: KryptonBlock = get("oak_sign")
    @JvmField
    val SPRUCE_SIGN: KryptonBlock = get("spruce_sign")
    @JvmField
    val BIRCH_SIGN: KryptonBlock = get("birch_sign")
    @JvmField
    val ACACIA_SIGN: KryptonBlock = get("acacia_sign")
    @JvmField
    val JUNGLE_SIGN: KryptonBlock = get("jungle_sign")
    @JvmField
    val DARK_OAK_SIGN: KryptonBlock = get("dark_oak_sign")
    @JvmField
    val MANGROVE_SIGN: KryptonBlock = get("mangrove_sign")
    @JvmField
    val OAK_DOOR: KryptonBlock = get("oak_door")
    @JvmField
    val LADDER: KryptonBlock = get("ladder")
    @JvmField
    val RAIL: KryptonBlock = get("rail")
    @JvmField
    val COBBLESTONE_STAIRS: KryptonBlock = get("cobblestone_stairs")
    @JvmField
    val OAK_WALL_SIGN: KryptonBlock = get("oak_wall_sign")
    @JvmField
    val SPRUCE_WALL_SIGN: KryptonBlock = get("spruce_wall_sign")
    @JvmField
    val BIRCH_WALL_SIGN: KryptonBlock = get("birch_wall_sign")
    @JvmField
    val ACACIA_WALL_SIGN: KryptonBlock = get("acacia_wall_sign")
    @JvmField
    val JUNGLE_WALL_SIGN: KryptonBlock = get("jungle_wall_sign")
    @JvmField
    val DARK_OAK_WALL_SIGN: KryptonBlock = get("dark_oak_wall_sign")
    @JvmField
    val MANGROVE_WALL_SIGN: KryptonBlock = get("mangrove_wall_sign")
    @JvmField
    val LEVER: KryptonBlock = get("lever")
    @JvmField
    val STONE_PRESSURE_PLATE: KryptonBlock = get("stone_pressure_plate")
    @JvmField
    val IRON_DOOR: KryptonBlock = get("iron_door")
    @JvmField
    val OAK_PRESSURE_PLATE: KryptonBlock = get("oak_pressure_plate")
    @JvmField
    val SPRUCE_PRESSURE_PLATE: KryptonBlock = get("spruce_pressure_plate")
    @JvmField
    val BIRCH_PRESSURE_PLATE: KryptonBlock = get("birch_pressure_plate")
    @JvmField
    val JUNGLE_PRESSURE_PLATE: KryptonBlock = get("jungle_pressure_plate")
    @JvmField
    val ACACIA_PRESSURE_PLATE: KryptonBlock = get("acacia_pressure_plate")
    @JvmField
    val DARK_OAK_PRESSURE_PLATE: KryptonBlock = get("dark_oak_pressure_plate")
    @JvmField
    val MANGROVE_PRESSURE_PLATE: KryptonBlock = get("mangrove_pressure_plate")
    @JvmField
    val REDSTONE_ORE: KryptonBlock = get("redstone_ore")
    @JvmField
    val DEEPSLATE_REDSTONE_ORE: KryptonBlock = get("deepslate_redstone_ore")
    @JvmField
    val REDSTONE_TORCH: KryptonBlock = get("redstone_torch")
    @JvmField
    val REDSTONE_WALL_TORCH: KryptonBlock = get("redstone_wall_torch")
    @JvmField
    val STONE_BUTTON: KryptonBlock = get("stone_button")
    @JvmField
    val SNOW: KryptonBlock = get("snow")
    @JvmField
    val ICE: KryptonBlock = get("ice")
    @JvmField
    val SNOW_BLOCK: KryptonBlock = get("snow_block")
    @JvmField
    val CACTUS: KryptonBlock = get("cactus")
    @JvmField
    val CLAY: KryptonBlock = get("clay")
    @JvmField
    val SUGAR_CANE: KryptonBlock = get("sugar_cane")
    @JvmField
    val JUKEBOX: KryptonBlock = get("jukebox")
    @JvmField
    val OAK_FENCE: KryptonBlock = get("oak_fence")
    @JvmField
    val PUMPKIN: KryptonBlock = get("pumpkin")
    @JvmField
    val NETHERRACK: KryptonBlock = get("netherrack")
    @JvmField
    val SOUL_SAND: KryptonBlock = get("soul_sand")
    @JvmField
    val SOUL_SOIL: KryptonBlock = get("soul_soil")
    @JvmField
    val BASALT: KryptonBlock = get("basalt")
    @JvmField
    val POLISHED_BASALT: KryptonBlock = get("polished_basalt")
    @JvmField
    val SOUL_TORCH: KryptonBlock = get("soul_torch")
    @JvmField
    val SOUL_WALL_TORCH: KryptonBlock = get("soul_wall_torch")
    @JvmField
    val GLOWSTONE: KryptonBlock = get("glowstone")
    @JvmField
    val NETHER_PORTAL: KryptonBlock = get("nether_portal")
    @JvmField
    val CARVED_PUMPKIN: KryptonBlock = get("carved_pumpkin")
    @JvmField
    val JACK_O_LANTERN: KryptonBlock = get("jack_o_lantern")
    @JvmField
    val CAKE: KryptonBlock = get("cake")
    @JvmField
    val REPEATER: KryptonBlock = get("repeater")
    @JvmField
    val WHITE_STAINED_GLASS: KryptonBlock = get("white_stained_glass")
    @JvmField
    val ORANGE_STAINED_GLASS: KryptonBlock = get("orange_stained_glass")
    @JvmField
    val MAGENTA_STAINED_GLASS: KryptonBlock = get("magenta_stained_glass")
    @JvmField
    val LIGHT_BLUE_STAINED_GLASS: KryptonBlock = get("light_blue_stained_glass")
    @JvmField
    val YELLOW_STAINED_GLASS: KryptonBlock = get("yellow_stained_glass")
    @JvmField
    val LIME_STAINED_GLASS: KryptonBlock = get("lime_stained_glass")
    @JvmField
    val PINK_STAINED_GLASS: KryptonBlock = get("pink_stained_glass")
    @JvmField
    val GRAY_STAINED_GLASS: KryptonBlock = get("gray_stained_glass")
    @JvmField
    val LIGHT_GRAY_STAINED_GLASS: KryptonBlock = get("light_gray_stained_glass")
    @JvmField
    val CYAN_STAINED_GLASS: KryptonBlock = get("cyan_stained_glass")
    @JvmField
    val PURPLE_STAINED_GLASS: KryptonBlock = get("purple_stained_glass")
    @JvmField
    val BLUE_STAINED_GLASS: KryptonBlock = get("blue_stained_glass")
    @JvmField
    val BROWN_STAINED_GLASS: KryptonBlock = get("brown_stained_glass")
    @JvmField
    val GREEN_STAINED_GLASS: KryptonBlock = get("green_stained_glass")
    @JvmField
    val RED_STAINED_GLASS: KryptonBlock = get("red_stained_glass")
    @JvmField
    val BLACK_STAINED_GLASS: KryptonBlock = get("black_stained_glass")
    @JvmField
    val OAK_TRAPDOOR: KryptonBlock = get("oak_trapdoor")
    @JvmField
    val SPRUCE_TRAPDOOR: KryptonBlock = get("spruce_trapdoor")
    @JvmField
    val BIRCH_TRAPDOOR: KryptonBlock = get("birch_trapdoor")
    @JvmField
    val JUNGLE_TRAPDOOR: KryptonBlock = get("jungle_trapdoor")
    @JvmField
    val ACACIA_TRAPDOOR: KryptonBlock = get("acacia_trapdoor")
    @JvmField
    val DARK_OAK_TRAPDOOR: KryptonBlock = get("dark_oak_trapdoor")
    @JvmField
    val MANGROVE_TRAPDOOR: KryptonBlock = get("mangrove_trapdoor")
    @JvmField
    val STONE_BRICKS: KryptonBlock = get("stone_bricks")
    @JvmField
    val MOSSY_STONE_BRICKS: KryptonBlock = get("mossy_stone_bricks")
    @JvmField
    val CRACKED_STONE_BRICKS: KryptonBlock = get("cracked_stone_bricks")
    @JvmField
    val CHISELED_STONE_BRICKS: KryptonBlock = get("chiseled_stone_bricks")
    @JvmField
    val PACKED_MUD: KryptonBlock = get("packed_mud")
    @JvmField
    val MUD_BRICKS: KryptonBlock = get("mud_bricks")
    @JvmField
    val INFESTED_STONE: KryptonBlock = get("infested_stone")
    @JvmField
    val INFESTED_COBBLESTONE: KryptonBlock = get("infested_cobblestone")
    @JvmField
    val INFESTED_STONE_BRICKS: KryptonBlock = get("infested_stone_bricks")
    @JvmField
    val INFESTED_MOSSY_STONE_BRICKS: KryptonBlock = get("infested_mossy_stone_bricks")
    @JvmField
    val INFESTED_CRACKED_STONE_BRICKS: KryptonBlock = get("infested_cracked_stone_bricks")
    @JvmField
    val INFESTED_CHISELED_STONE_BRICKS: KryptonBlock = get("infested_chiseled_stone_bricks")
    @JvmField
    val BROWN_MUSHROOM_BLOCK: KryptonBlock = get("brown_mushroom_block")
    @JvmField
    val RED_MUSHROOM_BLOCK: KryptonBlock = get("red_mushroom_block")
    @JvmField
    val MUSHROOM_STEM: KryptonBlock = get("mushroom_stem")
    @JvmField
    val IRON_BARS: KryptonBlock = get("iron_bars")
    @JvmField
    val CHAIN: KryptonBlock = get("chain")
    @JvmField
    val GLASS_PANE: KryptonBlock = get("glass_pane")
    @JvmField
    val MELON: KryptonBlock = get("melon")
    @JvmField
    val ATTACHED_PUMPKIN_STEM: KryptonBlock = get("attached_pumpkin_stem")
    @JvmField
    val ATTACHED_MELON_STEM: KryptonBlock = get("attached_melon_stem")
    @JvmField
    val PUMPKIN_STEM: KryptonBlock = get("pumpkin_stem")
    @JvmField
    val MELON_STEM: KryptonBlock = get("melon_stem")
    @JvmField
    val VINE: KryptonBlock = get("vine")
    @JvmField
    val GLOW_LICHEN: KryptonBlock = get("glow_lichen")
    @JvmField
    val OAK_FENCE_GATE: KryptonBlock = get("oak_fence_gate")
    @JvmField
    val BRICK_STAIRS: KryptonBlock = get("brick_stairs")
    @JvmField
    val STONE_BRICK_STAIRS: KryptonBlock = get("stone_brick_stairs")
    @JvmField
    val MUD_BRICK_STAIRS: KryptonBlock = get("mud_brick_stairs")
    @JvmField
    val MYCELIUM: KryptonBlock = get("mycelium")
    @JvmField
    val LILY_PAD: KryptonBlock = get("lily_pad")
    @JvmField
    val NETHER_BRICKS: KryptonBlock = get("nether_bricks")
    @JvmField
    val NETHER_BRICK_FENCE: KryptonBlock = get("nether_brick_fence")
    @JvmField
    val NETHER_BRICK_STAIRS: KryptonBlock = get("nether_brick_stairs")
    @JvmField
    val NETHER_WART: KryptonBlock = get("nether_wart")
    @JvmField
    val ENCHANTING_TABLE: KryptonBlock = get("enchanting_table")
    @JvmField
    val BREWING_STAND: KryptonBlock = get("brewing_stand")
    @JvmField
    val CAULDRON: KryptonBlock = get("cauldron")
    @JvmField
    val WATER_CAULDRON: KryptonBlock = get("water_cauldron")
    @JvmField
    val LAVA_CAULDRON: KryptonBlock = get("lava_cauldron")
    @JvmField
    val POWDER_SNOW_CAULDRON: KryptonBlock = get("powder_snow_cauldron")
    @JvmField
    val END_PORTAL: KryptonBlock = get("end_portal")
    @JvmField
    val END_PORTAL_FRAME: KryptonBlock = get("end_portal_frame")
    @JvmField
    val END_STONE: KryptonBlock = get("end_stone")
    @JvmField
    val DRAGON_EGG: KryptonBlock = get("dragon_egg")
    @JvmField
    val REDSTONE_LAMP: KryptonBlock = get("redstone_lamp")
    @JvmField
    val COCOA: KryptonBlock = get("cocoa")
    @JvmField
    val SANDSTONE_STAIRS: KryptonBlock = get("sandstone_stairs")
    @JvmField
    val EMERALD_ORE: KryptonBlock = get("emerald_ore")
    @JvmField
    val DEEPSLATE_EMERALD_ORE: KryptonBlock = get("deepslate_emerald_ore")
    @JvmField
    val ENDER_CHEST: KryptonBlock = get("ender_chest")
    @JvmField
    val TRIPWIRE_HOOK: KryptonBlock = get("tripwire_hook")
    @JvmField
    val TRIPWIRE: KryptonBlock = get("tripwire")
    @JvmField
    val EMERALD_BLOCK: KryptonBlock = get("emerald_block")
    @JvmField
    val SPRUCE_STAIRS: KryptonBlock = get("spruce_stairs")
    @JvmField
    val BIRCH_STAIRS: KryptonBlock = get("birch_stairs")
    @JvmField
    val JUNGLE_STAIRS: KryptonBlock = get("jungle_stairs")
    @JvmField
    val COMMAND_BLOCK: KryptonBlock = get("command_block")
    @JvmField
    val BEACON: KryptonBlock = get("beacon")
    @JvmField
    val COBBLESTONE_WALL: KryptonBlock = get("cobblestone_wall")
    @JvmField
    val MOSSY_COBBLESTONE_WALL: KryptonBlock = get("mossy_cobblestone_wall")
    @JvmField
    val FLOWER_POT: KryptonBlock = get("flower_pot")
    @JvmField
    val POTTED_OAK_SAPLING: KryptonBlock = get("potted_oak_sapling")
    @JvmField
    val POTTED_SPRUCE_SAPLING: KryptonBlock = get("potted_spruce_sapling")
    @JvmField
    val POTTED_BIRCH_SAPLING: KryptonBlock = get("potted_birch_sapling")
    @JvmField
    val POTTED_JUNGLE_SAPLING: KryptonBlock = get("potted_jungle_sapling")
    @JvmField
    val POTTED_ACACIA_SAPLING: KryptonBlock = get("potted_acacia_sapling")
    @JvmField
    val POTTED_DARK_OAK_SAPLING: KryptonBlock = get("potted_dark_oak_sapling")
    @JvmField
    val POTTED_MANGROVE_PROPAGULE: KryptonBlock = get("potted_mangrove_propagule")
    @JvmField
    val POTTED_FERN: KryptonBlock = get("potted_fern")
    @JvmField
    val POTTED_DANDELION: KryptonBlock = get("potted_dandelion")
    @JvmField
    val POTTED_POPPY: KryptonBlock = get("potted_poppy")
    @JvmField
    val POTTED_BLUE_ORCHID: KryptonBlock = get("potted_blue_orchid")
    @JvmField
    val POTTED_ALLIUM: KryptonBlock = get("potted_allium")
    @JvmField
    val POTTED_AZURE_BLUET: KryptonBlock = get("potted_azure_bluet")
    @JvmField
    val POTTED_RED_TULIP: KryptonBlock = get("potted_red_tulip")
    @JvmField
    val POTTED_ORANGE_TULIP: KryptonBlock = get("potted_orange_tulip")
    @JvmField
    val POTTED_WHITE_TULIP: KryptonBlock = get("potted_white_tulip")
    @JvmField
    val POTTED_PINK_TULIP: KryptonBlock = get("potted_pink_tulip")
    @JvmField
    val POTTED_OXEYE_DAISY: KryptonBlock = get("potted_oxeye_daisy")
    @JvmField
    val POTTED_CORNFLOWER: KryptonBlock = get("potted_cornflower")
    @JvmField
    val POTTED_LILY_OF_THE_VALLEY: KryptonBlock = get("potted_lily_of_the_valley")
    @JvmField
    val POTTED_WITHER_ROSE: KryptonBlock = get("potted_wither_rose")
    @JvmField
    val POTTED_RED_MUSHROOM: KryptonBlock = get("potted_red_mushroom")
    @JvmField
    val POTTED_BROWN_MUSHROOM: KryptonBlock = get("potted_brown_mushroom")
    @JvmField
    val POTTED_DEAD_BUSH: KryptonBlock = get("potted_dead_bush")
    @JvmField
    val POTTED_CACTUS: KryptonBlock = get("potted_cactus")
    @JvmField
    val CARROTS: KryptonBlock = get("carrots")
    @JvmField
    val POTATOES: KryptonBlock = get("potatoes")
    @JvmField
    val OAK_BUTTON: KryptonBlock = get("oak_button")
    @JvmField
    val SPRUCE_BUTTON: KryptonBlock = get("spruce_button")
    @JvmField
    val BIRCH_BUTTON: KryptonBlock = get("birch_button")
    @JvmField
    val JUNGLE_BUTTON: KryptonBlock = get("jungle_button")
    @JvmField
    val ACACIA_BUTTON: KryptonBlock = get("acacia_button")
    @JvmField
    val DARK_OAK_BUTTON: KryptonBlock = get("dark_oak_button")
    @JvmField
    val MANGROVE_BUTTON: KryptonBlock = get("mangrove_button")
    @JvmField
    val SKELETON_SKULL: KryptonBlock = get("skeleton_skull")
    @JvmField
    val SKELETON_WALL_SKULL: KryptonBlock = get("skeleton_wall_skull")
    @JvmField
    val WITHER_SKELETON_SKULL: KryptonBlock = get("wither_skeleton_skull")
    @JvmField
    val WITHER_SKELETON_WALL_SKULL: KryptonBlock = get("wither_skeleton_wall_skull")
    @JvmField
    val ZOMBIE_HEAD: KryptonBlock = get("zombie_head")
    @JvmField
    val ZOMBIE_WALL_HEAD: KryptonBlock = get("zombie_wall_head")
    @JvmField
    val PLAYER_HEAD: KryptonBlock = get("player_head")
    @JvmField
    val PLAYER_WALL_HEAD: KryptonBlock = get("player_wall_head")
    @JvmField
    val CREEPER_HEAD: KryptonBlock = get("creeper_head")
    @JvmField
    val CREEPER_WALL_HEAD: KryptonBlock = get("creeper_wall_head")
    @JvmField
    val DRAGON_HEAD: KryptonBlock = get("dragon_head")
    @JvmField
    val DRAGON_WALL_HEAD: KryptonBlock = get("dragon_wall_head")
    @JvmField
    val ANVIL: KryptonBlock = get("anvil")
    @JvmField
    val CHIPPED_ANVIL: KryptonBlock = get("chipped_anvil")
    @JvmField
    val DAMAGED_ANVIL: KryptonBlock = get("damaged_anvil")
    @JvmField
    val TRAPPED_CHEST: KryptonBlock = get("trapped_chest")
    @JvmField
    val LIGHT_WEIGHTED_PRESSURE_PLATE: KryptonBlock = get("light_weighted_pressure_plate")
    @JvmField
    val HEAVY_WEIGHTED_PRESSURE_PLATE: KryptonBlock = get("heavy_weighted_pressure_plate")
    @JvmField
    val COMPARATOR: KryptonBlock = get("comparator")
    @JvmField
    val DAYLIGHT_DETECTOR: KryptonBlock = get("daylight_detector")
    @JvmField
    val REDSTONE_BLOCK: KryptonBlock = get("redstone_block")
    @JvmField
    val NETHER_QUARTZ_ORE: KryptonBlock = get("nether_quartz_ore")
    @JvmField
    val HOPPER: KryptonBlock = get("hopper")
    @JvmField
    val QUARTZ_BLOCK: KryptonBlock = get("quartz_block")
    @JvmField
    val CHISELED_QUARTZ_BLOCK: KryptonBlock = get("chiseled_quartz_block")
    @JvmField
    val QUARTZ_PILLAR: KryptonBlock = get("quartz_pillar")
    @JvmField
    val QUARTZ_STAIRS: KryptonBlock = get("quartz_stairs")
    @JvmField
    val ACTIVATOR_RAIL: KryptonBlock = get("activator_rail")
    @JvmField
    val DROPPER: KryptonBlock = get("dropper")
    @JvmField
    val WHITE_TERRACOTTA: KryptonBlock = get("white_terracotta")
    @JvmField
    val ORANGE_TERRACOTTA: KryptonBlock = get("orange_terracotta")
    @JvmField
    val MAGENTA_TERRACOTTA: KryptonBlock = get("magenta_terracotta")
    @JvmField
    val LIGHT_BLUE_TERRACOTTA: KryptonBlock = get("light_blue_terracotta")
    @JvmField
    val YELLOW_TERRACOTTA: KryptonBlock = get("yellow_terracotta")
    @JvmField
    val LIME_TERRACOTTA: KryptonBlock = get("lime_terracotta")
    @JvmField
    val PINK_TERRACOTTA: KryptonBlock = get("pink_terracotta")
    @JvmField
    val GRAY_TERRACOTTA: KryptonBlock = get("gray_terracotta")
    @JvmField
    val LIGHT_GRAY_TERRACOTTA: KryptonBlock = get("light_gray_terracotta")
    @JvmField
    val CYAN_TERRACOTTA: KryptonBlock = get("cyan_terracotta")
    @JvmField
    val PURPLE_TERRACOTTA: KryptonBlock = get("purple_terracotta")
    @JvmField
    val BLUE_TERRACOTTA: KryptonBlock = get("blue_terracotta")
    @JvmField
    val BROWN_TERRACOTTA: KryptonBlock = get("brown_terracotta")
    @JvmField
    val GREEN_TERRACOTTA: KryptonBlock = get("green_terracotta")
    @JvmField
    val RED_TERRACOTTA: KryptonBlock = get("red_terracotta")
    @JvmField
    val BLACK_TERRACOTTA: KryptonBlock = get("black_terracotta")
    @JvmField
    val WHITE_STAINED_GLASS_PANE: KryptonBlock = get("white_stained_glass_pane")
    @JvmField
    val ORANGE_STAINED_GLASS_PANE: KryptonBlock = get("orange_stained_glass_pane")
    @JvmField
    val MAGENTA_STAINED_GLASS_PANE: KryptonBlock = get("magenta_stained_glass_pane")
    @JvmField
    val LIGHT_BLUE_STAINED_GLASS_PANE: KryptonBlock = get("light_blue_stained_glass_pane")
    @JvmField
    val YELLOW_STAINED_GLASS_PANE: KryptonBlock = get("yellow_stained_glass_pane")
    @JvmField
    val LIME_STAINED_GLASS_PANE: KryptonBlock = get("lime_stained_glass_pane")
    @JvmField
    val PINK_STAINED_GLASS_PANE: KryptonBlock = get("pink_stained_glass_pane")
    @JvmField
    val GRAY_STAINED_GLASS_PANE: KryptonBlock = get("gray_stained_glass_pane")
    @JvmField
    val LIGHT_GRAY_STAINED_GLASS_PANE: KryptonBlock = get("light_gray_stained_glass_pane")
    @JvmField
    val CYAN_STAINED_GLASS_PANE: KryptonBlock = get("cyan_stained_glass_pane")
    @JvmField
    val PURPLE_STAINED_GLASS_PANE: KryptonBlock = get("purple_stained_glass_pane")
    @JvmField
    val BLUE_STAINED_GLASS_PANE: KryptonBlock = get("blue_stained_glass_pane")
    @JvmField
    val BROWN_STAINED_GLASS_PANE: KryptonBlock = get("brown_stained_glass_pane")
    @JvmField
    val GREEN_STAINED_GLASS_PANE: KryptonBlock = get("green_stained_glass_pane")
    @JvmField
    val RED_STAINED_GLASS_PANE: KryptonBlock = get("red_stained_glass_pane")
    @JvmField
    val BLACK_STAINED_GLASS_PANE: KryptonBlock = get("black_stained_glass_pane")
    @JvmField
    val ACACIA_STAIRS: KryptonBlock = get("acacia_stairs")
    @JvmField
    val DARK_OAK_STAIRS: KryptonBlock = get("dark_oak_stairs")
    @JvmField
    val MANGROVE_STAIRS: KryptonBlock = get("mangrove_stairs")
    @JvmField
    val SLIME_BLOCK: KryptonBlock = get("slime_block")
    @JvmField
    val BARRIER: KryptonBlock = get("barrier")
    @JvmField
    val LIGHT: KryptonBlock = get("light")
    @JvmField
    val IRON_TRAPDOOR: KryptonBlock = get("iron_trapdoor")
    @JvmField
    val PRISMARINE: KryptonBlock = get("prismarine")
    @JvmField
    val PRISMARINE_BRICKS: KryptonBlock = get("prismarine_bricks")
    @JvmField
    val DARK_PRISMARINE: KryptonBlock = get("dark_prismarine")
    @JvmField
    val PRISMARINE_STAIRS: KryptonBlock = get("prismarine_stairs")
    @JvmField
    val PRISMARINE_BRICK_STAIRS: KryptonBlock = get("prismarine_brick_stairs")
    @JvmField
    val DARK_PRISMARINE_STAIRS: KryptonBlock = get("dark_prismarine_stairs")
    @JvmField
    val PRISMARINE_SLAB: KryptonBlock = get("prismarine_slab")
    @JvmField
    val PRISMARINE_BRICK_SLAB: KryptonBlock = get("prismarine_brick_slab")
    @JvmField
    val DARK_PRISMARINE_SLAB: KryptonBlock = get("dark_prismarine_slab")
    @JvmField
    val SEA_LANTERN: KryptonBlock = get("sea_lantern")
    @JvmField
    val HAY_BLOCK: KryptonBlock = get("hay_block")
    @JvmField
    val WHITE_CARPET: KryptonBlock = get("white_carpet")
    @JvmField
    val ORANGE_CARPET: KryptonBlock = get("orange_carpet")
    @JvmField
    val MAGENTA_CARPET: KryptonBlock = get("magenta_carpet")
    @JvmField
    val LIGHT_BLUE_CARPET: KryptonBlock = get("light_blue_carpet")
    @JvmField
    val YELLOW_CARPET: KryptonBlock = get("yellow_carpet")
    @JvmField
    val LIME_CARPET: KryptonBlock = get("lime_carpet")
    @JvmField
    val PINK_CARPET: KryptonBlock = get("pink_carpet")
    @JvmField
    val GRAY_CARPET: KryptonBlock = get("gray_carpet")
    @JvmField
    val LIGHT_GRAY_CARPET: KryptonBlock = get("light_gray_carpet")
    @JvmField
    val CYAN_CARPET: KryptonBlock = get("cyan_carpet")
    @JvmField
    val PURPLE_CARPET: KryptonBlock = get("purple_carpet")
    @JvmField
    val BLUE_CARPET: KryptonBlock = get("blue_carpet")
    @JvmField
    val BROWN_CARPET: KryptonBlock = get("brown_carpet")
    @JvmField
    val GREEN_CARPET: KryptonBlock = get("green_carpet")
    @JvmField
    val RED_CARPET: KryptonBlock = get("red_carpet")
    @JvmField
    val BLACK_CARPET: KryptonBlock = get("black_carpet")
    @JvmField
    val TERRACOTTA: KryptonBlock = get("terracotta")
    @JvmField
    val COAL_BLOCK: KryptonBlock = get("coal_block")
    @JvmField
    val PACKED_ICE: KryptonBlock = get("packed_ice")
    @JvmField
    val SUNFLOWER: KryptonBlock = get("sunflower")
    @JvmField
    val LILAC: KryptonBlock = get("lilac")
    @JvmField
    val ROSE_BUSH: KryptonBlock = get("rose_bush")
    @JvmField
    val PEONY: KryptonBlock = get("peony")
    @JvmField
    val TALL_GRASS: KryptonBlock = get("tall_grass")
    @JvmField
    val LARGE_FERN: KryptonBlock = get("large_fern")
    @JvmField
    val WHITE_BANNER: KryptonBlock = get("white_banner")
    @JvmField
    val ORANGE_BANNER: KryptonBlock = get("orange_banner")
    @JvmField
    val MAGENTA_BANNER: KryptonBlock = get("magenta_banner")
    @JvmField
    val LIGHT_BLUE_BANNER: KryptonBlock = get("light_blue_banner")
    @JvmField
    val YELLOW_BANNER: KryptonBlock = get("yellow_banner")
    @JvmField
    val LIME_BANNER: KryptonBlock = get("lime_banner")
    @JvmField
    val PINK_BANNER: KryptonBlock = get("pink_banner")
    @JvmField
    val GRAY_BANNER: KryptonBlock = get("gray_banner")
    @JvmField
    val LIGHT_GRAY_BANNER: KryptonBlock = get("light_gray_banner")
    @JvmField
    val CYAN_BANNER: KryptonBlock = get("cyan_banner")
    @JvmField
    val PURPLE_BANNER: KryptonBlock = get("purple_banner")
    @JvmField
    val BLUE_BANNER: KryptonBlock = get("blue_banner")
    @JvmField
    val BROWN_BANNER: KryptonBlock = get("brown_banner")
    @JvmField
    val GREEN_BANNER: KryptonBlock = get("green_banner")
    @JvmField
    val RED_BANNER: KryptonBlock = get("red_banner")
    @JvmField
    val BLACK_BANNER: KryptonBlock = get("black_banner")
    @JvmField
    val WHITE_WALL_BANNER: KryptonBlock = get("white_wall_banner")
    @JvmField
    val ORANGE_WALL_BANNER: KryptonBlock = get("orange_wall_banner")
    @JvmField
    val MAGENTA_WALL_BANNER: KryptonBlock = get("magenta_wall_banner")
    @JvmField
    val LIGHT_BLUE_WALL_BANNER: KryptonBlock = get("light_blue_wall_banner")
    @JvmField
    val YELLOW_WALL_BANNER: KryptonBlock = get("yellow_wall_banner")
    @JvmField
    val LIME_WALL_BANNER: KryptonBlock = get("lime_wall_banner")
    @JvmField
    val PINK_WALL_BANNER: KryptonBlock = get("pink_wall_banner")
    @JvmField
    val GRAY_WALL_BANNER: KryptonBlock = get("gray_wall_banner")
    @JvmField
    val LIGHT_GRAY_WALL_BANNER: KryptonBlock = get("light_gray_wall_banner")
    @JvmField
    val CYAN_WALL_BANNER: KryptonBlock = get("cyan_wall_banner")
    @JvmField
    val PURPLE_WALL_BANNER: KryptonBlock = get("purple_wall_banner")
    @JvmField
    val BLUE_WALL_BANNER: KryptonBlock = get("blue_wall_banner")
    @JvmField
    val BROWN_WALL_BANNER: KryptonBlock = get("brown_wall_banner")
    @JvmField
    val GREEN_WALL_BANNER: KryptonBlock = get("green_wall_banner")
    @JvmField
    val RED_WALL_BANNER: KryptonBlock = get("red_wall_banner")
    @JvmField
    val BLACK_WALL_BANNER: KryptonBlock = get("black_wall_banner")
    @JvmField
    val RED_SANDSTONE: KryptonBlock = get("red_sandstone")
    @JvmField
    val CHISELED_RED_SANDSTONE: KryptonBlock = get("chiseled_red_sandstone")
    @JvmField
    val CUT_RED_SANDSTONE: KryptonBlock = get("cut_red_sandstone")
    @JvmField
    val RED_SANDSTONE_STAIRS: KryptonBlock = get("red_sandstone_stairs")
    @JvmField
    val OAK_SLAB: KryptonBlock = get("oak_slab")
    @JvmField
    val SPRUCE_SLAB: KryptonBlock = get("spruce_slab")
    @JvmField
    val BIRCH_SLAB: KryptonBlock = get("birch_slab")
    @JvmField
    val JUNGLE_SLAB: KryptonBlock = get("jungle_slab")
    @JvmField
    val ACACIA_SLAB: KryptonBlock = get("acacia_slab")
    @JvmField
    val DARK_OAK_SLAB: KryptonBlock = get("dark_oak_slab")
    @JvmField
    val MANGROVE_SLAB: KryptonBlock = get("mangrove_slab")
    @JvmField
    val STONE_SLAB: KryptonBlock = get("stone_slab")
    @JvmField
    val SMOOTH_STONE_SLAB: KryptonBlock = get("smooth_stone_slab")
    @JvmField
    val SANDSTONE_SLAB: KryptonBlock = get("sandstone_slab")
    @JvmField
    val CUT_SANDSTONE_SLAB: KryptonBlock = get("cut_sandstone_slab")
    @JvmField
    val PETRIFIED_OAK_SLAB: KryptonBlock = get("petrified_oak_slab")
    @JvmField
    val COBBLESTONE_SLAB: KryptonBlock = get("cobblestone_slab")
    @JvmField
    val BRICK_SLAB: KryptonBlock = get("brick_slab")
    @JvmField
    val STONE_BRICK_SLAB: KryptonBlock = get("stone_brick_slab")
    @JvmField
    val MUD_BRICK_SLAB: KryptonBlock = get("mud_brick_slab")
    @JvmField
    val NETHER_BRICK_SLAB: KryptonBlock = get("nether_brick_slab")
    @JvmField
    val QUARTZ_SLAB: KryptonBlock = get("quartz_slab")
    @JvmField
    val RED_SANDSTONE_SLAB: KryptonBlock = get("red_sandstone_slab")
    @JvmField
    val CUT_RED_SANDSTONE_SLAB: KryptonBlock = get("cut_red_sandstone_slab")
    @JvmField
    val PURPUR_SLAB: KryptonBlock = get("purpur_slab")
    @JvmField
    val SMOOTH_STONE: KryptonBlock = get("smooth_stone")
    @JvmField
    val SMOOTH_SANDSTONE: KryptonBlock = get("smooth_sandstone")
    @JvmField
    val SMOOTH_QUARTZ: KryptonBlock = get("smooth_quartz")
    @JvmField
    val SMOOTH_RED_SANDSTONE: KryptonBlock = get("smooth_red_sandstone")
    @JvmField
    val SPRUCE_FENCE_GATE: KryptonBlock = get("spruce_fence_gate")
    @JvmField
    val BIRCH_FENCE_GATE: KryptonBlock = get("birch_fence_gate")
    @JvmField
    val JUNGLE_FENCE_GATE: KryptonBlock = get("jungle_fence_gate")
    @JvmField
    val ACACIA_FENCE_GATE: KryptonBlock = get("acacia_fence_gate")
    @JvmField
    val DARK_OAK_FENCE_GATE: KryptonBlock = get("dark_oak_fence_gate")
    @JvmField
    val MANGROVE_FENCE_GATE: KryptonBlock = get("mangrove_fence_gate")
    @JvmField
    val SPRUCE_FENCE: KryptonBlock = get("spruce_fence")
    @JvmField
    val BIRCH_FENCE: KryptonBlock = get("birch_fence")
    @JvmField
    val JUNGLE_FENCE: KryptonBlock = get("jungle_fence")
    @JvmField
    val ACACIA_FENCE: KryptonBlock = get("acacia_fence")
    @JvmField
    val DARK_OAK_FENCE: KryptonBlock = get("dark_oak_fence")
    @JvmField
    val MANGROVE_FENCE: KryptonBlock = get("mangrove_fence")
    @JvmField
    val SPRUCE_DOOR: KryptonBlock = get("spruce_door")
    @JvmField
    val BIRCH_DOOR: KryptonBlock = get("birch_door")
    @JvmField
    val JUNGLE_DOOR: KryptonBlock = get("jungle_door")
    @JvmField
    val ACACIA_DOOR: KryptonBlock = get("acacia_door")
    @JvmField
    val DARK_OAK_DOOR: KryptonBlock = get("dark_oak_door")
    @JvmField
    val MANGROVE_DOOR: KryptonBlock = get("mangrove_door")
    @JvmField
    val END_ROD: KryptonBlock = get("end_rod")
    @JvmField
    val CHORUS_PLANT: KryptonBlock = get("chorus_plant")
    @JvmField
    val CHORUS_FLOWER: KryptonBlock = get("chorus_flower")
    @JvmField
    val PURPUR_BLOCK: KryptonBlock = get("purpur_block")
    @JvmField
    val PURPUR_PILLAR: KryptonBlock = get("purpur_pillar")
    @JvmField
    val PURPUR_STAIRS: KryptonBlock = get("purpur_stairs")
    @JvmField
    val END_STONE_BRICKS: KryptonBlock = get("end_stone_bricks")
    @JvmField
    val BEETROOTS: KryptonBlock = get("beetroots")
    @JvmField
    val DIRT_PATH: KryptonBlock = get("dirt_path")
    @JvmField
    val END_GATEWAY: KryptonBlock = get("end_gateway")
    @JvmField
    val REPEATING_COMMAND_BLOCK: KryptonBlock = get("repeating_command_block")
    @JvmField
    val CHAIN_COMMAND_BLOCK: KryptonBlock = get("chain_command_block")
    @JvmField
    val FROSTED_ICE: KryptonBlock = get("frosted_ice")
    @JvmField
    val MAGMA_BLOCK: KryptonBlock = get("magma_block")
    @JvmField
    val NETHER_WART_BLOCK: KryptonBlock = get("nether_wart_block")
    @JvmField
    val RED_NETHER_BRICKS: KryptonBlock = get("red_nether_bricks")
    @JvmField
    val BONE_BLOCK: KryptonBlock = get("bone_block")
    @JvmField
    val STRUCTURE_VOID: KryptonBlock = get("structure_void")
    @JvmField
    val OBSERVER: KryptonBlock = get("observer")
    @JvmField
    val SHULKER_BOX: KryptonBlock = get("shulker_box")
    @JvmField
    val WHITE_SHULKER_BOX: KryptonBlock = get("white_shulker_box")
    @JvmField
    val ORANGE_SHULKER_BOX: KryptonBlock = get("orange_shulker_box")
    @JvmField
    val MAGENTA_SHULKER_BOX: KryptonBlock = get("magenta_shulker_box")
    @JvmField
    val LIGHT_BLUE_SHULKER_BOX: KryptonBlock = get("light_blue_shulker_box")
    @JvmField
    val YELLOW_SHULKER_BOX: KryptonBlock = get("yellow_shulker_box")
    @JvmField
    val LIME_SHULKER_BOX: KryptonBlock = get("lime_shulker_box")
    @JvmField
    val PINK_SHULKER_BOX: KryptonBlock = get("pink_shulker_box")
    @JvmField
    val GRAY_SHULKER_BOX: KryptonBlock = get("gray_shulker_box")
    @JvmField
    val LIGHT_GRAY_SHULKER_BOX: KryptonBlock = get("light_gray_shulker_box")
    @JvmField
    val CYAN_SHULKER_BOX: KryptonBlock = get("cyan_shulker_box")
    @JvmField
    val PURPLE_SHULKER_BOX: KryptonBlock = get("purple_shulker_box")
    @JvmField
    val BLUE_SHULKER_BOX: KryptonBlock = get("blue_shulker_box")
    @JvmField
    val BROWN_SHULKER_BOX: KryptonBlock = get("brown_shulker_box")
    @JvmField
    val GREEN_SHULKER_BOX: KryptonBlock = get("green_shulker_box")
    @JvmField
    val RED_SHULKER_BOX: KryptonBlock = get("red_shulker_box")
    @JvmField
    val BLACK_SHULKER_BOX: KryptonBlock = get("black_shulker_box")
    @JvmField
    val WHITE_GLAZED_TERRACOTTA: KryptonBlock = get("white_glazed_terracotta")
    @JvmField
    val ORANGE_GLAZED_TERRACOTTA: KryptonBlock = get("orange_glazed_terracotta")
    @JvmField
    val MAGENTA_GLAZED_TERRACOTTA: KryptonBlock = get("magenta_glazed_terracotta")
    @JvmField
    val LIGHT_BLUE_GLAZED_TERRACOTTA: KryptonBlock = get("light_blue_glazed_terracotta")
    @JvmField
    val YELLOW_GLAZED_TERRACOTTA: KryptonBlock = get("yellow_glazed_terracotta")
    @JvmField
    val LIME_GLAZED_TERRACOTTA: KryptonBlock = get("lime_glazed_terracotta")
    @JvmField
    val PINK_GLAZED_TERRACOTTA: KryptonBlock = get("pink_glazed_terracotta")
    @JvmField
    val GRAY_GLAZED_TERRACOTTA: KryptonBlock = get("gray_glazed_terracotta")
    @JvmField
    val LIGHT_GRAY_GLAZED_TERRACOTTA: KryptonBlock = get("light_gray_glazed_terracotta")
    @JvmField
    val CYAN_GLAZED_TERRACOTTA: KryptonBlock = get("cyan_glazed_terracotta")
    @JvmField
    val PURPLE_GLAZED_TERRACOTTA: KryptonBlock = get("purple_glazed_terracotta")
    @JvmField
    val BLUE_GLAZED_TERRACOTTA: KryptonBlock = get("blue_glazed_terracotta")
    @JvmField
    val BROWN_GLAZED_TERRACOTTA: KryptonBlock = get("brown_glazed_terracotta")
    @JvmField
    val GREEN_GLAZED_TERRACOTTA: KryptonBlock = get("green_glazed_terracotta")
    @JvmField
    val RED_GLAZED_TERRACOTTA: KryptonBlock = get("red_glazed_terracotta")
    @JvmField
    val BLACK_GLAZED_TERRACOTTA: KryptonBlock = get("black_glazed_terracotta")
    @JvmField
    val WHITE_CONCRETE: KryptonBlock = get("white_concrete")
    @JvmField
    val ORANGE_CONCRETE: KryptonBlock = get("orange_concrete")
    @JvmField
    val MAGENTA_CONCRETE: KryptonBlock = get("magenta_concrete")
    @JvmField
    val LIGHT_BLUE_CONCRETE: KryptonBlock = get("light_blue_concrete")
    @JvmField
    val YELLOW_CONCRETE: KryptonBlock = get("yellow_concrete")
    @JvmField
    val LIME_CONCRETE: KryptonBlock = get("lime_concrete")
    @JvmField
    val PINK_CONCRETE: KryptonBlock = get("pink_concrete")
    @JvmField
    val GRAY_CONCRETE: KryptonBlock = get("gray_concrete")
    @JvmField
    val LIGHT_GRAY_CONCRETE: KryptonBlock = get("light_gray_concrete")
    @JvmField
    val CYAN_CONCRETE: KryptonBlock = get("cyan_concrete")
    @JvmField
    val PURPLE_CONCRETE: KryptonBlock = get("purple_concrete")
    @JvmField
    val BLUE_CONCRETE: KryptonBlock = get("blue_concrete")
    @JvmField
    val BROWN_CONCRETE: KryptonBlock = get("brown_concrete")
    @JvmField
    val GREEN_CONCRETE: KryptonBlock = get("green_concrete")
    @JvmField
    val RED_CONCRETE: KryptonBlock = get("red_concrete")
    @JvmField
    val BLACK_CONCRETE: KryptonBlock = get("black_concrete")
    @JvmField
    val WHITE_CONCRETE_POWDER: KryptonBlock = get("white_concrete_powder")
    @JvmField
    val ORANGE_CONCRETE_POWDER: KryptonBlock = get("orange_concrete_powder")
    @JvmField
    val MAGENTA_CONCRETE_POWDER: KryptonBlock = get("magenta_concrete_powder")
    @JvmField
    val LIGHT_BLUE_CONCRETE_POWDER: KryptonBlock = get("light_blue_concrete_powder")
    @JvmField
    val YELLOW_CONCRETE_POWDER: KryptonBlock = get("yellow_concrete_powder")
    @JvmField
    val LIME_CONCRETE_POWDER: KryptonBlock = get("lime_concrete_powder")
    @JvmField
    val PINK_CONCRETE_POWDER: KryptonBlock = get("pink_concrete_powder")
    @JvmField
    val GRAY_CONCRETE_POWDER: KryptonBlock = get("gray_concrete_powder")
    @JvmField
    val LIGHT_GRAY_CONCRETE_POWDER: KryptonBlock = get("light_gray_concrete_powder")
    @JvmField
    val CYAN_CONCRETE_POWDER: KryptonBlock = get("cyan_concrete_powder")
    @JvmField
    val PURPLE_CONCRETE_POWDER: KryptonBlock = get("purple_concrete_powder")
    @JvmField
    val BLUE_CONCRETE_POWDER: KryptonBlock = get("blue_concrete_powder")
    @JvmField
    val BROWN_CONCRETE_POWDER: KryptonBlock = get("brown_concrete_powder")
    @JvmField
    val GREEN_CONCRETE_POWDER: KryptonBlock = get("green_concrete_powder")
    @JvmField
    val RED_CONCRETE_POWDER: KryptonBlock = get("red_concrete_powder")
    @JvmField
    val BLACK_CONCRETE_POWDER: KryptonBlock = get("black_concrete_powder")
    @JvmField
    val KELP: KryptonBlock = get("kelp")
    @JvmField
    val KELP_PLANT: KryptonBlock = get("kelp_plant")
    @JvmField
    val DRIED_KELP_BLOCK: KryptonBlock = get("dried_kelp_block")
    @JvmField
    val TURTLE_EGG: KryptonBlock = get("turtle_egg")
    @JvmField
    val DEAD_TUBE_CORAL_BLOCK: KryptonBlock = get("dead_tube_coral_block")
    @JvmField
    val DEAD_BRAIN_CORAL_BLOCK: KryptonBlock = get("dead_brain_coral_block")
    @JvmField
    val DEAD_BUBBLE_CORAL_BLOCK: KryptonBlock = get("dead_bubble_coral_block")
    @JvmField
    val DEAD_FIRE_CORAL_BLOCK: KryptonBlock = get("dead_fire_coral_block")
    @JvmField
    val DEAD_HORN_CORAL_BLOCK: KryptonBlock = get("dead_horn_coral_block")
    @JvmField
    val TUBE_CORAL_BLOCK: KryptonBlock = get("tube_coral_block")
    @JvmField
    val BRAIN_CORAL_BLOCK: KryptonBlock = get("brain_coral_block")
    @JvmField
    val BUBBLE_CORAL_BLOCK: KryptonBlock = get("bubble_coral_block")
    @JvmField
    val FIRE_CORAL_BLOCK: KryptonBlock = get("fire_coral_block")
    @JvmField
    val HORN_CORAL_BLOCK: KryptonBlock = get("horn_coral_block")
    @JvmField
    val DEAD_TUBE_CORAL: KryptonBlock = get("dead_tube_coral")
    @JvmField
    val DEAD_BRAIN_CORAL: KryptonBlock = get("dead_brain_coral")
    @JvmField
    val DEAD_BUBBLE_CORAL: KryptonBlock = get("dead_bubble_coral")
    @JvmField
    val DEAD_FIRE_CORAL: KryptonBlock = get("dead_fire_coral")
    @JvmField
    val DEAD_HORN_CORAL: KryptonBlock = get("dead_horn_coral")
    @JvmField
    val TUBE_CORAL: KryptonBlock = get("tube_coral")
    @JvmField
    val BRAIN_CORAL: KryptonBlock = get("brain_coral")
    @JvmField
    val BUBBLE_CORAL: KryptonBlock = get("bubble_coral")
    @JvmField
    val FIRE_CORAL: KryptonBlock = get("fire_coral")
    @JvmField
    val HORN_CORAL: KryptonBlock = get("horn_coral")
    @JvmField
    val DEAD_TUBE_CORAL_FAN: KryptonBlock = get("dead_tube_coral_fan")
    @JvmField
    val DEAD_BRAIN_CORAL_FAN: KryptonBlock = get("dead_brain_coral_fan")
    @JvmField
    val DEAD_BUBBLE_CORAL_FAN: KryptonBlock = get("dead_bubble_coral_fan")
    @JvmField
    val DEAD_FIRE_CORAL_FAN: KryptonBlock = get("dead_fire_coral_fan")
    @JvmField
    val DEAD_HORN_CORAL_FAN: KryptonBlock = get("dead_horn_coral_fan")
    @JvmField
    val TUBE_CORAL_FAN: KryptonBlock = get("tube_coral_fan")
    @JvmField
    val BRAIN_CORAL_FAN: KryptonBlock = get("brain_coral_fan")
    @JvmField
    val BUBBLE_CORAL_FAN: KryptonBlock = get("bubble_coral_fan")
    @JvmField
    val FIRE_CORAL_FAN: KryptonBlock = get("fire_coral_fan")
    @JvmField
    val HORN_CORAL_FAN: KryptonBlock = get("horn_coral_fan")
    @JvmField
    val DEAD_TUBE_CORAL_WALL_FAN: KryptonBlock = get("dead_tube_coral_wall_fan")
    @JvmField
    val DEAD_BRAIN_CORAL_WALL_FAN: KryptonBlock = get("dead_brain_coral_wall_fan")
    @JvmField
    val DEAD_BUBBLE_CORAL_WALL_FAN: KryptonBlock = get("dead_bubble_coral_wall_fan")
    @JvmField
    val DEAD_FIRE_CORAL_WALL_FAN: KryptonBlock = get("dead_fire_coral_wall_fan")
    @JvmField
    val DEAD_HORN_CORAL_WALL_FAN: KryptonBlock = get("dead_horn_coral_wall_fan")
    @JvmField
    val TUBE_CORAL_WALL_FAN: KryptonBlock = get("tube_coral_wall_fan")
    @JvmField
    val BRAIN_CORAL_WALL_FAN: KryptonBlock = get("brain_coral_wall_fan")
    @JvmField
    val BUBBLE_CORAL_WALL_FAN: KryptonBlock = get("bubble_coral_wall_fan")
    @JvmField
    val FIRE_CORAL_WALL_FAN: KryptonBlock = get("fire_coral_wall_fan")
    @JvmField
    val HORN_CORAL_WALL_FAN: KryptonBlock = get("horn_coral_wall_fan")
    @JvmField
    val SEA_PICKLE: KryptonBlock = get("sea_pickle")
    @JvmField
    val BLUE_ICE: KryptonBlock = get("blue_ice")
    @JvmField
    val CONDUIT: KryptonBlock = get("conduit")
    @JvmField
    val BAMBOO_SAPLING: KryptonBlock = get("bamboo_sapling")
    @JvmField
    val BAMBOO: KryptonBlock = get("bamboo")
    @JvmField
    val POTTED_BAMBOO: KryptonBlock = get("potted_bamboo")
    @JvmField
    val VOID_AIR: KryptonBlock = get("void_air")
    @JvmField
    val CAVE_AIR: KryptonBlock = get("cave_air")
    @JvmField
    val BUBBLE_COLUMN: KryptonBlock = get("bubble_column")
    @JvmField
    val POLISHED_GRANITE_STAIRS: KryptonBlock = get("polished_granite_stairs")
    @JvmField
    val SMOOTH_RED_SANDSTONE_STAIRS: KryptonBlock = get("smooth_red_sandstone_stairs")
    @JvmField
    val MOSSY_STONE_BRICK_STAIRS: KryptonBlock = get("mossy_stone_brick_stairs")
    @JvmField
    val POLISHED_DIORITE_STAIRS: KryptonBlock = get("polished_diorite_stairs")
    @JvmField
    val MOSSY_COBBLESTONE_STAIRS: KryptonBlock = get("mossy_cobblestone_stairs")
    @JvmField
    val END_STONE_BRICK_STAIRS: KryptonBlock = get("end_stone_brick_stairs")
    @JvmField
    val STONE_STAIRS: KryptonBlock = get("stone_stairs")
    @JvmField
    val SMOOTH_SANDSTONE_STAIRS: KryptonBlock = get("smooth_sandstone_stairs")
    @JvmField
    val SMOOTH_QUARTZ_STAIRS: KryptonBlock = get("smooth_quartz_stairs")
    @JvmField
    val GRANITE_STAIRS: KryptonBlock = get("granite_stairs")
    @JvmField
    val ANDESITE_STAIRS: KryptonBlock = get("andesite_stairs")
    @JvmField
    val RED_NETHER_BRICK_STAIRS: KryptonBlock = get("red_nether_brick_stairs")
    @JvmField
    val POLISHED_ANDESITE_STAIRS: KryptonBlock = get("polished_andesite_stairs")
    @JvmField
    val DIORITE_STAIRS: KryptonBlock = get("diorite_stairs")
    @JvmField
    val POLISHED_GRANITE_SLAB: KryptonBlock = get("polished_granite_slab")
    @JvmField
    val SMOOTH_RED_SANDSTONE_SLAB: KryptonBlock = get("smooth_red_sandstone_slab")
    @JvmField
    val MOSSY_STONE_BRICK_SLAB: KryptonBlock = get("mossy_stone_brick_slab")
    @JvmField
    val POLISHED_DIORITE_SLAB: KryptonBlock = get("polished_diorite_slab")
    @JvmField
    val MOSSY_COBBLESTONE_SLAB: KryptonBlock = get("mossy_cobblestone_slab")
    @JvmField
    val END_STONE_BRICK_SLAB: KryptonBlock = get("end_stone_brick_slab")
    @JvmField
    val SMOOTH_SANDSTONE_SLAB: KryptonBlock = get("smooth_sandstone_slab")
    @JvmField
    val SMOOTH_QUARTZ_SLAB: KryptonBlock = get("smooth_quartz_slab")
    @JvmField
    val GRANITE_SLAB: KryptonBlock = get("granite_slab")
    @JvmField
    val ANDESITE_SLAB: KryptonBlock = get("andesite_slab")
    @JvmField
    val RED_NETHER_BRICK_SLAB: KryptonBlock = get("red_nether_brick_slab")
    @JvmField
    val POLISHED_ANDESITE_SLAB: KryptonBlock = get("polished_andesite_slab")
    @JvmField
    val DIORITE_SLAB: KryptonBlock = get("diorite_slab")
    @JvmField
    val BRICK_WALL: KryptonBlock = get("brick_wall")
    @JvmField
    val PRISMARINE_WALL: KryptonBlock = get("prismarine_wall")
    @JvmField
    val RED_SANDSTONE_WALL: KryptonBlock = get("red_sandstone_wall")
    @JvmField
    val MOSSY_STONE_BRICK_WALL: KryptonBlock = get("mossy_stone_brick_wall")
    @JvmField
    val GRANITE_WALL: KryptonBlock = get("granite_wall")
    @JvmField
    val STONE_BRICK_WALL: KryptonBlock = get("stone_brick_wall")
    @JvmField
    val MUD_BRICK_WALL: KryptonBlock = get("mud_brick_wall")
    @JvmField
    val NETHER_BRICK_WALL: KryptonBlock = get("nether_brick_wall")
    @JvmField
    val ANDESITE_WALL: KryptonBlock = get("andesite_wall")
    @JvmField
    val RED_NETHER_BRICK_WALL: KryptonBlock = get("red_nether_brick_wall")
    @JvmField
    val SANDSTONE_WALL: KryptonBlock = get("sandstone_wall")
    @JvmField
    val END_STONE_BRICK_WALL: KryptonBlock = get("end_stone_brick_wall")
    @JvmField
    val DIORITE_WALL: KryptonBlock = get("diorite_wall")
    @JvmField
    val SCAFFOLDING: KryptonBlock = get("scaffolding")
    @JvmField
    val LOOM: KryptonBlock = get("loom")
    @JvmField
    val BARREL: KryptonBlock = get("barrel")
    @JvmField
    val SMOKER: KryptonBlock = get("smoker")
    @JvmField
    val BLAST_FURNACE: KryptonBlock = get("blast_furnace")
    @JvmField
    val CARTOGRAPHY_TABLE: KryptonBlock = get("cartography_table")
    @JvmField
    val FLETCHING_TABLE: KryptonBlock = get("fletching_table")
    @JvmField
    val GRINDSTONE: KryptonBlock = get("grindstone")
    @JvmField
    val LECTERN: KryptonBlock = get("lectern")
    @JvmField
    val SMITHING_TABLE: KryptonBlock = get("smithing_table")
    @JvmField
    val STONECUTTER: KryptonBlock = get("stonecutter")
    @JvmField
    val BELL: KryptonBlock = get("bell")
    @JvmField
    val LANTERN: KryptonBlock = get("lantern")
    @JvmField
    val SOUL_LANTERN: KryptonBlock = get("soul_lantern")
    @JvmField
    val CAMPFIRE: KryptonBlock = get("campfire")
    @JvmField
    val SOUL_CAMPFIRE: KryptonBlock = get("soul_campfire")
    @JvmField
    val SWEET_BERRY_BUSH: KryptonBlock = get("sweet_berry_bush")
    @JvmField
    val WARPED_STEM: KryptonBlock = get("warped_stem")
    @JvmField
    val STRIPPED_WARPED_STEM: KryptonBlock = get("stripped_warped_stem")
    @JvmField
    val WARPED_HYPHAE: KryptonBlock = get("warped_hyphae")
    @JvmField
    val STRIPPED_WARPED_HYPHAE: KryptonBlock = get("stripped_warped_hyphae")
    @JvmField
    val WARPED_NYLIUM: KryptonBlock = get("warped_nylium")
    @JvmField
    val WARPED_FUNGUS: KryptonBlock = get("warped_fungus")
    @JvmField
    val WARPED_WART_BLOCK: KryptonBlock = get("warped_wart_block")
    @JvmField
    val WARPED_ROOTS: KryptonBlock = get("warped_roots")
    @JvmField
    val NETHER_SPROUTS: KryptonBlock = get("nether_sprouts")
    @JvmField
    val CRIMSON_STEM: KryptonBlock = get("crimson_stem")
    @JvmField
    val STRIPPED_CRIMSON_STEM: KryptonBlock = get("stripped_crimson_stem")
    @JvmField
    val CRIMSON_HYPHAE: KryptonBlock = get("crimson_hyphae")
    @JvmField
    val STRIPPED_CRIMSON_HYPHAE: KryptonBlock = get("stripped_crimson_hyphae")
    @JvmField
    val CRIMSON_NYLIUM: KryptonBlock = get("crimson_nylium")
    @JvmField
    val CRIMSON_FUNGUS: KryptonBlock = get("crimson_fungus")
    @JvmField
    val SHROOMLIGHT: KryptonBlock = get("shroomlight")
    @JvmField
    val WEEPING_VINES: KryptonBlock = get("weeping_vines")
    @JvmField
    val WEEPING_VINES_PLANT: KryptonBlock = get("weeping_vines_plant")
    @JvmField
    val TWISTING_VINES: KryptonBlock = get("twisting_vines")
    @JvmField
    val TWISTING_VINES_PLANT: KryptonBlock = get("twisting_vines_plant")
    @JvmField
    val CRIMSON_ROOTS: KryptonBlock = get("crimson_roots")
    @JvmField
    val CRIMSON_PLANKS: KryptonBlock = get("crimson_planks")
    @JvmField
    val WARPED_PLANKS: KryptonBlock = get("warped_planks")
    @JvmField
    val CRIMSON_SLAB: KryptonBlock = get("crimson_slab")
    @JvmField
    val WARPED_SLAB: KryptonBlock = get("warped_slab")
    @JvmField
    val CRIMSON_PRESSURE_PLATE: KryptonBlock = get("crimson_pressure_plate")
    @JvmField
    val WARPED_PRESSURE_PLATE: KryptonBlock = get("warped_pressure_plate")
    @JvmField
    val CRIMSON_FENCE: KryptonBlock = get("crimson_fence")
    @JvmField
    val WARPED_FENCE: KryptonBlock = get("warped_fence")
    @JvmField
    val CRIMSON_TRAPDOOR: KryptonBlock = get("crimson_trapdoor")
    @JvmField
    val WARPED_TRAPDOOR: KryptonBlock = get("warped_trapdoor")
    @JvmField
    val CRIMSON_FENCE_GATE: KryptonBlock = get("crimson_fence_gate")
    @JvmField
    val WARPED_FENCE_GATE: KryptonBlock = get("warped_fence_gate")
    @JvmField
    val CRIMSON_STAIRS: KryptonBlock = get("crimson_stairs")
    @JvmField
    val WARPED_STAIRS: KryptonBlock = get("warped_stairs")
    @JvmField
    val CRIMSON_BUTTON: KryptonBlock = get("crimson_button")
    @JvmField
    val WARPED_BUTTON: KryptonBlock = get("warped_button")
    @JvmField
    val CRIMSON_DOOR: KryptonBlock = get("crimson_door")
    @JvmField
    val WARPED_DOOR: KryptonBlock = get("warped_door")
    @JvmField
    val CRIMSON_SIGN: KryptonBlock = get("crimson_sign")
    @JvmField
    val WARPED_SIGN: KryptonBlock = get("warped_sign")
    @JvmField
    val CRIMSON_WALL_SIGN: KryptonBlock = get("crimson_wall_sign")
    @JvmField
    val WARPED_WALL_SIGN: KryptonBlock = get("warped_wall_sign")
    @JvmField
    val STRUCTURE_BLOCK: KryptonBlock = get("structure_block")
    @JvmField
    val JIGSAW: KryptonBlock = get("jigsaw")
    @JvmField
    val COMPOSTER: KryptonBlock = get("composter")
    @JvmField
    val TARGET: KryptonBlock = get("target")
    @JvmField
    val BEE_NEST: KryptonBlock = get("bee_nest")
    @JvmField
    val BEEHIVE: KryptonBlock = get("beehive")
    @JvmField
    val HONEY_BLOCK: KryptonBlock = get("honey_block")
    @JvmField
    val HONEYCOMB_BLOCK: KryptonBlock = get("honeycomb_block")
    @JvmField
    val NETHERITE_BLOCK: KryptonBlock = get("netherite_block")
    @JvmField
    val ANCIENT_DEBRIS: KryptonBlock = get("ancient_debris")
    @JvmField
    val CRYING_OBSIDIAN: KryptonBlock = get("crying_obsidian")
    @JvmField
    val RESPAWN_ANCHOR: KryptonBlock = get("respawn_anchor")
    @JvmField
    val POTTED_CRIMSON_FUNGUS: KryptonBlock = get("potted_crimson_fungus")
    @JvmField
    val POTTED_WARPED_FUNGUS: KryptonBlock = get("potted_warped_fungus")
    @JvmField
    val POTTED_CRIMSON_ROOTS: KryptonBlock = get("potted_crimson_roots")
    @JvmField
    val POTTED_WARPED_ROOTS: KryptonBlock = get("potted_warped_roots")
    @JvmField
    val LODESTONE: KryptonBlock = get("lodestone")
    @JvmField
    val BLACKSTONE: KryptonBlock = get("blackstone")
    @JvmField
    val BLACKSTONE_STAIRS: KryptonBlock = get("blackstone_stairs")
    @JvmField
    val BLACKSTONE_WALL: KryptonBlock = get("blackstone_wall")
    @JvmField
    val BLACKSTONE_SLAB: KryptonBlock = get("blackstone_slab")
    @JvmField
    val POLISHED_BLACKSTONE: KryptonBlock = get("polished_blackstone")
    @JvmField
    val POLISHED_BLACKSTONE_BRICKS: KryptonBlock = get("polished_blackstone_bricks")
    @JvmField
    val CRACKED_POLISHED_BLACKSTONE_BRICKS: KryptonBlock = get("cracked_polished_blackstone_bricks")
    @JvmField
    val CHISELED_POLISHED_BLACKSTONE: KryptonBlock = get("chiseled_polished_blackstone")
    @JvmField
    val POLISHED_BLACKSTONE_BRICK_SLAB: KryptonBlock = get("polished_blackstone_brick_slab")
    @JvmField
    val POLISHED_BLACKSTONE_BRICK_STAIRS: KryptonBlock = get("polished_blackstone_brick_stairs")
    @JvmField
    val POLISHED_BLACKSTONE_BRICK_WALL: KryptonBlock = get("polished_blackstone_brick_wall")
    @JvmField
    val GILDED_BLACKSTONE: KryptonBlock = get("gilded_blackstone")
    @JvmField
    val POLISHED_BLACKSTONE_STAIRS: KryptonBlock = get("polished_blackstone_stairs")
    @JvmField
    val POLISHED_BLACKSTONE_SLAB: KryptonBlock = get("polished_blackstone_slab")
    @JvmField
    val POLISHED_BLACKSTONE_PRESSURE_PLATE: KryptonBlock = get("polished_blackstone_pressure_plate")
    @JvmField
    val POLISHED_BLACKSTONE_BUTTON: KryptonBlock = get("polished_blackstone_button")
    @JvmField
    val POLISHED_BLACKSTONE_WALL: KryptonBlock = get("polished_blackstone_wall")
    @JvmField
    val CHISELED_NETHER_BRICKS: KryptonBlock = get("chiseled_nether_bricks")
    @JvmField
    val CRACKED_NETHER_BRICKS: KryptonBlock = get("cracked_nether_bricks")
    @JvmField
    val QUARTZ_BRICKS: KryptonBlock = get("quartz_bricks")
    @JvmField
    val CANDLE: KryptonBlock = get("candle")
    @JvmField
    val WHITE_CANDLE: KryptonBlock = get("white_candle")
    @JvmField
    val ORANGE_CANDLE: KryptonBlock = get("orange_candle")
    @JvmField
    val MAGENTA_CANDLE: KryptonBlock = get("magenta_candle")
    @JvmField
    val LIGHT_BLUE_CANDLE: KryptonBlock = get("light_blue_candle")
    @JvmField
    val YELLOW_CANDLE: KryptonBlock = get("yellow_candle")
    @JvmField
    val LIME_CANDLE: KryptonBlock = get("lime_candle")
    @JvmField
    val PINK_CANDLE: KryptonBlock = get("pink_candle")
    @JvmField
    val GRAY_CANDLE: KryptonBlock = get("gray_candle")
    @JvmField
    val LIGHT_GRAY_CANDLE: KryptonBlock = get("light_gray_candle")
    @JvmField
    val CYAN_CANDLE: KryptonBlock = get("cyan_candle")
    @JvmField
    val PURPLE_CANDLE: KryptonBlock = get("purple_candle")
    @JvmField
    val BLUE_CANDLE: KryptonBlock = get("blue_candle")
    @JvmField
    val BROWN_CANDLE: KryptonBlock = get("brown_candle")
    @JvmField
    val GREEN_CANDLE: KryptonBlock = get("green_candle")
    @JvmField
    val RED_CANDLE: KryptonBlock = get("red_candle")
    @JvmField
    val BLACK_CANDLE: KryptonBlock = get("black_candle")
    @JvmField
    val CANDLE_CAKE: KryptonBlock = get("candle_cake")
    @JvmField
    val WHITE_CANDLE_CAKE: KryptonBlock = get("white_candle_cake")
    @JvmField
    val ORANGE_CANDLE_CAKE: KryptonBlock = get("orange_candle_cake")
    @JvmField
    val MAGENTA_CANDLE_CAKE: KryptonBlock = get("magenta_candle_cake")
    @JvmField
    val LIGHT_BLUE_CANDLE_CAKE: KryptonBlock = get("light_blue_candle_cake")
    @JvmField
    val YELLOW_CANDLE_CAKE: KryptonBlock = get("yellow_candle_cake")
    @JvmField
    val LIME_CANDLE_CAKE: KryptonBlock = get("lime_candle_cake")
    @JvmField
    val PINK_CANDLE_CAKE: KryptonBlock = get("pink_candle_cake")
    @JvmField
    val GRAY_CANDLE_CAKE: KryptonBlock = get("gray_candle_cake")
    @JvmField
    val LIGHT_GRAY_CANDLE_CAKE: KryptonBlock = get("light_gray_candle_cake")
    @JvmField
    val CYAN_CANDLE_CAKE: KryptonBlock = get("cyan_candle_cake")
    @JvmField
    val PURPLE_CANDLE_CAKE: KryptonBlock = get("purple_candle_cake")
    @JvmField
    val BLUE_CANDLE_CAKE: KryptonBlock = get("blue_candle_cake")
    @JvmField
    val BROWN_CANDLE_CAKE: KryptonBlock = get("brown_candle_cake")
    @JvmField
    val GREEN_CANDLE_CAKE: KryptonBlock = get("green_candle_cake")
    @JvmField
    val RED_CANDLE_CAKE: KryptonBlock = get("red_candle_cake")
    @JvmField
    val BLACK_CANDLE_CAKE: KryptonBlock = get("black_candle_cake")
    @JvmField
    val AMETHYST_BLOCK: KryptonBlock = get("amethyst_block")
    @JvmField
    val BUDDING_AMETHYST: KryptonBlock = get("budding_amethyst")
    @JvmField
    val AMETHYST_CLUSTER: KryptonBlock = get("amethyst_cluster")
    @JvmField
    val LARGE_AMETHYST_BUD: KryptonBlock = get("large_amethyst_bud")
    @JvmField
    val MEDIUM_AMETHYST_BUD: KryptonBlock = get("medium_amethyst_bud")
    @JvmField
    val SMALL_AMETHYST_BUD: KryptonBlock = get("small_amethyst_bud")
    @JvmField
    val TUFF: KryptonBlock = get("tuff")
    @JvmField
    val CALCITE: KryptonBlock = get("calcite")
    @JvmField
    val TINTED_GLASS: KryptonBlock = get("tinted_glass")
    @JvmField
    val POWDER_SNOW: KryptonBlock = get("powder_snow")
    @JvmField
    val SCULK_SENSOR: KryptonBlock = get("sculk_sensor")
    @JvmField
    val SCULK: KryptonBlock = get("sculk")
    @JvmField
    val SCULK_VEIN: KryptonBlock = get("sculk_vein")
    @JvmField
    val SCULK_CATALYST: KryptonBlock = get("sculk_catalyst")
    @JvmField
    val SCULK_SHRIEKER: KryptonBlock = get("sculk_shrieker")
    @JvmField
    val OXIDIZED_COPPER: KryptonBlock = get("oxidized_copper")
    @JvmField
    val WEATHERED_COPPER: KryptonBlock = get("weathered_copper")
    @JvmField
    val EXPOSED_COPPER: KryptonBlock = get("exposed_copper")
    @JvmField
    val COPPER_BLOCK: KryptonBlock = get("copper_block")
    @JvmField
    val COPPER_ORE: KryptonBlock = get("copper_ore")
    @JvmField
    val DEEPSLATE_COPPER_ORE: KryptonBlock = get("deepslate_copper_ore")
    @JvmField
    val OXIDIZED_CUT_COPPER: KryptonBlock = get("oxidized_cut_copper")
    @JvmField
    val WEATHERED_CUT_COPPER: KryptonBlock = get("weathered_cut_copper")
    @JvmField
    val EXPOSED_CUT_COPPER: KryptonBlock = get("exposed_cut_copper")
    @JvmField
    val CUT_COPPER: KryptonBlock = get("cut_copper")
    @JvmField
    val OXIDIZED_CUT_COPPER_STAIRS: KryptonBlock = get("oxidized_cut_copper_stairs")
    @JvmField
    val WEATHERED_CUT_COPPER_STAIRS: KryptonBlock = get("weathered_cut_copper_stairs")
    @JvmField
    val EXPOSED_CUT_COPPER_STAIRS: KryptonBlock = get("exposed_cut_copper_stairs")
    @JvmField
    val CUT_COPPER_STAIRS: KryptonBlock = get("cut_copper_stairs")
    @JvmField
    val OXIDIZED_CUT_COPPER_SLAB: KryptonBlock = get("oxidized_cut_copper_slab")
    @JvmField
    val WEATHERED_CUT_COPPER_SLAB: KryptonBlock = get("weathered_cut_copper_slab")
    @JvmField
    val EXPOSED_CUT_COPPER_SLAB: KryptonBlock = get("exposed_cut_copper_slab")
    @JvmField
    val CUT_COPPER_SLAB: KryptonBlock = get("cut_copper_slab")
    @JvmField
    val WAXED_COPPER_BLOCK: KryptonBlock = get("waxed_copper_block")
    @JvmField
    val WAXED_WEATHERED_COPPER: KryptonBlock = get("waxed_weathered_copper")
    @JvmField
    val WAXED_EXPOSED_COPPER: KryptonBlock = get("waxed_exposed_copper")
    @JvmField
    val WAXED_OXIDIZED_COPPER: KryptonBlock = get("waxed_oxidized_copper")
    @JvmField
    val WAXED_OXIDIZED_CUT_COPPER: KryptonBlock = get("waxed_oxidized_cut_copper")
    @JvmField
    val WAXED_WEATHERED_CUT_COPPER: KryptonBlock = get("waxed_weathered_cut_copper")
    @JvmField
    val WAXED_EXPOSED_CUT_COPPER: KryptonBlock = get("waxed_exposed_cut_copper")
    @JvmField
    val WAXED_CUT_COPPER: KryptonBlock = get("waxed_cut_copper")
    @JvmField
    val WAXED_OXIDIZED_CUT_COPPER_STAIRS: KryptonBlock = get("waxed_oxidized_cut_copper_stairs")
    @JvmField
    val WAXED_WEATHERED_CUT_COPPER_STAIRS: KryptonBlock = get("waxed_weathered_cut_copper_stairs")
    @JvmField
    val WAXED_EXPOSED_CUT_COPPER_STAIRS: KryptonBlock = get("waxed_exposed_cut_copper_stairs")
    @JvmField
    val WAXED_CUT_COPPER_STAIRS: KryptonBlock = get("waxed_cut_copper_stairs")
    @JvmField
    val WAXED_OXIDIZED_CUT_COPPER_SLAB: KryptonBlock = get("waxed_oxidized_cut_copper_slab")
    @JvmField
    val WAXED_WEATHERED_CUT_COPPER_SLAB: KryptonBlock = get("waxed_weathered_cut_copper_slab")
    @JvmField
    val WAXED_EXPOSED_CUT_COPPER_SLAB: KryptonBlock = get("waxed_exposed_cut_copper_slab")
    @JvmField
    val WAXED_CUT_COPPER_SLAB: KryptonBlock = get("waxed_cut_copper_slab")
    @JvmField
    val LIGHTNING_ROD: KryptonBlock = get("lightning_rod")
    @JvmField
    val POINTED_DRIPSTONE: KryptonBlock = get("pointed_dripstone")
    @JvmField
    val DRIPSTONE_BLOCK: KryptonBlock = get("dripstone_block")
    @JvmField
    val CAVE_VINES: KryptonBlock = get("cave_vines")
    @JvmField
    val CAVE_VINES_PLANT: KryptonBlock = get("cave_vines_plant")
    @JvmField
    val SPORE_BLOSSOM: KryptonBlock = get("spore_blossom")
    @JvmField
    val AZALEA: KryptonBlock = get("azalea")
    @JvmField
    val FLOWERING_AZALEA: KryptonBlock = get("flowering_azalea")
    @JvmField
    val MOSS_CARPET: KryptonBlock = get("moss_carpet")
    @JvmField
    val MOSS_BLOCK: KryptonBlock = get("moss_block")
    @JvmField
    val BIG_DRIPLEAF: KryptonBlock = get("big_dripleaf")
    @JvmField
    val BIG_DRIPLEAF_STEM: KryptonBlock = get("big_dripleaf_stem")
    @JvmField
    val SMALL_DRIPLEAF: KryptonBlock = get("small_dripleaf")
    @JvmField
    val HANGING_ROOTS: KryptonBlock = get("hanging_roots")
    @JvmField
    val ROOTED_DIRT: KryptonBlock = get("rooted_dirt")
    @JvmField
    val MUD: KryptonBlock = get("mud")
    @JvmField
    val DEEPSLATE: KryptonBlock = get("deepslate")
    @JvmField
    val COBBLED_DEEPSLATE: KryptonBlock = get("cobbled_deepslate")
    @JvmField
    val COBBLED_DEEPSLATE_STAIRS: KryptonBlock = get("cobbled_deepslate_stairs")
    @JvmField
    val COBBLED_DEEPSLATE_SLAB: KryptonBlock = get("cobbled_deepslate_slab")
    @JvmField
    val COBBLED_DEEPSLATE_WALL: KryptonBlock = get("cobbled_deepslate_wall")
    @JvmField
    val POLISHED_DEEPSLATE: KryptonBlock = get("polished_deepslate")
    @JvmField
    val POLISHED_DEEPSLATE_STAIRS: KryptonBlock = get("polished_deepslate_stairs")
    @JvmField
    val POLISHED_DEEPSLATE_SLAB: KryptonBlock = get("polished_deepslate_slab")
    @JvmField
    val POLISHED_DEEPSLATE_WALL: KryptonBlock = get("polished_deepslate_wall")
    @JvmField
    val DEEPSLATE_TILES: KryptonBlock = get("deepslate_tiles")
    @JvmField
    val DEEPSLATE_TILE_STAIRS: KryptonBlock = get("deepslate_tile_stairs")
    @JvmField
    val DEEPSLATE_TILE_SLAB: KryptonBlock = get("deepslate_tile_slab")
    @JvmField
    val DEEPSLATE_TILE_WALL: KryptonBlock = get("deepslate_tile_wall")
    @JvmField
    val DEEPSLATE_BRICKS: KryptonBlock = get("deepslate_bricks")
    @JvmField
    val DEEPSLATE_BRICK_STAIRS: KryptonBlock = get("deepslate_brick_stairs")
    @JvmField
    val DEEPSLATE_BRICK_SLAB: KryptonBlock = get("deepslate_brick_slab")
    @JvmField
    val DEEPSLATE_BRICK_WALL: KryptonBlock = get("deepslate_brick_wall")
    @JvmField
    val CHISELED_DEEPSLATE: KryptonBlock = get("chiseled_deepslate")
    @JvmField
    val CRACKED_DEEPSLATE_BRICKS: KryptonBlock = get("cracked_deepslate_bricks")
    @JvmField
    val CRACKED_DEEPSLATE_TILES: KryptonBlock = get("cracked_deepslate_tiles")
    @JvmField
    val INFESTED_DEEPSLATE: KryptonBlock = get("infested_deepslate")
    @JvmField
    val SMOOTH_BASALT: KryptonBlock = get("smooth_basalt")
    @JvmField
    val RAW_IRON_BLOCK: KryptonBlock = get("raw_iron_block")
    @JvmField
    val RAW_COPPER_BLOCK: KryptonBlock = get("raw_copper_block")
    @JvmField
    val RAW_GOLD_BLOCK: KryptonBlock = get("raw_gold_block")
    @JvmField
    val POTTED_AZALEA: KryptonBlock = get("potted_azalea_bush")
    @JvmField
    val POTTED_FLOWERING_AZALEA: KryptonBlock = get("potted_flowering_azalea_bush")
    @JvmField
    val OCHRE_FROGLIGHT: KryptonBlock = get("ochre_froglight")
    @JvmField
    val VERDANT_FROGLIGHT: KryptonBlock = get("verdant_froglight")
    @JvmField
    val PEARLESCENT_FROGLIGHT: KryptonBlock = get("pearlescent_froglight")
    @JvmField
    val FROGSPAWN: KryptonBlock = get("frogspawn")
    @JvmField
    val REINFORCED_DEEPSLATE: KryptonBlock = get("reinforced_deepslate")

    @JvmStatic
    fun initializeStates() {
        KryptonRegistries.BLOCK.forEach { block ->
            block.stateDefinition.states.forEach { state ->
                KryptonBlock.STATES.add(state)
                state.initCache()
            }
            block.lootTable()
        }
    }

    @JvmStatic
    private fun get(name: String): KryptonBlock = KryptonRegistries.BLOCK.get(Key.key(name))
}

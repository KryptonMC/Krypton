/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Block::class)
public object Blocks {

    // @formatter:off
    @JvmField
    public val AIR: Block = get("air")
    @JvmField
    public val STONE: Block = get("stone")
    @JvmField
    public val GRANITE: Block = get("granite")
    @JvmField
    public val POLISHED_GRANITE: Block = get("polished_granite")
    @JvmField
    public val DIORITE: Block = get("diorite")
    @JvmField
    public val POLISHED_DIORITE: Block = get("polished_diorite")
    @JvmField
    public val ANDESITE: Block = get("andesite")
    @JvmField
    public val POLISHED_ANDESITE: Block = get("polished_andesite")
    @JvmField
    public val GRASS_BLOCK: Block = get("grass_block")
    @JvmField
    public val DIRT: Block = get("dirt")
    @JvmField
    public val COARSE_DIRT: Block = get("coarse_dirt")
    @JvmField
    public val PODZOL: Block = get("podzol")
    @JvmField
    public val COBBLESTONE: Block = get("cobblestone")
    @JvmField
    public val OAK_PLANKS: Block = get("oak_planks")
    @JvmField
    public val SPRUCE_PLANKS: Block = get("spruce_planks")
    @JvmField
    public val BIRCH_PLANKS: Block = get("birch_planks")
    @JvmField
    public val JUNGLE_PLANKS: Block = get("jungle_planks")
    @JvmField
    public val ACACIA_PLANKS: Block = get("acacia_planks")
    @JvmField
    public val DARK_OAK_PLANKS: Block = get("dark_oak_planks")
    @JvmField
    public val OAK_SAPLING: Block = get("oak_sapling")
    @JvmField
    public val SPRUCE_SAPLING: Block = get("spruce_sapling")
    @JvmField
    public val BIRCH_SAPLING: Block = get("birch_sapling")
    @JvmField
    public val JUNGLE_SAPLING: Block = get("jungle_sapling")
    @JvmField
    public val ACACIA_SAPLING: Block = get("acacia_sapling")
    @JvmField
    public val DARK_OAK_SAPLING: Block = get("dark_oak_sapling")
    @JvmField
    public val BEDROCK: Block = get("bedrock")
    @JvmField
    public val WATER: Block = get("water")
    @JvmField
    public val LAVA: Block = get("lava")
    @JvmField
    public val SAND: Block = get("sand")
    @JvmField
    public val RED_SAND: Block = get("red_sand")
    @JvmField
    public val GRAVEL: Block = get("gravel")
    @JvmField
    public val GOLD_ORE: Block = get("gold_ore")
    @JvmField
    public val DEEPSLATE_GOLD_ORE: Block = get("deepslate_gold_ore")
    @JvmField
    public val IRON_ORE: Block = get("iron_ore")
    @JvmField
    public val DEEPSLATE_IRON_ORE: Block = get("deepslate_iron_ore")
    @JvmField
    public val COAL_ORE: Block = get("coal_ore")
    @JvmField
    public val DEEPSLATE_COAL_ORE: Block = get("deepslate_coal_ore")
    @JvmField
    public val NETHER_GOLD_ORE: Block = get("nether_gold_ore")
    @JvmField
    public val OAK_LOG: Block = get("oak_log")
    @JvmField
    public val SPRUCE_LOG: Block = get("spruce_log")
    @JvmField
    public val BIRCH_LOG: Block = get("birch_log")
    @JvmField
    public val JUNGLE_LOG: Block = get("jungle_log")
    @JvmField
    public val ACACIA_LOG: Block = get("acacia_log")
    @JvmField
    public val DARK_OAK_LOG: Block = get("dark_oak_log")
    @JvmField
    public val STRIPPED_SPRUCE_LOG: Block = get("stripped_spruce_log")
    @JvmField
    public val STRIPPED_BIRCH_LOG: Block = get("stripped_birch_log")
    @JvmField
    public val STRIPPED_JUNGLE_LOG: Block = get("stripped_jungle_log")
    @JvmField
    public val STRIPPED_ACACIA_LOG: Block = get("stripped_acacia_log")
    @JvmField
    public val STRIPPED_DARK_OAK_LOG: Block = get("stripped_dark_oak_log")
    @JvmField
    public val STRIPPED_OAK_LOG: Block = get("stripped_oak_log")
    @JvmField
    public val OAK_WOOD: Block = get("oak_wood")
    @JvmField
    public val SPRUCE_WOOD: Block = get("spruce_wood")
    @JvmField
    public val BIRCH_WOOD: Block = get("birch_wood")
    @JvmField
    public val JUNGLE_WOOD: Block = get("jungle_wood")
    @JvmField
    public val ACACIA_WOOD: Block = get("acacia_wood")
    @JvmField
    public val DARK_OAK_WOOD: Block = get("dark_oak_wood")
    @JvmField
    public val STRIPPED_OAK_WOOD: Block = get("stripped_oak_wood")
    @JvmField
    public val STRIPPED_SPRUCE_WOOD: Block = get("stripped_spruce_wood")
    @JvmField
    public val STRIPPED_BIRCH_WOOD: Block = get("stripped_birch_wood")
    @JvmField
    public val STRIPPED_JUNGLE_WOOD: Block = get("stripped_jungle_wood")
    @JvmField
    public val STRIPPED_ACACIA_WOOD: Block = get("stripped_acacia_wood")
    @JvmField
    public val STRIPPED_DARK_OAK_WOOD: Block = get("stripped_dark_oak_wood")
    @JvmField
    public val OAK_LEAVES: Block = get("oak_leaves")
    @JvmField
    public val SPRUCE_LEAVES: Block = get("spruce_leaves")
    @JvmField
    public val BIRCH_LEAVES: Block = get("birch_leaves")
    @JvmField
    public val JUNGLE_LEAVES: Block = get("jungle_leaves")
    @JvmField
    public val ACACIA_LEAVES: Block = get("acacia_leaves")
    @JvmField
    public val DARK_OAK_LEAVES: Block = get("dark_oak_leaves")
    @JvmField
    public val AZALEA_LEAVES: Block = get("azalea_leaves")
    @JvmField
    public val FLOWERING_AZALEA_LEAVES: Block = get("flowering_azalea_leaves")
    @JvmField
    public val SPONGE: Block = get("sponge")
    @JvmField
    public val WET_SPONGE: Block = get("wet_sponge")
    @JvmField
    public val GLASS: Block = get("glass")
    @JvmField
    public val LAPIS_ORE: Block = get("lapis_ore")
    @JvmField
    public val DEEPSLATE_LAPIS_ORE: Block = get("deepslate_lapis_ore")
    @JvmField
    public val LAPIS_BLOCK: Block = get("lapis_block")
    @JvmField
    public val DISPENSER: Block = get("dispenser")
    @JvmField
    public val SANDSTONE: Block = get("sandstone")
    @JvmField
    public val CHISELED_SANDSTONE: Block = get("chiseled_sandstone")
    @JvmField
    public val CUT_SANDSTONE: Block = get("cut_sandstone")
    @JvmField
    public val NOTE_BLOCK: Block = get("note_block")
    @JvmField
    public val WHITE_BED: Block = get("white_bed")
    @JvmField
    public val ORANGE_BED: Block = get("orange_bed")
    @JvmField
    public val MAGENTA_BED: Block = get("magenta_bed")
    @JvmField
    public val LIGHT_BLUE_BED: Block = get("light_blue_bed")
    @JvmField
    public val YELLOW_BED: Block = get("yellow_bed")
    @JvmField
    public val LIME_BED: Block = get("lime_bed")
    @JvmField
    public val PINK_BED: Block = get("pink_bed")
    @JvmField
    public val GRAY_BED: Block = get("gray_bed")
    @JvmField
    public val LIGHT_GRAY_BED: Block = get("light_gray_bed")
    @JvmField
    public val CYAN_BED: Block = get("cyan_bed")
    @JvmField
    public val PURPLE_BED: Block = get("purple_bed")
    @JvmField
    public val BLUE_BED: Block = get("blue_bed")
    @JvmField
    public val BROWN_BED: Block = get("brown_bed")
    @JvmField
    public val GREEN_BED: Block = get("green_bed")
    @JvmField
    public val RED_BED: Block = get("red_bed")
    @JvmField
    public val BLACK_BED: Block = get("black_bed")
    @JvmField
    public val POWERED_RAIL: Block = get("powered_rail")
    @JvmField
    public val DETECTOR_RAIL: Block = get("detector_rail")
    @JvmField
    public val STICKY_PISTON: Block = get("sticky_piston")
    @JvmField
    public val COBWEB: Block = get("cobweb")
    @JvmField
    public val GRASS: Block = get("grass")
    @JvmField
    public val FERN: Block = get("fern")
    @JvmField
    public val DEAD_BUSH: Block = get("dead_bush")
    @JvmField
    public val SEAGRASS: Block = get("seagrass")
    @JvmField
    public val TALL_SEAGRASS: Block = get("tall_seagrass")
    @JvmField
    public val PISTON: Block = get("piston")
    @JvmField
    public val PISTON_HEAD: Block = get("piston_head")
    @JvmField
    public val WHITE_WOOL: Block = get("white_wool")
    @JvmField
    public val ORANGE_WOOL: Block = get("orange_wool")
    @JvmField
    public val MAGENTA_WOOL: Block = get("magenta_wool")
    @JvmField
    public val LIGHT_BLUE_WOOL: Block = get("light_blue_wool")
    @JvmField
    public val YELLOW_WOOL: Block = get("yellow_wool")
    @JvmField
    public val LIME_WOOL: Block = get("lime_wool")
    @JvmField
    public val PINK_WOOL: Block = get("pink_wool")
    @JvmField
    public val GRAY_WOOL: Block = get("gray_wool")
    @JvmField
    public val LIGHT_GRAY_WOOL: Block = get("light_gray_wool")
    @JvmField
    public val CYAN_WOOL: Block = get("cyan_wool")
    @JvmField
    public val PURPLE_WOOL: Block = get("purple_wool")
    @JvmField
    public val BLUE_WOOL: Block = get("blue_wool")
    @JvmField
    public val BROWN_WOOL: Block = get("brown_wool")
    @JvmField
    public val GREEN_WOOL: Block = get("green_wool")
    @JvmField
    public val RED_WOOL: Block = get("red_wool")
    @JvmField
    public val BLACK_WOOL: Block = get("black_wool")
    @JvmField
    public val MOVING_PISTON: Block = get("moving_piston")
    @JvmField
    public val DANDELION: Block = get("dandelion")
    @JvmField
    public val POPPY: Block = get("poppy")
    @JvmField
    public val BLUE_ORCHID: Block = get("blue_orchid")
    @JvmField
    public val ALLIUM: Block = get("allium")
    @JvmField
    public val AZURE_BLUET: Block = get("azure_bluet")
    @JvmField
    public val RED_TULIP: Block = get("red_tulip")
    @JvmField
    public val ORANGE_TULIP: Block = get("orange_tulip")
    @JvmField
    public val WHITE_TULIP: Block = get("white_tulip")
    @JvmField
    public val PINK_TULIP: Block = get("pink_tulip")
    @JvmField
    public val OXEYE_DAISY: Block = get("oxeye_daisy")
    @JvmField
    public val CORNFLOWER: Block = get("cornflower")
    @JvmField
    public val WITHER_ROSE: Block = get("wither_rose")
    @JvmField
    public val LILY_OF_THE_VALLEY: Block = get("lily_of_the_valley")
    @JvmField
    public val BROWN_MUSHROOM: Block = get("brown_mushroom")
    @JvmField
    public val RED_MUSHROOM: Block = get("red_mushroom")
    @JvmField
    public val GOLD_BLOCK: Block = get("gold_block")
    @JvmField
    public val IRON_BLOCK: Block = get("iron_block")
    @JvmField
    public val BRICKS: Block = get("bricks")
    @JvmField
    public val TNT: Block = get("tnt")
    @JvmField
    public val BOOKSHELF: Block = get("bookshelf")
    @JvmField
    public val MOSSY_COBBLESTONE: Block = get("mossy_cobblestone")
    @JvmField
    public val OBSIDIAN: Block = get("obsidian")
    @JvmField
    public val TORCH: Block = get("torch")
    @JvmField
    public val WALL_TORCH: Block = get("wall_torch")
    @JvmField
    public val FIRE: Block = get("fire")
    @JvmField
    public val SOUL_FIRE: Block = get("soul_fire")
    @JvmField
    public val SPAWNER: Block = get("spawner")
    @JvmField
    public val OAK_STAIRS: Block = get("oak_stairs")
    @JvmField
    public val CHEST: Block = get("chest")
    @JvmField
    public val REDSTONE_WIRE: Block = get("redstone_wire")
    @JvmField
    public val DIAMOND_ORE: Block = get("diamond_ore")
    @JvmField
    public val DEEPSLATE_DIAMOND_ORE: Block = get("deepslate_diamond_ore")
    @JvmField
    public val DIAMOND_BLOCK: Block = get("diamond_block")
    @JvmField
    public val CRAFTING_TABLE: Block = get("crafting_table")
    @JvmField
    public val WHEAT: Block = get("wheat")
    @JvmField
    public val FARMLAND: Block = get("farmland")
    @JvmField
    public val FURNACE: Block = get("furnace")
    @JvmField
    public val OAK_SIGN: Block = get("oak_sign")
    @JvmField
    public val SPRUCE_SIGN: Block = get("spruce_sign")
    @JvmField
    public val BIRCH_SIGN: Block = get("birch_sign")
    @JvmField
    public val ACACIA_SIGN: Block = get("acacia_sign")
    @JvmField
    public val JUNGLE_SIGN: Block = get("jungle_sign")
    @JvmField
    public val DARK_OAK_SIGN: Block = get("dark_oak_sign")
    @JvmField
    public val OAK_DOOR: Block = get("oak_door")
    @JvmField
    public val LADDER: Block = get("ladder")
    @JvmField
    public val RAIL: Block = get("rail")
    @JvmField
    public val COBBLESTONE_STAIRS: Block = get("cobblestone_stairs")
    @JvmField
    public val OAK_WALL_SIGN: Block = get("oak_wall_sign")
    @JvmField
    public val SPRUCE_WALL_SIGN: Block = get("spruce_wall_sign")
    @JvmField
    public val BIRCH_WALL_SIGN: Block = get("birch_wall_sign")
    @JvmField
    public val ACACIA_WALL_SIGN: Block = get("acacia_wall_sign")
    @JvmField
    public val JUNGLE_WALL_SIGN: Block = get("jungle_wall_sign")
    @JvmField
    public val DARK_OAK_WALL_SIGN: Block = get("dark_oak_wall_sign")
    @JvmField
    public val LEVER: Block = get("lever")
    @JvmField
    public val STONE_PRESSURE_PLATE: Block = get("stone_pressure_plate")
    @JvmField
    public val IRON_DOOR: Block = get("iron_door")
    @JvmField
    public val OAK_PRESSURE_PLATE: Block = get("oak_pressure_plate")
    @JvmField
    public val SPRUCE_PRESSURE_PLATE: Block = get("spruce_pressure_plate")
    @JvmField
    public val BIRCH_PRESSURE_PLATE: Block = get("birch_pressure_plate")
    @JvmField
    public val JUNGLE_PRESSURE_PLATE: Block = get("jungle_pressure_plate")
    @JvmField
    public val ACACIA_PRESSURE_PLATE: Block = get("acacia_pressure_plate")
    @JvmField
    public val DARK_OAK_PRESSURE_PLATE: Block = get("dark_oak_pressure_plate")
    @JvmField
    public val REDSTONE_ORE: Block = get("redstone_ore")
    @JvmField
    public val DEEPSLATE_REDSTONE_ORE: Block = get("deepslate_redstone_ore")
    @JvmField
    public val REDSTONE_TORCH: Block = get("redstone_torch")
    @JvmField
    public val REDSTONE_WALL_TORCH: Block = get("redstone_wall_torch")
    @JvmField
    public val STONE_BUTTON: Block = get("stone_button")
    @JvmField
    public val SNOW: Block = get("snow")
    @JvmField
    public val ICE: Block = get("ice")
    @JvmField
    public val SNOW_BLOCK: Block = get("snow_block")
    @JvmField
    public val CACTUS: Block = get("cactus")
    @JvmField
    public val CLAY: Block = get("clay")
    @JvmField
    public val SUGAR_CANE: Block = get("sugar_cane")
    @JvmField
    public val JUKEBOX: Block = get("jukebox")
    @JvmField
    public val OAK_FENCE: Block = get("oak_fence")
    @JvmField
    public val PUMPKIN: Block = get("pumpkin")
    @JvmField
    public val NETHERRACK: Block = get("netherrack")
    @JvmField
    public val SOUL_SAND: Block = get("soul_sand")
    @JvmField
    public val SOUL_SOIL: Block = get("soul_soil")
    @JvmField
    public val BASALT: Block = get("basalt")
    @JvmField
    public val POLISHED_BASALT: Block = get("polished_basalt")
    @JvmField
    public val SOUL_TORCH: Block = get("soul_torch")
    @JvmField
    public val SOUL_WALL_TORCH: Block = get("soul_wall_torch")
    @JvmField
    public val GLOWSTONE: Block = get("glowstone")
    @JvmField
    public val NETHER_PORTAL: Block = get("nether_portal")
    @JvmField
    public val CARVED_PUMPKIN: Block = get("carved_pumpkin")
    @JvmField
    public val JACK_O_LANTERN: Block = get("jack_o_lantern")
    @JvmField
    public val CAKE: Block = get("cake")
    @JvmField
    public val REPEATER: Block = get("repeater")
    @JvmField
    public val WHITE_STAINED_GLASS: Block = get("white_stained_glass")
    @JvmField
    public val ORANGE_STAINED_GLASS: Block = get("orange_stained_glass")
    @JvmField
    public val MAGENTA_STAINED_GLASS: Block = get("magenta_stained_glass")
    @JvmField
    public val LIGHT_BLUE_STAINED_GLASS: Block = get("light_blue_stained_glass")
    @JvmField
    public val YELLOW_STAINED_GLASS: Block = get("yellow_stained_glass")
    @JvmField
    public val LIME_STAINED_GLASS: Block = get("lime_stained_glass")
    @JvmField
    public val PINK_STAINED_GLASS: Block = get("pink_stained_glass")
    @JvmField
    public val GRAY_STAINED_GLASS: Block = get("gray_stained_glass")
    @JvmField
    public val LIGHT_GRAY_STAINED_GLASS: Block = get("light_gray_stained_glass")
    @JvmField
    public val CYAN_STAINED_GLASS: Block = get("cyan_stained_glass")
    @JvmField
    public val PURPLE_STAINED_GLASS: Block = get("purple_stained_glass")
    @JvmField
    public val BLUE_STAINED_GLASS: Block = get("blue_stained_glass")
    @JvmField
    public val BROWN_STAINED_GLASS: Block = get("brown_stained_glass")
    @JvmField
    public val GREEN_STAINED_GLASS: Block = get("green_stained_glass")
    @JvmField
    public val RED_STAINED_GLASS: Block = get("red_stained_glass")
    @JvmField
    public val BLACK_STAINED_GLASS: Block = get("black_stained_glass")
    @JvmField
    public val OAK_TRAPDOOR: Block = get("oak_trapdoor")
    @JvmField
    public val SPRUCE_TRAPDOOR: Block = get("spruce_trapdoor")
    @JvmField
    public val BIRCH_TRAPDOOR: Block = get("birch_trapdoor")
    @JvmField
    public val JUNGLE_TRAPDOOR: Block = get("jungle_trapdoor")
    @JvmField
    public val ACACIA_TRAPDOOR: Block = get("acacia_trapdoor")
    @JvmField
    public val DARK_OAK_TRAPDOOR: Block = get("dark_oak_trapdoor")
    @JvmField
    public val STONE_BRICKS: Block = get("stone_bricks")
    @JvmField
    public val MOSSY_STONE_BRICKS: Block = get("mossy_stone_bricks")
    @JvmField
    public val CRACKED_STONE_BRICKS: Block = get("cracked_stone_bricks")
    @JvmField
    public val CHISELED_STONE_BRICKS: Block = get("chiseled_stone_bricks")
    @JvmField
    public val INFESTED_STONE: Block = get("infested_stone")
    @JvmField
    public val INFESTED_COBBLESTONE: Block = get("infested_cobblestone")
    @JvmField
    public val INFESTED_STONE_BRICKS: Block = get("infested_stone_bricks")
    @JvmField
    public val INFESTED_MOSSY_STONE_BRICKS: Block = get("infested_mossy_stone_bricks")
    @JvmField
    public val INFESTED_CRACKED_STONE_BRICKS: Block = get("infested_cracked_stone_bricks")
    @JvmField
    public val INFESTED_CHISELED_STONE_BRICKS: Block = get("infested_chiseled_stone_bricks")
    @JvmField
    public val BROWN_MUSHROOM_BLOCK: Block = get("brown_mushroom_block")
    @JvmField
    public val RED_MUSHROOM_BLOCK: Block = get("red_mushroom_block")
    @JvmField
    public val MUSHROOM_STEM: Block = get("mushroom_stem")
    @JvmField
    public val IRON_BARS: Block = get("iron_bars")
    @JvmField
    public val CHAIN: Block = get("chain")
    @JvmField
    public val GLASS_PANE: Block = get("glass_pane")
    @JvmField
    public val MELON: Block = get("melon")
    @JvmField
    public val ATTACHED_PUMPKIN_STEM: Block = get("attached_pumpkin_stem")
    @JvmField
    public val ATTACHED_MELON_STEM: Block = get("attached_melon_stem")
    @JvmField
    public val PUMPKIN_STEM: Block = get("pumpkin_stem")
    @JvmField
    public val MELON_STEM: Block = get("melon_stem")
    @JvmField
    public val VINE: Block = get("vine")
    @JvmField
    public val GLOW_LICHEN: Block = get("glow_lichen")
    @JvmField
    public val OAK_FENCE_GATE: Block = get("oak_fence_gate")
    @JvmField
    public val BRICK_STAIRS: Block = get("brick_stairs")
    @JvmField
    public val STONE_BRICK_STAIRS: Block = get("stone_brick_stairs")
    @JvmField
    public val MYCELIUM: Block = get("mycelium")
    @JvmField
    public val LILY_PAD: Block = get("lily_pad")
    @JvmField
    public val NETHER_BRICKS: Block = get("nether_bricks")
    @JvmField
    public val NETHER_BRICK_FENCE: Block = get("nether_brick_fence")
    @JvmField
    public val NETHER_BRICK_STAIRS: Block = get("nether_brick_stairs")
    @JvmField
    public val NETHER_WART: Block = get("nether_wart")
    @JvmField
    public val ENCHANTING_TABLE: Block = get("enchanting_table")
    @JvmField
    public val BREWING_STAND: Block = get("brewing_stand")
    @JvmField
    public val CAULDRON: Block = get("cauldron")
    @JvmField
    public val WATER_CAULDRON: Block = get("water_cauldron")
    @JvmField
    public val LAVA_CAULDRON: Block = get("lava_cauldron")
    @JvmField
    public val POWDER_SNOW_CAULDRON: Block = get("powder_snow_cauldron")
    @JvmField
    public val END_PORTAL: Block = get("end_portal")
    @JvmField
    public val END_PORTAL_FRAME: Block = get("end_portal_frame")
    @JvmField
    public val END_STONE: Block = get("end_stone")
    @JvmField
    public val DRAGON_EGG: Block = get("dragon_egg")
    @JvmField
    public val REDSTONE_LAMP: Block = get("redstone_lamp")
    @JvmField
    public val COCOA: Block = get("cocoa")
    @JvmField
    public val SANDSTONE_STAIRS: Block = get("sandstone_stairs")
    @JvmField
    public val EMERALD_ORE: Block = get("emerald_ore")
    @JvmField
    public val DEEPSLATE_EMERALD_ORE: Block = get("deepslate_emerald_ore")
    @JvmField
    public val ENDER_CHEST: Block = get("ender_chest")
    @JvmField
    public val TRIPWIRE_HOOK: Block = get("tripwire_hook")
    @JvmField
    public val TRIPWIRE: Block = get("tripwire")
    @JvmField
    public val EMERALD_BLOCK: Block = get("emerald_block")
    @JvmField
    public val SPRUCE_STAIRS: Block = get("spruce_stairs")
    @JvmField
    public val BIRCH_STAIRS: Block = get("birch_stairs")
    @JvmField
    public val JUNGLE_STAIRS: Block = get("jungle_stairs")
    @JvmField
    public val COMMAND_BLOCK: Block = get("command_block")
    @JvmField
    public val BEACON: Block = get("beacon")
    @JvmField
    public val COBBLESTONE_WALL: Block = get("cobblestone_wall")
    @JvmField
    public val MOSSY_COBBLESTONE_WALL: Block = get("mossy_cobblestone_wall")
    @JvmField
    public val FLOWER_POT: Block = get("flower_pot")
    @JvmField
    public val POTTED_OAK_SAPLING: Block = get("potted_oak_sapling")
    @JvmField
    public val POTTED_SPRUCE_SAPLING: Block = get("potted_spruce_sapling")
    @JvmField
    public val POTTED_BIRCH_SAPLING: Block = get("potted_birch_sapling")
    @JvmField
    public val POTTED_JUNGLE_SAPLING: Block = get("potted_jungle_sapling")
    @JvmField
    public val POTTED_ACACIA_SAPLING: Block = get("potted_acacia_sapling")
    @JvmField
    public val POTTED_DARK_OAK_SAPLING: Block = get("potted_dark_oak_sapling")
    @JvmField
    public val POTTED_FERN: Block = get("potted_fern")
    @JvmField
    public val POTTED_DANDELION: Block = get("potted_dandelion")
    @JvmField
    public val POTTED_POPPY: Block = get("potted_poppy")
    @JvmField
    public val POTTED_BLUE_ORCHID: Block = get("potted_blue_orchid")
    @JvmField
    public val POTTED_ALLIUM: Block = get("potted_allium")
    @JvmField
    public val POTTED_AZURE_BLUET: Block = get("potted_azure_bluet")
    @JvmField
    public val POTTED_RED_TULIP: Block = get("potted_red_tulip")
    @JvmField
    public val POTTED_ORANGE_TULIP: Block = get("potted_orange_tulip")
    @JvmField
    public val POTTED_WHITE_TULIP: Block = get("potted_white_tulip")
    @JvmField
    public val POTTED_PINK_TULIP: Block = get("potted_pink_tulip")
    @JvmField
    public val POTTED_OXEYE_DAISY: Block = get("potted_oxeye_daisy")
    @JvmField
    public val POTTED_CORNFLOWER: Block = get("potted_cornflower")
    @JvmField
    public val POTTED_LILY_OF_THE_VALLEY: Block = get("potted_lily_of_the_valley")
    @JvmField
    public val POTTED_WITHER_ROSE: Block = get("potted_wither_rose")
    @JvmField
    public val POTTED_RED_MUSHROOM: Block = get("potted_red_mushroom")
    @JvmField
    public val POTTED_BROWN_MUSHROOM: Block = get("potted_brown_mushroom")
    @JvmField
    public val POTTED_DEAD_BUSH: Block = get("potted_dead_bush")
    @JvmField
    public val POTTED_CACTUS: Block = get("potted_cactus")
    @JvmField
    public val CARROTS: Block = get("carrots")
    @JvmField
    public val POTATOES: Block = get("potatoes")
    @JvmField
    public val OAK_BUTTON: Block = get("oak_button")
    @JvmField
    public val SPRUCE_BUTTON: Block = get("spruce_button")
    @JvmField
    public val BIRCH_BUTTON: Block = get("birch_button")
    @JvmField
    public val JUNGLE_BUTTON: Block = get("jungle_button")
    @JvmField
    public val ACACIA_BUTTON: Block = get("acacia_button")
    @JvmField
    public val DARK_OAK_BUTTON: Block = get("dark_oak_button")
    @JvmField
    public val SKELETON_SKULL: Block = get("skeleton_skull")
    @JvmField
    public val SKELETON_WALL_SKULL: Block = get("skeleton_wall_skull")
    @JvmField
    public val WITHER_SKELETON_SKULL: Block = get("wither_skeleton_skull")
    @JvmField
    public val WITHER_SKELETON_WALL_SKULL: Block = get("wither_skeleton_wall_skull")
    @JvmField
    public val ZOMBIE_HEAD: Block = get("zombie_head")
    @JvmField
    public val ZOMBIE_WALL_HEAD: Block = get("zombie_wall_head")
    @JvmField
    public val PLAYER_HEAD: Block = get("player_head")
    @JvmField
    public val PLAYER_WALL_HEAD: Block = get("player_wall_head")
    @JvmField
    public val CREEPER_HEAD: Block = get("creeper_head")
    @JvmField
    public val CREEPER_WALL_HEAD: Block = get("creeper_wall_head")
    @JvmField
    public val DRAGON_HEAD: Block = get("dragon_head")
    @JvmField
    public val DRAGON_WALL_HEAD: Block = get("dragon_wall_head")
    @JvmField
    public val ANVIL: Block = get("anvil")
    @JvmField
    public val CHIPPED_ANVIL: Block = get("chipped_anvil")
    @JvmField
    public val DAMAGED_ANVIL: Block = get("damaged_anvil")
    @JvmField
    public val TRAPPED_CHEST: Block = get("trapped_chest")
    @JvmField
    public val LIGHT_WEIGHTED_PRESSURE_PLATE: Block = get("light_weighted_pressure_plate")
    @JvmField
    public val HEAVY_WEIGHTED_PRESSURE_PLATE: Block = get("heavy_weighted_pressure_plate")
    @JvmField
    public val COMPARATOR: Block = get("comparator")
    @JvmField
    public val DAYLIGHT_DETECTOR: Block = get("daylight_detector")
    @JvmField
    public val REDSTONE_BLOCK: Block = get("redstone_block")
    @JvmField
    public val NETHER_QUARTZ_ORE: Block = get("nether_quartz_ore")
    @JvmField
    public val HOPPER: Block = get("hopper")
    @JvmField
    public val QUARTZ_BLOCK: Block = get("quartz_block")
    @JvmField
    public val CHISELED_QUARTZ_BLOCK: Block = get("chiseled_quartz_block")
    @JvmField
    public val QUARTZ_PILLAR: Block = get("quartz_pillar")
    @JvmField
    public val QUARTZ_STAIRS: Block = get("quartz_stairs")
    @JvmField
    public val ACTIVATOR_RAIL: Block = get("activator_rail")
    @JvmField
    public val DROPPER: Block = get("dropper")
    @JvmField
    public val WHITE_TERRACOTTA: Block = get("white_terracotta")
    @JvmField
    public val ORANGE_TERRACOTTA: Block = get("orange_terracotta")
    @JvmField
    public val MAGENTA_TERRACOTTA: Block = get("magenta_terracotta")
    @JvmField
    public val LIGHT_BLUE_TERRACOTTA: Block = get("light_blue_terracotta")
    @JvmField
    public val YELLOW_TERRACOTTA: Block = get("yellow_terracotta")
    @JvmField
    public val LIME_TERRACOTTA: Block = get("lime_terracotta")
    @JvmField
    public val PINK_TERRACOTTA: Block = get("pink_terracotta")
    @JvmField
    public val GRAY_TERRACOTTA: Block = get("gray_terracotta")
    @JvmField
    public val LIGHT_GRAY_TERRACOTTA: Block = get("light_gray_terracotta")
    @JvmField
    public val CYAN_TERRACOTTA: Block = get("cyan_terracotta")
    @JvmField
    public val PURPLE_TERRACOTTA: Block = get("purple_terracotta")
    @JvmField
    public val BLUE_TERRACOTTA: Block = get("blue_terracotta")
    @JvmField
    public val BROWN_TERRACOTTA: Block = get("brown_terracotta")
    @JvmField
    public val GREEN_TERRACOTTA: Block = get("green_terracotta")
    @JvmField
    public val RED_TERRACOTTA: Block = get("red_terracotta")
    @JvmField
    public val BLACK_TERRACOTTA: Block = get("black_terracotta")
    @JvmField
    public val WHITE_STAINED_GLASS_PANE: Block = get("white_stained_glass_pane")
    @JvmField
    public val ORANGE_STAINED_GLASS_PANE: Block = get("orange_stained_glass_pane")
    @JvmField
    public val MAGENTA_STAINED_GLASS_PANE: Block = get("magenta_stained_glass_pane")
    @JvmField
    public val LIGHT_BLUE_STAINED_GLASS_PANE: Block = get("light_blue_stained_glass_pane")
    @JvmField
    public val YELLOW_STAINED_GLASS_PANE: Block = get("yellow_stained_glass_pane")
    @JvmField
    public val LIME_STAINED_GLASS_PANE: Block = get("lime_stained_glass_pane")
    @JvmField
    public val PINK_STAINED_GLASS_PANE: Block = get("pink_stained_glass_pane")
    @JvmField
    public val GRAY_STAINED_GLASS_PANE: Block = get("gray_stained_glass_pane")
    @JvmField
    public val LIGHT_GRAY_STAINED_GLASS_PANE: Block = get("light_gray_stained_glass_pane")
    @JvmField
    public val CYAN_STAINED_GLASS_PANE: Block = get("cyan_stained_glass_pane")
    @JvmField
    public val PURPLE_STAINED_GLASS_PANE: Block = get("purple_stained_glass_pane")
    @JvmField
    public val BLUE_STAINED_GLASS_PANE: Block = get("blue_stained_glass_pane")
    @JvmField
    public val BROWN_STAINED_GLASS_PANE: Block = get("brown_stained_glass_pane")
    @JvmField
    public val GREEN_STAINED_GLASS_PANE: Block = get("green_stained_glass_pane")
    @JvmField
    public val RED_STAINED_GLASS_PANE: Block = get("red_stained_glass_pane")
    @JvmField
    public val BLACK_STAINED_GLASS_PANE: Block = get("black_stained_glass_pane")
    @JvmField
    public val ACACIA_STAIRS: Block = get("acacia_stairs")
    @JvmField
    public val DARK_OAK_STAIRS: Block = get("dark_oak_stairs")
    @JvmField
    public val SLIME_BLOCK: Block = get("slime_block")
    @JvmField
    public val BARRIER: Block = get("barrier")
    @JvmField
    public val LIGHT: Block = get("light")
    @JvmField
    public val IRON_TRAPDOOR: Block = get("iron_trapdoor")
    @JvmField
    public val PRISMARINE: Block = get("prismarine")
    @JvmField
    public val PRISMARINE_BRICKS: Block = get("prismarine_bricks")
    @JvmField
    public val DARK_PRISMARINE: Block = get("dark_prismarine")
    @JvmField
    public val PRISMARINE_STAIRS: Block = get("prismarine_stairs")
    @JvmField
    public val PRISMARINE_BRICK_STAIRS: Block = get("prismarine_brick_stairs")
    @JvmField
    public val DARK_PRISMARINE_STAIRS: Block = get("dark_prismarine_stairs")
    @JvmField
    public val PRISMARINE_SLAB: Block = get("prismarine_slab")
    @JvmField
    public val PRISMARINE_BRICK_SLAB: Block = get("prismarine_brick_slab")
    @JvmField
    public val DARK_PRISMARINE_SLAB: Block = get("dark_prismarine_slab")
    @JvmField
    public val SEA_LANTERN: Block = get("sea_lantern")
    @JvmField
    public val HAY_BLOCK: Block = get("hay_block")
    @JvmField
    public val WHITE_CARPET: Block = get("white_carpet")
    @JvmField
    public val ORANGE_CARPET: Block = get("orange_carpet")
    @JvmField
    public val MAGENTA_CARPET: Block = get("magenta_carpet")
    @JvmField
    public val LIGHT_BLUE_CARPET: Block = get("light_blue_carpet")
    @JvmField
    public val YELLOW_CARPET: Block = get("yellow_carpet")
    @JvmField
    public val LIME_CARPET: Block = get("lime_carpet")
    @JvmField
    public val PINK_CARPET: Block = get("pink_carpet")
    @JvmField
    public val GRAY_CARPET: Block = get("gray_carpet")
    @JvmField
    public val LIGHT_GRAY_CARPET: Block = get("light_gray_carpet")
    @JvmField
    public val CYAN_CARPET: Block = get("cyan_carpet")
    @JvmField
    public val PURPLE_CARPET: Block = get("purple_carpet")
    @JvmField
    public val BLUE_CARPET: Block = get("blue_carpet")
    @JvmField
    public val BROWN_CARPET: Block = get("brown_carpet")
    @JvmField
    public val GREEN_CARPET: Block = get("green_carpet")
    @JvmField
    public val RED_CARPET: Block = get("red_carpet")
    @JvmField
    public val BLACK_CARPET: Block = get("black_carpet")
    @JvmField
    public val TERRACOTTA: Block = get("terracotta")
    @JvmField
    public val COAL_BLOCK: Block = get("coal_block")
    @JvmField
    public val PACKED_ICE: Block = get("packed_ice")
    @JvmField
    public val SUNFLOWER: Block = get("sunflower")
    @JvmField
    public val LILAC: Block = get("lilac")
    @JvmField
    public val ROSE_BUSH: Block = get("rose_bush")
    @JvmField
    public val PEONY: Block = get("peony")
    @JvmField
    public val TALL_GRASS: Block = get("tall_grass")
    @JvmField
    public val LARGE_FERN: Block = get("large_fern")
    @JvmField
    public val WHITE_BANNER: Block = get("white_banner")
    @JvmField
    public val ORANGE_BANNER: Block = get("orange_banner")
    @JvmField
    public val MAGENTA_BANNER: Block = get("magenta_banner")
    @JvmField
    public val LIGHT_BLUE_BANNER: Block = get("light_blue_banner")
    @JvmField
    public val YELLOW_BANNER: Block = get("yellow_banner")
    @JvmField
    public val LIME_BANNER: Block = get("lime_banner")
    @JvmField
    public val PINK_BANNER: Block = get("pink_banner")
    @JvmField
    public val GRAY_BANNER: Block = get("gray_banner")
    @JvmField
    public val LIGHT_GRAY_BANNER: Block = get("light_gray_banner")
    @JvmField
    public val CYAN_BANNER: Block = get("cyan_banner")
    @JvmField
    public val PURPLE_BANNER: Block = get("purple_banner")
    @JvmField
    public val BLUE_BANNER: Block = get("blue_banner")
    @JvmField
    public val BROWN_BANNER: Block = get("brown_banner")
    @JvmField
    public val GREEN_BANNER: Block = get("green_banner")
    @JvmField
    public val RED_BANNER: Block = get("red_banner")
    @JvmField
    public val BLACK_BANNER: Block = get("black_banner")
    @JvmField
    public val WHITE_WALL_BANNER: Block = get("white_wall_banner")
    @JvmField
    public val ORANGE_WALL_BANNER: Block = get("orange_wall_banner")
    @JvmField
    public val MAGENTA_WALL_BANNER: Block = get("magenta_wall_banner")
    @JvmField
    public val LIGHT_BLUE_WALL_BANNER: Block = get("light_blue_wall_banner")
    @JvmField
    public val YELLOW_WALL_BANNER: Block = get("yellow_wall_banner")
    @JvmField
    public val LIME_WALL_BANNER: Block = get("lime_wall_banner")
    @JvmField
    public val PINK_WALL_BANNER: Block = get("pink_wall_banner")
    @JvmField
    public val GRAY_WALL_BANNER: Block = get("gray_wall_banner")
    @JvmField
    public val LIGHT_GRAY_WALL_BANNER: Block = get("light_gray_wall_banner")
    @JvmField
    public val CYAN_WALL_BANNER: Block = get("cyan_wall_banner")
    @JvmField
    public val PURPLE_WALL_BANNER: Block = get("purple_wall_banner")
    @JvmField
    public val BLUE_WALL_BANNER: Block = get("blue_wall_banner")
    @JvmField
    public val BROWN_WALL_BANNER: Block = get("brown_wall_banner")
    @JvmField
    public val GREEN_WALL_BANNER: Block = get("green_wall_banner")
    @JvmField
    public val RED_WALL_BANNER: Block = get("red_wall_banner")
    @JvmField
    public val BLACK_WALL_BANNER: Block = get("black_wall_banner")
    @JvmField
    public val RED_SANDSTONE: Block = get("red_sandstone")
    @JvmField
    public val CHISELED_RED_SANDSTONE: Block = get("chiseled_red_sandstone")
    @JvmField
    public val CUT_RED_SANDSTONE: Block = get("cut_red_sandstone")
    @JvmField
    public val RED_SANDSTONE_STAIRS: Block = get("red_sandstone_stairs")
    @JvmField
    public val OAK_SLAB: Block = get("oak_slab")
    @JvmField
    public val SPRUCE_SLAB: Block = get("spruce_slab")
    @JvmField
    public val BIRCH_SLAB: Block = get("birch_slab")
    @JvmField
    public val JUNGLE_SLAB: Block = get("jungle_slab")
    @JvmField
    public val ACACIA_SLAB: Block = get("acacia_slab")
    @JvmField
    public val DARK_OAK_SLAB: Block = get("dark_oak_slab")
    @JvmField
    public val STONE_SLAB: Block = get("stone_slab")
    @JvmField
    public val SMOOTH_STONE_SLAB: Block = get("smooth_stone_slab")
    @JvmField
    public val SANDSTONE_SLAB: Block = get("sandstone_slab")
    @JvmField
    public val CUT_SANDSTONE_SLAB: Block = get("cut_sandstone_slab")
    @JvmField
    public val PETRIFIED_OAK_SLAB: Block = get("petrified_oak_slab")
    @JvmField
    public val COBBLESTONE_SLAB: Block = get("cobblestone_slab")
    @JvmField
    public val BRICK_SLAB: Block = get("brick_slab")
    @JvmField
    public val STONE_BRICK_SLAB: Block = get("stone_brick_slab")
    @JvmField
    public val NETHER_BRICK_SLAB: Block = get("nether_brick_slab")
    @JvmField
    public val QUARTZ_SLAB: Block = get("quartz_slab")
    @JvmField
    public val RED_SANDSTONE_SLAB: Block = get("red_sandstone_slab")
    @JvmField
    public val CUT_RED_SANDSTONE_SLAB: Block = get("cut_red_sandstone_slab")
    @JvmField
    public val PURPUR_SLAB: Block = get("purpur_slab")
    @JvmField
    public val SMOOTH_STONE: Block = get("smooth_stone")
    @JvmField
    public val SMOOTH_SANDSTONE: Block = get("smooth_sandstone")
    @JvmField
    public val SMOOTH_QUARTZ: Block = get("smooth_quartz")
    @JvmField
    public val SMOOTH_RED_SANDSTONE: Block = get("smooth_red_sandstone")
    @JvmField
    public val SPRUCE_FENCE_GATE: Block = get("spruce_fence_gate")
    @JvmField
    public val BIRCH_FENCE_GATE: Block = get("birch_fence_gate")
    @JvmField
    public val JUNGLE_FENCE_GATE: Block = get("jungle_fence_gate")
    @JvmField
    public val ACACIA_FENCE_GATE: Block = get("acacia_fence_gate")
    @JvmField
    public val DARK_OAK_FENCE_GATE: Block = get("dark_oak_fence_gate")
    @JvmField
    public val SPRUCE_FENCE: Block = get("spruce_fence")
    @JvmField
    public val BIRCH_FENCE: Block = get("birch_fence")
    @JvmField
    public val JUNGLE_FENCE: Block = get("jungle_fence")
    @JvmField
    public val ACACIA_FENCE: Block = get("acacia_fence")
    @JvmField
    public val DARK_OAK_FENCE: Block = get("dark_oak_fence")
    @JvmField
    public val SPRUCE_DOOR: Block = get("spruce_door")
    @JvmField
    public val BIRCH_DOOR: Block = get("birch_door")
    @JvmField
    public val JUNGLE_DOOR: Block = get("jungle_door")
    @JvmField
    public val ACACIA_DOOR: Block = get("acacia_door")
    @JvmField
    public val DARK_OAK_DOOR: Block = get("dark_oak_door")
    @JvmField
    public val END_ROD: Block = get("end_rod")
    @JvmField
    public val CHORUS_PLANT: Block = get("chorus_plant")
    @JvmField
    public val CHORUS_FLOWER: Block = get("chorus_flower")
    @JvmField
    public val PURPUR_BLOCK: Block = get("purpur_block")
    @JvmField
    public val PURPUR_PILLAR: Block = get("purpur_pillar")
    @JvmField
    public val PURPUR_STAIRS: Block = get("purpur_stairs")
    @JvmField
    public val END_STONE_BRICKS: Block = get("end_stone_bricks")
    @JvmField
    public val BEETROOTS: Block = get("beetroots")
    @JvmField
    public val DIRT_PATH: Block = get("dirt_path")
    @JvmField
    public val END_GATEWAY: Block = get("end_gateway")
    @JvmField
    public val REPEATING_COMMAND_BLOCK: Block = get("repeating_command_block")
    @JvmField
    public val CHAIN_COMMAND_BLOCK: Block = get("chain_command_block")
    @JvmField
    public val FROSTED_ICE: Block = get("frosted_ice")
    @JvmField
    public val MAGMA_BLOCK: Block = get("magma_block")
    @JvmField
    public val NETHER_WART_BLOCK: Block = get("nether_wart_block")
    @JvmField
    public val RED_NETHER_BRICKS: Block = get("red_nether_bricks")
    @JvmField
    public val BONE_BLOCK: Block = get("bone_block")
    @JvmField
    public val STRUCTURE_VOID: Block = get("structure_void")
    @JvmField
    public val OBSERVER: Block = get("observer")
    @JvmField
    public val SHULKER_BOX: Block = get("shulker_box")
    @JvmField
    public val WHITE_SHULKER_BOX: Block = get("white_shulker_box")
    @JvmField
    public val ORANGE_SHULKER_BOX: Block = get("orange_shulker_box")
    @JvmField
    public val MAGENTA_SHULKER_BOX: Block = get("magenta_shulker_box")
    @JvmField
    public val LIGHT_BLUE_SHULKER_BOX: Block = get("light_blue_shulker_box")
    @JvmField
    public val YELLOW_SHULKER_BOX: Block = get("yellow_shulker_box")
    @JvmField
    public val LIME_SHULKER_BOX: Block = get("lime_shulker_box")
    @JvmField
    public val PINK_SHULKER_BOX: Block = get("pink_shulker_box")
    @JvmField
    public val GRAY_SHULKER_BOX: Block = get("gray_shulker_box")
    @JvmField
    public val LIGHT_GRAY_SHULKER_BOX: Block = get("light_gray_shulker_box")
    @JvmField
    public val CYAN_SHULKER_BOX: Block = get("cyan_shulker_box")
    @JvmField
    public val PURPLE_SHULKER_BOX: Block = get("purple_shulker_box")
    @JvmField
    public val BLUE_SHULKER_BOX: Block = get("blue_shulker_box")
    @JvmField
    public val BROWN_SHULKER_BOX: Block = get("brown_shulker_box")
    @JvmField
    public val GREEN_SHULKER_BOX: Block = get("green_shulker_box")
    @JvmField
    public val RED_SHULKER_BOX: Block = get("red_shulker_box")
    @JvmField
    public val BLACK_SHULKER_BOX: Block = get("black_shulker_box")
    @JvmField
    public val WHITE_GLAZED_TERRACOTTA: Block = get("white_glazed_terracotta")
    @JvmField
    public val ORANGE_GLAZED_TERRACOTTA: Block = get("orange_glazed_terracotta")
    @JvmField
    public val MAGENTA_GLAZED_TERRACOTTA: Block = get("magenta_glazed_terracotta")
    @JvmField
    public val LIGHT_BLUE_GLAZED_TERRACOTTA: Block = get("light_blue_glazed_terracotta")
    @JvmField
    public val YELLOW_GLAZED_TERRACOTTA: Block = get("yellow_glazed_terracotta")
    @JvmField
    public val LIME_GLAZED_TERRACOTTA: Block = get("lime_glazed_terracotta")
    @JvmField
    public val PINK_GLAZED_TERRACOTTA: Block = get("pink_glazed_terracotta")
    @JvmField
    public val GRAY_GLAZED_TERRACOTTA: Block = get("gray_glazed_terracotta")
    @JvmField
    public val LIGHT_GRAY_GLAZED_TERRACOTTA: Block = get("light_gray_glazed_terracotta")
    @JvmField
    public val CYAN_GLAZED_TERRACOTTA: Block = get("cyan_glazed_terracotta")
    @JvmField
    public val PURPLE_GLAZED_TERRACOTTA: Block = get("purple_glazed_terracotta")
    @JvmField
    public val BLUE_GLAZED_TERRACOTTA: Block = get("blue_glazed_terracotta")
    @JvmField
    public val BROWN_GLAZED_TERRACOTTA: Block = get("brown_glazed_terracotta")
    @JvmField
    public val GREEN_GLAZED_TERRACOTTA: Block = get("green_glazed_terracotta")
    @JvmField
    public val RED_GLAZED_TERRACOTTA: Block = get("red_glazed_terracotta")
    @JvmField
    public val BLACK_GLAZED_TERRACOTTA: Block = get("black_glazed_terracotta")
    @JvmField
    public val WHITE_CONCRETE: Block = get("white_concrete")
    @JvmField
    public val ORANGE_CONCRETE: Block = get("orange_concrete")
    @JvmField
    public val MAGENTA_CONCRETE: Block = get("magenta_concrete")
    @JvmField
    public val LIGHT_BLUE_CONCRETE: Block = get("light_blue_concrete")
    @JvmField
    public val YELLOW_CONCRETE: Block = get("yellow_concrete")
    @JvmField
    public val LIME_CONCRETE: Block = get("lime_concrete")
    @JvmField
    public val PINK_CONCRETE: Block = get("pink_concrete")
    @JvmField
    public val GRAY_CONCRETE: Block = get("gray_concrete")
    @JvmField
    public val LIGHT_GRAY_CONCRETE: Block = get("light_gray_concrete")
    @JvmField
    public val CYAN_CONCRETE: Block = get("cyan_concrete")
    @JvmField
    public val PURPLE_CONCRETE: Block = get("purple_concrete")
    @JvmField
    public val BLUE_CONCRETE: Block = get("blue_concrete")
    @JvmField
    public val BROWN_CONCRETE: Block = get("brown_concrete")
    @JvmField
    public val GREEN_CONCRETE: Block = get("green_concrete")
    @JvmField
    public val RED_CONCRETE: Block = get("red_concrete")
    @JvmField
    public val BLACK_CONCRETE: Block = get("black_concrete")
    @JvmField
    public val WHITE_CONCRETE_POWDER: Block = get("white_concrete_powder")
    @JvmField
    public val ORANGE_CONCRETE_POWDER: Block = get("orange_concrete_powder")
    @JvmField
    public val MAGENTA_CONCRETE_POWDER: Block = get("magenta_concrete_powder")
    @JvmField
    public val LIGHT_BLUE_CONCRETE_POWDER: Block = get("light_blue_concrete_powder")
    @JvmField
    public val YELLOW_CONCRETE_POWDER: Block = get("yellow_concrete_powder")
    @JvmField
    public val LIME_CONCRETE_POWDER: Block = get("lime_concrete_powder")
    @JvmField
    public val PINK_CONCRETE_POWDER: Block = get("pink_concrete_powder")
    @JvmField
    public val GRAY_CONCRETE_POWDER: Block = get("gray_concrete_powder")
    @JvmField
    public val LIGHT_GRAY_CONCRETE_POWDER: Block = get("light_gray_concrete_powder")
    @JvmField
    public val CYAN_CONCRETE_POWDER: Block = get("cyan_concrete_powder")
    @JvmField
    public val PURPLE_CONCRETE_POWDER: Block = get("purple_concrete_powder")
    @JvmField
    public val BLUE_CONCRETE_POWDER: Block = get("blue_concrete_powder")
    @JvmField
    public val BROWN_CONCRETE_POWDER: Block = get("brown_concrete_powder")
    @JvmField
    public val GREEN_CONCRETE_POWDER: Block = get("green_concrete_powder")
    @JvmField
    public val RED_CONCRETE_POWDER: Block = get("red_concrete_powder")
    @JvmField
    public val BLACK_CONCRETE_POWDER: Block = get("black_concrete_powder")
    @JvmField
    public val KELP: Block = get("kelp")
    @JvmField
    public val KELP_PLANT: Block = get("kelp_plant")
    @JvmField
    public val DRIED_KELP_BLOCK: Block = get("dried_kelp_block")
    @JvmField
    public val TURTLE_EGG: Block = get("turtle_egg")
    @JvmField
    public val DEAD_TUBE_CORAL_BLOCK: Block = get("dead_tube_coral_block")
    @JvmField
    public val DEAD_BRAIN_CORAL_BLOCK: Block = get("dead_brain_coral_block")
    @JvmField
    public val DEAD_BUBBLE_CORAL_BLOCK: Block = get("dead_bubble_coral_block")
    @JvmField
    public val DEAD_FIRE_CORAL_BLOCK: Block = get("dead_fire_coral_block")
    @JvmField
    public val DEAD_HORN_CORAL_BLOCK: Block = get("dead_horn_coral_block")
    @JvmField
    public val TUBE_CORAL_BLOCK: Block = get("tube_coral_block")
    @JvmField
    public val BRAIN_CORAL_BLOCK: Block = get("brain_coral_block")
    @JvmField
    public val BUBBLE_CORAL_BLOCK: Block = get("bubble_coral_block")
    @JvmField
    public val FIRE_CORAL_BLOCK: Block = get("fire_coral_block")
    @JvmField
    public val HORN_CORAL_BLOCK: Block = get("horn_coral_block")
    @JvmField
    public val DEAD_TUBE_CORAL: Block = get("dead_tube_coral")
    @JvmField
    public val DEAD_BRAIN_CORAL: Block = get("dead_brain_coral")
    @JvmField
    public val DEAD_BUBBLE_CORAL: Block = get("dead_bubble_coral")
    @JvmField
    public val DEAD_FIRE_CORAL: Block = get("dead_fire_coral")
    @JvmField
    public val DEAD_HORN_CORAL: Block = get("dead_horn_coral")
    @JvmField
    public val TUBE_CORAL: Block = get("tube_coral")
    @JvmField
    public val BRAIN_CORAL: Block = get("brain_coral")
    @JvmField
    public val BUBBLE_CORAL: Block = get("bubble_coral")
    @JvmField
    public val FIRE_CORAL: Block = get("fire_coral")
    @JvmField
    public val HORN_CORAL: Block = get("horn_coral")
    @JvmField
    public val DEAD_TUBE_CORAL_FAN: Block = get("dead_tube_coral_fan")
    @JvmField
    public val DEAD_BRAIN_CORAL_FAN: Block = get("dead_brain_coral_fan")
    @JvmField
    public val DEAD_BUBBLE_CORAL_FAN: Block = get("dead_bubble_coral_fan")
    @JvmField
    public val DEAD_FIRE_CORAL_FAN: Block = get("dead_fire_coral_fan")
    @JvmField
    public val DEAD_HORN_CORAL_FAN: Block = get("dead_horn_coral_fan")
    @JvmField
    public val TUBE_CORAL_FAN: Block = get("tube_coral_fan")
    @JvmField
    public val BRAIN_CORAL_FAN: Block = get("brain_coral_fan")
    @JvmField
    public val BUBBLE_CORAL_FAN: Block = get("bubble_coral_fan")
    @JvmField
    public val FIRE_CORAL_FAN: Block = get("fire_coral_fan")
    @JvmField
    public val HORN_CORAL_FAN: Block = get("horn_coral_fan")
    @JvmField
    public val DEAD_TUBE_CORAL_WALL_FAN: Block = get("dead_tube_coral_wall_fan")
    @JvmField
    public val DEAD_BRAIN_CORAL_WALL_FAN: Block = get("dead_brain_coral_wall_fan")
    @JvmField
    public val DEAD_BUBBLE_CORAL_WALL_FAN: Block = get("dead_bubble_coral_wall_fan")
    @JvmField
    public val DEAD_FIRE_CORAL_WALL_FAN: Block = get("dead_fire_coral_wall_fan")
    @JvmField
    public val DEAD_HORN_CORAL_WALL_FAN: Block = get("dead_horn_coral_wall_fan")
    @JvmField
    public val TUBE_CORAL_WALL_FAN: Block = get("tube_coral_wall_fan")
    @JvmField
    public val BRAIN_CORAL_WALL_FAN: Block = get("brain_coral_wall_fan")
    @JvmField
    public val BUBBLE_CORAL_WALL_FAN: Block = get("bubble_coral_wall_fan")
    @JvmField
    public val FIRE_CORAL_WALL_FAN: Block = get("fire_coral_wall_fan")
    @JvmField
    public val HORN_CORAL_WALL_FAN: Block = get("horn_coral_wall_fan")
    @JvmField
    public val SEA_PICKLE: Block = get("sea_pickle")
    @JvmField
    public val BLUE_ICE: Block = get("blue_ice")
    @JvmField
    public val CONDUIT: Block = get("conduit")
    @JvmField
    public val BAMBOO_SAPLING: Block = get("bamboo_sapling")
    @JvmField
    public val BAMBOO: Block = get("bamboo")
    @JvmField
    public val POTTED_BAMBOO: Block = get("potted_bamboo")
    @JvmField
    public val VOID_AIR: Block = get("void_air")
    @JvmField
    public val CAVE_AIR: Block = get("cave_air")
    @JvmField
    public val BUBBLE_COLUMN: Block = get("bubble_column")
    @JvmField
    public val POLISHED_GRANITE_STAIRS: Block = get("polished_granite_stairs")
    @JvmField
    public val SMOOTH_RED_SANDSTONE_STAIRS: Block = get("smooth_red_sandstone_stairs")
    @JvmField
    public val MOSSY_STONE_BRICK_STAIRS: Block = get("mossy_stone_brick_stairs")
    @JvmField
    public val POLISHED_DIORITE_STAIRS: Block = get("polished_diorite_stairs")
    @JvmField
    public val MOSSY_COBBLESTONE_STAIRS: Block = get("mossy_cobblestone_stairs")
    @JvmField
    public val END_STONE_BRICK_STAIRS: Block = get("end_stone_brick_stairs")
    @JvmField
    public val STONE_STAIRS: Block = get("stone_stairs")
    @JvmField
    public val SMOOTH_SANDSTONE_STAIRS: Block = get("smooth_sandstone_stairs")
    @JvmField
    public val SMOOTH_QUARTZ_STAIRS: Block = get("smooth_quartz_stairs")
    @JvmField
    public val GRANITE_STAIRS: Block = get("granite_stairs")
    @JvmField
    public val ANDESITE_STAIRS: Block = get("andesite_stairs")
    @JvmField
    public val RED_NETHER_BRICK_STAIRS: Block = get("red_nether_brick_stairs")
    @JvmField
    public val POLISHED_ANDESITE_STAIRS: Block = get("polished_andesite_stairs")
    @JvmField
    public val DIORITE_STAIRS: Block = get("diorite_stairs")
    @JvmField
    public val POLISHED_GRANITE_SLAB: Block = get("polished_granite_slab")
    @JvmField
    public val SMOOTH_RED_SANDSTONE_SLAB: Block = get("smooth_red_sandstone_slab")
    @JvmField
    public val MOSSY_STONE_BRICK_SLAB: Block = get("mossy_stone_brick_slab")
    @JvmField
    public val POLISHED_DIORITE_SLAB: Block = get("polished_diorite_slab")
    @JvmField
    public val MOSSY_COBBLESTONE_SLAB: Block = get("mossy_cobblestone_slab")
    @JvmField
    public val END_STONE_BRICK_SLAB: Block = get("end_stone_brick_slab")
    @JvmField
    public val SMOOTH_SANDSTONE_SLAB: Block = get("smooth_sandstone_slab")
    @JvmField
    public val SMOOTH_QUARTZ_SLAB: Block = get("smooth_quartz_slab")
    @JvmField
    public val GRANITE_SLAB: Block = get("granite_slab")
    @JvmField
    public val ANDESITE_SLAB: Block = get("andesite_slab")
    @JvmField
    public val RED_NETHER_BRICK_SLAB: Block = get("red_nether_brick_slab")
    @JvmField
    public val POLISHED_ANDESITE_SLAB: Block = get("polished_andesite_slab")
    @JvmField
    public val DIORITE_SLAB: Block = get("diorite_slab")
    @JvmField
    public val BRICK_WALL: Block = get("brick_wall")
    @JvmField
    public val PRISMARINE_WALL: Block = get("prismarine_wall")
    @JvmField
    public val RED_SANDSTONE_WALL: Block = get("red_sandstone_wall")
    @JvmField
    public val MOSSY_STONE_BRICK_WALL: Block = get("mossy_stone_brick_wall")
    @JvmField
    public val GRANITE_WALL: Block = get("granite_wall")
    @JvmField
    public val STONE_BRICK_WALL: Block = get("stone_brick_wall")
    @JvmField
    public val NETHER_BRICK_WALL: Block = get("nether_brick_wall")
    @JvmField
    public val ANDESITE_WALL: Block = get("andesite_wall")
    @JvmField
    public val RED_NETHER_BRICK_WALL: Block = get("red_nether_brick_wall")
    @JvmField
    public val SANDSTONE_WALL: Block = get("sandstone_wall")
    @JvmField
    public val END_STONE_BRICK_WALL: Block = get("end_stone_brick_wall")
    @JvmField
    public val DIORITE_WALL: Block = get("diorite_wall")
    @JvmField
    public val SCAFFOLDING: Block = get("scaffolding")
    @JvmField
    public val LOOM: Block = get("loom")
    @JvmField
    public val BARREL: Block = get("barrel")
    @JvmField
    public val SMOKER: Block = get("smoker")
    @JvmField
    public val BLAST_FURNACE: Block = get("blast_furnace")
    @JvmField
    public val CARTOGRAPHY_TABLE: Block = get("cartography_table")
    @JvmField
    public val FLETCHING_TABLE: Block = get("fletching_table")
    @JvmField
    public val GRINDSTONE: Block = get("grindstone")
    @JvmField
    public val LECTERN: Block = get("lectern")
    @JvmField
    public val SMITHING_TABLE: Block = get("smithing_table")
    @JvmField
    public val STONECUTTER: Block = get("stonecutter")
    @JvmField
    public val BELL: Block = get("bell")
    @JvmField
    public val LANTERN: Block = get("lantern")
    @JvmField
    public val SOUL_LANTERN: Block = get("soul_lantern")
    @JvmField
    public val CAMPFIRE: Block = get("campfire")
    @JvmField
    public val SOUL_CAMPFIRE: Block = get("soul_campfire")
    @JvmField
    public val SWEET_BERRY_BUSH: Block = get("sweet_berry_bush")
    @JvmField
    public val WARPED_STEM: Block = get("warped_stem")
    @JvmField
    public val STRIPPED_WARPED_STEM: Block = get("stripped_warped_stem")
    @JvmField
    public val WARPED_HYPHAE: Block = get("warped_hyphae")
    @JvmField
    public val STRIPPED_WARPED_HYPHAE: Block = get("stripped_warped_hyphae")
    @JvmField
    public val WARPED_NYLIUM: Block = get("warped_nylium")
    @JvmField
    public val WARPED_FUNGUS: Block = get("warped_fungus")
    @JvmField
    public val WARPED_WART_BLOCK: Block = get("warped_wart_block")
    @JvmField
    public val WARPED_ROOTS: Block = get("warped_roots")
    @JvmField
    public val NETHER_SPROUTS: Block = get("nether_sprouts")
    @JvmField
    public val CRIMSON_STEM: Block = get("crimson_stem")
    @JvmField
    public val STRIPPED_CRIMSON_STEM: Block = get("stripped_crimson_stem")
    @JvmField
    public val CRIMSON_HYPHAE: Block = get("crimson_hyphae")
    @JvmField
    public val STRIPPED_CRIMSON_HYPHAE: Block = get("stripped_crimson_hyphae")
    @JvmField
    public val CRIMSON_NYLIUM: Block = get("crimson_nylium")
    @JvmField
    public val CRIMSON_FUNGUS: Block = get("crimson_fungus")
    @JvmField
    public val SHROOMLIGHT: Block = get("shroomlight")
    @JvmField
    public val WEEPING_VINES: Block = get("weeping_vines")
    @JvmField
    public val WEEPING_VINES_PLANT: Block = get("weeping_vines_plant")
    @JvmField
    public val TWISTING_VINES: Block = get("twisting_vines")
    @JvmField
    public val TWISTING_VINES_PLANT: Block = get("twisting_vines_plant")
    @JvmField
    public val CRIMSON_ROOTS: Block = get("crimson_roots")
    @JvmField
    public val CRIMSON_PLANKS: Block = get("crimson_planks")
    @JvmField
    public val WARPED_PLANKS: Block = get("warped_planks")
    @JvmField
    public val CRIMSON_SLAB: Block = get("crimson_slab")
    @JvmField
    public val WARPED_SLAB: Block = get("warped_slab")
    @JvmField
    public val CRIMSON_PRESSURE_PLATE: Block = get("crimson_pressure_plate")
    @JvmField
    public val WARPED_PRESSURE_PLATE: Block = get("warped_pressure_plate")
    @JvmField
    public val CRIMSON_FENCE: Block = get("crimson_fence")
    @JvmField
    public val WARPED_FENCE: Block = get("warped_fence")
    @JvmField
    public val CRIMSON_TRAPDOOR: Block = get("crimson_trapdoor")
    @JvmField
    public val WARPED_TRAPDOOR: Block = get("warped_trapdoor")
    @JvmField
    public val CRIMSON_FENCE_GATE: Block = get("crimson_fence_gate")
    @JvmField
    public val WARPED_FENCE_GATE: Block = get("warped_fence_gate")
    @JvmField
    public val CRIMSON_STAIRS: Block = get("crimson_stairs")
    @JvmField
    public val WARPED_STAIRS: Block = get("warped_stairs")
    @JvmField
    public val CRIMSON_BUTTON: Block = get("crimson_button")
    @JvmField
    public val WARPED_BUTTON: Block = get("warped_button")
    @JvmField
    public val CRIMSON_DOOR: Block = get("crimson_door")
    @JvmField
    public val WARPED_DOOR: Block = get("warped_door")
    @JvmField
    public val CRIMSON_SIGN: Block = get("crimson_sign")
    @JvmField
    public val WARPED_SIGN: Block = get("warped_sign")
    @JvmField
    public val CRIMSON_WALL_SIGN: Block = get("crimson_wall_sign")
    @JvmField
    public val WARPED_WALL_SIGN: Block = get("warped_wall_sign")
    @JvmField
    public val STRUCTURE_BLOCK: Block = get("structure_block")
    @JvmField
    public val JIGSAW: Block = get("jigsaw")
    @JvmField
    public val COMPOSTER: Block = get("composter")
    @JvmField
    public val TARGET: Block = get("target")
    @JvmField
    public val BEE_NEST: Block = get("bee_nest")
    @JvmField
    public val BEEHIVE: Block = get("beehive")
    @JvmField
    public val HONEY_BLOCK: Block = get("honey_block")
    @JvmField
    public val HONEYCOMB_BLOCK: Block = get("honeycomb_block")
    @JvmField
    public val NETHERITE_BLOCK: Block = get("netherite_block")
    @JvmField
    public val ANCIENT_DEBRIS: Block = get("ancient_debris")
    @JvmField
    public val CRYING_OBSIDIAN: Block = get("crying_obsidian")
    @JvmField
    public val RESPAWN_ANCHOR: Block = get("respawn_anchor")
    @JvmField
    public val POTTED_CRIMSON_FUNGUS: Block = get("potted_crimson_fungus")
    @JvmField
    public val POTTED_WARPED_FUNGUS: Block = get("potted_warped_fungus")
    @JvmField
    public val POTTED_CRIMSON_ROOTS: Block = get("potted_crimson_roots")
    @JvmField
    public val POTTED_WARPED_ROOTS: Block = get("potted_warped_roots")
    @JvmField
    public val LODESTONE: Block = get("lodestone")
    @JvmField
    public val BLACKSTONE: Block = get("blackstone")
    @JvmField
    public val BLACKSTONE_STAIRS: Block = get("blackstone_stairs")
    @JvmField
    public val BLACKSTONE_WALL: Block = get("blackstone_wall")
    @JvmField
    public val BLACKSTONE_SLAB: Block = get("blackstone_slab")
    @JvmField
    public val POLISHED_BLACKSTONE: Block = get("polished_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICKS: Block = get("polished_blackstone_bricks")
    @JvmField
    public val CRACKED_POLISHED_BLACKSTONE_BRICKS: Block = get("cracked_polished_blackstone_bricks")
    @JvmField
    public val CHISELED_POLISHED_BLACKSTONE: Block = get("chiseled_polished_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_SLAB: Block = get("polished_blackstone_brick_slab")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_STAIRS: Block = get("polished_blackstone_brick_stairs")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_WALL: Block = get("polished_blackstone_brick_wall")
    @JvmField
    public val GILDED_BLACKSTONE: Block = get("gilded_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_STAIRS: Block = get("polished_blackstone_stairs")
    @JvmField
    public val POLISHED_BLACKSTONE_SLAB: Block = get("polished_blackstone_slab")
    @JvmField
    public val POLISHED_BLACKSTONE_PRESSURE_PLATE: Block = get("polished_blackstone_pressure_plate")
    @JvmField
    public val POLISHED_BLACKSTONE_BUTTON: Block = get("polished_blackstone_button")
    @JvmField
    public val POLISHED_BLACKSTONE_WALL: Block = get("polished_blackstone_wall")
    @JvmField
    public val CHISELED_NETHER_BRICKS: Block = get("chiseled_nether_bricks")
    @JvmField
    public val CRACKED_NETHER_BRICKS: Block = get("cracked_nether_bricks")
    @JvmField
    public val QUARTZ_BRICKS: Block = get("quartz_bricks")
    @JvmField
    public val CANDLE: Block = get("candle")
    @JvmField
    public val WHITE_CANDLE: Block = get("white_candle")
    @JvmField
    public val ORANGE_CANDLE: Block = get("orange_candle")
    @JvmField
    public val MAGENTA_CANDLE: Block = get("magenta_candle")
    @JvmField
    public val LIGHT_BLUE_CANDLE: Block = get("light_blue_candle")
    @JvmField
    public val YELLOW_CANDLE: Block = get("yellow_candle")
    @JvmField
    public val LIME_CANDLE: Block = get("lime_candle")
    @JvmField
    public val PINK_CANDLE: Block = get("pink_candle")
    @JvmField
    public val GRAY_CANDLE: Block = get("gray_candle")
    @JvmField
    public val LIGHT_GRAY_CANDLE: Block = get("light_gray_candle")
    @JvmField
    public val CYAN_CANDLE: Block = get("cyan_candle")
    @JvmField
    public val PURPLE_CANDLE: Block = get("purple_candle")
    @JvmField
    public val BLUE_CANDLE: Block = get("blue_candle")
    @JvmField
    public val BROWN_CANDLE: Block = get("brown_candle")
    @JvmField
    public val GREEN_CANDLE: Block = get("green_candle")
    @JvmField
    public val RED_CANDLE: Block = get("red_candle")
    @JvmField
    public val BLACK_CANDLE: Block = get("black_candle")
    @JvmField
    public val CANDLE_CAKE: Block = get("candle_cake")
    @JvmField
    public val WHITE_CANDLE_CAKE: Block = get("white_candle_cake")
    @JvmField
    public val ORANGE_CANDLE_CAKE: Block = get("orange_candle_cake")
    @JvmField
    public val MAGENTA_CANDLE_CAKE: Block = get("magenta_candle_cake")
    @JvmField
    public val LIGHT_BLUE_CANDLE_CAKE: Block = get("light_blue_candle_cake")
    @JvmField
    public val YELLOW_CANDLE_CAKE: Block = get("yellow_candle_cake")
    @JvmField
    public val LIME_CANDLE_CAKE: Block = get("lime_candle_cake")
    @JvmField
    public val PINK_CANDLE_CAKE: Block = get("pink_candle_cake")
    @JvmField
    public val GRAY_CANDLE_CAKE: Block = get("gray_candle_cake")
    @JvmField
    public val LIGHT_GRAY_CANDLE_CAKE: Block = get("light_gray_candle_cake")
    @JvmField
    public val CYAN_CANDLE_CAKE: Block = get("cyan_candle_cake")
    @JvmField
    public val PURPLE_CANDLE_CAKE: Block = get("purple_candle_cake")
    @JvmField
    public val BLUE_CANDLE_CAKE: Block = get("blue_candle_cake")
    @JvmField
    public val BROWN_CANDLE_CAKE: Block = get("brown_candle_cake")
    @JvmField
    public val GREEN_CANDLE_CAKE: Block = get("green_candle_cake")
    @JvmField
    public val RED_CANDLE_CAKE: Block = get("red_candle_cake")
    @JvmField
    public val BLACK_CANDLE_CAKE: Block = get("black_candle_cake")
    @JvmField
    public val AMETHYST_BLOCK: Block = get("amethyst_block")
    @JvmField
    public val BUDDING_AMETHYST: Block = get("budding_amethyst")
    @JvmField
    public val AMETHYST_CLUSTER: Block = get("amethyst_cluster")
    @JvmField
    public val LARGE_AMETHYST_BUD: Block = get("large_amethyst_bud")
    @JvmField
    public val MEDIUM_AMETHYST_BUD: Block = get("medium_amethyst_bud")
    @JvmField
    public val SMALL_AMETHYST_BUD: Block = get("small_amethyst_bud")
    @JvmField
    public val TUFF: Block = get("tuff")
    @JvmField
    public val CALCITE: Block = get("calcite")
    @JvmField
    public val TINTED_GLASS: Block = get("tinted_glass")
    @JvmField
    public val POWDER_SNOW: Block = get("powder_snow")
    @JvmField
    public val SCULK_SENSOR: Block = get("sculk_sensor")
    @JvmField
    public val OXIDIZED_COPPER: Block = get("oxidized_copper")
    @JvmField
    public val WEATHERED_COPPER: Block = get("weathered_copper")
    @JvmField
    public val EXPOSED_COPPER: Block = get("exposed_copper")
    @JvmField
    public val COPPER_BLOCK: Block = get("copper_block")
    @JvmField
    public val COPPER_ORE: Block = get("copper_ore")
    @JvmField
    public val DEEPSLATE_COPPER_ORE: Block = get("deepslate_copper_ore")
    @JvmField
    public val OXIDIZED_CUT_COPPER: Block = get("oxidized_cut_copper")
    @JvmField
    public val WEATHERED_CUT_COPPER: Block = get("weathered_cut_copper")
    @JvmField
    public val EXPOSED_CUT_COPPER: Block = get("exposed_cut_copper")
    @JvmField
    public val CUT_COPPER: Block = get("cut_copper")
    @JvmField
    public val OXIDIZED_CUT_COPPER_STAIRS: Block = get("oxidized_cut_copper_stairs")
    @JvmField
    public val WEATHERED_CUT_COPPER_STAIRS: Block = get("weathered_cut_copper_stairs")
    @JvmField
    public val EXPOSED_CUT_COPPER_STAIRS: Block = get("exposed_cut_copper_stairs")
    @JvmField
    public val CUT_COPPER_STAIRS: Block = get("cut_copper_stairs")
    @JvmField
    public val OXIDIZED_CUT_COPPER_SLAB: Block = get("oxidized_cut_copper_slab")
    @JvmField
    public val WEATHERED_CUT_COPPER_SLAB: Block = get("weathered_cut_copper_slab")
    @JvmField
    public val EXPOSED_CUT_COPPER_SLAB: Block = get("exposed_cut_copper_slab")
    @JvmField
    public val CUT_COPPER_SLAB: Block = get("cut_copper_slab")
    @JvmField
    public val WAXED_COPPER_BLOCK: Block = get("waxed_copper_block")
    @JvmField
    public val WAXED_WEATHERED_COPPER: Block = get("waxed_weathered_copper")
    @JvmField
    public val WAXED_EXPOSED_COPPER: Block = get("waxed_exposed_copper")
    @JvmField
    public val WAXED_OXIDIZED_COPPER: Block = get("waxed_oxidized_copper")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER: Block = get("waxed_oxidized_cut_copper")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER: Block = get("waxed_weathered_cut_copper")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER: Block = get("waxed_exposed_cut_copper")
    @JvmField
    public val WAXED_CUT_COPPER: Block = get("waxed_cut_copper")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER_STAIRS: Block = get("waxed_oxidized_cut_copper_stairs")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER_STAIRS: Block = get("waxed_weathered_cut_copper_stairs")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER_STAIRS: Block = get("waxed_exposed_cut_copper_stairs")
    @JvmField
    public val WAXED_CUT_COPPER_STAIRS: Block = get("waxed_cut_copper_stairs")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER_SLAB: Block = get("waxed_oxidized_cut_copper_slab")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER_SLAB: Block = get("waxed_weathered_cut_copper_slab")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER_SLAB: Block = get("waxed_exposed_cut_copper_slab")
    @JvmField
    public val WAXED_CUT_COPPER_SLAB: Block = get("waxed_cut_copper_slab")
    @JvmField
    public val LIGHTNING_ROD: Block = get("lightning_rod")
    @JvmField
    public val POINTED_DRIPSTONE: Block = get("pointed_dripstone")
    @JvmField
    public val DRIPSTONE_BLOCK: Block = get("dripstone_block")
    @JvmField
    public val CAVE_VINES: Block = get("cave_vines")
    @JvmField
    public val CAVE_VINES_PLANT: Block = get("cave_vines_plant")
    @JvmField
    public val SPORE_BLOSSOM: Block = get("spore_blossom")
    @JvmField
    public val AZALEA: Block = get("azalea")
    @JvmField
    public val FLOWERING_AZALEA: Block = get("flowering_azalea")
    @JvmField
    public val MOSS_CARPET: Block = get("moss_carpet")
    @JvmField
    public val MOSS_BLOCK: Block = get("moss_block")
    @JvmField
    public val BIG_DRIPLEAF: Block = get("big_dripleaf")
    @JvmField
    public val BIG_DRIPLEAF_STEM: Block = get("big_dripleaf_stem")
    @JvmField
    public val SMALL_DRIPLEAF: Block = get("small_dripleaf")
    @JvmField
    public val HANGING_ROOTS: Block = get("hanging_roots")
    @JvmField
    public val ROOTED_DIRT: Block = get("rooted_dirt")
    @JvmField
    public val DEEPSLATE: Block = get("deepslate")
    @JvmField
    public val COBBLED_DEEPSLATE: Block = get("cobbled_deepslate")
    @JvmField
    public val COBBLED_DEEPSLATE_STAIRS: Block = get("cobbled_deepslate_stairs")
    @JvmField
    public val COBBLED_DEEPSLATE_SLAB: Block = get("cobbled_deepslate_slab")
    @JvmField
    public val COBBLED_DEEPSLATE_WALL: Block = get("cobbled_deepslate_wall")
    @JvmField
    public val POLISHED_DEEPSLATE: Block = get("polished_deepslate")
    @JvmField
    public val POLISHED_DEEPSLATE_STAIRS: Block = get("polished_deepslate_stairs")
    @JvmField
    public val POLISHED_DEEPSLATE_SLAB: Block = get("polished_deepslate_slab")
    @JvmField
    public val POLISHED_DEEPSLATE_WALL: Block = get("polished_deepslate_wall")
    @JvmField
    public val DEEPSLATE_TILES: Block = get("deepslate_tiles")
    @JvmField
    public val DEEPSLATE_TILE_STAIRS: Block = get("deepslate_tile_stairs")
    @JvmField
    public val DEEPSLATE_TILE_SLAB: Block = get("deepslate_tile_slab")
    @JvmField
    public val DEEPSLATE_TILE_WALL: Block = get("deepslate_tile_wall")
    @JvmField
    public val DEEPSLATE_BRICKS: Block = get("deepslate_bricks")
    @JvmField
    public val DEEPSLATE_BRICK_STAIRS: Block = get("deepslate_brick_stairs")
    @JvmField
    public val DEEPSLATE_BRICK_SLAB: Block = get("deepslate_brick_slab")
    @JvmField
    public val DEEPSLATE_BRICK_WALL: Block = get("deepslate_brick_wall")
    @JvmField
    public val CHISELED_DEEPSLATE: Block = get("chiseled_deepslate")
    @JvmField
    public val CRACKED_DEEPSLATE_BRICKS: Block = get("cracked_deepslate_bricks")
    @JvmField
    public val CRACKED_DEEPSLATE_TILES: Block = get("cracked_deepslate_tiles")
    @JvmField
    public val INFESTED_DEEPSLATE: Block = get("infested_deepslate")
    @JvmField
    public val SMOOTH_BASALT: Block = get("smooth_basalt")
    @JvmField
    public val RAW_IRON_BLOCK: Block = get("raw_iron_block")
    @JvmField
    public val RAW_COPPER_BLOCK: Block = get("raw_copper_block")
    @JvmField
    public val RAW_GOLD_BLOCK: Block = get("raw_gold_block")
    @JvmField
    public val POTTED_AZALEA: Block = get("potted_azalea_bush")
    @JvmField
    public val POTTED_FLOWERING_AZALEA: Block = get("potted_flowering_azalea_bush")

    // @formatter:on
    @JvmStatic
    private fun get(key: String): Block = Registries.BLOCK[Key.key(key)]!!
}

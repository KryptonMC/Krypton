/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.block

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(Block::class)
public object Blocks {

    // @formatter:off
    @JvmField
    public val AIR: RegistryReference<Block> = of("air")
    @JvmField
    public val STONE: RegistryReference<Block> = of("stone")
    @JvmField
    public val GRANITE: RegistryReference<Block> = of("granite")
    @JvmField
    public val POLISHED_GRANITE: RegistryReference<Block> = of("polished_granite")
    @JvmField
    public val DIORITE: RegistryReference<Block> = of("diorite")
    @JvmField
    public val POLISHED_DIORITE: RegistryReference<Block> = of("polished_diorite")
    @JvmField
    public val ANDESITE: RegistryReference<Block> = of("andesite")
    @JvmField
    public val POLISHED_ANDESITE: RegistryReference<Block> = of("polished_andesite")
    @JvmField
    public val GRASS_BLOCK: RegistryReference<Block> = of("grass_block")
    @JvmField
    public val DIRT: RegistryReference<Block> = of("dirt")
    @JvmField
    public val COARSE_DIRT: RegistryReference<Block> = of("coarse_dirt")
    @JvmField
    public val PODZOL: RegistryReference<Block> = of("podzol")
    @JvmField
    public val COBBLESTONE: RegistryReference<Block> = of("cobblestone")
    @JvmField
    public val OAK_PLANKS: RegistryReference<Block> = of("oak_planks")
    @JvmField
    public val SPRUCE_PLANKS: RegistryReference<Block> = of("spruce_planks")
    @JvmField
    public val BIRCH_PLANKS: RegistryReference<Block> = of("birch_planks")
    @JvmField
    public val JUNGLE_PLANKS: RegistryReference<Block> = of("jungle_planks")
    @JvmField
    public val ACACIA_PLANKS: RegistryReference<Block> = of("acacia_planks")
    @JvmField
    public val DARK_OAK_PLANKS: RegistryReference<Block> = of("dark_oak_planks")
    @JvmField
    public val MANGROVE_PLANKS: RegistryReference<Block> = of("mangrove_planks")
    @JvmField
    public val OAK_SAPLING: RegistryReference<Block> = of("oak_sapling")
    @JvmField
    public val SPRUCE_SAPLING: RegistryReference<Block> = of("spruce_sapling")
    @JvmField
    public val BIRCH_SAPLING: RegistryReference<Block> = of("birch_sapling")
    @JvmField
    public val JUNGLE_SAPLING: RegistryReference<Block> = of("jungle_sapling")
    @JvmField
    public val ACACIA_SAPLING: RegistryReference<Block> = of("acacia_sapling")
    @JvmField
    public val DARK_OAK_SAPLING: RegistryReference<Block> = of("dark_oak_sapling")
    @JvmField
    public val MANGROVE_PROPAGULE: RegistryReference<Block> = of("mangrove_propagule")
    @JvmField
    public val BEDROCK: RegistryReference<Block> = of("bedrock")
    @JvmField
    public val WATER: RegistryReference<Block> = of("water")
    @JvmField
    public val LAVA: RegistryReference<Block> = of("lava")
    @JvmField
    public val SAND: RegistryReference<Block> = of("sand")
    @JvmField
    public val RED_SAND: RegistryReference<Block> = of("red_sand")
    @JvmField
    public val GRAVEL: RegistryReference<Block> = of("gravel")
    @JvmField
    public val GOLD_ORE: RegistryReference<Block> = of("gold_ore")
    @JvmField
    public val DEEPSLATE_GOLD_ORE: RegistryReference<Block> = of("deepslate_gold_ore")
    @JvmField
    public val IRON_ORE: RegistryReference<Block> = of("iron_ore")
    @JvmField
    public val DEEPSLATE_IRON_ORE: RegistryReference<Block> = of("deepslate_iron_ore")
    @JvmField
    public val COAL_ORE: RegistryReference<Block> = of("coal_ore")
    @JvmField
    public val DEEPSLATE_COAL_ORE: RegistryReference<Block> = of("deepslate_coal_ore")
    @JvmField
    public val NETHER_GOLD_ORE: RegistryReference<Block> = of("nether_gold_ore")
    @JvmField
    public val OAK_LOG: RegistryReference<Block> = of("oak_log")
    @JvmField
    public val SPRUCE_LOG: RegistryReference<Block> = of("spruce_log")
    @JvmField
    public val BIRCH_LOG: RegistryReference<Block> = of("birch_log")
    @JvmField
    public val JUNGLE_LOG: RegistryReference<Block> = of("jungle_log")
    @JvmField
    public val ACACIA_LOG: RegistryReference<Block> = of("acacia_log")
    @JvmField
    public val DARK_OAK_LOG: RegistryReference<Block> = of("dark_oak_log")
    @JvmField
    public val MANGROVE_LOG: RegistryReference<Block> = of("mangrove_log")
    @JvmField
    public val MANGROVE_ROOTS: RegistryReference<Block> = of("mangrove_roots")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS: RegistryReference<Block> = of("muddy_mangrove_roots")
    @JvmField
    public val STRIPPED_SPRUCE_LOG: RegistryReference<Block> = of("stripped_spruce_log")
    @JvmField
    public val STRIPPED_BIRCH_LOG: RegistryReference<Block> = of("stripped_birch_log")
    @JvmField
    public val STRIPPED_JUNGLE_LOG: RegistryReference<Block> = of("stripped_jungle_log")
    @JvmField
    public val STRIPPED_ACACIA_LOG: RegistryReference<Block> = of("stripped_acacia_log")
    @JvmField
    public val STRIPPED_DARK_OAK_LOG: RegistryReference<Block> = of("stripped_dark_oak_log")
    @JvmField
    public val STRIPPED_OAK_LOG: RegistryReference<Block> = of("stripped_oak_log")
    @JvmField
    public val STRIPPED_MANGROVE_LOG: RegistryReference<Block> = of("stripped_mangrove_log")
    @JvmField
    public val OAK_WOOD: RegistryReference<Block> = of("oak_wood")
    @JvmField
    public val SPRUCE_WOOD: RegistryReference<Block> = of("spruce_wood")
    @JvmField
    public val BIRCH_WOOD: RegistryReference<Block> = of("birch_wood")
    @JvmField
    public val JUNGLE_WOOD: RegistryReference<Block> = of("jungle_wood")
    @JvmField
    public val ACACIA_WOOD: RegistryReference<Block> = of("acacia_wood")
    @JvmField
    public val DARK_OAK_WOOD: RegistryReference<Block> = of("dark_oak_wood")
    @JvmField
    public val MANGROVE_WOOD: RegistryReference<Block> = of("mangrove_wood")
    @JvmField
    public val STRIPPED_OAK_WOOD: RegistryReference<Block> = of("stripped_oak_wood")
    @JvmField
    public val STRIPPED_SPRUCE_WOOD: RegistryReference<Block> = of("stripped_spruce_wood")
    @JvmField
    public val STRIPPED_BIRCH_WOOD: RegistryReference<Block> = of("stripped_birch_wood")
    @JvmField
    public val STRIPPED_JUNGLE_WOOD: RegistryReference<Block> = of("stripped_jungle_wood")
    @JvmField
    public val STRIPPED_ACACIA_WOOD: RegistryReference<Block> = of("stripped_acacia_wood")
    @JvmField
    public val STRIPPED_DARK_OAK_WOOD: RegistryReference<Block> = of("stripped_dark_oak_wood")
    @JvmField
    public val STRIPPED_MANGROVE_WOOD: RegistryReference<Block> = of("stripped_mangrove_wood")
    @JvmField
    public val OAK_LEAVES: RegistryReference<Block> = of("oak_leaves")
    @JvmField
    public val SPRUCE_LEAVES: RegistryReference<Block> = of("spruce_leaves")
    @JvmField
    public val BIRCH_LEAVES: RegistryReference<Block> = of("birch_leaves")
    @JvmField
    public val JUNGLE_LEAVES: RegistryReference<Block> = of("jungle_leaves")
    @JvmField
    public val ACACIA_LEAVES: RegistryReference<Block> = of("acacia_leaves")
    @JvmField
    public val DARK_OAK_LEAVES: RegistryReference<Block> = of("dark_oak_leaves")
    @JvmField
    public val MANGROVE_LEAVES: RegistryReference<Block> = of("mangrove_leaves")
    @JvmField
    public val AZALEA_LEAVES: RegistryReference<Block> = of("azalea_leaves")
    @JvmField
    public val FLOWERING_AZALEA_LEAVES: RegistryReference<Block> = of("flowering_azalea_leaves")
    @JvmField
    public val SPONGE: RegistryReference<Block> = of("sponge")
    @JvmField
    public val WET_SPONGE: RegistryReference<Block> = of("wet_sponge")
    @JvmField
    public val GLASS: RegistryReference<Block> = of("glass")
    @JvmField
    public val LAPIS_ORE: RegistryReference<Block> = of("lapis_ore")
    @JvmField
    public val DEEPSLATE_LAPIS_ORE: RegistryReference<Block> = of("deepslate_lapis_ore")
    @JvmField
    public val LAPIS_BLOCK: RegistryReference<Block> = of("lapis_block")
    @JvmField
    public val DISPENSER: RegistryReference<Block> = of("dispenser")
    @JvmField
    public val SANDSTONE: RegistryReference<Block> = of("sandstone")
    @JvmField
    public val CHISELED_SANDSTONE: RegistryReference<Block> = of("chiseled_sandstone")
    @JvmField
    public val CUT_SANDSTONE: RegistryReference<Block> = of("cut_sandstone")
    @JvmField
    public val NOTE_BLOCK: RegistryReference<Block> = of("note_block")
    @JvmField
    public val WHITE_BED: RegistryReference<Block> = of("white_bed")
    @JvmField
    public val ORANGE_BED: RegistryReference<Block> = of("orange_bed")
    @JvmField
    public val MAGENTA_BED: RegistryReference<Block> = of("magenta_bed")
    @JvmField
    public val LIGHT_BLUE_BED: RegistryReference<Block> = of("light_blue_bed")
    @JvmField
    public val YELLOW_BED: RegistryReference<Block> = of("yellow_bed")
    @JvmField
    public val LIME_BED: RegistryReference<Block> = of("lime_bed")
    @JvmField
    public val PINK_BED: RegistryReference<Block> = of("pink_bed")
    @JvmField
    public val GRAY_BED: RegistryReference<Block> = of("gray_bed")
    @JvmField
    public val LIGHT_GRAY_BED: RegistryReference<Block> = of("light_gray_bed")
    @JvmField
    public val CYAN_BED: RegistryReference<Block> = of("cyan_bed")
    @JvmField
    public val PURPLE_BED: RegistryReference<Block> = of("purple_bed")
    @JvmField
    public val BLUE_BED: RegistryReference<Block> = of("blue_bed")
    @JvmField
    public val BROWN_BED: RegistryReference<Block> = of("brown_bed")
    @JvmField
    public val GREEN_BED: RegistryReference<Block> = of("green_bed")
    @JvmField
    public val RED_BED: RegistryReference<Block> = of("red_bed")
    @JvmField
    public val BLACK_BED: RegistryReference<Block> = of("black_bed")
    @JvmField
    public val POWERED_RAIL: RegistryReference<Block> = of("powered_rail")
    @JvmField
    public val DETECTOR_RAIL: RegistryReference<Block> = of("detector_rail")
    @JvmField
    public val STICKY_PISTON: RegistryReference<Block> = of("sticky_piston")
    @JvmField
    public val COBWEB: RegistryReference<Block> = of("cobweb")
    @JvmField
    public val GRASS: RegistryReference<Block> = of("grass")
    @JvmField
    public val FERN: RegistryReference<Block> = of("fern")
    @JvmField
    public val DEAD_BUSH: RegistryReference<Block> = of("dead_bush")
    @JvmField
    public val SEAGRASS: RegistryReference<Block> = of("seagrass")
    @JvmField
    public val TALL_SEAGRASS: RegistryReference<Block> = of("tall_seagrass")
    @JvmField
    public val PISTON: RegistryReference<Block> = of("piston")
    @JvmField
    public val PISTON_HEAD: RegistryReference<Block> = of("piston_head")
    @JvmField
    public val WHITE_WOOL: RegistryReference<Block> = of("white_wool")
    @JvmField
    public val ORANGE_WOOL: RegistryReference<Block> = of("orange_wool")
    @JvmField
    public val MAGENTA_WOOL: RegistryReference<Block> = of("magenta_wool")
    @JvmField
    public val LIGHT_BLUE_WOOL: RegistryReference<Block> = of("light_blue_wool")
    @JvmField
    public val YELLOW_WOOL: RegistryReference<Block> = of("yellow_wool")
    @JvmField
    public val LIME_WOOL: RegistryReference<Block> = of("lime_wool")
    @JvmField
    public val PINK_WOOL: RegistryReference<Block> = of("pink_wool")
    @JvmField
    public val GRAY_WOOL: RegistryReference<Block> = of("gray_wool")
    @JvmField
    public val LIGHT_GRAY_WOOL: RegistryReference<Block> = of("light_gray_wool")
    @JvmField
    public val CYAN_WOOL: RegistryReference<Block> = of("cyan_wool")
    @JvmField
    public val PURPLE_WOOL: RegistryReference<Block> = of("purple_wool")
    @JvmField
    public val BLUE_WOOL: RegistryReference<Block> = of("blue_wool")
    @JvmField
    public val BROWN_WOOL: RegistryReference<Block> = of("brown_wool")
    @JvmField
    public val GREEN_WOOL: RegistryReference<Block> = of("green_wool")
    @JvmField
    public val RED_WOOL: RegistryReference<Block> = of("red_wool")
    @JvmField
    public val BLACK_WOOL: RegistryReference<Block> = of("black_wool")
    @JvmField
    public val MOVING_PISTON: RegistryReference<Block> = of("moving_piston")
    @JvmField
    public val DANDELION: RegistryReference<Block> = of("dandelion")
    @JvmField
    public val POPPY: RegistryReference<Block> = of("poppy")
    @JvmField
    public val BLUE_ORCHID: RegistryReference<Block> = of("blue_orchid")
    @JvmField
    public val ALLIUM: RegistryReference<Block> = of("allium")
    @JvmField
    public val AZURE_BLUET: RegistryReference<Block> = of("azure_bluet")
    @JvmField
    public val RED_TULIP: RegistryReference<Block> = of("red_tulip")
    @JvmField
    public val ORANGE_TULIP: RegistryReference<Block> = of("orange_tulip")
    @JvmField
    public val WHITE_TULIP: RegistryReference<Block> = of("white_tulip")
    @JvmField
    public val PINK_TULIP: RegistryReference<Block> = of("pink_tulip")
    @JvmField
    public val OXEYE_DAISY: RegistryReference<Block> = of("oxeye_daisy")
    @JvmField
    public val CORNFLOWER: RegistryReference<Block> = of("cornflower")
    @JvmField
    public val WITHER_ROSE: RegistryReference<Block> = of("wither_rose")
    @JvmField
    public val LILY_OF_THE_VALLEY: RegistryReference<Block> = of("lily_of_the_valley")
    @JvmField
    public val BROWN_MUSHROOM: RegistryReference<Block> = of("brown_mushroom")
    @JvmField
    public val RED_MUSHROOM: RegistryReference<Block> = of("red_mushroom")
    @JvmField
    public val GOLD_BLOCK: RegistryReference<Block> = of("gold_block")
    @JvmField
    public val IRON_BLOCK: RegistryReference<Block> = of("iron_block")
    @JvmField
    public val BRICKS: RegistryReference<Block> = of("bricks")
    @JvmField
    public val TNT: RegistryReference<Block> = of("tnt")
    @JvmField
    public val BOOKSHELF: RegistryReference<Block> = of("bookshelf")
    @JvmField
    public val MOSSY_COBBLESTONE: RegistryReference<Block> = of("mossy_cobblestone")
    @JvmField
    public val OBSIDIAN: RegistryReference<Block> = of("obsidian")
    @JvmField
    public val TORCH: RegistryReference<Block> = of("torch")
    @JvmField
    public val WALL_TORCH: RegistryReference<Block> = of("wall_torch")
    @JvmField
    public val FIRE: RegistryReference<Block> = of("fire")
    @JvmField
    public val SOUL_FIRE: RegistryReference<Block> = of("soul_fire")
    @JvmField
    public val SPAWNER: RegistryReference<Block> = of("spawner")
    @JvmField
    public val OAK_STAIRS: RegistryReference<Block> = of("oak_stairs")
    @JvmField
    public val CHEST: RegistryReference<Block> = of("chest")
    @JvmField
    public val REDSTONE_WIRE: RegistryReference<Block> = of("redstone_wire")
    @JvmField
    public val DIAMOND_ORE: RegistryReference<Block> = of("diamond_ore")
    @JvmField
    public val DEEPSLATE_DIAMOND_ORE: RegistryReference<Block> = of("deepslate_diamond_ore")
    @JvmField
    public val DIAMOND_BLOCK: RegistryReference<Block> = of("diamond_block")
    @JvmField
    public val CRAFTING_TABLE: RegistryReference<Block> = of("crafting_table")
    @JvmField
    public val WHEAT: RegistryReference<Block> = of("wheat")
    @JvmField
    public val FARMLAND: RegistryReference<Block> = of("farmland")
    @JvmField
    public val FURNACE: RegistryReference<Block> = of("furnace")
    @JvmField
    public val OAK_SIGN: RegistryReference<Block> = of("oak_sign")
    @JvmField
    public val SPRUCE_SIGN: RegistryReference<Block> = of("spruce_sign")
    @JvmField
    public val BIRCH_SIGN: RegistryReference<Block> = of("birch_sign")
    @JvmField
    public val ACACIA_SIGN: RegistryReference<Block> = of("acacia_sign")
    @JvmField
    public val JUNGLE_SIGN: RegistryReference<Block> = of("jungle_sign")
    @JvmField
    public val DARK_OAK_SIGN: RegistryReference<Block> = of("dark_oak_sign")
    @JvmField
    public val MANGROVE_SIGN: RegistryReference<Block> = of("mangrove_sign")
    @JvmField
    public val OAK_DOOR: RegistryReference<Block> = of("oak_door")
    @JvmField
    public val LADDER: RegistryReference<Block> = of("ladder")
    @JvmField
    public val RAIL: RegistryReference<Block> = of("rail")
    @JvmField
    public val COBBLESTONE_STAIRS: RegistryReference<Block> = of("cobblestone_stairs")
    @JvmField
    public val OAK_WALL_SIGN: RegistryReference<Block> = of("oak_wall_sign")
    @JvmField
    public val SPRUCE_WALL_SIGN: RegistryReference<Block> = of("spruce_wall_sign")
    @JvmField
    public val BIRCH_WALL_SIGN: RegistryReference<Block> = of("birch_wall_sign")
    @JvmField
    public val ACACIA_WALL_SIGN: RegistryReference<Block> = of("acacia_wall_sign")
    @JvmField
    public val JUNGLE_WALL_SIGN: RegistryReference<Block> = of("jungle_wall_sign")
    @JvmField
    public val DARK_OAK_WALL_SIGN: RegistryReference<Block> = of("dark_oak_wall_sign")
    @JvmField
    public val MANGROVE_WALL_SIGN: RegistryReference<Block> = of("mangrove_wall_sign")
    @JvmField
    public val LEVER: RegistryReference<Block> = of("lever")
    @JvmField
    public val STONE_PRESSURE_PLATE: RegistryReference<Block> = of("stone_pressure_plate")
    @JvmField
    public val IRON_DOOR: RegistryReference<Block> = of("iron_door")
    @JvmField
    public val OAK_PRESSURE_PLATE: RegistryReference<Block> = of("oak_pressure_plate")
    @JvmField
    public val SPRUCE_PRESSURE_PLATE: RegistryReference<Block> = of("spruce_pressure_plate")
    @JvmField
    public val BIRCH_PRESSURE_PLATE: RegistryReference<Block> = of("birch_pressure_plate")
    @JvmField
    public val JUNGLE_PRESSURE_PLATE: RegistryReference<Block> = of("jungle_pressure_plate")
    @JvmField
    public val ACACIA_PRESSURE_PLATE: RegistryReference<Block> = of("acacia_pressure_plate")
    @JvmField
    public val DARK_OAK_PRESSURE_PLATE: RegistryReference<Block> = of("dark_oak_pressure_plate")
    @JvmField
    public val MANGROVE_PRESSURE_PLATE: RegistryReference<Block> = of("mangrove_pressure_plate")
    @JvmField
    public val REDSTONE_ORE: RegistryReference<Block> = of("redstone_ore")
    @JvmField
    public val DEEPSLATE_REDSTONE_ORE: RegistryReference<Block> = of("deepslate_redstone_ore")
    @JvmField
    public val REDSTONE_TORCH: RegistryReference<Block> = of("redstone_torch")
    @JvmField
    public val REDSTONE_WALL_TORCH: RegistryReference<Block> = of("redstone_wall_torch")
    @JvmField
    public val STONE_BUTTON: RegistryReference<Block> = of("stone_button")
    @JvmField
    public val SNOW: RegistryReference<Block> = of("snow")
    @JvmField
    public val ICE: RegistryReference<Block> = of("ice")
    @JvmField
    public val SNOW_BLOCK: RegistryReference<Block> = of("snow_block")
    @JvmField
    public val CACTUS: RegistryReference<Block> = of("cactus")
    @JvmField
    public val CLAY: RegistryReference<Block> = of("clay")
    @JvmField
    public val SUGAR_CANE: RegistryReference<Block> = of("sugar_cane")
    @JvmField
    public val JUKEBOX: RegistryReference<Block> = of("jukebox")
    @JvmField
    public val OAK_FENCE: RegistryReference<Block> = of("oak_fence")
    @JvmField
    public val PUMPKIN: RegistryReference<Block> = of("pumpkin")
    @JvmField
    public val NETHERRACK: RegistryReference<Block> = of("netherrack")
    @JvmField
    public val SOUL_SAND: RegistryReference<Block> = of("soul_sand")
    @JvmField
    public val SOUL_SOIL: RegistryReference<Block> = of("soul_soil")
    @JvmField
    public val BASALT: RegistryReference<Block> = of("basalt")
    @JvmField
    public val POLISHED_BASALT: RegistryReference<Block> = of("polished_basalt")
    @JvmField
    public val SOUL_TORCH: RegistryReference<Block> = of("soul_torch")
    @JvmField
    public val SOUL_WALL_TORCH: RegistryReference<Block> = of("soul_wall_torch")
    @JvmField
    public val GLOWSTONE: RegistryReference<Block> = of("glowstone")
    @JvmField
    public val NETHER_PORTAL: RegistryReference<Block> = of("nether_portal")
    @JvmField
    public val CARVED_PUMPKIN: RegistryReference<Block> = of("carved_pumpkin")
    @JvmField
    public val JACK_O_LANTERN: RegistryReference<Block> = of("jack_o_lantern")
    @JvmField
    public val CAKE: RegistryReference<Block> = of("cake")
    @JvmField
    public val REPEATER: RegistryReference<Block> = of("repeater")
    @JvmField
    public val WHITE_STAINED_GLASS: RegistryReference<Block> = of("white_stained_glass")
    @JvmField
    public val ORANGE_STAINED_GLASS: RegistryReference<Block> = of("orange_stained_glass")
    @JvmField
    public val MAGENTA_STAINED_GLASS: RegistryReference<Block> = of("magenta_stained_glass")
    @JvmField
    public val LIGHT_BLUE_STAINED_GLASS: RegistryReference<Block> = of("light_blue_stained_glass")
    @JvmField
    public val YELLOW_STAINED_GLASS: RegistryReference<Block> = of("yellow_stained_glass")
    @JvmField
    public val LIME_STAINED_GLASS: RegistryReference<Block> = of("lime_stained_glass")
    @JvmField
    public val PINK_STAINED_GLASS: RegistryReference<Block> = of("pink_stained_glass")
    @JvmField
    public val GRAY_STAINED_GLASS: RegistryReference<Block> = of("gray_stained_glass")
    @JvmField
    public val LIGHT_GRAY_STAINED_GLASS: RegistryReference<Block> = of("light_gray_stained_glass")
    @JvmField
    public val CYAN_STAINED_GLASS: RegistryReference<Block> = of("cyan_stained_glass")
    @JvmField
    public val PURPLE_STAINED_GLASS: RegistryReference<Block> = of("purple_stained_glass")
    @JvmField
    public val BLUE_STAINED_GLASS: RegistryReference<Block> = of("blue_stained_glass")
    @JvmField
    public val BROWN_STAINED_GLASS: RegistryReference<Block> = of("brown_stained_glass")
    @JvmField
    public val GREEN_STAINED_GLASS: RegistryReference<Block> = of("green_stained_glass")
    @JvmField
    public val RED_STAINED_GLASS: RegistryReference<Block> = of("red_stained_glass")
    @JvmField
    public val BLACK_STAINED_GLASS: RegistryReference<Block> = of("black_stained_glass")
    @JvmField
    public val OAK_TRAPDOOR: RegistryReference<Block> = of("oak_trapdoor")
    @JvmField
    public val SPRUCE_TRAPDOOR: RegistryReference<Block> = of("spruce_trapdoor")
    @JvmField
    public val BIRCH_TRAPDOOR: RegistryReference<Block> = of("birch_trapdoor")
    @JvmField
    public val JUNGLE_TRAPDOOR: RegistryReference<Block> = of("jungle_trapdoor")
    @JvmField
    public val ACACIA_TRAPDOOR: RegistryReference<Block> = of("acacia_trapdoor")
    @JvmField
    public val DARK_OAK_TRAPDOOR: RegistryReference<Block> = of("dark_oak_trapdoor")
    @JvmField
    public val MANGROVE_TRAPDOOR: RegistryReference<Block> = of("mangrove_trapdoor")
    @JvmField
    public val STONE_BRICKS: RegistryReference<Block> = of("stone_bricks")
    @JvmField
    public val MOSSY_STONE_BRICKS: RegistryReference<Block> = of("mossy_stone_bricks")
    @JvmField
    public val CRACKED_STONE_BRICKS: RegistryReference<Block> = of("cracked_stone_bricks")
    @JvmField
    public val CHISELED_STONE_BRICKS: RegistryReference<Block> = of("chiseled_stone_bricks")
    @JvmField
    public val PACKED_MUD: RegistryReference<Block> = of("packed_mud")
    @JvmField
    public val MUD_BRICKS: RegistryReference<Block> = of("mud_bricks")
    @JvmField
    public val INFESTED_STONE: RegistryReference<Block> = of("infested_stone")
    @JvmField
    public val INFESTED_COBBLESTONE: RegistryReference<Block> = of("infested_cobblestone")
    @JvmField
    public val INFESTED_STONE_BRICKS: RegistryReference<Block> = of("infested_stone_bricks")
    @JvmField
    public val INFESTED_MOSSY_STONE_BRICKS: RegistryReference<Block> = of("infested_mossy_stone_bricks")
    @JvmField
    public val INFESTED_CRACKED_STONE_BRICKS: RegistryReference<Block> = of("infested_cracked_stone_bricks")
    @JvmField
    public val INFESTED_CHISELED_STONE_BRICKS: RegistryReference<Block> = of("infested_chiseled_stone_bricks")
    @JvmField
    public val BROWN_MUSHROOM_BLOCK: RegistryReference<Block> = of("brown_mushroom_block")
    @JvmField
    public val RED_MUSHROOM_BLOCK: RegistryReference<Block> = of("red_mushroom_block")
    @JvmField
    public val MUSHROOM_STEM: RegistryReference<Block> = of("mushroom_stem")
    @JvmField
    public val IRON_BARS: RegistryReference<Block> = of("iron_bars")
    @JvmField
    public val CHAIN: RegistryReference<Block> = of("chain")
    @JvmField
    public val GLASS_PANE: RegistryReference<Block> = of("glass_pane")
    @JvmField
    public val MELON: RegistryReference<Block> = of("melon")
    @JvmField
    public val ATTACHED_PUMPKIN_STEM: RegistryReference<Block> = of("attached_pumpkin_stem")
    @JvmField
    public val ATTACHED_MELON_STEM: RegistryReference<Block> = of("attached_melon_stem")
    @JvmField
    public val PUMPKIN_STEM: RegistryReference<Block> = of("pumpkin_stem")
    @JvmField
    public val MELON_STEM: RegistryReference<Block> = of("melon_stem")
    @JvmField
    public val VINE: RegistryReference<Block> = of("vine")
    @JvmField
    public val GLOW_LICHEN: RegistryReference<Block> = of("glow_lichen")
    @JvmField
    public val OAK_FENCE_GATE: RegistryReference<Block> = of("oak_fence_gate")
    @JvmField
    public val BRICK_STAIRS: RegistryReference<Block> = of("brick_stairs")
    @JvmField
    public val STONE_BRICK_STAIRS: RegistryReference<Block> = of("stone_brick_stairs")
    @JvmField
    public val MUD_BRICK_STAIRS: RegistryReference<Block> = of("mud_brick_stairs")
    @JvmField
    public val MYCELIUM: RegistryReference<Block> = of("mycelium")
    @JvmField
    public val LILY_PAD: RegistryReference<Block> = of("lily_pad")
    @JvmField
    public val NETHER_BRICKS: RegistryReference<Block> = of("nether_bricks")
    @JvmField
    public val NETHER_BRICK_FENCE: RegistryReference<Block> = of("nether_brick_fence")
    @JvmField
    public val NETHER_BRICK_STAIRS: RegistryReference<Block> = of("nether_brick_stairs")
    @JvmField
    public val NETHER_WART: RegistryReference<Block> = of("nether_wart")
    @JvmField
    public val ENCHANTING_TABLE: RegistryReference<Block> = of("enchanting_table")
    @JvmField
    public val BREWING_STAND: RegistryReference<Block> = of("brewing_stand")
    @JvmField
    public val CAULDRON: RegistryReference<Block> = of("cauldron")
    @JvmField
    public val WATER_CAULDRON: RegistryReference<Block> = of("water_cauldron")
    @JvmField
    public val LAVA_CAULDRON: RegistryReference<Block> = of("lava_cauldron")
    @JvmField
    public val POWDER_SNOW_CAULDRON: RegistryReference<Block> = of("powder_snow_cauldron")
    @JvmField
    public val END_PORTAL: RegistryReference<Block> = of("end_portal")
    @JvmField
    public val END_PORTAL_FRAME: RegistryReference<Block> = of("end_portal_frame")
    @JvmField
    public val END_STONE: RegistryReference<Block> = of("end_stone")
    @JvmField
    public val DRAGON_EGG: RegistryReference<Block> = of("dragon_egg")
    @JvmField
    public val REDSTONE_LAMP: RegistryReference<Block> = of("redstone_lamp")
    @JvmField
    public val COCOA: RegistryReference<Block> = of("cocoa")
    @JvmField
    public val SANDSTONE_STAIRS: RegistryReference<Block> = of("sandstone_stairs")
    @JvmField
    public val EMERALD_ORE: RegistryReference<Block> = of("emerald_ore")
    @JvmField
    public val DEEPSLATE_EMERALD_ORE: RegistryReference<Block> = of("deepslate_emerald_ore")
    @JvmField
    public val ENDER_CHEST: RegistryReference<Block> = of("ender_chest")
    @JvmField
    public val TRIPWIRE_HOOK: RegistryReference<Block> = of("tripwire_hook")
    @JvmField
    public val TRIPWIRE: RegistryReference<Block> = of("tripwire")
    @JvmField
    public val EMERALD_BLOCK: RegistryReference<Block> = of("emerald_block")
    @JvmField
    public val SPRUCE_STAIRS: RegistryReference<Block> = of("spruce_stairs")
    @JvmField
    public val BIRCH_STAIRS: RegistryReference<Block> = of("birch_stairs")
    @JvmField
    public val JUNGLE_STAIRS: RegistryReference<Block> = of("jungle_stairs")
    @JvmField
    public val COMMAND_BLOCK: RegistryReference<Block> = of("command_block")
    @JvmField
    public val BEACON: RegistryReference<Block> = of("beacon")
    @JvmField
    public val COBBLESTONE_WALL: RegistryReference<Block> = of("cobblestone_wall")
    @JvmField
    public val MOSSY_COBBLESTONE_WALL: RegistryReference<Block> = of("mossy_cobblestone_wall")
    @JvmField
    public val FLOWER_POT: RegistryReference<Block> = of("flower_pot")
    @JvmField
    public val POTTED_OAK_SAPLING: RegistryReference<Block> = of("potted_oak_sapling")
    @JvmField
    public val POTTED_SPRUCE_SAPLING: RegistryReference<Block> = of("potted_spruce_sapling")
    @JvmField
    public val POTTED_BIRCH_SAPLING: RegistryReference<Block> = of("potted_birch_sapling")
    @JvmField
    public val POTTED_JUNGLE_SAPLING: RegistryReference<Block> = of("potted_jungle_sapling")
    @JvmField
    public val POTTED_ACACIA_SAPLING: RegistryReference<Block> = of("potted_acacia_sapling")
    @JvmField
    public val POTTED_DARK_OAK_SAPLING: RegistryReference<Block> = of("potted_dark_oak_sapling")
    @JvmField
    public val POTTED_MANGROVE_PROPAGULE: RegistryReference<Block> = of("potted_mangrove_propagule")
    @JvmField
    public val POTTED_FERN: RegistryReference<Block> = of("potted_fern")
    @JvmField
    public val POTTED_DANDELION: RegistryReference<Block> = of("potted_dandelion")
    @JvmField
    public val POTTED_POPPY: RegistryReference<Block> = of("potted_poppy")
    @JvmField
    public val POTTED_BLUE_ORCHID: RegistryReference<Block> = of("potted_blue_orchid")
    @JvmField
    public val POTTED_ALLIUM: RegistryReference<Block> = of("potted_allium")
    @JvmField
    public val POTTED_AZURE_BLUET: RegistryReference<Block> = of("potted_azure_bluet")
    @JvmField
    public val POTTED_RED_TULIP: RegistryReference<Block> = of("potted_red_tulip")
    @JvmField
    public val POTTED_ORANGE_TULIP: RegistryReference<Block> = of("potted_orange_tulip")
    @JvmField
    public val POTTED_WHITE_TULIP: RegistryReference<Block> = of("potted_white_tulip")
    @JvmField
    public val POTTED_PINK_TULIP: RegistryReference<Block> = of("potted_pink_tulip")
    @JvmField
    public val POTTED_OXEYE_DAISY: RegistryReference<Block> = of("potted_oxeye_daisy")
    @JvmField
    public val POTTED_CORNFLOWER: RegistryReference<Block> = of("potted_cornflower")
    @JvmField
    public val POTTED_LILY_OF_THE_VALLEY: RegistryReference<Block> = of("potted_lily_of_the_valley")
    @JvmField
    public val POTTED_WITHER_ROSE: RegistryReference<Block> = of("potted_wither_rose")
    @JvmField
    public val POTTED_RED_MUSHROOM: RegistryReference<Block> = of("potted_red_mushroom")
    @JvmField
    public val POTTED_BROWN_MUSHROOM: RegistryReference<Block> = of("potted_brown_mushroom")
    @JvmField
    public val POTTED_DEAD_BUSH: RegistryReference<Block> = of("potted_dead_bush")
    @JvmField
    public val POTTED_CACTUS: RegistryReference<Block> = of("potted_cactus")
    @JvmField
    public val CARROTS: RegistryReference<Block> = of("carrots")
    @JvmField
    public val POTATOES: RegistryReference<Block> = of("potatoes")
    @JvmField
    public val OAK_BUTTON: RegistryReference<Block> = of("oak_button")
    @JvmField
    public val SPRUCE_BUTTON: RegistryReference<Block> = of("spruce_button")
    @JvmField
    public val BIRCH_BUTTON: RegistryReference<Block> = of("birch_button")
    @JvmField
    public val JUNGLE_BUTTON: RegistryReference<Block> = of("jungle_button")
    @JvmField
    public val ACACIA_BUTTON: RegistryReference<Block> = of("acacia_button")
    @JvmField
    public val DARK_OAK_BUTTON: RegistryReference<Block> = of("dark_oak_button")
    @JvmField
    public val MANGROVE_BUTTON: RegistryReference<Block> = of("mangrove_button")
    @JvmField
    public val SKELETON_SKULL: RegistryReference<Block> = of("skeleton_skull")
    @JvmField
    public val SKELETON_WALL_SKULL: RegistryReference<Block> = of("skeleton_wall_skull")
    @JvmField
    public val WITHER_SKELETON_SKULL: RegistryReference<Block> = of("wither_skeleton_skull")
    @JvmField
    public val WITHER_SKELETON_WALL_SKULL: RegistryReference<Block> = of("wither_skeleton_wall_skull")
    @JvmField
    public val ZOMBIE_HEAD: RegistryReference<Block> = of("zombie_head")
    @JvmField
    public val ZOMBIE_WALL_HEAD: RegistryReference<Block> = of("zombie_wall_head")
    @JvmField
    public val PLAYER_HEAD: RegistryReference<Block> = of("player_head")
    @JvmField
    public val PLAYER_WALL_HEAD: RegistryReference<Block> = of("player_wall_head")
    @JvmField
    public val CREEPER_HEAD: RegistryReference<Block> = of("creeper_head")
    @JvmField
    public val CREEPER_WALL_HEAD: RegistryReference<Block> = of("creeper_wall_head")
    @JvmField
    public val DRAGON_HEAD: RegistryReference<Block> = of("dragon_head")
    @JvmField
    public val DRAGON_WALL_HEAD: RegistryReference<Block> = of("dragon_wall_head")
    @JvmField
    public val ANVIL: RegistryReference<Block> = of("anvil")
    @JvmField
    public val CHIPPED_ANVIL: RegistryReference<Block> = of("chipped_anvil")
    @JvmField
    public val DAMAGED_ANVIL: RegistryReference<Block> = of("damaged_anvil")
    @JvmField
    public val TRAPPED_CHEST: RegistryReference<Block> = of("trapped_chest")
    @JvmField
    public val LIGHT_WEIGHTED_PRESSURE_PLATE: RegistryReference<Block> = of("light_weighted_pressure_plate")
    @JvmField
    public val HEAVY_WEIGHTED_PRESSURE_PLATE: RegistryReference<Block> = of("heavy_weighted_pressure_plate")
    @JvmField
    public val COMPARATOR: RegistryReference<Block> = of("comparator")
    @JvmField
    public val DAYLIGHT_DETECTOR: RegistryReference<Block> = of("daylight_detector")
    @JvmField
    public val REDSTONE_BLOCK: RegistryReference<Block> = of("redstone_block")
    @JvmField
    public val NETHER_QUARTZ_ORE: RegistryReference<Block> = of("nether_quartz_ore")
    @JvmField
    public val HOPPER: RegistryReference<Block> = of("hopper")
    @JvmField
    public val QUARTZ_BLOCK: RegistryReference<Block> = of("quartz_block")
    @JvmField
    public val CHISELED_QUARTZ_BLOCK: RegistryReference<Block> = of("chiseled_quartz_block")
    @JvmField
    public val QUARTZ_PILLAR: RegistryReference<Block> = of("quartz_pillar")
    @JvmField
    public val QUARTZ_STAIRS: RegistryReference<Block> = of("quartz_stairs")
    @JvmField
    public val ACTIVATOR_RAIL: RegistryReference<Block> = of("activator_rail")
    @JvmField
    public val DROPPER: RegistryReference<Block> = of("dropper")
    @JvmField
    public val WHITE_TERRACOTTA: RegistryReference<Block> = of("white_terracotta")
    @JvmField
    public val ORANGE_TERRACOTTA: RegistryReference<Block> = of("orange_terracotta")
    @JvmField
    public val MAGENTA_TERRACOTTA: RegistryReference<Block> = of("magenta_terracotta")
    @JvmField
    public val LIGHT_BLUE_TERRACOTTA: RegistryReference<Block> = of("light_blue_terracotta")
    @JvmField
    public val YELLOW_TERRACOTTA: RegistryReference<Block> = of("yellow_terracotta")
    @JvmField
    public val LIME_TERRACOTTA: RegistryReference<Block> = of("lime_terracotta")
    @JvmField
    public val PINK_TERRACOTTA: RegistryReference<Block> = of("pink_terracotta")
    @JvmField
    public val GRAY_TERRACOTTA: RegistryReference<Block> = of("gray_terracotta")
    @JvmField
    public val LIGHT_GRAY_TERRACOTTA: RegistryReference<Block> = of("light_gray_terracotta")
    @JvmField
    public val CYAN_TERRACOTTA: RegistryReference<Block> = of("cyan_terracotta")
    @JvmField
    public val PURPLE_TERRACOTTA: RegistryReference<Block> = of("purple_terracotta")
    @JvmField
    public val BLUE_TERRACOTTA: RegistryReference<Block> = of("blue_terracotta")
    @JvmField
    public val BROWN_TERRACOTTA: RegistryReference<Block> = of("brown_terracotta")
    @JvmField
    public val GREEN_TERRACOTTA: RegistryReference<Block> = of("green_terracotta")
    @JvmField
    public val RED_TERRACOTTA: RegistryReference<Block> = of("red_terracotta")
    @JvmField
    public val BLACK_TERRACOTTA: RegistryReference<Block> = of("black_terracotta")
    @JvmField
    public val WHITE_STAINED_GLASS_PANE: RegistryReference<Block> = of("white_stained_glass_pane")
    @JvmField
    public val ORANGE_STAINED_GLASS_PANE: RegistryReference<Block> = of("orange_stained_glass_pane")
    @JvmField
    public val MAGENTA_STAINED_GLASS_PANE: RegistryReference<Block> = of("magenta_stained_glass_pane")
    @JvmField
    public val LIGHT_BLUE_STAINED_GLASS_PANE: RegistryReference<Block> = of("light_blue_stained_glass_pane")
    @JvmField
    public val YELLOW_STAINED_GLASS_PANE: RegistryReference<Block> = of("yellow_stained_glass_pane")
    @JvmField
    public val LIME_STAINED_GLASS_PANE: RegistryReference<Block> = of("lime_stained_glass_pane")
    @JvmField
    public val PINK_STAINED_GLASS_PANE: RegistryReference<Block> = of("pink_stained_glass_pane")
    @JvmField
    public val GRAY_STAINED_GLASS_PANE: RegistryReference<Block> = of("gray_stained_glass_pane")
    @JvmField
    public val LIGHT_GRAY_STAINED_GLASS_PANE: RegistryReference<Block> = of("light_gray_stained_glass_pane")
    @JvmField
    public val CYAN_STAINED_GLASS_PANE: RegistryReference<Block> = of("cyan_stained_glass_pane")
    @JvmField
    public val PURPLE_STAINED_GLASS_PANE: RegistryReference<Block> = of("purple_stained_glass_pane")
    @JvmField
    public val BLUE_STAINED_GLASS_PANE: RegistryReference<Block> = of("blue_stained_glass_pane")
    @JvmField
    public val BROWN_STAINED_GLASS_PANE: RegistryReference<Block> = of("brown_stained_glass_pane")
    @JvmField
    public val GREEN_STAINED_GLASS_PANE: RegistryReference<Block> = of("green_stained_glass_pane")
    @JvmField
    public val RED_STAINED_GLASS_PANE: RegistryReference<Block> = of("red_stained_glass_pane")
    @JvmField
    public val BLACK_STAINED_GLASS_PANE: RegistryReference<Block> = of("black_stained_glass_pane")
    @JvmField
    public val ACACIA_STAIRS: RegistryReference<Block> = of("acacia_stairs")
    @JvmField
    public val DARK_OAK_STAIRS: RegistryReference<Block> = of("dark_oak_stairs")
    @JvmField
    public val MANGROVE_STAIRS: RegistryReference<Block> = of("mangrove_stairs")
    @JvmField
    public val SLIME_BLOCK: RegistryReference<Block> = of("slime_block")
    @JvmField
    public val BARRIER: RegistryReference<Block> = of("barrier")
    @JvmField
    public val LIGHT: RegistryReference<Block> = of("light")
    @JvmField
    public val IRON_TRAPDOOR: RegistryReference<Block> = of("iron_trapdoor")
    @JvmField
    public val PRISMARINE: RegistryReference<Block> = of("prismarine")
    @JvmField
    public val PRISMARINE_BRICKS: RegistryReference<Block> = of("prismarine_bricks")
    @JvmField
    public val DARK_PRISMARINE: RegistryReference<Block> = of("dark_prismarine")
    @JvmField
    public val PRISMARINE_STAIRS: RegistryReference<Block> = of("prismarine_stairs")
    @JvmField
    public val PRISMARINE_BRICK_STAIRS: RegistryReference<Block> = of("prismarine_brick_stairs")
    @JvmField
    public val DARK_PRISMARINE_STAIRS: RegistryReference<Block> = of("dark_prismarine_stairs")
    @JvmField
    public val PRISMARINE_SLAB: RegistryReference<Block> = of("prismarine_slab")
    @JvmField
    public val PRISMARINE_BRICK_SLAB: RegistryReference<Block> = of("prismarine_brick_slab")
    @JvmField
    public val DARK_PRISMARINE_SLAB: RegistryReference<Block> = of("dark_prismarine_slab")
    @JvmField
    public val SEA_LANTERN: RegistryReference<Block> = of("sea_lantern")
    @JvmField
    public val HAY_BLOCK: RegistryReference<Block> = of("hay_block")
    @JvmField
    public val WHITE_CARPET: RegistryReference<Block> = of("white_carpet")
    @JvmField
    public val ORANGE_CARPET: RegistryReference<Block> = of("orange_carpet")
    @JvmField
    public val MAGENTA_CARPET: RegistryReference<Block> = of("magenta_carpet")
    @JvmField
    public val LIGHT_BLUE_CARPET: RegistryReference<Block> = of("light_blue_carpet")
    @JvmField
    public val YELLOW_CARPET: RegistryReference<Block> = of("yellow_carpet")
    @JvmField
    public val LIME_CARPET: RegistryReference<Block> = of("lime_carpet")
    @JvmField
    public val PINK_CARPET: RegistryReference<Block> = of("pink_carpet")
    @JvmField
    public val GRAY_CARPET: RegistryReference<Block> = of("gray_carpet")
    @JvmField
    public val LIGHT_GRAY_CARPET: RegistryReference<Block> = of("light_gray_carpet")
    @JvmField
    public val CYAN_CARPET: RegistryReference<Block> = of("cyan_carpet")
    @JvmField
    public val PURPLE_CARPET: RegistryReference<Block> = of("purple_carpet")
    @JvmField
    public val BLUE_CARPET: RegistryReference<Block> = of("blue_carpet")
    @JvmField
    public val BROWN_CARPET: RegistryReference<Block> = of("brown_carpet")
    @JvmField
    public val GREEN_CARPET: RegistryReference<Block> = of("green_carpet")
    @JvmField
    public val RED_CARPET: RegistryReference<Block> = of("red_carpet")
    @JvmField
    public val BLACK_CARPET: RegistryReference<Block> = of("black_carpet")
    @JvmField
    public val TERRACOTTA: RegistryReference<Block> = of("terracotta")
    @JvmField
    public val COAL_BLOCK: RegistryReference<Block> = of("coal_block")
    @JvmField
    public val PACKED_ICE: RegistryReference<Block> = of("packed_ice")
    @JvmField
    public val SUNFLOWER: RegistryReference<Block> = of("sunflower")
    @JvmField
    public val LILAC: RegistryReference<Block> = of("lilac")
    @JvmField
    public val ROSE_BUSH: RegistryReference<Block> = of("rose_bush")
    @JvmField
    public val PEONY: RegistryReference<Block> = of("peony")
    @JvmField
    public val TALL_GRASS: RegistryReference<Block> = of("tall_grass")
    @JvmField
    public val LARGE_FERN: RegistryReference<Block> = of("large_fern")
    @JvmField
    public val WHITE_BANNER: RegistryReference<Block> = of("white_banner")
    @JvmField
    public val ORANGE_BANNER: RegistryReference<Block> = of("orange_banner")
    @JvmField
    public val MAGENTA_BANNER: RegistryReference<Block> = of("magenta_banner")
    @JvmField
    public val LIGHT_BLUE_BANNER: RegistryReference<Block> = of("light_blue_banner")
    @JvmField
    public val YELLOW_BANNER: RegistryReference<Block> = of("yellow_banner")
    @JvmField
    public val LIME_BANNER: RegistryReference<Block> = of("lime_banner")
    @JvmField
    public val PINK_BANNER: RegistryReference<Block> = of("pink_banner")
    @JvmField
    public val GRAY_BANNER: RegistryReference<Block> = of("gray_banner")
    @JvmField
    public val LIGHT_GRAY_BANNER: RegistryReference<Block> = of("light_gray_banner")
    @JvmField
    public val CYAN_BANNER: RegistryReference<Block> = of("cyan_banner")
    @JvmField
    public val PURPLE_BANNER: RegistryReference<Block> = of("purple_banner")
    @JvmField
    public val BLUE_BANNER: RegistryReference<Block> = of("blue_banner")
    @JvmField
    public val BROWN_BANNER: RegistryReference<Block> = of("brown_banner")
    @JvmField
    public val GREEN_BANNER: RegistryReference<Block> = of("green_banner")
    @JvmField
    public val RED_BANNER: RegistryReference<Block> = of("red_banner")
    @JvmField
    public val BLACK_BANNER: RegistryReference<Block> = of("black_banner")
    @JvmField
    public val WHITE_WALL_BANNER: RegistryReference<Block> = of("white_wall_banner")
    @JvmField
    public val ORANGE_WALL_BANNER: RegistryReference<Block> = of("orange_wall_banner")
    @JvmField
    public val MAGENTA_WALL_BANNER: RegistryReference<Block> = of("magenta_wall_banner")
    @JvmField
    public val LIGHT_BLUE_WALL_BANNER: RegistryReference<Block> = of("light_blue_wall_banner")
    @JvmField
    public val YELLOW_WALL_BANNER: RegistryReference<Block> = of("yellow_wall_banner")
    @JvmField
    public val LIME_WALL_BANNER: RegistryReference<Block> = of("lime_wall_banner")
    @JvmField
    public val PINK_WALL_BANNER: RegistryReference<Block> = of("pink_wall_banner")
    @JvmField
    public val GRAY_WALL_BANNER: RegistryReference<Block> = of("gray_wall_banner")
    @JvmField
    public val LIGHT_GRAY_WALL_BANNER: RegistryReference<Block> = of("light_gray_wall_banner")
    @JvmField
    public val CYAN_WALL_BANNER: RegistryReference<Block> = of("cyan_wall_banner")
    @JvmField
    public val PURPLE_WALL_BANNER: RegistryReference<Block> = of("purple_wall_banner")
    @JvmField
    public val BLUE_WALL_BANNER: RegistryReference<Block> = of("blue_wall_banner")
    @JvmField
    public val BROWN_WALL_BANNER: RegistryReference<Block> = of("brown_wall_banner")
    @JvmField
    public val GREEN_WALL_BANNER: RegistryReference<Block> = of("green_wall_banner")
    @JvmField
    public val RED_WALL_BANNER: RegistryReference<Block> = of("red_wall_banner")
    @JvmField
    public val BLACK_WALL_BANNER: RegistryReference<Block> = of("black_wall_banner")
    @JvmField
    public val RED_SANDSTONE: RegistryReference<Block> = of("red_sandstone")
    @JvmField
    public val CHISELED_RED_SANDSTONE: RegistryReference<Block> = of("chiseled_red_sandstone")
    @JvmField
    public val CUT_RED_SANDSTONE: RegistryReference<Block> = of("cut_red_sandstone")
    @JvmField
    public val RED_SANDSTONE_STAIRS: RegistryReference<Block> = of("red_sandstone_stairs")
    @JvmField
    public val OAK_SLAB: RegistryReference<Block> = of("oak_slab")
    @JvmField
    public val SPRUCE_SLAB: RegistryReference<Block> = of("spruce_slab")
    @JvmField
    public val BIRCH_SLAB: RegistryReference<Block> = of("birch_slab")
    @JvmField
    public val JUNGLE_SLAB: RegistryReference<Block> = of("jungle_slab")
    @JvmField
    public val ACACIA_SLAB: RegistryReference<Block> = of("acacia_slab")
    @JvmField
    public val DARK_OAK_SLAB: RegistryReference<Block> = of("dark_oak_slab")
    @JvmField
    public val MANGROVE_SLAB: RegistryReference<Block> = of("mangrove_slab")
    @JvmField
    public val STONE_SLAB: RegistryReference<Block> = of("stone_slab")
    @JvmField
    public val SMOOTH_STONE_SLAB: RegistryReference<Block> = of("smooth_stone_slab")
    @JvmField
    public val SANDSTONE_SLAB: RegistryReference<Block> = of("sandstone_slab")
    @JvmField
    public val CUT_SANDSTONE_SLAB: RegistryReference<Block> = of("cut_sandstone_slab")
    @JvmField
    public val PETRIFIED_OAK_SLAB: RegistryReference<Block> = of("petrified_oak_slab")
    @JvmField
    public val COBBLESTONE_SLAB: RegistryReference<Block> = of("cobblestone_slab")
    @JvmField
    public val BRICK_SLAB: RegistryReference<Block> = of("brick_slab")
    @JvmField
    public val STONE_BRICK_SLAB: RegistryReference<Block> = of("stone_brick_slab")
    @JvmField
    public val MUD_BRICK_SLAB: RegistryReference<Block> = of("mud_brick_slab")
    @JvmField
    public val NETHER_BRICK_SLAB: RegistryReference<Block> = of("nether_brick_slab")
    @JvmField
    public val QUARTZ_SLAB: RegistryReference<Block> = of("quartz_slab")
    @JvmField
    public val RED_SANDSTONE_SLAB: RegistryReference<Block> = of("red_sandstone_slab")
    @JvmField
    public val CUT_RED_SANDSTONE_SLAB: RegistryReference<Block> = of("cut_red_sandstone_slab")
    @JvmField
    public val PURPUR_SLAB: RegistryReference<Block> = of("purpur_slab")
    @JvmField
    public val SMOOTH_STONE: RegistryReference<Block> = of("smooth_stone")
    @JvmField
    public val SMOOTH_SANDSTONE: RegistryReference<Block> = of("smooth_sandstone")
    @JvmField
    public val SMOOTH_QUARTZ: RegistryReference<Block> = of("smooth_quartz")
    @JvmField
    public val SMOOTH_RED_SANDSTONE: RegistryReference<Block> = of("smooth_red_sandstone")
    @JvmField
    public val SPRUCE_FENCE_GATE: RegistryReference<Block> = of("spruce_fence_gate")
    @JvmField
    public val BIRCH_FENCE_GATE: RegistryReference<Block> = of("birch_fence_gate")
    @JvmField
    public val JUNGLE_FENCE_GATE: RegistryReference<Block> = of("jungle_fence_gate")
    @JvmField
    public val ACACIA_FENCE_GATE: RegistryReference<Block> = of("acacia_fence_gate")
    @JvmField
    public val DARK_OAK_FENCE_GATE: RegistryReference<Block> = of("dark_oak_fence_gate")
    @JvmField
    public val MANGROVE_FENCE_GATE: RegistryReference<Block> = of("mangrove_fence_gate")
    @JvmField
    public val SPRUCE_FENCE: RegistryReference<Block> = of("spruce_fence")
    @JvmField
    public val BIRCH_FENCE: RegistryReference<Block> = of("birch_fence")
    @JvmField
    public val JUNGLE_FENCE: RegistryReference<Block> = of("jungle_fence")
    @JvmField
    public val ACACIA_FENCE: RegistryReference<Block> = of("acacia_fence")
    @JvmField
    public val DARK_OAK_FENCE: RegistryReference<Block> = of("dark_oak_fence")
    @JvmField
    public val MANGROVE_FENCE: RegistryReference<Block> = of("mangrove_fence")
    @JvmField
    public val SPRUCE_DOOR: RegistryReference<Block> = of("spruce_door")
    @JvmField
    public val BIRCH_DOOR: RegistryReference<Block> = of("birch_door")
    @JvmField
    public val JUNGLE_DOOR: RegistryReference<Block> = of("jungle_door")
    @JvmField
    public val ACACIA_DOOR: RegistryReference<Block> = of("acacia_door")
    @JvmField
    public val DARK_OAK_DOOR: RegistryReference<Block> = of("dark_oak_door")
    @JvmField
    public val MANGROVE_DOOR: RegistryReference<Block> = of("mangrove_door")
    @JvmField
    public val END_ROD: RegistryReference<Block> = of("end_rod")
    @JvmField
    public val CHORUS_PLANT: RegistryReference<Block> = of("chorus_plant")
    @JvmField
    public val CHORUS_FLOWER: RegistryReference<Block> = of("chorus_flower")
    @JvmField
    public val PURPUR_BLOCK: RegistryReference<Block> = of("purpur_block")
    @JvmField
    public val PURPUR_PILLAR: RegistryReference<Block> = of("purpur_pillar")
    @JvmField
    public val PURPUR_STAIRS: RegistryReference<Block> = of("purpur_stairs")
    @JvmField
    public val END_STONE_BRICKS: RegistryReference<Block> = of("end_stone_bricks")
    @JvmField
    public val BEETROOTS: RegistryReference<Block> = of("beetroots")
    @JvmField
    public val DIRT_PATH: RegistryReference<Block> = of("dirt_path")
    @JvmField
    public val END_GATEWAY: RegistryReference<Block> = of("end_gateway")
    @JvmField
    public val REPEATING_COMMAND_BLOCK: RegistryReference<Block> = of("repeating_command_block")
    @JvmField
    public val CHAIN_COMMAND_BLOCK: RegistryReference<Block> = of("chain_command_block")
    @JvmField
    public val FROSTED_ICE: RegistryReference<Block> = of("frosted_ice")
    @JvmField
    public val MAGMA_BLOCK: RegistryReference<Block> = of("magma_block")
    @JvmField
    public val NETHER_WART_BLOCK: RegistryReference<Block> = of("nether_wart_block")
    @JvmField
    public val RED_NETHER_BRICKS: RegistryReference<Block> = of("red_nether_bricks")
    @JvmField
    public val BONE_BLOCK: RegistryReference<Block> = of("bone_block")
    @JvmField
    public val STRUCTURE_VOID: RegistryReference<Block> = of("structure_void")
    @JvmField
    public val OBSERVER: RegistryReference<Block> = of("observer")
    @JvmField
    public val SHULKER_BOX: RegistryReference<Block> = of("shulker_box")
    @JvmField
    public val WHITE_SHULKER_BOX: RegistryReference<Block> = of("white_shulker_box")
    @JvmField
    public val ORANGE_SHULKER_BOX: RegistryReference<Block> = of("orange_shulker_box")
    @JvmField
    public val MAGENTA_SHULKER_BOX: RegistryReference<Block> = of("magenta_shulker_box")
    @JvmField
    public val LIGHT_BLUE_SHULKER_BOX: RegistryReference<Block> = of("light_blue_shulker_box")
    @JvmField
    public val YELLOW_SHULKER_BOX: RegistryReference<Block> = of("yellow_shulker_box")
    @JvmField
    public val LIME_SHULKER_BOX: RegistryReference<Block> = of("lime_shulker_box")
    @JvmField
    public val PINK_SHULKER_BOX: RegistryReference<Block> = of("pink_shulker_box")
    @JvmField
    public val GRAY_SHULKER_BOX: RegistryReference<Block> = of("gray_shulker_box")
    @JvmField
    public val LIGHT_GRAY_SHULKER_BOX: RegistryReference<Block> = of("light_gray_shulker_box")
    @JvmField
    public val CYAN_SHULKER_BOX: RegistryReference<Block> = of("cyan_shulker_box")
    @JvmField
    public val PURPLE_SHULKER_BOX: RegistryReference<Block> = of("purple_shulker_box")
    @JvmField
    public val BLUE_SHULKER_BOX: RegistryReference<Block> = of("blue_shulker_box")
    @JvmField
    public val BROWN_SHULKER_BOX: RegistryReference<Block> = of("brown_shulker_box")
    @JvmField
    public val GREEN_SHULKER_BOX: RegistryReference<Block> = of("green_shulker_box")
    @JvmField
    public val RED_SHULKER_BOX: RegistryReference<Block> = of("red_shulker_box")
    @JvmField
    public val BLACK_SHULKER_BOX: RegistryReference<Block> = of("black_shulker_box")
    @JvmField
    public val WHITE_GLAZED_TERRACOTTA: RegistryReference<Block> = of("white_glazed_terracotta")
    @JvmField
    public val ORANGE_GLAZED_TERRACOTTA: RegistryReference<Block> = of("orange_glazed_terracotta")
    @JvmField
    public val MAGENTA_GLAZED_TERRACOTTA: RegistryReference<Block> = of("magenta_glazed_terracotta")
    @JvmField
    public val LIGHT_BLUE_GLAZED_TERRACOTTA: RegistryReference<Block> = of("light_blue_glazed_terracotta")
    @JvmField
    public val YELLOW_GLAZED_TERRACOTTA: RegistryReference<Block> = of("yellow_glazed_terracotta")
    @JvmField
    public val LIME_GLAZED_TERRACOTTA: RegistryReference<Block> = of("lime_glazed_terracotta")
    @JvmField
    public val PINK_GLAZED_TERRACOTTA: RegistryReference<Block> = of("pink_glazed_terracotta")
    @JvmField
    public val GRAY_GLAZED_TERRACOTTA: RegistryReference<Block> = of("gray_glazed_terracotta")
    @JvmField
    public val LIGHT_GRAY_GLAZED_TERRACOTTA: RegistryReference<Block> = of("light_gray_glazed_terracotta")
    @JvmField
    public val CYAN_GLAZED_TERRACOTTA: RegistryReference<Block> = of("cyan_glazed_terracotta")
    @JvmField
    public val PURPLE_GLAZED_TERRACOTTA: RegistryReference<Block> = of("purple_glazed_terracotta")
    @JvmField
    public val BLUE_GLAZED_TERRACOTTA: RegistryReference<Block> = of("blue_glazed_terracotta")
    @JvmField
    public val BROWN_GLAZED_TERRACOTTA: RegistryReference<Block> = of("brown_glazed_terracotta")
    @JvmField
    public val GREEN_GLAZED_TERRACOTTA: RegistryReference<Block> = of("green_glazed_terracotta")
    @JvmField
    public val RED_GLAZED_TERRACOTTA: RegistryReference<Block> = of("red_glazed_terracotta")
    @JvmField
    public val BLACK_GLAZED_TERRACOTTA: RegistryReference<Block> = of("black_glazed_terracotta")
    @JvmField
    public val WHITE_CONCRETE: RegistryReference<Block> = of("white_concrete")
    @JvmField
    public val ORANGE_CONCRETE: RegistryReference<Block> = of("orange_concrete")
    @JvmField
    public val MAGENTA_CONCRETE: RegistryReference<Block> = of("magenta_concrete")
    @JvmField
    public val LIGHT_BLUE_CONCRETE: RegistryReference<Block> = of("light_blue_concrete")
    @JvmField
    public val YELLOW_CONCRETE: RegistryReference<Block> = of("yellow_concrete")
    @JvmField
    public val LIME_CONCRETE: RegistryReference<Block> = of("lime_concrete")
    @JvmField
    public val PINK_CONCRETE: RegistryReference<Block> = of("pink_concrete")
    @JvmField
    public val GRAY_CONCRETE: RegistryReference<Block> = of("gray_concrete")
    @JvmField
    public val LIGHT_GRAY_CONCRETE: RegistryReference<Block> = of("light_gray_concrete")
    @JvmField
    public val CYAN_CONCRETE: RegistryReference<Block> = of("cyan_concrete")
    @JvmField
    public val PURPLE_CONCRETE: RegistryReference<Block> = of("purple_concrete")
    @JvmField
    public val BLUE_CONCRETE: RegistryReference<Block> = of("blue_concrete")
    @JvmField
    public val BROWN_CONCRETE: RegistryReference<Block> = of("brown_concrete")
    @JvmField
    public val GREEN_CONCRETE: RegistryReference<Block> = of("green_concrete")
    @JvmField
    public val RED_CONCRETE: RegistryReference<Block> = of("red_concrete")
    @JvmField
    public val BLACK_CONCRETE: RegistryReference<Block> = of("black_concrete")
    @JvmField
    public val WHITE_CONCRETE_POWDER: RegistryReference<Block> = of("white_concrete_powder")
    @JvmField
    public val ORANGE_CONCRETE_POWDER: RegistryReference<Block> = of("orange_concrete_powder")
    @JvmField
    public val MAGENTA_CONCRETE_POWDER: RegistryReference<Block> = of("magenta_concrete_powder")
    @JvmField
    public val LIGHT_BLUE_CONCRETE_POWDER: RegistryReference<Block> = of("light_blue_concrete_powder")
    @JvmField
    public val YELLOW_CONCRETE_POWDER: RegistryReference<Block> = of("yellow_concrete_powder")
    @JvmField
    public val LIME_CONCRETE_POWDER: RegistryReference<Block> = of("lime_concrete_powder")
    @JvmField
    public val PINK_CONCRETE_POWDER: RegistryReference<Block> = of("pink_concrete_powder")
    @JvmField
    public val GRAY_CONCRETE_POWDER: RegistryReference<Block> = of("gray_concrete_powder")
    @JvmField
    public val LIGHT_GRAY_CONCRETE_POWDER: RegistryReference<Block> = of("light_gray_concrete_powder")
    @JvmField
    public val CYAN_CONCRETE_POWDER: RegistryReference<Block> = of("cyan_concrete_powder")
    @JvmField
    public val PURPLE_CONCRETE_POWDER: RegistryReference<Block> = of("purple_concrete_powder")
    @JvmField
    public val BLUE_CONCRETE_POWDER: RegistryReference<Block> = of("blue_concrete_powder")
    @JvmField
    public val BROWN_CONCRETE_POWDER: RegistryReference<Block> = of("brown_concrete_powder")
    @JvmField
    public val GREEN_CONCRETE_POWDER: RegistryReference<Block> = of("green_concrete_powder")
    @JvmField
    public val RED_CONCRETE_POWDER: RegistryReference<Block> = of("red_concrete_powder")
    @JvmField
    public val BLACK_CONCRETE_POWDER: RegistryReference<Block> = of("black_concrete_powder")
    @JvmField
    public val KELP: RegistryReference<Block> = of("kelp")
    @JvmField
    public val KELP_PLANT: RegistryReference<Block> = of("kelp_plant")
    @JvmField
    public val DRIED_KELP_BLOCK: RegistryReference<Block> = of("dried_kelp_block")
    @JvmField
    public val TURTLE_EGG: RegistryReference<Block> = of("turtle_egg")
    @JvmField
    public val DEAD_TUBE_CORAL_BLOCK: RegistryReference<Block> = of("dead_tube_coral_block")
    @JvmField
    public val DEAD_BRAIN_CORAL_BLOCK: RegistryReference<Block> = of("dead_brain_coral_block")
    @JvmField
    public val DEAD_BUBBLE_CORAL_BLOCK: RegistryReference<Block> = of("dead_bubble_coral_block")
    @JvmField
    public val DEAD_FIRE_CORAL_BLOCK: RegistryReference<Block> = of("dead_fire_coral_block")
    @JvmField
    public val DEAD_HORN_CORAL_BLOCK: RegistryReference<Block> = of("dead_horn_coral_block")
    @JvmField
    public val TUBE_CORAL_BLOCK: RegistryReference<Block> = of("tube_coral_block")
    @JvmField
    public val BRAIN_CORAL_BLOCK: RegistryReference<Block> = of("brain_coral_block")
    @JvmField
    public val BUBBLE_CORAL_BLOCK: RegistryReference<Block> = of("bubble_coral_block")
    @JvmField
    public val FIRE_CORAL_BLOCK: RegistryReference<Block> = of("fire_coral_block")
    @JvmField
    public val HORN_CORAL_BLOCK: RegistryReference<Block> = of("horn_coral_block")
    @JvmField
    public val DEAD_TUBE_CORAL: RegistryReference<Block> = of("dead_tube_coral")
    @JvmField
    public val DEAD_BRAIN_CORAL: RegistryReference<Block> = of("dead_brain_coral")
    @JvmField
    public val DEAD_BUBBLE_CORAL: RegistryReference<Block> = of("dead_bubble_coral")
    @JvmField
    public val DEAD_FIRE_CORAL: RegistryReference<Block> = of("dead_fire_coral")
    @JvmField
    public val DEAD_HORN_CORAL: RegistryReference<Block> = of("dead_horn_coral")
    @JvmField
    public val TUBE_CORAL: RegistryReference<Block> = of("tube_coral")
    @JvmField
    public val BRAIN_CORAL: RegistryReference<Block> = of("brain_coral")
    @JvmField
    public val BUBBLE_CORAL: RegistryReference<Block> = of("bubble_coral")
    @JvmField
    public val FIRE_CORAL: RegistryReference<Block> = of("fire_coral")
    @JvmField
    public val HORN_CORAL: RegistryReference<Block> = of("horn_coral")
    @JvmField
    public val DEAD_TUBE_CORAL_FAN: RegistryReference<Block> = of("dead_tube_coral_fan")
    @JvmField
    public val DEAD_BRAIN_CORAL_FAN: RegistryReference<Block> = of("dead_brain_coral_fan")
    @JvmField
    public val DEAD_BUBBLE_CORAL_FAN: RegistryReference<Block> = of("dead_bubble_coral_fan")
    @JvmField
    public val DEAD_FIRE_CORAL_FAN: RegistryReference<Block> = of("dead_fire_coral_fan")
    @JvmField
    public val DEAD_HORN_CORAL_FAN: RegistryReference<Block> = of("dead_horn_coral_fan")
    @JvmField
    public val TUBE_CORAL_FAN: RegistryReference<Block> = of("tube_coral_fan")
    @JvmField
    public val BRAIN_CORAL_FAN: RegistryReference<Block> = of("brain_coral_fan")
    @JvmField
    public val BUBBLE_CORAL_FAN: RegistryReference<Block> = of("bubble_coral_fan")
    @JvmField
    public val FIRE_CORAL_FAN: RegistryReference<Block> = of("fire_coral_fan")
    @JvmField
    public val HORN_CORAL_FAN: RegistryReference<Block> = of("horn_coral_fan")
    @JvmField
    public val DEAD_TUBE_CORAL_WALL_FAN: RegistryReference<Block> = of("dead_tube_coral_wall_fan")
    @JvmField
    public val DEAD_BRAIN_CORAL_WALL_FAN: RegistryReference<Block> = of("dead_brain_coral_wall_fan")
    @JvmField
    public val DEAD_BUBBLE_CORAL_WALL_FAN: RegistryReference<Block> = of("dead_bubble_coral_wall_fan")
    @JvmField
    public val DEAD_FIRE_CORAL_WALL_FAN: RegistryReference<Block> = of("dead_fire_coral_wall_fan")
    @JvmField
    public val DEAD_HORN_CORAL_WALL_FAN: RegistryReference<Block> = of("dead_horn_coral_wall_fan")
    @JvmField
    public val TUBE_CORAL_WALL_FAN: RegistryReference<Block> = of("tube_coral_wall_fan")
    @JvmField
    public val BRAIN_CORAL_WALL_FAN: RegistryReference<Block> = of("brain_coral_wall_fan")
    @JvmField
    public val BUBBLE_CORAL_WALL_FAN: RegistryReference<Block> = of("bubble_coral_wall_fan")
    @JvmField
    public val FIRE_CORAL_WALL_FAN: RegistryReference<Block> = of("fire_coral_wall_fan")
    @JvmField
    public val HORN_CORAL_WALL_FAN: RegistryReference<Block> = of("horn_coral_wall_fan")
    @JvmField
    public val SEA_PICKLE: RegistryReference<Block> = of("sea_pickle")
    @JvmField
    public val BLUE_ICE: RegistryReference<Block> = of("blue_ice")
    @JvmField
    public val CONDUIT: RegistryReference<Block> = of("conduit")
    @JvmField
    public val BAMBOO_SAPLING: RegistryReference<Block> = of("bamboo_sapling")
    @JvmField
    public val BAMBOO: RegistryReference<Block> = of("bamboo")
    @JvmField
    public val POTTED_BAMBOO: RegistryReference<Block> = of("potted_bamboo")
    @JvmField
    public val VOID_AIR: RegistryReference<Block> = of("void_air")
    @JvmField
    public val CAVE_AIR: RegistryReference<Block> = of("cave_air")
    @JvmField
    public val BUBBLE_COLUMN: RegistryReference<Block> = of("bubble_column")
    @JvmField
    public val POLISHED_GRANITE_STAIRS: RegistryReference<Block> = of("polished_granite_stairs")
    @JvmField
    public val SMOOTH_RED_SANDSTONE_STAIRS: RegistryReference<Block> = of("smooth_red_sandstone_stairs")
    @JvmField
    public val MOSSY_STONE_BRICK_STAIRS: RegistryReference<Block> = of("mossy_stone_brick_stairs")
    @JvmField
    public val POLISHED_DIORITE_STAIRS: RegistryReference<Block> = of("polished_diorite_stairs")
    @JvmField
    public val MOSSY_COBBLESTONE_STAIRS: RegistryReference<Block> = of("mossy_cobblestone_stairs")
    @JvmField
    public val END_STONE_BRICK_STAIRS: RegistryReference<Block> = of("end_stone_brick_stairs")
    @JvmField
    public val STONE_STAIRS: RegistryReference<Block> = of("stone_stairs")
    @JvmField
    public val SMOOTH_SANDSTONE_STAIRS: RegistryReference<Block> = of("smooth_sandstone_stairs")
    @JvmField
    public val SMOOTH_QUARTZ_STAIRS: RegistryReference<Block> = of("smooth_quartz_stairs")
    @JvmField
    public val GRANITE_STAIRS: RegistryReference<Block> = of("granite_stairs")
    @JvmField
    public val ANDESITE_STAIRS: RegistryReference<Block> = of("andesite_stairs")
    @JvmField
    public val RED_NETHER_BRICK_STAIRS: RegistryReference<Block> = of("red_nether_brick_stairs")
    @JvmField
    public val POLISHED_ANDESITE_STAIRS: RegistryReference<Block> = of("polished_andesite_stairs")
    @JvmField
    public val DIORITE_STAIRS: RegistryReference<Block> = of("diorite_stairs")
    @JvmField
    public val POLISHED_GRANITE_SLAB: RegistryReference<Block> = of("polished_granite_slab")
    @JvmField
    public val SMOOTH_RED_SANDSTONE_SLAB: RegistryReference<Block> = of("smooth_red_sandstone_slab")
    @JvmField
    public val MOSSY_STONE_BRICK_SLAB: RegistryReference<Block> = of("mossy_stone_brick_slab")
    @JvmField
    public val POLISHED_DIORITE_SLAB: RegistryReference<Block> = of("polished_diorite_slab")
    @JvmField
    public val MOSSY_COBBLESTONE_SLAB: RegistryReference<Block> = of("mossy_cobblestone_slab")
    @JvmField
    public val END_STONE_BRICK_SLAB: RegistryReference<Block> = of("end_stone_brick_slab")
    @JvmField
    public val SMOOTH_SANDSTONE_SLAB: RegistryReference<Block> = of("smooth_sandstone_slab")
    @JvmField
    public val SMOOTH_QUARTZ_SLAB: RegistryReference<Block> = of("smooth_quartz_slab")
    @JvmField
    public val GRANITE_SLAB: RegistryReference<Block> = of("granite_slab")
    @JvmField
    public val ANDESITE_SLAB: RegistryReference<Block> = of("andesite_slab")
    @JvmField
    public val RED_NETHER_BRICK_SLAB: RegistryReference<Block> = of("red_nether_brick_slab")
    @JvmField
    public val POLISHED_ANDESITE_SLAB: RegistryReference<Block> = of("polished_andesite_slab")
    @JvmField
    public val DIORITE_SLAB: RegistryReference<Block> = of("diorite_slab")
    @JvmField
    public val BRICK_WALL: RegistryReference<Block> = of("brick_wall")
    @JvmField
    public val PRISMARINE_WALL: RegistryReference<Block> = of("prismarine_wall")
    @JvmField
    public val RED_SANDSTONE_WALL: RegistryReference<Block> = of("red_sandstone_wall")
    @JvmField
    public val MOSSY_STONE_BRICK_WALL: RegistryReference<Block> = of("mossy_stone_brick_wall")
    @JvmField
    public val GRANITE_WALL: RegistryReference<Block> = of("granite_wall")
    @JvmField
    public val STONE_BRICK_WALL: RegistryReference<Block> = of("stone_brick_wall")
    @JvmField
    public val MUD_BRICK_WALL: RegistryReference<Block> = of("mud_brick_wall")
    @JvmField
    public val NETHER_BRICK_WALL: RegistryReference<Block> = of("nether_brick_wall")
    @JvmField
    public val ANDESITE_WALL: RegistryReference<Block> = of("andesite_wall")
    @JvmField
    public val RED_NETHER_BRICK_WALL: RegistryReference<Block> = of("red_nether_brick_wall")
    @JvmField
    public val SANDSTONE_WALL: RegistryReference<Block> = of("sandstone_wall")
    @JvmField
    public val END_STONE_BRICK_WALL: RegistryReference<Block> = of("end_stone_brick_wall")
    @JvmField
    public val DIORITE_WALL: RegistryReference<Block> = of("diorite_wall")
    @JvmField
    public val SCAFFOLDING: RegistryReference<Block> = of("scaffolding")
    @JvmField
    public val LOOM: RegistryReference<Block> = of("loom")
    @JvmField
    public val BARREL: RegistryReference<Block> = of("barrel")
    @JvmField
    public val SMOKER: RegistryReference<Block> = of("smoker")
    @JvmField
    public val BLAST_FURNACE: RegistryReference<Block> = of("blast_furnace")
    @JvmField
    public val CARTOGRAPHY_TABLE: RegistryReference<Block> = of("cartography_table")
    @JvmField
    public val FLETCHING_TABLE: RegistryReference<Block> = of("fletching_table")
    @JvmField
    public val GRINDSTONE: RegistryReference<Block> = of("grindstone")
    @JvmField
    public val LECTERN: RegistryReference<Block> = of("lectern")
    @JvmField
    public val SMITHING_TABLE: RegistryReference<Block> = of("smithing_table")
    @JvmField
    public val STONECUTTER: RegistryReference<Block> = of("stonecutter")
    @JvmField
    public val BELL: RegistryReference<Block> = of("bell")
    @JvmField
    public val LANTERN: RegistryReference<Block> = of("lantern")
    @JvmField
    public val SOUL_LANTERN: RegistryReference<Block> = of("soul_lantern")
    @JvmField
    public val CAMPFIRE: RegistryReference<Block> = of("campfire")
    @JvmField
    public val SOUL_CAMPFIRE: RegistryReference<Block> = of("soul_campfire")
    @JvmField
    public val SWEET_BERRY_BUSH: RegistryReference<Block> = of("sweet_berry_bush")
    @JvmField
    public val WARPED_STEM: RegistryReference<Block> = of("warped_stem")
    @JvmField
    public val STRIPPED_WARPED_STEM: RegistryReference<Block> = of("stripped_warped_stem")
    @JvmField
    public val WARPED_HYPHAE: RegistryReference<Block> = of("warped_hyphae")
    @JvmField
    public val STRIPPED_WARPED_HYPHAE: RegistryReference<Block> = of("stripped_warped_hyphae")
    @JvmField
    public val WARPED_NYLIUM: RegistryReference<Block> = of("warped_nylium")
    @JvmField
    public val WARPED_FUNGUS: RegistryReference<Block> = of("warped_fungus")
    @JvmField
    public val WARPED_WART_BLOCK: RegistryReference<Block> = of("warped_wart_block")
    @JvmField
    public val WARPED_ROOTS: RegistryReference<Block> = of("warped_roots")
    @JvmField
    public val NETHER_SPROUTS: RegistryReference<Block> = of("nether_sprouts")
    @JvmField
    public val CRIMSON_STEM: RegistryReference<Block> = of("crimson_stem")
    @JvmField
    public val STRIPPED_CRIMSON_STEM: RegistryReference<Block> = of("stripped_crimson_stem")
    @JvmField
    public val CRIMSON_HYPHAE: RegistryReference<Block> = of("crimson_hyphae")
    @JvmField
    public val STRIPPED_CRIMSON_HYPHAE: RegistryReference<Block> = of("stripped_crimson_hyphae")
    @JvmField
    public val CRIMSON_NYLIUM: RegistryReference<Block> = of("crimson_nylium")
    @JvmField
    public val CRIMSON_FUNGUS: RegistryReference<Block> = of("crimson_fungus")
    @JvmField
    public val SHROOMLIGHT: RegistryReference<Block> = of("shroomlight")
    @JvmField
    public val WEEPING_VINES: RegistryReference<Block> = of("weeping_vines")
    @JvmField
    public val WEEPING_VINES_PLANT: RegistryReference<Block> = of("weeping_vines_plant")
    @JvmField
    public val TWISTING_VINES: RegistryReference<Block> = of("twisting_vines")
    @JvmField
    public val TWISTING_VINES_PLANT: RegistryReference<Block> = of("twisting_vines_plant")
    @JvmField
    public val CRIMSON_ROOTS: RegistryReference<Block> = of("crimson_roots")
    @JvmField
    public val CRIMSON_PLANKS: RegistryReference<Block> = of("crimson_planks")
    @JvmField
    public val WARPED_PLANKS: RegistryReference<Block> = of("warped_planks")
    @JvmField
    public val CRIMSON_SLAB: RegistryReference<Block> = of("crimson_slab")
    @JvmField
    public val WARPED_SLAB: RegistryReference<Block> = of("warped_slab")
    @JvmField
    public val CRIMSON_PRESSURE_PLATE: RegistryReference<Block> = of("crimson_pressure_plate")
    @JvmField
    public val WARPED_PRESSURE_PLATE: RegistryReference<Block> = of("warped_pressure_plate")
    @JvmField
    public val CRIMSON_FENCE: RegistryReference<Block> = of("crimson_fence")
    @JvmField
    public val WARPED_FENCE: RegistryReference<Block> = of("warped_fence")
    @JvmField
    public val CRIMSON_TRAPDOOR: RegistryReference<Block> = of("crimson_trapdoor")
    @JvmField
    public val WARPED_TRAPDOOR: RegistryReference<Block> = of("warped_trapdoor")
    @JvmField
    public val CRIMSON_FENCE_GATE: RegistryReference<Block> = of("crimson_fence_gate")
    @JvmField
    public val WARPED_FENCE_GATE: RegistryReference<Block> = of("warped_fence_gate")
    @JvmField
    public val CRIMSON_STAIRS: RegistryReference<Block> = of("crimson_stairs")
    @JvmField
    public val WARPED_STAIRS: RegistryReference<Block> = of("warped_stairs")
    @JvmField
    public val CRIMSON_BUTTON: RegistryReference<Block> = of("crimson_button")
    @JvmField
    public val WARPED_BUTTON: RegistryReference<Block> = of("warped_button")
    @JvmField
    public val CRIMSON_DOOR: RegistryReference<Block> = of("crimson_door")
    @JvmField
    public val WARPED_DOOR: RegistryReference<Block> = of("warped_door")
    @JvmField
    public val CRIMSON_SIGN: RegistryReference<Block> = of("crimson_sign")
    @JvmField
    public val WARPED_SIGN: RegistryReference<Block> = of("warped_sign")
    @JvmField
    public val CRIMSON_WALL_SIGN: RegistryReference<Block> = of("crimson_wall_sign")
    @JvmField
    public val WARPED_WALL_SIGN: RegistryReference<Block> = of("warped_wall_sign")
    @JvmField
    public val STRUCTURE_BLOCK: RegistryReference<Block> = of("structure_block")
    @JvmField
    public val JIGSAW: RegistryReference<Block> = of("jigsaw")
    @JvmField
    public val COMPOSTER: RegistryReference<Block> = of("composter")
    @JvmField
    public val TARGET: RegistryReference<Block> = of("target")
    @JvmField
    public val BEE_NEST: RegistryReference<Block> = of("bee_nest")
    @JvmField
    public val BEEHIVE: RegistryReference<Block> = of("beehive")
    @JvmField
    public val HONEY_BLOCK: RegistryReference<Block> = of("honey_block")
    @JvmField
    public val HONEYCOMB_BLOCK: RegistryReference<Block> = of("honeycomb_block")
    @JvmField
    public val NETHERITE_BLOCK: RegistryReference<Block> = of("netherite_block")
    @JvmField
    public val ANCIENT_DEBRIS: RegistryReference<Block> = of("ancient_debris")
    @JvmField
    public val CRYING_OBSIDIAN: RegistryReference<Block> = of("crying_obsidian")
    @JvmField
    public val RESPAWN_ANCHOR: RegistryReference<Block> = of("respawn_anchor")
    @JvmField
    public val POTTED_CRIMSON_FUNGUS: RegistryReference<Block> = of("potted_crimson_fungus")
    @JvmField
    public val POTTED_WARPED_FUNGUS: RegistryReference<Block> = of("potted_warped_fungus")
    @JvmField
    public val POTTED_CRIMSON_ROOTS: RegistryReference<Block> = of("potted_crimson_roots")
    @JvmField
    public val POTTED_WARPED_ROOTS: RegistryReference<Block> = of("potted_warped_roots")
    @JvmField
    public val LODESTONE: RegistryReference<Block> = of("lodestone")
    @JvmField
    public val BLACKSTONE: RegistryReference<Block> = of("blackstone")
    @JvmField
    public val BLACKSTONE_STAIRS: RegistryReference<Block> = of("blackstone_stairs")
    @JvmField
    public val BLACKSTONE_WALL: RegistryReference<Block> = of("blackstone_wall")
    @JvmField
    public val BLACKSTONE_SLAB: RegistryReference<Block> = of("blackstone_slab")
    @JvmField
    public val POLISHED_BLACKSTONE: RegistryReference<Block> = of("polished_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICKS: RegistryReference<Block> = of("polished_blackstone_bricks")
    @JvmField
    public val CRACKED_POLISHED_BLACKSTONE_BRICKS: RegistryReference<Block> = of("cracked_polished_blackstone_bricks")
    @JvmField
    public val CHISELED_POLISHED_BLACKSTONE: RegistryReference<Block> = of("chiseled_polished_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_SLAB: RegistryReference<Block> = of("polished_blackstone_brick_slab")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_STAIRS: RegistryReference<Block> = of("polished_blackstone_brick_stairs")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_WALL: RegistryReference<Block> = of("polished_blackstone_brick_wall")
    @JvmField
    public val GILDED_BLACKSTONE: RegistryReference<Block> = of("gilded_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_STAIRS: RegistryReference<Block> = of("polished_blackstone_stairs")
    @JvmField
    public val POLISHED_BLACKSTONE_SLAB: RegistryReference<Block> = of("polished_blackstone_slab")
    @JvmField
    public val POLISHED_BLACKSTONE_PRESSURE_PLATE: RegistryReference<Block> = of("polished_blackstone_pressure_plate")
    @JvmField
    public val POLISHED_BLACKSTONE_BUTTON: RegistryReference<Block> = of("polished_blackstone_button")
    @JvmField
    public val POLISHED_BLACKSTONE_WALL: RegistryReference<Block> = of("polished_blackstone_wall")
    @JvmField
    public val CHISELED_NETHER_BRICKS: RegistryReference<Block> = of("chiseled_nether_bricks")
    @JvmField
    public val CRACKED_NETHER_BRICKS: RegistryReference<Block> = of("cracked_nether_bricks")
    @JvmField
    public val QUARTZ_BRICKS: RegistryReference<Block> = of("quartz_bricks")
    @JvmField
    public val CANDLE: RegistryReference<Block> = of("candle")
    @JvmField
    public val WHITE_CANDLE: RegistryReference<Block> = of("white_candle")
    @JvmField
    public val ORANGE_CANDLE: RegistryReference<Block> = of("orange_candle")
    @JvmField
    public val MAGENTA_CANDLE: RegistryReference<Block> = of("magenta_candle")
    @JvmField
    public val LIGHT_BLUE_CANDLE: RegistryReference<Block> = of("light_blue_candle")
    @JvmField
    public val YELLOW_CANDLE: RegistryReference<Block> = of("yellow_candle")
    @JvmField
    public val LIME_CANDLE: RegistryReference<Block> = of("lime_candle")
    @JvmField
    public val PINK_CANDLE: RegistryReference<Block> = of("pink_candle")
    @JvmField
    public val GRAY_CANDLE: RegistryReference<Block> = of("gray_candle")
    @JvmField
    public val LIGHT_GRAY_CANDLE: RegistryReference<Block> = of("light_gray_candle")
    @JvmField
    public val CYAN_CANDLE: RegistryReference<Block> = of("cyan_candle")
    @JvmField
    public val PURPLE_CANDLE: RegistryReference<Block> = of("purple_candle")
    @JvmField
    public val BLUE_CANDLE: RegistryReference<Block> = of("blue_candle")
    @JvmField
    public val BROWN_CANDLE: RegistryReference<Block> = of("brown_candle")
    @JvmField
    public val GREEN_CANDLE: RegistryReference<Block> = of("green_candle")
    @JvmField
    public val RED_CANDLE: RegistryReference<Block> = of("red_candle")
    @JvmField
    public val BLACK_CANDLE: RegistryReference<Block> = of("black_candle")
    @JvmField
    public val CANDLE_CAKE: RegistryReference<Block> = of("candle_cake")
    @JvmField
    public val WHITE_CANDLE_CAKE: RegistryReference<Block> = of("white_candle_cake")
    @JvmField
    public val ORANGE_CANDLE_CAKE: RegistryReference<Block> = of("orange_candle_cake")
    @JvmField
    public val MAGENTA_CANDLE_CAKE: RegistryReference<Block> = of("magenta_candle_cake")
    @JvmField
    public val LIGHT_BLUE_CANDLE_CAKE: RegistryReference<Block> = of("light_blue_candle_cake")
    @JvmField
    public val YELLOW_CANDLE_CAKE: RegistryReference<Block> = of("yellow_candle_cake")
    @JvmField
    public val LIME_CANDLE_CAKE: RegistryReference<Block> = of("lime_candle_cake")
    @JvmField
    public val PINK_CANDLE_CAKE: RegistryReference<Block> = of("pink_candle_cake")
    @JvmField
    public val GRAY_CANDLE_CAKE: RegistryReference<Block> = of("gray_candle_cake")
    @JvmField
    public val LIGHT_GRAY_CANDLE_CAKE: RegistryReference<Block> = of("light_gray_candle_cake")
    @JvmField
    public val CYAN_CANDLE_CAKE: RegistryReference<Block> = of("cyan_candle_cake")
    @JvmField
    public val PURPLE_CANDLE_CAKE: RegistryReference<Block> = of("purple_candle_cake")
    @JvmField
    public val BLUE_CANDLE_CAKE: RegistryReference<Block> = of("blue_candle_cake")
    @JvmField
    public val BROWN_CANDLE_CAKE: RegistryReference<Block> = of("brown_candle_cake")
    @JvmField
    public val GREEN_CANDLE_CAKE: RegistryReference<Block> = of("green_candle_cake")
    @JvmField
    public val RED_CANDLE_CAKE: RegistryReference<Block> = of("red_candle_cake")
    @JvmField
    public val BLACK_CANDLE_CAKE: RegistryReference<Block> = of("black_candle_cake")
    @JvmField
    public val AMETHYST_BLOCK: RegistryReference<Block> = of("amethyst_block")
    @JvmField
    public val BUDDING_AMETHYST: RegistryReference<Block> = of("budding_amethyst")
    @JvmField
    public val AMETHYST_CLUSTER: RegistryReference<Block> = of("amethyst_cluster")
    @JvmField
    public val LARGE_AMETHYST_BUD: RegistryReference<Block> = of("large_amethyst_bud")
    @JvmField
    public val MEDIUM_AMETHYST_BUD: RegistryReference<Block> = of("medium_amethyst_bud")
    @JvmField
    public val SMALL_AMETHYST_BUD: RegistryReference<Block> = of("small_amethyst_bud")
    @JvmField
    public val TUFF: RegistryReference<Block> = of("tuff")
    @JvmField
    public val CALCITE: RegistryReference<Block> = of("calcite")
    @JvmField
    public val TINTED_GLASS: RegistryReference<Block> = of("tinted_glass")
    @JvmField
    public val POWDER_SNOW: RegistryReference<Block> = of("powder_snow")
    @JvmField
    public val SCULK_SENSOR: RegistryReference<Block> = of("sculk_sensor")
    @JvmField
    public val SCULK: RegistryReference<Block> = of("sculk")
    @JvmField
    public val SCULK_VEIN: RegistryReference<Block> = of("sculk_vein")
    @JvmField
    public val SCULK_CATALYST: RegistryReference<Block> = of("sculk_catalyst")
    @JvmField
    public val SCULK_SHRIEKER: RegistryReference<Block> = of("sculk_shrieker")
    @JvmField
    public val OXIDIZED_COPPER: RegistryReference<Block> = of("oxidized_copper")
    @JvmField
    public val WEATHERED_COPPER: RegistryReference<Block> = of("weathered_copper")
    @JvmField
    public val EXPOSED_COPPER: RegistryReference<Block> = of("exposed_copper")
    @JvmField
    public val COPPER_BLOCK: RegistryReference<Block> = of("copper_block")
    @JvmField
    public val COPPER_ORE: RegistryReference<Block> = of("copper_ore")
    @JvmField
    public val DEEPSLATE_COPPER_ORE: RegistryReference<Block> = of("deepslate_copper_ore")
    @JvmField
    public val OXIDIZED_CUT_COPPER: RegistryReference<Block> = of("oxidized_cut_copper")
    @JvmField
    public val WEATHERED_CUT_COPPER: RegistryReference<Block> = of("weathered_cut_copper")
    @JvmField
    public val EXPOSED_CUT_COPPER: RegistryReference<Block> = of("exposed_cut_copper")
    @JvmField
    public val CUT_COPPER: RegistryReference<Block> = of("cut_copper")
    @JvmField
    public val OXIDIZED_CUT_COPPER_STAIRS: RegistryReference<Block> = of("oxidized_cut_copper_stairs")
    @JvmField
    public val WEATHERED_CUT_COPPER_STAIRS: RegistryReference<Block> = of("weathered_cut_copper_stairs")
    @JvmField
    public val EXPOSED_CUT_COPPER_STAIRS: RegistryReference<Block> = of("exposed_cut_copper_stairs")
    @JvmField
    public val CUT_COPPER_STAIRS: RegistryReference<Block> = of("cut_copper_stairs")
    @JvmField
    public val OXIDIZED_CUT_COPPER_SLAB: RegistryReference<Block> = of("oxidized_cut_copper_slab")
    @JvmField
    public val WEATHERED_CUT_COPPER_SLAB: RegistryReference<Block> = of("weathered_cut_copper_slab")
    @JvmField
    public val EXPOSED_CUT_COPPER_SLAB: RegistryReference<Block> = of("exposed_cut_copper_slab")
    @JvmField
    public val CUT_COPPER_SLAB: RegistryReference<Block> = of("cut_copper_slab")
    @JvmField
    public val WAXED_COPPER_BLOCK: RegistryReference<Block> = of("waxed_copper_block")
    @JvmField
    public val WAXED_WEATHERED_COPPER: RegistryReference<Block> = of("waxed_weathered_copper")
    @JvmField
    public val WAXED_EXPOSED_COPPER: RegistryReference<Block> = of("waxed_exposed_copper")
    @JvmField
    public val WAXED_OXIDIZED_COPPER: RegistryReference<Block> = of("waxed_oxidized_copper")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER: RegistryReference<Block> = of("waxed_oxidized_cut_copper")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER: RegistryReference<Block> = of("waxed_weathered_cut_copper")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER: RegistryReference<Block> = of("waxed_exposed_cut_copper")
    @JvmField
    public val WAXED_CUT_COPPER: RegistryReference<Block> = of("waxed_cut_copper")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER_STAIRS: RegistryReference<Block> = of("waxed_oxidized_cut_copper_stairs")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER_STAIRS: RegistryReference<Block> = of("waxed_weathered_cut_copper_stairs")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER_STAIRS: RegistryReference<Block> = of("waxed_exposed_cut_copper_stairs")
    @JvmField
    public val WAXED_CUT_COPPER_STAIRS: RegistryReference<Block> = of("waxed_cut_copper_stairs")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER_SLAB: RegistryReference<Block> = of("waxed_oxidized_cut_copper_slab")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER_SLAB: RegistryReference<Block> = of("waxed_weathered_cut_copper_slab")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER_SLAB: RegistryReference<Block> = of("waxed_exposed_cut_copper_slab")
    @JvmField
    public val WAXED_CUT_COPPER_SLAB: RegistryReference<Block> = of("waxed_cut_copper_slab")
    @JvmField
    public val LIGHTNING_ROD: RegistryReference<Block> = of("lightning_rod")
    @JvmField
    public val POINTED_DRIPSTONE: RegistryReference<Block> = of("pointed_dripstone")
    @JvmField
    public val DRIPSTONE_BLOCK: RegistryReference<Block> = of("dripstone_block")
    @JvmField
    public val CAVE_VINES: RegistryReference<Block> = of("cave_vines")
    @JvmField
    public val CAVE_VINES_PLANT: RegistryReference<Block> = of("cave_vines_plant")
    @JvmField
    public val SPORE_BLOSSOM: RegistryReference<Block> = of("spore_blossom")
    @JvmField
    public val AZALEA: RegistryReference<Block> = of("azalea")
    @JvmField
    public val FLOWERING_AZALEA: RegistryReference<Block> = of("flowering_azalea")
    @JvmField
    public val MOSS_CARPET: RegistryReference<Block> = of("moss_carpet")
    @JvmField
    public val MOSS_BLOCK: RegistryReference<Block> = of("moss_block")
    @JvmField
    public val BIG_DRIPLEAF: RegistryReference<Block> = of("big_dripleaf")
    @JvmField
    public val BIG_DRIPLEAF_STEM: RegistryReference<Block> = of("big_dripleaf_stem")
    @JvmField
    public val SMALL_DRIPLEAF: RegistryReference<Block> = of("small_dripleaf")
    @JvmField
    public val HANGING_ROOTS: RegistryReference<Block> = of("hanging_roots")
    @JvmField
    public val ROOTED_DIRT: RegistryReference<Block> = of("rooted_dirt")
    @JvmField
    public val MUD: RegistryReference<Block> = of("mud")
    @JvmField
    public val DEEPSLATE: RegistryReference<Block> = of("deepslate")
    @JvmField
    public val COBBLED_DEEPSLATE: RegistryReference<Block> = of("cobbled_deepslate")
    @JvmField
    public val COBBLED_DEEPSLATE_STAIRS: RegistryReference<Block> = of("cobbled_deepslate_stairs")
    @JvmField
    public val COBBLED_DEEPSLATE_SLAB: RegistryReference<Block> = of("cobbled_deepslate_slab")
    @JvmField
    public val COBBLED_DEEPSLATE_WALL: RegistryReference<Block> = of("cobbled_deepslate_wall")
    @JvmField
    public val POLISHED_DEEPSLATE: RegistryReference<Block> = of("polished_deepslate")
    @JvmField
    public val POLISHED_DEEPSLATE_STAIRS: RegistryReference<Block> = of("polished_deepslate_stairs")
    @JvmField
    public val POLISHED_DEEPSLATE_SLAB: RegistryReference<Block> = of("polished_deepslate_slab")
    @JvmField
    public val POLISHED_DEEPSLATE_WALL: RegistryReference<Block> = of("polished_deepslate_wall")
    @JvmField
    public val DEEPSLATE_TILES: RegistryReference<Block> = of("deepslate_tiles")
    @JvmField
    public val DEEPSLATE_TILE_STAIRS: RegistryReference<Block> = of("deepslate_tile_stairs")
    @JvmField
    public val DEEPSLATE_TILE_SLAB: RegistryReference<Block> = of("deepslate_tile_slab")
    @JvmField
    public val DEEPSLATE_TILE_WALL: RegistryReference<Block> = of("deepslate_tile_wall")
    @JvmField
    public val DEEPSLATE_BRICKS: RegistryReference<Block> = of("deepslate_bricks")
    @JvmField
    public val DEEPSLATE_BRICK_STAIRS: RegistryReference<Block> = of("deepslate_brick_stairs")
    @JvmField
    public val DEEPSLATE_BRICK_SLAB: RegistryReference<Block> = of("deepslate_brick_slab")
    @JvmField
    public val DEEPSLATE_BRICK_WALL: RegistryReference<Block> = of("deepslate_brick_wall")
    @JvmField
    public val CHISELED_DEEPSLATE: RegistryReference<Block> = of("chiseled_deepslate")
    @JvmField
    public val CRACKED_DEEPSLATE_BRICKS: RegistryReference<Block> = of("cracked_deepslate_bricks")
    @JvmField
    public val CRACKED_DEEPSLATE_TILES: RegistryReference<Block> = of("cracked_deepslate_tiles")
    @JvmField
    public val INFESTED_DEEPSLATE: RegistryReference<Block> = of("infested_deepslate")
    @JvmField
    public val SMOOTH_BASALT: RegistryReference<Block> = of("smooth_basalt")
    @JvmField
    public val RAW_IRON_BLOCK: RegistryReference<Block> = of("raw_iron_block")
    @JvmField
    public val RAW_COPPER_BLOCK: RegistryReference<Block> = of("raw_copper_block")
    @JvmField
    public val RAW_GOLD_BLOCK: RegistryReference<Block> = of("raw_gold_block")
    @JvmField
    public val POTTED_AZALEA: RegistryReference<Block> = of("potted_azalea_bush")
    @JvmField
    public val POTTED_FLOWERING_AZALEA: RegistryReference<Block> = of("potted_flowering_azalea_bush")
    @JvmField
    public val OCHRE_FROGLIGHT: RegistryReference<Block> = of("ochre_froglight")
    @JvmField
    public val VERDANT_FROGLIGHT: RegistryReference<Block> = of("verdant_froglight")
    @JvmField
    public val PEARLESCENT_FROGLIGHT: RegistryReference<Block> = of("pearlescent_froglight")
    @JvmField
    public val FROGSPAWN: RegistryReference<Block> = of("frogspawn")
    @JvmField
    public val REINFORCED_DEEPSLATE: RegistryReference<Block> = of("reinforced_deepslate")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<Block> = RegistryReference.of(Registries.BLOCK, Key.key(name))
}

/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.registry.RegistryReference
import org.kryptonmc.internal.annotations.Catalogue

/**
 * This file is auto-generated. Do not edit this manually!
 */
@Catalogue(ItemType::class)
public object ItemTypes {

    // @formatter:off
    @JvmField
    public val AIR: RegistryReference<ItemType> = of("air")
    @JvmField
    public val STONE: RegistryReference<ItemType> = of("stone")
    @JvmField
    public val GRANITE: RegistryReference<ItemType> = of("granite")
    @JvmField
    public val POLISHED_GRANITE: RegistryReference<ItemType> = of("polished_granite")
    @JvmField
    public val DIORITE: RegistryReference<ItemType> = of("diorite")
    @JvmField
    public val POLISHED_DIORITE: RegistryReference<ItemType> = of("polished_diorite")
    @JvmField
    public val ANDESITE: RegistryReference<ItemType> = of("andesite")
    @JvmField
    public val POLISHED_ANDESITE: RegistryReference<ItemType> = of("polished_andesite")
    @JvmField
    public val DEEPSLATE: RegistryReference<ItemType> = of("deepslate")
    @JvmField
    public val COBBLED_DEEPSLATE: RegistryReference<ItemType> = of("cobbled_deepslate")
    @JvmField
    public val POLISHED_DEEPSLATE: RegistryReference<ItemType> = of("polished_deepslate")
    @JvmField
    public val CALCITE: RegistryReference<ItemType> = of("calcite")
    @JvmField
    public val TUFF: RegistryReference<ItemType> = of("tuff")
    @JvmField
    public val DRIPSTONE_BLOCK: RegistryReference<ItemType> = of("dripstone_block")
    @JvmField
    public val GRASS_BLOCK: RegistryReference<ItemType> = of("grass_block")
    @JvmField
    public val DIRT: RegistryReference<ItemType> = of("dirt")
    @JvmField
    public val COARSE_DIRT: RegistryReference<ItemType> = of("coarse_dirt")
    @JvmField
    public val PODZOL: RegistryReference<ItemType> = of("podzol")
    @JvmField
    public val ROOTED_DIRT: RegistryReference<ItemType> = of("rooted_dirt")
    @JvmField
    public val MUD: RegistryReference<ItemType> = of("mud")
    @JvmField
    public val CRIMSON_NYLIUM: RegistryReference<ItemType> = of("crimson_nylium")
    @JvmField
    public val WARPED_NYLIUM: RegistryReference<ItemType> = of("warped_nylium")
    @JvmField
    public val COBBLESTONE: RegistryReference<ItemType> = of("cobblestone")
    @JvmField
    public val OAK_PLANKS: RegistryReference<ItemType> = of("oak_planks")
    @JvmField
    public val SPRUCE_PLANKS: RegistryReference<ItemType> = of("spruce_planks")
    @JvmField
    public val BIRCH_PLANKS: RegistryReference<ItemType> = of("birch_planks")
    @JvmField
    public val JUNGLE_PLANKS: RegistryReference<ItemType> = of("jungle_planks")
    @JvmField
    public val ACACIA_PLANKS: RegistryReference<ItemType> = of("acacia_planks")
    @JvmField
    public val DARK_OAK_PLANKS: RegistryReference<ItemType> = of("dark_oak_planks")
    @JvmField
    public val MANGROVE_PLANKS: RegistryReference<ItemType> = of("mangrove_planks")
    @JvmField
    public val CRIMSON_PLANKS: RegistryReference<ItemType> = of("crimson_planks")
    @JvmField
    public val WARPED_PLANKS: RegistryReference<ItemType> = of("warped_planks")
    @JvmField
    public val OAK_SAPLING: RegistryReference<ItemType> = of("oak_sapling")
    @JvmField
    public val SPRUCE_SAPLING: RegistryReference<ItemType> = of("spruce_sapling")
    @JvmField
    public val BIRCH_SAPLING: RegistryReference<ItemType> = of("birch_sapling")
    @JvmField
    public val JUNGLE_SAPLING: RegistryReference<ItemType> = of("jungle_sapling")
    @JvmField
    public val ACACIA_SAPLING: RegistryReference<ItemType> = of("acacia_sapling")
    @JvmField
    public val DARK_OAK_SAPLING: RegistryReference<ItemType> = of("dark_oak_sapling")
    @JvmField
    public val MANGROVE_PROPAGULE: RegistryReference<ItemType> = of("mangrove_propagule")
    @JvmField
    public val BEDROCK: RegistryReference<ItemType> = of("bedrock")
    @JvmField
    public val SAND: RegistryReference<ItemType> = of("sand")
    @JvmField
    public val RED_SAND: RegistryReference<ItemType> = of("red_sand")
    @JvmField
    public val GRAVEL: RegistryReference<ItemType> = of("gravel")
    @JvmField
    public val COAL_ORE: RegistryReference<ItemType> = of("coal_ore")
    @JvmField
    public val DEEPSLATE_COAL_ORE: RegistryReference<ItemType> = of("deepslate_coal_ore")
    @JvmField
    public val IRON_ORE: RegistryReference<ItemType> = of("iron_ore")
    @JvmField
    public val DEEPSLATE_IRON_ORE: RegistryReference<ItemType> = of("deepslate_iron_ore")
    @JvmField
    public val COPPER_ORE: RegistryReference<ItemType> = of("copper_ore")
    @JvmField
    public val DEEPSLATE_COPPER_ORE: RegistryReference<ItemType> = of("deepslate_copper_ore")
    @JvmField
    public val GOLD_ORE: RegistryReference<ItemType> = of("gold_ore")
    @JvmField
    public val DEEPSLATE_GOLD_ORE: RegistryReference<ItemType> = of("deepslate_gold_ore")
    @JvmField
    public val REDSTONE_ORE: RegistryReference<ItemType> = of("redstone_ore")
    @JvmField
    public val DEEPSLATE_REDSTONE_ORE: RegistryReference<ItemType> = of("deepslate_redstone_ore")
    @JvmField
    public val EMERALD_ORE: RegistryReference<ItemType> = of("emerald_ore")
    @JvmField
    public val DEEPSLATE_EMERALD_ORE: RegistryReference<ItemType> = of("deepslate_emerald_ore")
    @JvmField
    public val LAPIS_ORE: RegistryReference<ItemType> = of("lapis_ore")
    @JvmField
    public val DEEPSLATE_LAPIS_ORE: RegistryReference<ItemType> = of("deepslate_lapis_ore")
    @JvmField
    public val DIAMOND_ORE: RegistryReference<ItemType> = of("diamond_ore")
    @JvmField
    public val DEEPSLATE_DIAMOND_ORE: RegistryReference<ItemType> = of("deepslate_diamond_ore")
    @JvmField
    public val NETHER_GOLD_ORE: RegistryReference<ItemType> = of("nether_gold_ore")
    @JvmField
    public val NETHER_QUARTZ_ORE: RegistryReference<ItemType> = of("nether_quartz_ore")
    @JvmField
    public val ANCIENT_DEBRIS: RegistryReference<ItemType> = of("ancient_debris")
    @JvmField
    public val COAL_BLOCK: RegistryReference<ItemType> = of("coal_block")
    @JvmField
    public val RAW_IRON_BLOCK: RegistryReference<ItemType> = of("raw_iron_block")
    @JvmField
    public val RAW_COPPER_BLOCK: RegistryReference<ItemType> = of("raw_copper_block")
    @JvmField
    public val RAW_GOLD_BLOCK: RegistryReference<ItemType> = of("raw_gold_block")
    @JvmField
    public val AMETHYST_BLOCK: RegistryReference<ItemType> = of("amethyst_block")
    @JvmField
    public val BUDDING_AMETHYST: RegistryReference<ItemType> = of("budding_amethyst")
    @JvmField
    public val IRON_BLOCK: RegistryReference<ItemType> = of("iron_block")
    @JvmField
    public val COPPER_BLOCK: RegistryReference<ItemType> = of("copper_block")
    @JvmField
    public val GOLD_BLOCK: RegistryReference<ItemType> = of("gold_block")
    @JvmField
    public val DIAMOND_BLOCK: RegistryReference<ItemType> = of("diamond_block")
    @JvmField
    public val NETHERITE_BLOCK: RegistryReference<ItemType> = of("netherite_block")
    @JvmField
    public val EXPOSED_COPPER: RegistryReference<ItemType> = of("exposed_copper")
    @JvmField
    public val WEATHERED_COPPER: RegistryReference<ItemType> = of("weathered_copper")
    @JvmField
    public val OXIDIZED_COPPER: RegistryReference<ItemType> = of("oxidized_copper")
    @JvmField
    public val CUT_COPPER: RegistryReference<ItemType> = of("cut_copper")
    @JvmField
    public val EXPOSED_CUT_COPPER: RegistryReference<ItemType> = of("exposed_cut_copper")
    @JvmField
    public val WEATHERED_CUT_COPPER: RegistryReference<ItemType> = of("weathered_cut_copper")
    @JvmField
    public val OXIDIZED_CUT_COPPER: RegistryReference<ItemType> = of("oxidized_cut_copper")
    @JvmField
    public val CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("cut_copper_stairs")
    @JvmField
    public val EXPOSED_CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("exposed_cut_copper_stairs")
    @JvmField
    public val WEATHERED_CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("weathered_cut_copper_stairs")
    @JvmField
    public val OXIDIZED_CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("oxidized_cut_copper_stairs")
    @JvmField
    public val CUT_COPPER_SLAB: RegistryReference<ItemType> = of("cut_copper_slab")
    @JvmField
    public val EXPOSED_CUT_COPPER_SLAB: RegistryReference<ItemType> = of("exposed_cut_copper_slab")
    @JvmField
    public val WEATHERED_CUT_COPPER_SLAB: RegistryReference<ItemType> = of("weathered_cut_copper_slab")
    @JvmField
    public val OXIDIZED_CUT_COPPER_SLAB: RegistryReference<ItemType> = of("oxidized_cut_copper_slab")
    @JvmField
    public val WAXED_COPPER_BLOCK: RegistryReference<ItemType> = of("waxed_copper_block")
    @JvmField
    public val WAXED_EXPOSED_COPPER: RegistryReference<ItemType> = of("waxed_exposed_copper")
    @JvmField
    public val WAXED_WEATHERED_COPPER: RegistryReference<ItemType> = of("waxed_weathered_copper")
    @JvmField
    public val WAXED_OXIDIZED_COPPER: RegistryReference<ItemType> = of("waxed_oxidized_copper")
    @JvmField
    public val WAXED_CUT_COPPER: RegistryReference<ItemType> = of("waxed_cut_copper")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER: RegistryReference<ItemType> = of("waxed_exposed_cut_copper")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER: RegistryReference<ItemType> = of("waxed_weathered_cut_copper")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER: RegistryReference<ItemType> = of("waxed_oxidized_cut_copper")
    @JvmField
    public val WAXED_CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("waxed_cut_copper_stairs")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("waxed_exposed_cut_copper_stairs")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("waxed_weathered_cut_copper_stairs")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER_STAIRS: RegistryReference<ItemType> = of("waxed_oxidized_cut_copper_stairs")
    @JvmField
    public val WAXED_CUT_COPPER_SLAB: RegistryReference<ItemType> = of("waxed_cut_copper_slab")
    @JvmField
    public val WAXED_EXPOSED_CUT_COPPER_SLAB: RegistryReference<ItemType> = of("waxed_exposed_cut_copper_slab")
    @JvmField
    public val WAXED_WEATHERED_CUT_COPPER_SLAB: RegistryReference<ItemType> = of("waxed_weathered_cut_copper_slab")
    @JvmField
    public val WAXED_OXIDIZED_CUT_COPPER_SLAB: RegistryReference<ItemType> = of("waxed_oxidized_cut_copper_slab")
    @JvmField
    public val OAK_LOG: RegistryReference<ItemType> = of("oak_log")
    @JvmField
    public val SPRUCE_LOG: RegistryReference<ItemType> = of("spruce_log")
    @JvmField
    public val BIRCH_LOG: RegistryReference<ItemType> = of("birch_log")
    @JvmField
    public val JUNGLE_LOG: RegistryReference<ItemType> = of("jungle_log")
    @JvmField
    public val ACACIA_LOG: RegistryReference<ItemType> = of("acacia_log")
    @JvmField
    public val DARK_OAK_LOG: RegistryReference<ItemType> = of("dark_oak_log")
    @JvmField
    public val MANGROVE_LOG: RegistryReference<ItemType> = of("mangrove_log")
    @JvmField
    public val MANGROVE_ROOTS: RegistryReference<ItemType> = of("mangrove_roots")
    @JvmField
    public val MUDDY_MANGROVE_ROOTS: RegistryReference<ItemType> = of("muddy_mangrove_roots")
    @JvmField
    public val CRIMSON_STEM: RegistryReference<ItemType> = of("crimson_stem")
    @JvmField
    public val WARPED_STEM: RegistryReference<ItemType> = of("warped_stem")
    @JvmField
    public val STRIPPED_OAK_LOG: RegistryReference<ItemType> = of("stripped_oak_log")
    @JvmField
    public val STRIPPED_SPRUCE_LOG: RegistryReference<ItemType> = of("stripped_spruce_log")
    @JvmField
    public val STRIPPED_BIRCH_LOG: RegistryReference<ItemType> = of("stripped_birch_log")
    @JvmField
    public val STRIPPED_JUNGLE_LOG: RegistryReference<ItemType> = of("stripped_jungle_log")
    @JvmField
    public val STRIPPED_ACACIA_LOG: RegistryReference<ItemType> = of("stripped_acacia_log")
    @JvmField
    public val STRIPPED_DARK_OAK_LOG: RegistryReference<ItemType> = of("stripped_dark_oak_log")
    @JvmField
    public val STRIPPED_MANGROVE_LOG: RegistryReference<ItemType> = of("stripped_mangrove_log")
    @JvmField
    public val STRIPPED_CRIMSON_STEM: RegistryReference<ItemType> = of("stripped_crimson_stem")
    @JvmField
    public val STRIPPED_WARPED_STEM: RegistryReference<ItemType> = of("stripped_warped_stem")
    @JvmField
    public val STRIPPED_OAK_WOOD: RegistryReference<ItemType> = of("stripped_oak_wood")
    @JvmField
    public val STRIPPED_SPRUCE_WOOD: RegistryReference<ItemType> = of("stripped_spruce_wood")
    @JvmField
    public val STRIPPED_BIRCH_WOOD: RegistryReference<ItemType> = of("stripped_birch_wood")
    @JvmField
    public val STRIPPED_JUNGLE_WOOD: RegistryReference<ItemType> = of("stripped_jungle_wood")
    @JvmField
    public val STRIPPED_ACACIA_WOOD: RegistryReference<ItemType> = of("stripped_acacia_wood")
    @JvmField
    public val STRIPPED_DARK_OAK_WOOD: RegistryReference<ItemType> = of("stripped_dark_oak_wood")
    @JvmField
    public val STRIPPED_MANGROVE_WOOD: RegistryReference<ItemType> = of("stripped_mangrove_wood")
    @JvmField
    public val STRIPPED_CRIMSON_HYPHAE: RegistryReference<ItemType> = of("stripped_crimson_hyphae")
    @JvmField
    public val STRIPPED_WARPED_HYPHAE: RegistryReference<ItemType> = of("stripped_warped_hyphae")
    @JvmField
    public val OAK_WOOD: RegistryReference<ItemType> = of("oak_wood")
    @JvmField
    public val SPRUCE_WOOD: RegistryReference<ItemType> = of("spruce_wood")
    @JvmField
    public val BIRCH_WOOD: RegistryReference<ItemType> = of("birch_wood")
    @JvmField
    public val JUNGLE_WOOD: RegistryReference<ItemType> = of("jungle_wood")
    @JvmField
    public val ACACIA_WOOD: RegistryReference<ItemType> = of("acacia_wood")
    @JvmField
    public val DARK_OAK_WOOD: RegistryReference<ItemType> = of("dark_oak_wood")
    @JvmField
    public val MANGROVE_WOOD: RegistryReference<ItemType> = of("mangrove_wood")
    @JvmField
    public val CRIMSON_HYPHAE: RegistryReference<ItemType> = of("crimson_hyphae")
    @JvmField
    public val WARPED_HYPHAE: RegistryReference<ItemType> = of("warped_hyphae")
    @JvmField
    public val OAK_LEAVES: RegistryReference<ItemType> = of("oak_leaves")
    @JvmField
    public val SPRUCE_LEAVES: RegistryReference<ItemType> = of("spruce_leaves")
    @JvmField
    public val BIRCH_LEAVES: RegistryReference<ItemType> = of("birch_leaves")
    @JvmField
    public val JUNGLE_LEAVES: RegistryReference<ItemType> = of("jungle_leaves")
    @JvmField
    public val ACACIA_LEAVES: RegistryReference<ItemType> = of("acacia_leaves")
    @JvmField
    public val DARK_OAK_LEAVES: RegistryReference<ItemType> = of("dark_oak_leaves")
    @JvmField
    public val MANGROVE_LEAVES: RegistryReference<ItemType> = of("mangrove_leaves")
    @JvmField
    public val AZALEA_LEAVES: RegistryReference<ItemType> = of("azalea_leaves")
    @JvmField
    public val FLOWERING_AZALEA_LEAVES: RegistryReference<ItemType> = of("flowering_azalea_leaves")
    @JvmField
    public val SPONGE: RegistryReference<ItemType> = of("sponge")
    @JvmField
    public val WET_SPONGE: RegistryReference<ItemType> = of("wet_sponge")
    @JvmField
    public val GLASS: RegistryReference<ItemType> = of("glass")
    @JvmField
    public val TINTED_GLASS: RegistryReference<ItemType> = of("tinted_glass")
    @JvmField
    public val LAPIS_BLOCK: RegistryReference<ItemType> = of("lapis_block")
    @JvmField
    public val SANDSTONE: RegistryReference<ItemType> = of("sandstone")
    @JvmField
    public val CHISELED_SANDSTONE: RegistryReference<ItemType> = of("chiseled_sandstone")
    @JvmField
    public val CUT_SANDSTONE: RegistryReference<ItemType> = of("cut_sandstone")
    @JvmField
    public val COBWEB: RegistryReference<ItemType> = of("cobweb")
    @JvmField
    public val GRASS: RegistryReference<ItemType> = of("grass")
    @JvmField
    public val FERN: RegistryReference<ItemType> = of("fern")
    @JvmField
    public val AZALEA: RegistryReference<ItemType> = of("azalea")
    @JvmField
    public val FLOWERING_AZALEA: RegistryReference<ItemType> = of("flowering_azalea")
    @JvmField
    public val DEAD_BUSH: RegistryReference<ItemType> = of("dead_bush")
    @JvmField
    public val SEAGRASS: RegistryReference<ItemType> = of("seagrass")
    @JvmField
    public val SEA_PICKLE: RegistryReference<ItemType> = of("sea_pickle")
    @JvmField
    public val WHITE_WOOL: RegistryReference<ItemType> = of("white_wool")
    @JvmField
    public val ORANGE_WOOL: RegistryReference<ItemType> = of("orange_wool")
    @JvmField
    public val MAGENTA_WOOL: RegistryReference<ItemType> = of("magenta_wool")
    @JvmField
    public val LIGHT_BLUE_WOOL: RegistryReference<ItemType> = of("light_blue_wool")
    @JvmField
    public val YELLOW_WOOL: RegistryReference<ItemType> = of("yellow_wool")
    @JvmField
    public val LIME_WOOL: RegistryReference<ItemType> = of("lime_wool")
    @JvmField
    public val PINK_WOOL: RegistryReference<ItemType> = of("pink_wool")
    @JvmField
    public val GRAY_WOOL: RegistryReference<ItemType> = of("gray_wool")
    @JvmField
    public val LIGHT_GRAY_WOOL: RegistryReference<ItemType> = of("light_gray_wool")
    @JvmField
    public val CYAN_WOOL: RegistryReference<ItemType> = of("cyan_wool")
    @JvmField
    public val PURPLE_WOOL: RegistryReference<ItemType> = of("purple_wool")
    @JvmField
    public val BLUE_WOOL: RegistryReference<ItemType> = of("blue_wool")
    @JvmField
    public val BROWN_WOOL: RegistryReference<ItemType> = of("brown_wool")
    @JvmField
    public val GREEN_WOOL: RegistryReference<ItemType> = of("green_wool")
    @JvmField
    public val RED_WOOL: RegistryReference<ItemType> = of("red_wool")
    @JvmField
    public val BLACK_WOOL: RegistryReference<ItemType> = of("black_wool")
    @JvmField
    public val DANDELION: RegistryReference<ItemType> = of("dandelion")
    @JvmField
    public val POPPY: RegistryReference<ItemType> = of("poppy")
    @JvmField
    public val BLUE_ORCHID: RegistryReference<ItemType> = of("blue_orchid")
    @JvmField
    public val ALLIUM: RegistryReference<ItemType> = of("allium")
    @JvmField
    public val AZURE_BLUET: RegistryReference<ItemType> = of("azure_bluet")
    @JvmField
    public val RED_TULIP: RegistryReference<ItemType> = of("red_tulip")
    @JvmField
    public val ORANGE_TULIP: RegistryReference<ItemType> = of("orange_tulip")
    @JvmField
    public val WHITE_TULIP: RegistryReference<ItemType> = of("white_tulip")
    @JvmField
    public val PINK_TULIP: RegistryReference<ItemType> = of("pink_tulip")
    @JvmField
    public val OXEYE_DAISY: RegistryReference<ItemType> = of("oxeye_daisy")
    @JvmField
    public val CORNFLOWER: RegistryReference<ItemType> = of("cornflower")
    @JvmField
    public val LILY_OF_THE_VALLEY: RegistryReference<ItemType> = of("lily_of_the_valley")
    @JvmField
    public val WITHER_ROSE: RegistryReference<ItemType> = of("wither_rose")
    @JvmField
    public val SPORE_BLOSSOM: RegistryReference<ItemType> = of("spore_blossom")
    @JvmField
    public val BROWN_MUSHROOM: RegistryReference<ItemType> = of("brown_mushroom")
    @JvmField
    public val RED_MUSHROOM: RegistryReference<ItemType> = of("red_mushroom")
    @JvmField
    public val CRIMSON_FUNGUS: RegistryReference<ItemType> = of("crimson_fungus")
    @JvmField
    public val WARPED_FUNGUS: RegistryReference<ItemType> = of("warped_fungus")
    @JvmField
    public val CRIMSON_ROOTS: RegistryReference<ItemType> = of("crimson_roots")
    @JvmField
    public val WARPED_ROOTS: RegistryReference<ItemType> = of("warped_roots")
    @JvmField
    public val NETHER_SPROUTS: RegistryReference<ItemType> = of("nether_sprouts")
    @JvmField
    public val WEEPING_VINES: RegistryReference<ItemType> = of("weeping_vines")
    @JvmField
    public val TWISTING_VINES: RegistryReference<ItemType> = of("twisting_vines")
    @JvmField
    public val SUGAR_CANE: RegistryReference<ItemType> = of("sugar_cane")
    @JvmField
    public val KELP: RegistryReference<ItemType> = of("kelp")
    @JvmField
    public val MOSS_CARPET: RegistryReference<ItemType> = of("moss_carpet")
    @JvmField
    public val MOSS_BLOCK: RegistryReference<ItemType> = of("moss_block")
    @JvmField
    public val HANGING_ROOTS: RegistryReference<ItemType> = of("hanging_roots")
    @JvmField
    public val BIG_DRIPLEAF: RegistryReference<ItemType> = of("big_dripleaf")
    @JvmField
    public val SMALL_DRIPLEAF: RegistryReference<ItemType> = of("small_dripleaf")
    @JvmField
    public val BAMBOO: RegistryReference<ItemType> = of("bamboo")
    @JvmField
    public val OAK_SLAB: RegistryReference<ItemType> = of("oak_slab")
    @JvmField
    public val SPRUCE_SLAB: RegistryReference<ItemType> = of("spruce_slab")
    @JvmField
    public val BIRCH_SLAB: RegistryReference<ItemType> = of("birch_slab")
    @JvmField
    public val JUNGLE_SLAB: RegistryReference<ItemType> = of("jungle_slab")
    @JvmField
    public val ACACIA_SLAB: RegistryReference<ItemType> = of("acacia_slab")
    @JvmField
    public val DARK_OAK_SLAB: RegistryReference<ItemType> = of("dark_oak_slab")
    @JvmField
    public val MANGROVE_SLAB: RegistryReference<ItemType> = of("mangrove_slab")
    @JvmField
    public val CRIMSON_SLAB: RegistryReference<ItemType> = of("crimson_slab")
    @JvmField
    public val WARPED_SLAB: RegistryReference<ItemType> = of("warped_slab")
    @JvmField
    public val STONE_SLAB: RegistryReference<ItemType> = of("stone_slab")
    @JvmField
    public val SMOOTH_STONE_SLAB: RegistryReference<ItemType> = of("smooth_stone_slab")
    @JvmField
    public val SANDSTONE_SLAB: RegistryReference<ItemType> = of("sandstone_slab")
    @JvmField
    public val CUT_STANDSTONE_SLAB: RegistryReference<ItemType> = of("cut_sandstone_slab")
    @JvmField
    public val PETRIFIED_OAK_SLAB: RegistryReference<ItemType> = of("petrified_oak_slab")
    @JvmField
    public val COBBLESTONE_SLAB: RegistryReference<ItemType> = of("cobblestone_slab")
    @JvmField
    public val BRICK_SLAB: RegistryReference<ItemType> = of("brick_slab")
    @JvmField
    public val STONE_BRICK_SLAB: RegistryReference<ItemType> = of("stone_brick_slab")
    @JvmField
    public val MUD_BRICK_SLAB: RegistryReference<ItemType> = of("mud_brick_slab")
    @JvmField
    public val NETHER_BRICK_SLAB: RegistryReference<ItemType> = of("nether_brick_slab")
    @JvmField
    public val QUARTZ_SLAB: RegistryReference<ItemType> = of("quartz_slab")
    @JvmField
    public val RED_SANDSTONE_SLAB: RegistryReference<ItemType> = of("red_sandstone_slab")
    @JvmField
    public val CUT_RED_SANDSTONE_SLAB: RegistryReference<ItemType> = of("cut_red_sandstone_slab")
    @JvmField
    public val PURPUR_SLAB: RegistryReference<ItemType> = of("purpur_slab")
    @JvmField
    public val PRISMARINE_SLAB: RegistryReference<ItemType> = of("prismarine_slab")
    @JvmField
    public val PRISMARINE_BRICK_SLAB: RegistryReference<ItemType> = of("prismarine_brick_slab")
    @JvmField
    public val DARK_PRISMARINE_SLAB: RegistryReference<ItemType> = of("dark_prismarine_slab")
    @JvmField
    public val SMOOTH_QUARTZ: RegistryReference<ItemType> = of("smooth_quartz")
    @JvmField
    public val SMOOTH_RED_SANDSTONE: RegistryReference<ItemType> = of("smooth_red_sandstone")
    @JvmField
    public val SMOOTH_SANDSTONE: RegistryReference<ItemType> = of("smooth_sandstone")
    @JvmField
    public val SMOOTH_STONE: RegistryReference<ItemType> = of("smooth_stone")
    @JvmField
    public val BRICKS: RegistryReference<ItemType> = of("bricks")
    @JvmField
    public val BOOKSHELF: RegistryReference<ItemType> = of("bookshelf")
    @JvmField
    public val MOSSY_COBBLESTONE: RegistryReference<ItemType> = of("mossy_cobblestone")
    @JvmField
    public val OBSIDIAN: RegistryReference<ItemType> = of("obsidian")
    @JvmField
    public val TORCH: RegistryReference<ItemType> = of("torch")
    @JvmField
    public val END_ROD: RegistryReference<ItemType> = of("end_rod")
    @JvmField
    public val CHORUS_PLANT: RegistryReference<ItemType> = of("chorus_plant")
    @JvmField
    public val CHORUS_FLOWER: RegistryReference<ItemType> = of("chorus_flower")
    @JvmField
    public val PURPUR_BLOCK: RegistryReference<ItemType> = of("purpur_block")
    @JvmField
    public val PURPUR_PILLAR: RegistryReference<ItemType> = of("purpur_pillar")
    @JvmField
    public val PURPUR_STAIRS: RegistryReference<ItemType> = of("purpur_stairs")
    @JvmField
    public val SPAWNER: RegistryReference<ItemType> = of("spawner")
    @JvmField
    public val CHEST: RegistryReference<ItemType> = of("chest")
    @JvmField
    public val CRAFTING_TABLE: RegistryReference<ItemType> = of("crafting_table")
    @JvmField
    public val FARMLAND: RegistryReference<ItemType> = of("farmland")
    @JvmField
    public val FURNACE: RegistryReference<ItemType> = of("furnace")
    @JvmField
    public val LADDER: RegistryReference<ItemType> = of("ladder")
    @JvmField
    public val COBBLESTONE_STAIRS: RegistryReference<ItemType> = of("cobblestone_stairs")
    @JvmField
    public val SNOW: RegistryReference<ItemType> = of("snow")
    @JvmField
    public val ICE: RegistryReference<ItemType> = of("ice")
    @JvmField
    public val SNOW_BLOCK: RegistryReference<ItemType> = of("snow_block")
    @JvmField
    public val CACTUS: RegistryReference<ItemType> = of("cactus")
    @JvmField
    public val CLAY: RegistryReference<ItemType> = of("clay")
    @JvmField
    public val JUKEBOX: RegistryReference<ItemType> = of("jukebox")
    @JvmField
    public val OAK_FENCE: RegistryReference<ItemType> = of("oak_fence")
    @JvmField
    public val SPRUCE_FENCE: RegistryReference<ItemType> = of("spruce_fence")
    @JvmField
    public val BIRCH_FENCE: RegistryReference<ItemType> = of("birch_fence")
    @JvmField
    public val JUNGLE_FENCE: RegistryReference<ItemType> = of("jungle_fence")
    @JvmField
    public val ACACIA_FENCE: RegistryReference<ItemType> = of("acacia_fence")
    @JvmField
    public val DARK_OAK_FENCE: RegistryReference<ItemType> = of("dark_oak_fence")
    @JvmField
    public val MANGROVE_FENCE: RegistryReference<ItemType> = of("mangrove_fence")
    @JvmField
    public val CRIMSON_FENCE: RegistryReference<ItemType> = of("crimson_fence")
    @JvmField
    public val WARPED_FENCE: RegistryReference<ItemType> = of("warped_fence")
    @JvmField
    public val PUMPKIN: RegistryReference<ItemType> = of("pumpkin")
    @JvmField
    public val CARVED_PUMPKIN: RegistryReference<ItemType> = of("carved_pumpkin")
    @JvmField
    public val JACK_O_LANTERN: RegistryReference<ItemType> = of("jack_o_lantern")
    @JvmField
    public val NETHERRACK: RegistryReference<ItemType> = of("netherrack")
    @JvmField
    public val SOUL_SAND: RegistryReference<ItemType> = of("soul_sand")
    @JvmField
    public val SOUL_SOIL: RegistryReference<ItemType> = of("soul_soil")
    @JvmField
    public val BASALT: RegistryReference<ItemType> = of("basalt")
    @JvmField
    public val POLISHED_BASALT: RegistryReference<ItemType> = of("polished_basalt")
    @JvmField
    public val SMOOTH_BASALT: RegistryReference<ItemType> = of("smooth_basalt")
    @JvmField
    public val SOUL_TORCH: RegistryReference<ItemType> = of("soul_torch")
    @JvmField
    public val GLOWSTONE: RegistryReference<ItemType> = of("glowstone")
    @JvmField
    public val INFESTED_STONE: RegistryReference<ItemType> = of("infested_stone")
    @JvmField
    public val INFESTED_COBBLESTONE: RegistryReference<ItemType> = of("infested_cobblestone")
    @JvmField
    public val INFESTED_STONE_BRICKS: RegistryReference<ItemType> = of("infested_stone_bricks")
    @JvmField
    public val INFESTED_MOSSY_STONE_BRICKS: RegistryReference<ItemType> = of("infested_mossy_stone_bricks")
    @JvmField
    public val INFESTED_CRACKED_STONE_BRICKS: RegistryReference<ItemType> = of("infested_cracked_stone_bricks")
    @JvmField
    public val INFESTED_CHISELED_STONE_BRICKS: RegistryReference<ItemType> = of("infested_chiseled_stone_bricks")
    @JvmField
    public val INFESTED_DEEPSLATE: RegistryReference<ItemType> = of("infested_deepslate")
    @JvmField
    public val STONE_BRICKS: RegistryReference<ItemType> = of("stone_bricks")
    @JvmField
    public val MOSSY_STONE_BRICKS: RegistryReference<ItemType> = of("mossy_stone_bricks")
    @JvmField
    public val CRACKED_STONE_BRICKS: RegistryReference<ItemType> = of("cracked_stone_bricks")
    @JvmField
    public val CHISELED_STONE_BRICKS: RegistryReference<ItemType> = of("chiseled_stone_bricks")
    @JvmField
    public val PACKED_MUD: RegistryReference<ItemType> = of("packed_mud")
    @JvmField
    public val MUD_BRICKS: RegistryReference<ItemType> = of("mud_bricks")
    @JvmField
    public val DEEPSLATE_BRICKS: RegistryReference<ItemType> = of("deepslate_bricks")
    @JvmField
    public val CRACKED_DEEPSLATE_BRICKS: RegistryReference<ItemType> = of("cracked_deepslate_bricks")
    @JvmField
    public val DEEPSLATE_TILES: RegistryReference<ItemType> = of("deepslate_tiles")
    @JvmField
    public val CRACKED_DEEPSLATE_TILES: RegistryReference<ItemType> = of("cracked_deepslate_tiles")
    @JvmField
    public val CHISELED_DEEPSLATE: RegistryReference<ItemType> = of("chiseled_deepslate")
    @JvmField
    public val REINFORCED_DEEPSLATE: RegistryReference<ItemType> = of("reinforced_deepslate")
    @JvmField
    public val BROWN_MUSHROOM_BLOCK: RegistryReference<ItemType> = of("brown_mushroom_block")
    @JvmField
    public val RED_MUSHROOM_BLOCK: RegistryReference<ItemType> = of("red_mushroom_block")
    @JvmField
    public val MUSHROOM_STEM: RegistryReference<ItemType> = of("mushroom_stem")
    @JvmField
    public val IRON_BARS: RegistryReference<ItemType> = of("iron_bars")
    @JvmField
    public val CHAIN: RegistryReference<ItemType> = of("chain")
    @JvmField
    public val GLASS_PANE: RegistryReference<ItemType> = of("glass_pane")
    @JvmField
    public val MELON: RegistryReference<ItemType> = of("melon")
    @JvmField
    public val VINE: RegistryReference<ItemType> = of("vine")
    @JvmField
    public val GLOW_LICHEN: RegistryReference<ItemType> = of("glow_lichen")
    @JvmField
    public val BRICK_STAIRS: RegistryReference<ItemType> = of("brick_stairs")
    @JvmField
    public val STONE_BRICK_STAIRS: RegistryReference<ItemType> = of("stone_brick_stairs")
    @JvmField
    public val MUD_BRICK_STAIRS: RegistryReference<ItemType> = of("mud_brick_stairs")
    @JvmField
    public val MYCELIUM: RegistryReference<ItemType> = of("mycelium")
    @JvmField
    public val LILY_PAD: RegistryReference<ItemType> = of("lily_pad")
    @JvmField
    public val NETHER_BRICKS: RegistryReference<ItemType> = of("nether_bricks")
    @JvmField
    public val CRACKED_NETHER_BRICKS: RegistryReference<ItemType> = of("cracked_nether_bricks")
    @JvmField
    public val CHISELED_NETHER_BRICKS: RegistryReference<ItemType> = of("chiseled_nether_bricks")
    @JvmField
    public val NETHER_BRICK_FENCE: RegistryReference<ItemType> = of("nether_brick_fence")
    @JvmField
    public val NETHER_BRICK_STAIRS: RegistryReference<ItemType> = of("nether_brick_stairs")
    @JvmField
    public val SCULK: RegistryReference<ItemType> = of("sculk")
    @JvmField
    public val SCULK_VEIN: RegistryReference<ItemType> = of("sculk_vein")
    @JvmField
    public val SCULK_CATALYST: RegistryReference<ItemType> = of("sculk_catalyst")
    @JvmField
    public val SCULK_SHRIEKER: RegistryReference<ItemType> = of("sculk_shrieker")
    @JvmField
    public val ENCHANTING_TABLE: RegistryReference<ItemType> = of("enchanting_table")
    @JvmField
    public val END_PORTAL_FRAME: RegistryReference<ItemType> = of("end_portal_frame")
    @JvmField
    public val END_STONE: RegistryReference<ItemType> = of("end_stone")
    @JvmField
    public val END_STONE_BRICKS: RegistryReference<ItemType> = of("end_stone_bricks")
    @JvmField
    public val DRAGON_EGG: RegistryReference<ItemType> = of("dragon_egg")
    @JvmField
    public val SANDSTONE_STAIRS: RegistryReference<ItemType> = of("sandstone_stairs")
    @JvmField
    public val ENDER_CHEST: RegistryReference<ItemType> = of("ender_chest")
    @JvmField
    public val EMERALD_BLOCK: RegistryReference<ItemType> = of("emerald_block")
    @JvmField
    public val OAK_STAIRS: RegistryReference<ItemType> = of("oak_stairs")
    @JvmField
    public val SPRUCE_STAIRS: RegistryReference<ItemType> = of("spruce_stairs")
    @JvmField
    public val BIRCH_STAIRS: RegistryReference<ItemType> = of("birch_stairs")
    @JvmField
    public val JUNGLE_STAIRS: RegistryReference<ItemType> = of("jungle_stairs")
    @JvmField
    public val ACACIA_STAIRS: RegistryReference<ItemType> = of("acacia_stairs")
    @JvmField
    public val DARK_OAK_STAIRS: RegistryReference<ItemType> = of("dark_oak_stairs")
    @JvmField
    public val MANGROVE_STAIRS: RegistryReference<ItemType> = of("mangrove_stairs")
    @JvmField
    public val CRIMSON_STAIRS: RegistryReference<ItemType> = of("crimson_stairs")
    @JvmField
    public val WARPED_STAIRS: RegistryReference<ItemType> = of("warped_stairs")
    @JvmField
    public val COMMAND_BLOCK: RegistryReference<ItemType> = of("command_block")
    @JvmField
    public val BEACON: RegistryReference<ItemType> = of("beacon")
    @JvmField
    public val COBBLESTONE_WALL: RegistryReference<ItemType> = of("cobblestone_wall")
    @JvmField
    public val MOSSY_COBBLESTONE_WALL: RegistryReference<ItemType> = of("mossy_cobblestone_wall")
    @JvmField
    public val BRICK_WALL: RegistryReference<ItemType> = of("brick_wall")
    @JvmField
    public val PRISMARINE_WALL: RegistryReference<ItemType> = of("prismarine_wall")
    @JvmField
    public val RED_SANDSTONE_WALL: RegistryReference<ItemType> = of("red_sandstone_wall")
    @JvmField
    public val MOSSY_STONE_BRICK_WALL: RegistryReference<ItemType> = of("mossy_stone_brick_wall")
    @JvmField
    public val GRANITE_WALL: RegistryReference<ItemType> = of("granite_wall")
    @JvmField
    public val STONE_BRICK_WALL: RegistryReference<ItemType> = of("stone_brick_wall")
    @JvmField
    public val MUD_BRICK_WALL: RegistryReference<ItemType> = of("mud_brick_wall")
    @JvmField
    public val NETHER_BRICK_WALL: RegistryReference<ItemType> = of("nether_brick_wall")
    @JvmField
    public val ANDESITE_WALL: RegistryReference<ItemType> = of("andesite_wall")
    @JvmField
    public val RED_NETHER_BRICK_WALL: RegistryReference<ItemType> = of("red_nether_brick_wall")
    @JvmField
    public val SANDSTONE_WALL: RegistryReference<ItemType> = of("sandstone_wall")
    @JvmField
    public val END_STONE_BRICK_WALL: RegistryReference<ItemType> = of("end_stone_brick_wall")
    @JvmField
    public val DIORITE_WALL: RegistryReference<ItemType> = of("diorite_wall")
    @JvmField
    public val BLACKSTONE_WALL: RegistryReference<ItemType> = of("blackstone_wall")
    @JvmField
    public val POLISHED_BLACKSTONE_WALL: RegistryReference<ItemType> = of("polished_blackstone_wall")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_WALL: RegistryReference<ItemType> = of("polished_blackstone_brick_wall")
    @JvmField
    public val COBBLED_DEEPSLATE_WALL: RegistryReference<ItemType> = of("cobbled_deepslate_wall")
    @JvmField
    public val POLISHED_DEEPSLATE_WALL: RegistryReference<ItemType> = of("polished_deepslate_wall")
    @JvmField
    public val DEEPSLATE_BRICK_WALL: RegistryReference<ItemType> = of("deepslate_brick_wall")
    @JvmField
    public val DEEPSLATE_TILE_WALL: RegistryReference<ItemType> = of("deepslate_tile_wall")
    @JvmField
    public val ANVIL: RegistryReference<ItemType> = of("anvil")
    @JvmField
    public val CHIPPED_ANVIL: RegistryReference<ItemType> = of("chipped_anvil")
    @JvmField
    public val DAMAGED_ANVIL: RegistryReference<ItemType> = of("damaged_anvil")
    @JvmField
    public val CHISELED_QUARTZ_BLOCK: RegistryReference<ItemType> = of("chiseled_quartz_block")
    @JvmField
    public val QUARTZ_BLOCK: RegistryReference<ItemType> = of("quartz_block")
    @JvmField
    public val QUARTZ_BRICKS: RegistryReference<ItemType> = of("quartz_bricks")
    @JvmField
    public val QUARTZ_PILLAR: RegistryReference<ItemType> = of("quartz_pillar")
    @JvmField
    public val QUARTZ_STAIRS: RegistryReference<ItemType> = of("quartz_stairs")
    @JvmField
    public val WHITE_TERRACOTTA: RegistryReference<ItemType> = of("white_terracotta")
    @JvmField
    public val ORANGE_TERRACOTTA: RegistryReference<ItemType> = of("orange_terracotta")
    @JvmField
    public val MAGENTA_TERRACOTTA: RegistryReference<ItemType> = of("magenta_terracotta")
    @JvmField
    public val LIGHT_BLUE_TERRACOTTA: RegistryReference<ItemType> = of("light_blue_terracotta")
    @JvmField
    public val YELLOW_TERRACOTTA: RegistryReference<ItemType> = of("yellow_terracotta")
    @JvmField
    public val LIME_TERRACOTTA: RegistryReference<ItemType> = of("lime_terracotta")
    @JvmField
    public val PINK_TERRACOTTA: RegistryReference<ItemType> = of("pink_terracotta")
    @JvmField
    public val GRAY_TERRACOTTA: RegistryReference<ItemType> = of("gray_terracotta")
    @JvmField
    public val LIGHT_GRAY_TERRACOTTA: RegistryReference<ItemType> = of("light_gray_terracotta")
    @JvmField
    public val CYAN_TERRACOTTA: RegistryReference<ItemType> = of("cyan_terracotta")
    @JvmField
    public val PURPLE_TERRACOTTA: RegistryReference<ItemType> = of("purple_terracotta")
    @JvmField
    public val BLUE_TERRACOTTA: RegistryReference<ItemType> = of("blue_terracotta")
    @JvmField
    public val BROWN_TERRACOTTA: RegistryReference<ItemType> = of("brown_terracotta")
    @JvmField
    public val GREEN_TERRACOTTA: RegistryReference<ItemType> = of("green_terracotta")
    @JvmField
    public val RED_TERRACOTTA: RegistryReference<ItemType> = of("red_terracotta")
    @JvmField
    public val BLACK_TERRACOTTA: RegistryReference<ItemType> = of("black_terracotta")
    @JvmField
    public val BARRIER: RegistryReference<ItemType> = of("barrier")
    @JvmField
    public val LIGHT: RegistryReference<ItemType> = of("light")
    @JvmField
    public val HAY_BLOCK: RegistryReference<ItemType> = of("hay_block")
    @JvmField
    public val WHITE_CARPET: RegistryReference<ItemType> = of("white_carpet")
    @JvmField
    public val ORANGE_CARPET: RegistryReference<ItemType> = of("orange_carpet")
    @JvmField
    public val MAGENTA_CARPET: RegistryReference<ItemType> = of("magenta_carpet")
    @JvmField
    public val LIGHT_BLUE_CARPET: RegistryReference<ItemType> = of("light_blue_carpet")
    @JvmField
    public val YELLOW_CARPET: RegistryReference<ItemType> = of("yellow_carpet")
    @JvmField
    public val LIME_CARPET: RegistryReference<ItemType> = of("lime_carpet")
    @JvmField
    public val PINK_CARPET: RegistryReference<ItemType> = of("pink_carpet")
    @JvmField
    public val GRAY_CARPET: RegistryReference<ItemType> = of("gray_carpet")
    @JvmField
    public val LIGHT_GRAY_CARPET: RegistryReference<ItemType> = of("light_gray_carpet")
    @JvmField
    public val CYAN_CARPET: RegistryReference<ItemType> = of("cyan_carpet")
    @JvmField
    public val PURPLE_CARPET: RegistryReference<ItemType> = of("purple_carpet")
    @JvmField
    public val BLUE_CARPET: RegistryReference<ItemType> = of("blue_carpet")
    @JvmField
    public val BROWN_CARPET: RegistryReference<ItemType> = of("brown_carpet")
    @JvmField
    public val GREEN_CARPET: RegistryReference<ItemType> = of("green_carpet")
    @JvmField
    public val RED_CARPET: RegistryReference<ItemType> = of("red_carpet")
    @JvmField
    public val BLACK_CARPET: RegistryReference<ItemType> = of("black_carpet")
    @JvmField
    public val TERRACOTTA: RegistryReference<ItemType> = of("terracotta")
    @JvmField
    public val PACKED_ICE: RegistryReference<ItemType> = of("packed_ice")
    @JvmField
    public val DIRT_PATH: RegistryReference<ItemType> = of("dirt_path")
    @JvmField
    public val SUNFLOWER: RegistryReference<ItemType> = of("sunflower")
    @JvmField
    public val LILAC: RegistryReference<ItemType> = of("lilac")
    @JvmField
    public val ROSE_BUSH: RegistryReference<ItemType> = of("rose_bush")
    @JvmField
    public val PEONY: RegistryReference<ItemType> = of("peony")
    @JvmField
    public val TALL_GRASS: RegistryReference<ItemType> = of("tall_grass")
    @JvmField
    public val LARGE_FERN: RegistryReference<ItemType> = of("large_fern")
    @JvmField
    public val WHITE_STAINED_GLASS: RegistryReference<ItemType> = of("white_stained_glass")
    @JvmField
    public val ORANGE_STAINED_GLASS: RegistryReference<ItemType> = of("orange_stained_glass")
    @JvmField
    public val MAGENTA_STAINED_GLASS: RegistryReference<ItemType> = of("magenta_stained_glass")
    @JvmField
    public val LIGHT_BLUE_STAINED_GLASS: RegistryReference<ItemType> = of("light_blue_stained_glass")
    @JvmField
    public val YELLOW_STAINED_GLASS: RegistryReference<ItemType> = of("yellow_stained_glass")
    @JvmField
    public val LIME_STAINED_GLASS: RegistryReference<ItemType> = of("lime_stained_glass")
    @JvmField
    public val PINK_STAINED_GLASS: RegistryReference<ItemType> = of("pink_stained_glass")
    @JvmField
    public val GRAY_STAINED_GLASS: RegistryReference<ItemType> = of("gray_stained_glass")
    @JvmField
    public val LIGHT_GRAY_STAINED_GLASS: RegistryReference<ItemType> = of("light_gray_stained_glass")
    @JvmField
    public val CYAN_STAINED_GLASS: RegistryReference<ItemType> = of("cyan_stained_glass")
    @JvmField
    public val PURPLE_STAINED_GLASS: RegistryReference<ItemType> = of("purple_stained_glass")
    @JvmField
    public val BLUE_STAINED_GLASS: RegistryReference<ItemType> = of("blue_stained_glass")
    @JvmField
    public val BROWN_STAINED_GLASS: RegistryReference<ItemType> = of("brown_stained_glass")
    @JvmField
    public val GREEN_STAINED_GLASS: RegistryReference<ItemType> = of("green_stained_glass")
    @JvmField
    public val RED_STAINED_GLASS: RegistryReference<ItemType> = of("red_stained_glass")
    @JvmField
    public val BLACK_STAINED_GLASS: RegistryReference<ItemType> = of("black_stained_glass")
    @JvmField
    public val WHITE_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("white_stained_glass_pane")
    @JvmField
    public val ORANGE_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("orange_stained_glass_pane")
    @JvmField
    public val MAGENTA_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("magenta_stained_glass_pane")
    @JvmField
    public val LIGHT_BLUE_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("light_blue_stained_glass_pane")
    @JvmField
    public val YELLOW_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("yellow_stained_glass_pane")
    @JvmField
    public val LIME_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("lime_stained_glass_pane")
    @JvmField
    public val PINK_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("pink_stained_glass_pane")
    @JvmField
    public val GRAY_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("gray_stained_glass_pane")
    @JvmField
    public val LIGHT_GRAY_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("light_gray_stained_glass_pane")
    @JvmField
    public val CYAN_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("cyan_stained_glass_pane")
    @JvmField
    public val PURPLE_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("purple_stained_glass_pane")
    @JvmField
    public val BLUE_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("blue_stained_glass_pane")
    @JvmField
    public val BROWN_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("brown_stained_glass_pane")
    @JvmField
    public val GREEN_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("green_stained_glass_pane")
    @JvmField
    public val RED_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("red_stained_glass_pane")
    @JvmField
    public val BLACK_STAINED_GLASS_PANE: RegistryReference<ItemType> = of("black_stained_glass_pane")
    @JvmField
    public val PRISMARINE: RegistryReference<ItemType> = of("prismarine")
    @JvmField
    public val PRISMARINE_BRICKS: RegistryReference<ItemType> = of("prismarine_bricks")
    @JvmField
    public val DARK_PRISMARINE: RegistryReference<ItemType> = of("dark_prismarine")
    @JvmField
    public val PRISMARINE_STAIRS: RegistryReference<ItemType> = of("prismarine_stairs")
    @JvmField
    public val PRISMARINE_BRICK_STAIRS: RegistryReference<ItemType> = of("prismarine_brick_stairs")
    @JvmField
    public val DARK_PRISMARINE_STAIRS: RegistryReference<ItemType> = of("dark_prismarine_stairs")
    @JvmField
    public val SEA_LANTERN: RegistryReference<ItemType> = of("sea_lantern")
    @JvmField
    public val RED_SANDSTONE: RegistryReference<ItemType> = of("red_sandstone")
    @JvmField
    public val CHISELED_RED_SANDSTONE: RegistryReference<ItemType> = of("chiseled_red_sandstone")
    @JvmField
    public val CUT_RED_SANDSTONE: RegistryReference<ItemType> = of("cut_red_sandstone")
    @JvmField
    public val RED_SANDSTONE_STAIRS: RegistryReference<ItemType> = of("red_sandstone_stairs")
    @JvmField
    public val REPEATING_COMMAND_BLOCK: RegistryReference<ItemType> = of("repeating_command_block")
    @JvmField
    public val CHAIN_COMMAND_BLOCK: RegistryReference<ItemType> = of("chain_command_block")
    @JvmField
    public val MAGMA_BLOCK: RegistryReference<ItemType> = of("magma_block")
    @JvmField
    public val NETHER_WART_BLOCK: RegistryReference<ItemType> = of("nether_wart_block")
    @JvmField
    public val WARPED_WART_BLOCK: RegistryReference<ItemType> = of("warped_wart_block")
    @JvmField
    public val RED_NETHER_BRICKS: RegistryReference<ItemType> = of("red_nether_bricks")
    @JvmField
    public val BONE_BLOCK: RegistryReference<ItemType> = of("bone_block")
    @JvmField
    public val STRUCTURE_VOID: RegistryReference<ItemType> = of("structure_void")
    @JvmField
    public val SHULKER_BOX: RegistryReference<ItemType> = of("shulker_box")
    @JvmField
    public val WHITE_SHULKER_BOX: RegistryReference<ItemType> = of("white_shulker_box")
    @JvmField
    public val ORANGE_SHULKER_BOX: RegistryReference<ItemType> = of("orange_shulker_box")
    @JvmField
    public val MAGENTA_SHULKER_BOX: RegistryReference<ItemType> = of("magenta_shulker_box")
    @JvmField
    public val LIGHT_BLUE_SHULKER_BOX: RegistryReference<ItemType> = of("light_blue_shulker_box")
    @JvmField
    public val YELLOW_SHULKER_BOX: RegistryReference<ItemType> = of("yellow_shulker_box")
    @JvmField
    public val LIME_SHULKER_BOX: RegistryReference<ItemType> = of("lime_shulker_box")
    @JvmField
    public val PINK_SHULKER_BOX: RegistryReference<ItemType> = of("pink_shulker_box")
    @JvmField
    public val GRAY_SHULKER_BOX: RegistryReference<ItemType> = of("gray_shulker_box")
    @JvmField
    public val LIGHT_GRAY_SHULKER_BOX: RegistryReference<ItemType> = of("light_gray_shulker_box")
    @JvmField
    public val CYAN_SHULKER_BOX: RegistryReference<ItemType> = of("cyan_shulker_box")
    @JvmField
    public val PURPLE_SHULKER_BOX: RegistryReference<ItemType> = of("purple_shulker_box")
    @JvmField
    public val BLUE_SHULKER_BOX: RegistryReference<ItemType> = of("blue_shulker_box")
    @JvmField
    public val BROWN_SHULKER_BOX: RegistryReference<ItemType> = of("brown_shulker_box")
    @JvmField
    public val GREEN_SHULKER_BOX: RegistryReference<ItemType> = of("green_shulker_box")
    @JvmField
    public val RED_SHULKER_BOX: RegistryReference<ItemType> = of("red_shulker_box")
    @JvmField
    public val BLACK_SHULKER_BOX: RegistryReference<ItemType> = of("black_shulker_box")
    @JvmField
    public val WHITE_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("white_glazed_terracotta")
    @JvmField
    public val ORANGE_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("orange_glazed_terracotta")
    @JvmField
    public val MAGENTA_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("magenta_glazed_terracotta")
    @JvmField
    public val LIGHT_BLUE_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("light_blue_glazed_terracotta")
    @JvmField
    public val YELLOW_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("yellow_glazed_terracotta")
    @JvmField
    public val LIME_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("lime_glazed_terracotta")
    @JvmField
    public val PINK_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("pink_glazed_terracotta")
    @JvmField
    public val GRAY_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("gray_glazed_terracotta")
    @JvmField
    public val LIGHT_GRAY_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("light_gray_glazed_terracotta")
    @JvmField
    public val CYAN_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("cyan_glazed_terracotta")
    @JvmField
    public val PURPLE_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("purple_glazed_terracotta")
    @JvmField
    public val BLUE_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("blue_glazed_terracotta")
    @JvmField
    public val BROWN_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("brown_glazed_terracotta")
    @JvmField
    public val GREEN_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("green_glazed_terracotta")
    @JvmField
    public val RED_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("red_glazed_terracotta")
    @JvmField
    public val BLACK_GLAZED_TERRACOTTA: RegistryReference<ItemType> = of("black_glazed_terracotta")
    @JvmField
    public val WHITE_CONCRETE: RegistryReference<ItemType> = of("white_concrete")
    @JvmField
    public val ORANGE_CONCRETE: RegistryReference<ItemType> = of("orange_concrete")
    @JvmField
    public val MAGENTA_CONCRETE: RegistryReference<ItemType> = of("magenta_concrete")
    @JvmField
    public val LIGHT_BLUE_CONCRETE: RegistryReference<ItemType> = of("light_blue_concrete")
    @JvmField
    public val YELLOW_CONCRETE: RegistryReference<ItemType> = of("yellow_concrete")
    @JvmField
    public val LIME_CONCRETE: RegistryReference<ItemType> = of("lime_concrete")
    @JvmField
    public val PINK_CONCRETE: RegistryReference<ItemType> = of("pink_concrete")
    @JvmField
    public val GRAY_CONCRETE: RegistryReference<ItemType> = of("gray_concrete")
    @JvmField
    public val LIGHT_GRAY_CONCRETE: RegistryReference<ItemType> = of("light_gray_concrete")
    @JvmField
    public val CYAN_CONCRETE: RegistryReference<ItemType> = of("cyan_concrete")
    @JvmField
    public val PURPLE_CONCRETE: RegistryReference<ItemType> = of("purple_concrete")
    @JvmField
    public val BLUE_CONCRETE: RegistryReference<ItemType> = of("blue_concrete")
    @JvmField
    public val BROWN_CONCRETE: RegistryReference<ItemType> = of("brown_concrete")
    @JvmField
    public val GREEN_CONCRETE: RegistryReference<ItemType> = of("green_concrete")
    @JvmField
    public val RED_CONCRETE: RegistryReference<ItemType> = of("red_concrete")
    @JvmField
    public val BLACK_CONCRETE: RegistryReference<ItemType> = of("black_concrete")
    @JvmField
    public val WHITE_CONCRETE_POWDER: RegistryReference<ItemType> = of("white_concrete_powder")
    @JvmField
    public val ORANGE_CONCRETE_POWDER: RegistryReference<ItemType> = of("orange_concrete_powder")
    @JvmField
    public val MAGENTA_CONCRETE_POWDER: RegistryReference<ItemType> = of("magenta_concrete_powder")
    @JvmField
    public val LIGHT_BLUE_CONCRETE_POWDER: RegistryReference<ItemType> = of("light_blue_concrete_powder")
    @JvmField
    public val YELLOW_CONCRETE_POWDER: RegistryReference<ItemType> = of("yellow_concrete_powder")
    @JvmField
    public val LIME_CONCRETE_POWDER: RegistryReference<ItemType> = of("lime_concrete_powder")
    @JvmField
    public val PINK_CONCRETE_POWDER: RegistryReference<ItemType> = of("pink_concrete_powder")
    @JvmField
    public val GRAY_CONCRETE_POWDER: RegistryReference<ItemType> = of("gray_concrete_powder")
    @JvmField
    public val LIGHT_GRAY_CONCRETE_POWDER: RegistryReference<ItemType> = of("light_gray_concrete_powder")
    @JvmField
    public val CYAN_CONCRETE_POWDER: RegistryReference<ItemType> = of("cyan_concrete_powder")
    @JvmField
    public val PURPLE_CONCRETE_POWDER: RegistryReference<ItemType> = of("purple_concrete_powder")
    @JvmField
    public val BLUE_CONCRETE_POWDER: RegistryReference<ItemType> = of("blue_concrete_powder")
    @JvmField
    public val BROWN_CONCRETE_POWDER: RegistryReference<ItemType> = of("brown_concrete_powder")
    @JvmField
    public val GREEN_CONCRETE_POWDER: RegistryReference<ItemType> = of("green_concrete_powder")
    @JvmField
    public val RED_CONCRETE_POWDER: RegistryReference<ItemType> = of("red_concrete_powder")
    @JvmField
    public val BLACK_CONCRETE_POWDER: RegistryReference<ItemType> = of("black_concrete_powder")
    @JvmField
    public val TURTLE_EGG: RegistryReference<ItemType> = of("turtle_egg")
    @JvmField
    public val DEAD_TUBE_CORAL_BLOCK: RegistryReference<ItemType> = of("dead_tube_coral_block")
    @JvmField
    public val DEAD_BRAIN_CORAL_BLOCK: RegistryReference<ItemType> = of("dead_brain_coral_block")
    @JvmField
    public val DEAD_BUBBLE_CORAL_BLOCK: RegistryReference<ItemType> = of("dead_bubble_coral_block")
    @JvmField
    public val DEAD_FIRE_CORAL_BLOCK: RegistryReference<ItemType> = of("dead_fire_coral_block")
    @JvmField
    public val DEAD_HORN_CORAL_BLOCK: RegistryReference<ItemType> = of("dead_horn_coral_block")
    @JvmField
    public val TUBE_CORAL_BLOCK: RegistryReference<ItemType> = of("tube_coral_block")
    @JvmField
    public val BRAIN_CORAL_BLOCK: RegistryReference<ItemType> = of("brain_coral_block")
    @JvmField
    public val BUBBLE_CORAL_BLOCK: RegistryReference<ItemType> = of("bubble_coral_block")
    @JvmField
    public val FIRE_CORAL_BLOCK: RegistryReference<ItemType> = of("fire_coral_block")
    @JvmField
    public val HORN_CORAL_BLOCK: RegistryReference<ItemType> = of("horn_coral_block")
    @JvmField
    public val TUBE_CORAL: RegistryReference<ItemType> = of("tube_coral")
    @JvmField
    public val BRAIN_CORAL: RegistryReference<ItemType> = of("brain_coral")
    @JvmField
    public val BUBBLE_CORAL: RegistryReference<ItemType> = of("bubble_coral")
    @JvmField
    public val FIRE_CORAL: RegistryReference<ItemType> = of("fire_coral")
    @JvmField
    public val HORN_CORAL: RegistryReference<ItemType> = of("horn_coral")
    @JvmField
    public val DEAD_BRAIN_CORAL: RegistryReference<ItemType> = of("dead_brain_coral")
    @JvmField
    public val DEAD_BUBBLE_CORAL: RegistryReference<ItemType> = of("dead_bubble_coral")
    @JvmField
    public val DEAD_FIRE_CORAL: RegistryReference<ItemType> = of("dead_fire_coral")
    @JvmField
    public val DEAD_HORN_CORAL: RegistryReference<ItemType> = of("dead_horn_coral")
    @JvmField
    public val DEAD_TUBE_CORAL: RegistryReference<ItemType> = of("dead_tube_coral")
    @JvmField
    public val TUBE_CORAL_FAN: RegistryReference<ItemType> = of("tube_coral_fan")
    @JvmField
    public val BRAIN_CORAL_FAN: RegistryReference<ItemType> = of("brain_coral_fan")
    @JvmField
    public val BUBBLE_CORAL_FAN: RegistryReference<ItemType> = of("bubble_coral_fan")
    @JvmField
    public val FIRE_CORAL_FAN: RegistryReference<ItemType> = of("fire_coral_fan")
    @JvmField
    public val HORN_CORAL_FAN: RegistryReference<ItemType> = of("horn_coral_fan")
    @JvmField
    public val DEAD_TUBE_CORAL_FAN: RegistryReference<ItemType> = of("dead_tube_coral_fan")
    @JvmField
    public val DEAD_BRAIN_CORAL_FAN: RegistryReference<ItemType> = of("dead_brain_coral_fan")
    @JvmField
    public val DEAD_BUBBLE_CORAL_FAN: RegistryReference<ItemType> = of("dead_bubble_coral_fan")
    @JvmField
    public val DEAD_FIRE_CORAL_FAN: RegistryReference<ItemType> = of("dead_fire_coral_fan")
    @JvmField
    public val DEAD_HORN_CORAL_FAN: RegistryReference<ItemType> = of("dead_horn_coral_fan")
    @JvmField
    public val BLUE_ICE: RegistryReference<ItemType> = of("blue_ice")
    @JvmField
    public val CONDUIT: RegistryReference<ItemType> = of("conduit")
    @JvmField
    public val POLISHED_GRANITE_STAIRS: RegistryReference<ItemType> = of("polished_granite_stairs")
    @JvmField
    public val SMOOTH_RED_SANDSTONE_STAIRS: RegistryReference<ItemType> = of("smooth_red_sandstone_stairs")
    @JvmField
    public val MOSSY_STONE_BRICK_STAIRS: RegistryReference<ItemType> = of("mossy_stone_brick_stairs")
    @JvmField
    public val POLISHED_DIORITE_STAIRS: RegistryReference<ItemType> = of("polished_diorite_stairs")
    @JvmField
    public val MOSSY_COBBLESTONE_STAIRS: RegistryReference<ItemType> = of("mossy_cobblestone_stairs")
    @JvmField
    public val END_STONE_BRICK_STAIRS: RegistryReference<ItemType> = of("end_stone_brick_stairs")
    @JvmField
    public val STONE_STAIRS: RegistryReference<ItemType> = of("stone_stairs")
    @JvmField
    public val SMOOTH_SANDSTONE_STAIRS: RegistryReference<ItemType> = of("smooth_sandstone_stairs")
    @JvmField
    public val SMOOTH_QUARTZ_STAIRS: RegistryReference<ItemType> = of("smooth_quartz_stairs")
    @JvmField
    public val GRANITE_STAIRS: RegistryReference<ItemType> = of("granite_stairs")
    @JvmField
    public val ANDESITE_STAIRS: RegistryReference<ItemType> = of("andesite_stairs")
    @JvmField
    public val RED_NETHER_BRICK_STAIRS: RegistryReference<ItemType> = of("red_nether_brick_stairs")
    @JvmField
    public val POLISHED_ANDESITE_STAIRS: RegistryReference<ItemType> = of("polished_andesite_stairs")
    @JvmField
    public val DIORITE_STAIRS: RegistryReference<ItemType> = of("diorite_stairs")
    @JvmField
    public val COBBLED_DEEPSLATE_STAIRS: RegistryReference<ItemType> = of("cobbled_deepslate_stairs")
    @JvmField
    public val POLISHED_DEEPSLATE_STAIRS: RegistryReference<ItemType> = of("polished_deepslate_stairs")
    @JvmField
    public val DEEPSLATE_BRICK_STAIRS: RegistryReference<ItemType> = of("deepslate_brick_stairs")
    @JvmField
    public val DEEPSLATE_TILE_STAIRS: RegistryReference<ItemType> = of("deepslate_tile_stairs")
    @JvmField
    public val POLISHED_GRANITE_SLAB: RegistryReference<ItemType> = of("polished_granite_slab")
    @JvmField
    public val SMOOTH_RED_SANDSTONE_SLAB: RegistryReference<ItemType> = of("smooth_red_sandstone_slab")
    @JvmField
    public val MOSSY_STONE_BRICK_SLAB: RegistryReference<ItemType> = of("mossy_stone_brick_slab")
    @JvmField
    public val POLISHED_DIORITE_SLAB: RegistryReference<ItemType> = of("polished_diorite_slab")
    @JvmField
    public val MOSSY_COBBLESTONE_SLAB: RegistryReference<ItemType> = of("mossy_cobblestone_slab")
    @JvmField
    public val END_STONE_BRICK_SLAB: RegistryReference<ItemType> = of("end_stone_brick_slab")
    @JvmField
    public val SMOOTH_SANDSTONE_SLAB: RegistryReference<ItemType> = of("smooth_sandstone_slab")
    @JvmField
    public val SMOOTH_QUARTZ_SLAB: RegistryReference<ItemType> = of("smooth_quartz_slab")
    @JvmField
    public val GRANITE_SLAB: RegistryReference<ItemType> = of("granite_slab")
    @JvmField
    public val ANDESITE_SLAB: RegistryReference<ItemType> = of("andesite_slab")
    @JvmField
    public val RED_NETHER_BRICK_SLAB: RegistryReference<ItemType> = of("red_nether_brick_slab")
    @JvmField
    public val POLISHED_ANDESITE_SLAB: RegistryReference<ItemType> = of("polished_andesite_slab")
    @JvmField
    public val DIORITE_SLAB: RegistryReference<ItemType> = of("diorite_slab")
    @JvmField
    public val COBBLED_DEEPSLATE_SLAB: RegistryReference<ItemType> = of("cobbled_deepslate_slab")
    @JvmField
    public val POLISHED_DEEPSLATE_SLAB: RegistryReference<ItemType> = of("polished_deepslate_slab")
    @JvmField
    public val DEEPSLATE_BRICK_SLAB: RegistryReference<ItemType> = of("deepslate_brick_slab")
    @JvmField
    public val DEEPSLATE_TILE_SLAB: RegistryReference<ItemType> = of("deepslate_tile_slab")
    @JvmField
    public val SCAFFOLDING: RegistryReference<ItemType> = of("scaffolding")
    @JvmField
    public val REDSTONE: RegistryReference<ItemType> = of("redstone")
    @JvmField
    public val REDSTONE_TORCH: RegistryReference<ItemType> = of("redstone_torch")
    @JvmField
    public val REDSTONE_BLOCK: RegistryReference<ItemType> = of("redstone_block")
    @JvmField
    public val REPEATER: RegistryReference<ItemType> = of("repeater")
    @JvmField
    public val COMPARATOR: RegistryReference<ItemType> = of("comparator")
    @JvmField
    public val PISTON: RegistryReference<ItemType> = of("piston")
    @JvmField
    public val STICKY_PISTON: RegistryReference<ItemType> = of("sticky_piston")
    @JvmField
    public val SLIME_BLOCK: RegistryReference<ItemType> = of("slime_block")
    @JvmField
    public val HONEY_BLOCK: RegistryReference<ItemType> = of("honey_block")
    @JvmField
    public val OBSERVER: RegistryReference<ItemType> = of("observer")
    @JvmField
    public val HOPPER: RegistryReference<ItemType> = of("hopper")
    @JvmField
    public val DISPENSER: RegistryReference<ItemType> = of("dispenser")
    @JvmField
    public val DROPPER: RegistryReference<ItemType> = of("dropper")
    @JvmField
    public val LECTERN: RegistryReference<ItemType> = of("lectern")
    @JvmField
    public val TARGET: RegistryReference<ItemType> = of("target")
    @JvmField
    public val LEVER: RegistryReference<ItemType> = of("lever")
    @JvmField
    public val LIGHTNING_ROD: RegistryReference<ItemType> = of("lightning_rod")
    @JvmField
    public val DAYLIGHT_DETECTOR: RegistryReference<ItemType> = of("daylight_detector")
    @JvmField
    public val SCULK_SENSOR: RegistryReference<ItemType> = of("sculk_sensor")
    @JvmField
    public val TRIPWIRE_HOOK: RegistryReference<ItemType> = of("tripwire_hook")
    @JvmField
    public val TRAPPED_CHEST: RegistryReference<ItemType> = of("trapped_chest")
    @JvmField
    public val TNT: RegistryReference<ItemType> = of("tnt")
    @JvmField
    public val REDSTONE_LAMP: RegistryReference<ItemType> = of("redstone_lamp")
    @JvmField
    public val NOTE_BLOCK: RegistryReference<ItemType> = of("note_block")
    @JvmField
    public val STONE_BUTTON: RegistryReference<ItemType> = of("stone_button")
    @JvmField
    public val POLISHED_BLACKSTONE_BUTTON: RegistryReference<ItemType> = of("polished_blackstone_button")
    @JvmField
    public val OAK_BUTTON: RegistryReference<ItemType> = of("oak_button")
    @JvmField
    public val SPRUCE_BUTTON: RegistryReference<ItemType> = of("spruce_button")
    @JvmField
    public val BIRCH_BUTTON: RegistryReference<ItemType> = of("birch_button")
    @JvmField
    public val JUNGLE_BUTTON: RegistryReference<ItemType> = of("jungle_button")
    @JvmField
    public val ACACIA_BUTTON: RegistryReference<ItemType> = of("acacia_button")
    @JvmField
    public val DARK_OAK_BUTTON: RegistryReference<ItemType> = of("dark_oak_button")
    @JvmField
    public val MANGROVE_BUTTON: RegistryReference<ItemType> = of("mangrove_button")
    @JvmField
    public val CRIMSON_BUTTON: RegistryReference<ItemType> = of("crimson_button")
    @JvmField
    public val WARPED_BUTTON: RegistryReference<ItemType> = of("warped_button")
    @JvmField
    public val STONE_PRESSURE_PLATE: RegistryReference<ItemType> = of("stone_pressure_plate")
    @JvmField
    public val POLISHED_BLACKSTONE_PRESSURE_PLATE: RegistryReference<ItemType> = of("polished_blackstone_pressure_plate")
    @JvmField
    public val LIGHT_WEIGHTED_PRESSURE_PLATE: RegistryReference<ItemType> = of("light_weighted_pressure_plate")
    @JvmField
    public val HEAVY_WEIGHTED_PRESSURE_PLATE: RegistryReference<ItemType> = of("heavy_weighted_pressure_plate")
    @JvmField
    public val OAK_PRESSURE_PLATE: RegistryReference<ItemType> = of("oak_pressure_plate")
    @JvmField
    public val SPRUCE_PRESSURE_PLATE: RegistryReference<ItemType> = of("spruce_pressure_plate")
    @JvmField
    public val BIRCH_PRESSURE_PLATE: RegistryReference<ItemType> = of("birch_pressure_plate")
    @JvmField
    public val JUNGLE_PRESSURE_PLATE: RegistryReference<ItemType> = of("jungle_pressure_plate")
    @JvmField
    public val ACACIA_PRESSURE_PLATE: RegistryReference<ItemType> = of("acacia_pressure_plate")
    @JvmField
    public val DARK_OAK_PRESSURE_PLATE: RegistryReference<ItemType> = of("dark_oak_pressure_plate")
    @JvmField
    public val MANGROVE_PRESSURE_PLATE: RegistryReference<ItemType> = of("mangrove_pressure_plate")
    @JvmField
    public val CRIMSON_PRESSURE_PLATE: RegistryReference<ItemType> = of("crimson_pressure_plate")
    @JvmField
    public val WARPED_PRESSURE_PLATE: RegistryReference<ItemType> = of("warped_pressure_plate")
    @JvmField
    public val IRON_DOOR: RegistryReference<ItemType> = of("iron_door")
    @JvmField
    public val OAK_DOOR: RegistryReference<ItemType> = of("oak_door")
    @JvmField
    public val SPRUCE_DOOR: RegistryReference<ItemType> = of("spruce_door")
    @JvmField
    public val BIRCH_DOOR: RegistryReference<ItemType> = of("birch_door")
    @JvmField
    public val JUNGLE_DOOR: RegistryReference<ItemType> = of("jungle_door")
    @JvmField
    public val ACACIA_DOOR: RegistryReference<ItemType> = of("acacia_door")
    @JvmField
    public val DARK_OAK_DOOR: RegistryReference<ItemType> = of("dark_oak_door")
    @JvmField
    public val MANGROVE_DOOR: RegistryReference<ItemType> = of("mangrove_door")
    @JvmField
    public val CRIMSON_DOOR: RegistryReference<ItemType> = of("crimson_door")
    @JvmField
    public val WARPED_DOOR: RegistryReference<ItemType> = of("warped_door")
    @JvmField
    public val IRON_TRAPDOOR: RegistryReference<ItemType> = of("iron_trapdoor")
    @JvmField
    public val OAK_TRAPDOOR: RegistryReference<ItemType> = of("oak_trapdoor")
    @JvmField
    public val SPRUCE_TRAPDOOR: RegistryReference<ItemType> = of("spruce_trapdoor")
    @JvmField
    public val BIRCH_TRAPDOOR: RegistryReference<ItemType> = of("birch_trapdoor")
    @JvmField
    public val JUNGLE_TRAPDOOR: RegistryReference<ItemType> = of("jungle_trapdoor")
    @JvmField
    public val ACACIA_TRAPDOOR: RegistryReference<ItemType> = of("acacia_trapdoor")
    @JvmField
    public val DARK_OAK_TRAPDOOR: RegistryReference<ItemType> = of("dark_oak_trapdoor")
    @JvmField
    public val MANGROVE_TRAPDOOR: RegistryReference<ItemType> = of("mangrove_trapdoor")
    @JvmField
    public val CRIMSON_TRAPDOOR: RegistryReference<ItemType> = of("crimson_trapdoor")
    @JvmField
    public val WARPED_TRAPDOOR: RegistryReference<ItemType> = of("warped_trapdoor")
    @JvmField
    public val OAK_FENCE_GATE: RegistryReference<ItemType> = of("oak_fence_gate")
    @JvmField
    public val SPRUCE_FENCE_GATE: RegistryReference<ItemType> = of("spruce_fence_gate")
    @JvmField
    public val BIRCH_FENCE_GATE: RegistryReference<ItemType> = of("birch_fence_gate")
    @JvmField
    public val JUNGLE_FENCE_GATE: RegistryReference<ItemType> = of("jungle_fence_gate")
    @JvmField
    public val ACACIA_FENCE_GATE: RegistryReference<ItemType> = of("acacia_fence_gate")
    @JvmField
    public val DARK_OAK_FENCE_GATE: RegistryReference<ItemType> = of("dark_oak_fence_gate")
    @JvmField
    public val MANGROVE_FENCE_GATE: RegistryReference<ItemType> = of("mangrove_fence_gate")
    @JvmField
    public val CRIMSON_FENCE_GATE: RegistryReference<ItemType> = of("crimson_fence_gate")
    @JvmField
    public val WARPED_FENCE_GATE: RegistryReference<ItemType> = of("warped_fence_gate")
    @JvmField
    public val POWERED_RAIL: RegistryReference<ItemType> = of("powered_rail")
    @JvmField
    public val DETECTOR_RAIL: RegistryReference<ItemType> = of("detector_rail")
    @JvmField
    public val RAIL: RegistryReference<ItemType> = of("rail")
    @JvmField
    public val ACTIVATOR_RAIL: RegistryReference<ItemType> = of("activator_rail")
    @JvmField
    public val SADDLE: RegistryReference<ItemType> = of("saddle")
    @JvmField
    public val MINECART: RegistryReference<ItemType> = of("minecart")
    @JvmField
    public val CHEST_MINECART: RegistryReference<ItemType> = of("chest_minecart")
    @JvmField
    public val FURNACE_MINECART: RegistryReference<ItemType> = of("furnace_minecart")
    @JvmField
    public val TNT_MINECART: RegistryReference<ItemType> = of("tnt_minecart")
    @JvmField
    public val HOPPER_MINECART: RegistryReference<ItemType> = of("hopper_minecart")
    @JvmField
    public val CARROT_ON_A_STICK: RegistryReference<ItemType> = of("carrot_on_a_stick")
    @JvmField
    public val WARPED_FUNGUS_ON_A_STICK: RegistryReference<ItemType> = of("warped_fungus_on_a_stick")
    @JvmField
    public val ELYTRA: RegistryReference<ItemType> = of("elytra")
    @JvmField
    public val OAK_BOAT: RegistryReference<ItemType> = of("oak_boat")
    @JvmField
    public val OAK_CHEST_BOAT: RegistryReference<ItemType> = of("oak_chest_boat")
    @JvmField
    public val SPRUCE_BOAT: RegistryReference<ItemType> = of("spruce_boat")
    @JvmField
    public val SPRUCE_CHEST_BOAT: RegistryReference<ItemType> = of("spruce_chest_boat")
    @JvmField
    public val BIRCH_BOAT: RegistryReference<ItemType> = of("birch_boat")
    @JvmField
    public val BIRCH_CHEST_BOAT: RegistryReference<ItemType> = of("birch_chest_boat")
    @JvmField
    public val JUNGLE_BOAT: RegistryReference<ItemType> = of("jungle_boat")
    @JvmField
    public val JUNGLE_CHEST_BOAT: RegistryReference<ItemType> = of("jungle_chest_boat")
    @JvmField
    public val ACACIA_BOAT: RegistryReference<ItemType> = of("acacia_boat")
    @JvmField
    public val ACACIA_CHEST_BOAT: RegistryReference<ItemType> = of("acacia_chest_boat")
    @JvmField
    public val DARK_OAK_BOAT: RegistryReference<ItemType> = of("dark_oak_boat")
    @JvmField
    public val DARK_OAK_CHEST_BOAT: RegistryReference<ItemType> = of("dark_oak_chest_boat")
    @JvmField
    public val MANGROVE_BOAT: RegistryReference<ItemType> = of("mangrove_boat")
    @JvmField
    public val MANGROVE_CHEST_BOAT: RegistryReference<ItemType> = of("mangrove_chest_boat")
    @JvmField
    public val STRUCTURE_BLOCK: RegistryReference<ItemType> = of("structure_block")
    @JvmField
    public val JIGSAW: RegistryReference<ItemType> = of("jigsaw")
    @JvmField
    public val TURTLE_HELMET: RegistryReference<ItemType> = of("turtle_helmet")
    @JvmField
    public val SCUTE: RegistryReference<ItemType> = of("scute")
    @JvmField
    public val FLINT_AND_STEEL: RegistryReference<ItemType> = of("flint_and_steel")
    @JvmField
    public val APPLE: RegistryReference<ItemType> = of("apple")
    @JvmField
    public val BOW: RegistryReference<ItemType> = of("bow")
    @JvmField
    public val ARROW: RegistryReference<ItemType> = of("arrow")
    @JvmField
    public val COAL: RegistryReference<ItemType> = of("coal")
    @JvmField
    public val CHARCOAL: RegistryReference<ItemType> = of("charcoal")
    @JvmField
    public val DIAMOND: RegistryReference<ItemType> = of("diamond")
    @JvmField
    public val EMERALD: RegistryReference<ItemType> = of("emerald")
    @JvmField
    public val LAPIS_LAZULI: RegistryReference<ItemType> = of("lapis_lazuli")
    @JvmField
    public val QUARTZ: RegistryReference<ItemType> = of("quartz")
    @JvmField
    public val AMETHYST_SHARD: RegistryReference<ItemType> = of("amethyst_shard")
    @JvmField
    public val RAW_IRON: RegistryReference<ItemType> = of("raw_iron")
    @JvmField
    public val IRON_INGOT: RegistryReference<ItemType> = of("iron_ingot")
    @JvmField
    public val RAW_COPPER: RegistryReference<ItemType> = of("raw_copper")
    @JvmField
    public val COPPER_INGOT: RegistryReference<ItemType> = of("copper_ingot")
    @JvmField
    public val RAW_GOLD: RegistryReference<ItemType> = of("raw_gold")
    @JvmField
    public val GOLD_INGOT: RegistryReference<ItemType> = of("gold_ingot")
    @JvmField
    public val NETHERITE_INGOT: RegistryReference<ItemType> = of("netherite_ingot")
    @JvmField
    public val NETHERITE_SCRAP: RegistryReference<ItemType> = of("netherite_scrap")
    @JvmField
    public val WOODEN_SWORD: RegistryReference<ItemType> = of("wooden_sword")
    @JvmField
    public val WOODEN_SHOVEL: RegistryReference<ItemType> = of("wooden_shovel")
    @JvmField
    public val WOODEN_PICKAXE: RegistryReference<ItemType> = of("wooden_pickaxe")
    @JvmField
    public val WOODEN_AXE: RegistryReference<ItemType> = of("wooden_axe")
    @JvmField
    public val WOODEN_HOE: RegistryReference<ItemType> = of("wooden_hoe")
    @JvmField
    public val STONE_SWORD: RegistryReference<ItemType> = of("stone_sword")
    @JvmField
    public val STONE_SHOVEL: RegistryReference<ItemType> = of("stone_shovel")
    @JvmField
    public val STONE_PICKAXE: RegistryReference<ItemType> = of("stone_pickaxe")
    @JvmField
    public val STONE_AXE: RegistryReference<ItemType> = of("stone_axe")
    @JvmField
    public val STONE_HOE: RegistryReference<ItemType> = of("stone_hoe")
    @JvmField
    public val GOLDEN_SWORD: RegistryReference<ItemType> = of("golden_sword")
    @JvmField
    public val GOLDEN_SHOVEL: RegistryReference<ItemType> = of("golden_shovel")
    @JvmField
    public val GOLDEN_PICKAXE: RegistryReference<ItemType> = of("golden_pickaxe")
    @JvmField
    public val GOLDEN_AXE: RegistryReference<ItemType> = of("golden_axe")
    @JvmField
    public val GOLDEN_HOE: RegistryReference<ItemType> = of("golden_hoe")
    @JvmField
    public val IRON_SWORD: RegistryReference<ItemType> = of("iron_sword")
    @JvmField
    public val IRON_SHOVEL: RegistryReference<ItemType> = of("iron_shovel")
    @JvmField
    public val IRON_PICKAXE: RegistryReference<ItemType> = of("iron_pickaxe")
    @JvmField
    public val IRON_AXE: RegistryReference<ItemType> = of("iron_axe")
    @JvmField
    public val IRON_HOE: RegistryReference<ItemType> = of("iron_hoe")
    @JvmField
    public val DIAMOND_SWORD: RegistryReference<ItemType> = of("diamond_sword")
    @JvmField
    public val DIAMOND_SHOVEL: RegistryReference<ItemType> = of("diamond_shovel")
    @JvmField
    public val DIAMOND_PICKAXE: RegistryReference<ItemType> = of("diamond_pickaxe")
    @JvmField
    public val DIAMOND_AXE: RegistryReference<ItemType> = of("diamond_axe")
    @JvmField
    public val DIAMOND_HOE: RegistryReference<ItemType> = of("diamond_hoe")
    @JvmField
    public val NETHERITE_SWORD: RegistryReference<ItemType> = of("netherite_sword")
    @JvmField
    public val NETHERITE_SHOVEL: RegistryReference<ItemType> = of("netherite_shovel")
    @JvmField
    public val NETHERITE_PICKAXE: RegistryReference<ItemType> = of("netherite_pickaxe")
    @JvmField
    public val NETHERITE_AXE: RegistryReference<ItemType> = of("netherite_axe")
    @JvmField
    public val NETHERITE_HOE: RegistryReference<ItemType> = of("netherite_hoe")
    @JvmField
    public val STICK: RegistryReference<ItemType> = of("stick")
    @JvmField
    public val BOWL: RegistryReference<ItemType> = of("bowl")
    @JvmField
    public val MUSHROOM_STEW: RegistryReference<ItemType> = of("mushroom_stew")
    @JvmField
    public val STRING: RegistryReference<ItemType> = of("string")
    @JvmField
    public val FEATHER: RegistryReference<ItemType> = of("feather")
    @JvmField
    public val GUNPOWDER: RegistryReference<ItemType> = of("gunpowder")
    @JvmField
    public val WHEAT_SEEDS: RegistryReference<ItemType> = of("wheat_seeds")
    @JvmField
    public val WHEAT: RegistryReference<ItemType> = of("wheat")
    @JvmField
    public val BREAD: RegistryReference<ItemType> = of("bread")
    @JvmField
    public val LEATHER_HELMET: RegistryReference<ItemType> = of("leather_helmet")
    @JvmField
    public val LEATHER_CHESTPLATE: RegistryReference<ItemType> = of("leather_chestplate")
    @JvmField
    public val LEATHER_LEGGINGS: RegistryReference<ItemType> = of("leather_leggings")
    @JvmField
    public val LEATHER_BOOTS: RegistryReference<ItemType> = of("leather_boots")
    @JvmField
    public val CHAINMAIL_HELMET: RegistryReference<ItemType> = of("chainmail_helmet")
    @JvmField
    public val CHAINMAIL_CHESTPLATE: RegistryReference<ItemType> = of("chainmail_chestplate")
    @JvmField
    public val CHAINMAIL_LEGGINGS: RegistryReference<ItemType> = of("chainmail_leggings")
    @JvmField
    public val CHAINMAIL_BOOTS: RegistryReference<ItemType> = of("chainmail_boots")
    @JvmField
    public val IRON_HELMET: RegistryReference<ItemType> = of("iron_helmet")
    @JvmField
    public val IRON_CHESTPLATE: RegistryReference<ItemType> = of("iron_chestplate")
    @JvmField
    public val IRON_LEGGINGS: RegistryReference<ItemType> = of("iron_leggings")
    @JvmField
    public val IRON_BOOTS: RegistryReference<ItemType> = of("iron_boots")
    @JvmField
    public val DIAMOND_HELMET: RegistryReference<ItemType> = of("diamond_helmet")
    @JvmField
    public val DIAMOND_CHESTPLATE: RegistryReference<ItemType> = of("diamond_chestplate")
    @JvmField
    public val DIAMOND_LEGGINGS: RegistryReference<ItemType> = of("diamond_leggings")
    @JvmField
    public val DIAMOND_BOOTS: RegistryReference<ItemType> = of("diamond_boots")
    @JvmField
    public val GOLDEN_HELMET: RegistryReference<ItemType> = of("golden_helmet")
    @JvmField
    public val GOLDEN_CHESTPLATE: RegistryReference<ItemType> = of("golden_chestplate")
    @JvmField
    public val GOLDEN_LEGGINGS: RegistryReference<ItemType> = of("golden_leggings")
    @JvmField
    public val GOLDEN_BOOTS: RegistryReference<ItemType> = of("golden_boots")
    @JvmField
    public val NETHERITE_HELMET: RegistryReference<ItemType> = of("netherite_helmet")
    @JvmField
    public val NETHERITE_CHESTPLATE: RegistryReference<ItemType> = of("netherite_chestplate")
    @JvmField
    public val NETHERITE_LEGGINGS: RegistryReference<ItemType> = of("netherite_leggings")
    @JvmField
    public val NETHERITE_BOOTS: RegistryReference<ItemType> = of("netherite_boots")
    @JvmField
    public val FLINT: RegistryReference<ItemType> = of("flint")
    @JvmField
    public val PORKCHOP: RegistryReference<ItemType> = of("porkchop")
    @JvmField
    public val COOKED_PORKCHOP: RegistryReference<ItemType> = of("cooked_porkchop")
    @JvmField
    public val PAINTING: RegistryReference<ItemType> = of("painting")
    @JvmField
    public val GOLDEN_APPLE: RegistryReference<ItemType> = of("golden_apple")
    @JvmField
    public val ENCHANTED_GOLDEN_APPLE: RegistryReference<ItemType> = of("enchanted_golden_apple")
    @JvmField
    public val OAK_SIGN: RegistryReference<ItemType> = of("oak_sign")
    @JvmField
    public val SPRUCE_SIGN: RegistryReference<ItemType> = of("spruce_sign")
    @JvmField
    public val BIRCH_SIGN: RegistryReference<ItemType> = of("birch_sign")
    @JvmField
    public val JUNGLE_SIGN: RegistryReference<ItemType> = of("jungle_sign")
    @JvmField
    public val ACACIA_SIGN: RegistryReference<ItemType> = of("acacia_sign")
    @JvmField
    public val DARK_OAK_SIGN: RegistryReference<ItemType> = of("dark_oak_sign")
    @JvmField
    public val MANGROVE_SIGN: RegistryReference<ItemType> = of("mangrove_sign")
    @JvmField
    public val CRIMSON_SIGN: RegistryReference<ItemType> = of("crimson_sign")
    @JvmField
    public val WARPED_SIGN: RegistryReference<ItemType> = of("warped_sign")
    @JvmField
    public val BUCKET: RegistryReference<ItemType> = of("bucket")
    @JvmField
    public val WATER_BUCKET: RegistryReference<ItemType> = of("water_bucket")
    @JvmField
    public val LAVA_BUCKET: RegistryReference<ItemType> = of("lava_bucket")
    @JvmField
    public val POWDER_SNOW_BUCKET: RegistryReference<ItemType> = of("powder_snow_bucket")
    @JvmField
    public val SNOWBALL: RegistryReference<ItemType> = of("snowball")
    @JvmField
    public val LEATHER: RegistryReference<ItemType> = of("leather")
    @JvmField
    public val MILK_BUCKET: RegistryReference<ItemType> = of("milk_bucket")
    @JvmField
    public val PUFFERFISH_BUCKET: RegistryReference<ItemType> = of("pufferfish_bucket")
    @JvmField
    public val SALMON_BUCKET: RegistryReference<ItemType> = of("salmon_bucket")
    @JvmField
    public val COD_BUCKET: RegistryReference<ItemType> = of("cod_bucket")
    @JvmField
    public val TROPICAL_FISH_BUCKET: RegistryReference<ItemType> = of("tropical_fish_bucket")
    @JvmField
    public val AXOLOTL_BUCKET: RegistryReference<ItemType> = of("axolotl_bucket")
    @JvmField
    public val TADPOLE_BUCKET: RegistryReference<ItemType> = of("tadpole_bucket")
    @JvmField
    public val BRICK: RegistryReference<ItemType> = of("brick")
    @JvmField
    public val CLAY_BALL: RegistryReference<ItemType> = of("clay_ball")
    @JvmField
    public val DRIED_KELP_BLOCK: RegistryReference<ItemType> = of("dried_kelp_block")
    @JvmField
    public val PAPER: RegistryReference<ItemType> = of("paper")
    @JvmField
    public val BOOK: RegistryReference<ItemType> = of("book")
    @JvmField
    public val SLIME_BALL: RegistryReference<ItemType> = of("slime_ball")
    @JvmField
    public val EGG: RegistryReference<ItemType> = of("egg")
    @JvmField
    public val COMPASS: RegistryReference<ItemType> = of("compass")
    @JvmField
    public val RECOVERY_COMPASS: RegistryReference<ItemType> = of("recovery_compass")
    @JvmField
    public val BUNDLE: RegistryReference<ItemType> = of("bundle")
    @JvmField
    public val FISHING_ROD: RegistryReference<ItemType> = of("fishing_rod")
    @JvmField
    public val CLOCK: RegistryReference<ItemType> = of("clock")
    @JvmField
    public val SPYGLASS: RegistryReference<ItemType> = of("spyglass")
    @JvmField
    public val GLOWSTONE_DUST: RegistryReference<ItemType> = of("glowstone_dust")
    @JvmField
    public val COD: RegistryReference<ItemType> = of("cod")
    @JvmField
    public val SALMON: RegistryReference<ItemType> = of("salmon")
    @JvmField
    public val TROPICAL_FISH: RegistryReference<ItemType> = of("tropical_fish")
    @JvmField
    public val PUFFERFISH: RegistryReference<ItemType> = of("pufferfish")
    @JvmField
    public val COOKED_COD: RegistryReference<ItemType> = of("cooked_cod")
    @JvmField
    public val COOKED_SALMON: RegistryReference<ItemType> = of("cooked_salmon")
    @JvmField
    public val INK_SAC: RegistryReference<ItemType> = of("ink_sac")
    @JvmField
    public val GLOW_INK_SAC: RegistryReference<ItemType> = of("glow_ink_sac")
    @JvmField
    public val COCOA_BEANS: RegistryReference<ItemType> = of("cocoa_beans")
    @JvmField
    public val WHITE_DYE: RegistryReference<ItemType> = of("white_dye")
    @JvmField
    public val ORANGE_DYE: RegistryReference<ItemType> = of("orange_dye")
    @JvmField
    public val MAGENTA_DYE: RegistryReference<ItemType> = of("magenta_dye")
    @JvmField
    public val LIGHT_BLUE_DYE: RegistryReference<ItemType> = of("light_blue_dye")
    @JvmField
    public val YELLOW_DYE: RegistryReference<ItemType> = of("yellow_dye")
    @JvmField
    public val LIME_DYE: RegistryReference<ItemType> = of("lime_dye")
    @JvmField
    public val PINK_DYE: RegistryReference<ItemType> = of("pink_dye")
    @JvmField
    public val GRAY_DYE: RegistryReference<ItemType> = of("gray_dye")
    @JvmField
    public val LIGHT_GRAY_DYE: RegistryReference<ItemType> = of("light_gray_dye")
    @JvmField
    public val CYAN_DYE: RegistryReference<ItemType> = of("cyan_dye")
    @JvmField
    public val PURPLE_DYE: RegistryReference<ItemType> = of("purple_dye")
    @JvmField
    public val BLUE_DYE: RegistryReference<ItemType> = of("blue_dye")
    @JvmField
    public val BROWN_DYE: RegistryReference<ItemType> = of("brown_dye")
    @JvmField
    public val GREEN_DYE: RegistryReference<ItemType> = of("green_dye")
    @JvmField
    public val RED_DYE: RegistryReference<ItemType> = of("red_dye")
    @JvmField
    public val BLACK_DYE: RegistryReference<ItemType> = of("black_dye")
    @JvmField
    public val BONE_MEAL: RegistryReference<ItemType> = of("bone_meal")
    @JvmField
    public val BONE: RegistryReference<ItemType> = of("bone")
    @JvmField
    public val SUGAR: RegistryReference<ItemType> = of("sugar")
    @JvmField
    public val CAKE: RegistryReference<ItemType> = of("cake")
    @JvmField
    public val WHITE_BED: RegistryReference<ItemType> = of("white_bed")
    @JvmField
    public val ORANGE_BED: RegistryReference<ItemType> = of("orange_bed")
    @JvmField
    public val MAGENTA_BED: RegistryReference<ItemType> = of("magenta_bed")
    @JvmField
    public val LIGHT_BLUE_BED: RegistryReference<ItemType> = of("light_blue_bed")
    @JvmField
    public val YELLOW_BED: RegistryReference<ItemType> = of("yellow_bed")
    @JvmField
    public val LIME_BED: RegistryReference<ItemType> = of("lime_bed")
    @JvmField
    public val PINK_BED: RegistryReference<ItemType> = of("pink_bed")
    @JvmField
    public val GRAY_BED: RegistryReference<ItemType> = of("gray_bed")
    @JvmField
    public val LIGHT_GRAY_BED: RegistryReference<ItemType> = of("light_gray_bed")
    @JvmField
    public val CYAN_BED: RegistryReference<ItemType> = of("cyan_bed")
    @JvmField
    public val PURPLE_BED: RegistryReference<ItemType> = of("purple_bed")
    @JvmField
    public val BLUE_BED: RegistryReference<ItemType> = of("blue_bed")
    @JvmField
    public val BROWN_BED: RegistryReference<ItemType> = of("brown_bed")
    @JvmField
    public val GREEN_BED: RegistryReference<ItemType> = of("green_bed")
    @JvmField
    public val RED_BED: RegistryReference<ItemType> = of("red_bed")
    @JvmField
    public val BLACK_BED: RegistryReference<ItemType> = of("black_bed")
    @JvmField
    public val COOKIE: RegistryReference<ItemType> = of("cookie")
    @JvmField
    public val FILLED_MAP: RegistryReference<ItemType> = of("filled_map")
    @JvmField
    public val SHEARS: RegistryReference<ItemType> = of("shears")
    @JvmField
    public val MELON_SLICE: RegistryReference<ItemType> = of("melon_slice")
    @JvmField
    public val DRIED_KELP: RegistryReference<ItemType> = of("dried_kelp")
    @JvmField
    public val PUMPKIN_SEEDS: RegistryReference<ItemType> = of("pumpkin_seeds")
    @JvmField
    public val MELON_SEEDS: RegistryReference<ItemType> = of("melon_seeds")
    @JvmField
    public val BEEF: RegistryReference<ItemType> = of("beef")
    @JvmField
    public val COOKED_BEEF: RegistryReference<ItemType> = of("cooked_beef")
    @JvmField
    public val CHICKEN: RegistryReference<ItemType> = of("chicken")
    @JvmField
    public val COOKED_CHICKEN: RegistryReference<ItemType> = of("cooked_chicken")
    @JvmField
    public val ROTTEN_FLESH: RegistryReference<ItemType> = of("rotten_flesh")
    @JvmField
    public val ENDER_PEARL: RegistryReference<ItemType> = of("ender_pearl")
    @JvmField
    public val BLAZE_ROD: RegistryReference<ItemType> = of("blaze_rod")
    @JvmField
    public val GHAST_TEAR: RegistryReference<ItemType> = of("ghast_tear")
    @JvmField
    public val GOLD_NUGGET: RegistryReference<ItemType> = of("gold_nugget")
    @JvmField
    public val NETHER_WART: RegistryReference<ItemType> = of("nether_wart")
    @JvmField
    public val POTION: RegistryReference<ItemType> = of("potion")
    @JvmField
    public val GLASS_BOTTLE: RegistryReference<ItemType> = of("glass_bottle")
    @JvmField
    public val SPIDER_EYE: RegistryReference<ItemType> = of("spider_eye")
    @JvmField
    public val FERMENTED_SPIDER_EYE: RegistryReference<ItemType> = of("fermented_spider_eye")
    @JvmField
    public val BLAZE_POWDER: RegistryReference<ItemType> = of("blaze_powder")
    @JvmField
    public val MAGMA_CREAM: RegistryReference<ItemType> = of("magma_cream")
    @JvmField
    public val BREWING_STAND: RegistryReference<ItemType> = of("brewing_stand")
    @JvmField
    public val CAULDRON: RegistryReference<ItemType> = of("cauldron")
    @JvmField
    public val ENDER_EYE: RegistryReference<ItemType> = of("ender_eye")
    @JvmField
    public val GLISTERING_MELON_SLICE: RegistryReference<ItemType> = of("glistering_melon_slice")
    @JvmField
    public val ALLAY_SPAWN_EGG: RegistryReference<ItemType> = of("allay_spawn_egg")
    @JvmField
    public val AXOLOTL_SPAWN_EGG: RegistryReference<ItemType> = of("axolotl_spawn_egg")
    @JvmField
    public val BAT_SPAWN_EGG: RegistryReference<ItemType> = of("bat_spawn_egg")
    @JvmField
    public val BEE_SPAWN_EGG: RegistryReference<ItemType> = of("bee_spawn_egg")
    @JvmField
    public val BLAZE_SPAWN_EGG: RegistryReference<ItemType> = of("blaze_spawn_egg")
    @JvmField
    public val CAT_SPAWN_EGG: RegistryReference<ItemType> = of("cat_spawn_egg")
    @JvmField
    public val CAVE_SPIDER_SPAWN_EGG: RegistryReference<ItemType> = of("cave_spider_spawn_egg")
    @JvmField
    public val CHICKEN_SPAWN_EGG: RegistryReference<ItemType> = of("chicken_spawn_egg")
    @JvmField
    public val COD_SPAWN_EGG: RegistryReference<ItemType> = of("cod_spawn_egg")
    @JvmField
    public val COW_SPAWN_EGG: RegistryReference<ItemType> = of("cow_spawn_egg")
    @JvmField
    public val CREEPER_SPAWN_EGG: RegistryReference<ItemType> = of("creeper_spawn_egg")
    @JvmField
    public val DOLPHIN_SPAWN_EGG: RegistryReference<ItemType> = of("dolphin_spawn_egg")
    @JvmField
    public val DONKEY_SPAWN_EGG: RegistryReference<ItemType> = of("donkey_spawn_egg")
    @JvmField
    public val DROWNED_SPAWN_EGG: RegistryReference<ItemType> = of("drowned_spawn_egg")
    @JvmField
    public val ELDER_GUARDIAN_SPAWN_EGG: RegistryReference<ItemType> = of("elder_guardian_spawn_egg")
    @JvmField
    public val ENDERMAN_SPAWN_EGG: RegistryReference<ItemType> = of("enderman_spawn_egg")
    @JvmField
    public val ENDERMITE_SPAWN_EGG: RegistryReference<ItemType> = of("endermite_spawn_egg")
    @JvmField
    public val EVOKER_SPAWN_EGG: RegistryReference<ItemType> = of("evoker_spawn_egg")
    @JvmField
    public val FOX_SPAWN_EGG: RegistryReference<ItemType> = of("fox_spawn_egg")
    @JvmField
    public val FROG_SPAWN_EGG: RegistryReference<ItemType> = of("frog_spawn_egg")
    @JvmField
    public val GHAST_SPAWN_EGG: RegistryReference<ItemType> = of("ghast_spawn_egg")
    @JvmField
    public val GLOW_SQUID_SPAWN_EGG: RegistryReference<ItemType> = of("glow_squid_spawn_egg")
    @JvmField
    public val GOAT_SPAWN_EGG: RegistryReference<ItemType> = of("goat_spawn_egg")
    @JvmField
    public val GUARDIAN_SPAWN_EGG: RegistryReference<ItemType> = of("guardian_spawn_egg")
    @JvmField
    public val HOGLIN_SPAWN_EGG: RegistryReference<ItemType> = of("hoglin_spawn_egg")
    @JvmField
    public val HORSE_SPAWN_EGG: RegistryReference<ItemType> = of("horse_spawn_egg")
    @JvmField
    public val HUSK_SPAWN_EGG: RegistryReference<ItemType> = of("husk_spawn_egg")
    @JvmField
    public val LLAMA_SPAWN_EGG: RegistryReference<ItemType> = of("llama_spawn_egg")
    @JvmField
    public val MAGMA_CUBE_SPAWN_EGG: RegistryReference<ItemType> = of("magma_cube_spawn_egg")
    @JvmField
    public val MOOSHROOM_SPAWN_EGG: RegistryReference<ItemType> = of("mooshroom_spawn_egg")
    @JvmField
    public val MULE_SPAWN_EGG: RegistryReference<ItemType> = of("mule_spawn_egg")
    @JvmField
    public val OCELOT_SPAWN_EGG: RegistryReference<ItemType> = of("ocelot_spawn_egg")
    @JvmField
    public val PANDA_SPAWN_EGG: RegistryReference<ItemType> = of("panda_spawn_egg")
    @JvmField
    public val PARROT_SPAWN_EGG: RegistryReference<ItemType> = of("parrot_spawn_egg")
    @JvmField
    public val PHANTOM_SPAWN_EGG: RegistryReference<ItemType> = of("phantom_spawn_egg")
    @JvmField
    public val PIG_SPAWN_EGG: RegistryReference<ItemType> = of("pig_spawn_egg")
    @JvmField
    public val PIGLIN_SPAWN_EGG: RegistryReference<ItemType> = of("piglin_spawn_egg")
    @JvmField
    public val PIGLIN_BRUTE_SPAWN_EGG: RegistryReference<ItemType> = of("piglin_brute_spawn_egg")
    @JvmField
    public val PILLAGER_SPAWN_EGG: RegistryReference<ItemType> = of("pillager_spawn_egg")
    @JvmField
    public val POLAR_BEAR_SPAWN_EGG: RegistryReference<ItemType> = of("polar_bear_spawn_egg")
    @JvmField
    public val PUFFERFISH_SPAWN_EGG: RegistryReference<ItemType> = of("pufferfish_spawn_egg")
    @JvmField
    public val RABBIT_SPAWN_EGG: RegistryReference<ItemType> = of("rabbit_spawn_egg")
    @JvmField
    public val RAVAGER_SPAWN_EGG: RegistryReference<ItemType> = of("ravager_spawn_egg")
    @JvmField
    public val SALMON_SPAWN_EGG: RegistryReference<ItemType> = of("salmon_spawn_egg")
    @JvmField
    public val SHEEP_SPAWN_EGG: RegistryReference<ItemType> = of("sheep_spawn_egg")
    @JvmField
    public val SHULKER_SPAWN_EGG: RegistryReference<ItemType> = of("shulker_spawn_egg")
    @JvmField
    public val SILVERFISH_SPAWN_EGG: RegistryReference<ItemType> = of("silverfish_spawn_egg")
    @JvmField
    public val SKELETON_SPAWN_EGG: RegistryReference<ItemType> = of("skeleton_spawn_egg")
    @JvmField
    public val SKELETON_HORSE_SPAWN_EGG: RegistryReference<ItemType> = of("skeleton_horse_spawn_egg")
    @JvmField
    public val SLIME_SPAWN_EGG: RegistryReference<ItemType> = of("slime_spawn_egg")
    @JvmField
    public val SPIDER_SPAWN_EGG: RegistryReference<ItemType> = of("spider_spawn_egg")
    @JvmField
    public val SQUID_SPAWN_EGG: RegistryReference<ItemType> = of("squid_spawn_egg")
    @JvmField
    public val STRAY_SPAWN_EGG: RegistryReference<ItemType> = of("stray_spawn_egg")
    @JvmField
    public val STRIDER_SPAWN_EGG: RegistryReference<ItemType> = of("strider_spawn_egg")
    @JvmField
    public val TADPOLE_SPAWN_EGG: RegistryReference<ItemType> = of("tadpole_spawn_egg")
    @JvmField
    public val TRADER_LLAMA_SPAWN_EGG: RegistryReference<ItemType> = of("trader_llama_spawn_egg")
    @JvmField
    public val TROPICAL_FISH_SPAWN_EGG: RegistryReference<ItemType> = of("tropical_fish_spawn_egg")
    @JvmField
    public val TURTLE_SPAWN_EGG: RegistryReference<ItemType> = of("turtle_spawn_egg")
    @JvmField
    public val VEX_SPAWN_EGG: RegistryReference<ItemType> = of("vex_spawn_egg")
    @JvmField
    public val VILLAGER_SPAWN_EGG: RegistryReference<ItemType> = of("villager_spawn_egg")
    @JvmField
    public val VINDICATOR_SPAWN_EGG: RegistryReference<ItemType> = of("vindicator_spawn_egg")
    @JvmField
    public val WANDERING_TRADER_SPAWN_EGG: RegistryReference<ItemType> = of("wandering_trader_spawn_egg")
    @JvmField
    public val WARDEN_SPAWN_EGG: RegistryReference<ItemType> = of("warden_spawn_egg")
    @JvmField
    public val WITCH_SPAWN_EGG: RegistryReference<ItemType> = of("witch_spawn_egg")
    @JvmField
    public val WITHER_SKELETON_SPAWN_EGG: RegistryReference<ItemType> = of("wither_skeleton_spawn_egg")
    @JvmField
    public val WOLF_SPAWN_EGG: RegistryReference<ItemType> = of("wolf_spawn_egg")
    @JvmField
    public val ZOGLIN_SPAWN_EGG: RegistryReference<ItemType> = of("zoglin_spawn_egg")
    @JvmField
    public val ZOMBIE_SPAWN_EGG: RegistryReference<ItemType> = of("zombie_spawn_egg")
    @JvmField
    public val ZOMBIE_HORSE_SPAWN_EGG: RegistryReference<ItemType> = of("zombie_horse_spawn_egg")
    @JvmField
    public val ZOMBIE_VILLAGER_SPAWN_EGG: RegistryReference<ItemType> = of("zombie_villager_spawn_egg")
    @JvmField
    public val ZOMBIFIED_PIGLIN_SPAWN_EGG: RegistryReference<ItemType> = of("zombified_piglin_spawn_egg")
    @JvmField
    public val EXPERIENCE_BOTTLE: RegistryReference<ItemType> = of("experience_bottle")
    @JvmField
    public val FIRE_CHARGE: RegistryReference<ItemType> = of("fire_charge")
    @JvmField
    public val WRITABLE_BOOK: RegistryReference<ItemType> = of("writable_book")
    @JvmField
    public val WRITTEN_BOOK: RegistryReference<ItemType> = of("written_book")
    @JvmField
    public val ITEM_FRAME: RegistryReference<ItemType> = of("item_frame")
    @JvmField
    public val GLOW_ITEM_FRAME: RegistryReference<ItemType> = of("glow_item_frame")
    @JvmField
    public val FLOWER_POT: RegistryReference<ItemType> = of("flower_pot")
    @JvmField
    public val CARROT: RegistryReference<ItemType> = of("carrot")
    @JvmField
    public val POTATO: RegistryReference<ItemType> = of("potato")
    @JvmField
    public val BAKED_POTATO: RegistryReference<ItemType> = of("baked_potato")
    @JvmField
    public val POISONOUS_POTATO: RegistryReference<ItemType> = of("poisonous_potato")
    @JvmField
    public val MAP: RegistryReference<ItemType> = of("map")
    @JvmField
    public val GOLDEN_CARROT: RegistryReference<ItemType> = of("golden_carrot")
    @JvmField
    public val SKELETON_SKULL: RegistryReference<ItemType> = of("skeleton_skull")
    @JvmField
    public val WITHER_SKELETON_SKULL: RegistryReference<ItemType> = of("wither_skeleton_skull")
    @JvmField
    public val PLAYER_HEAD: RegistryReference<ItemType> = of("player_head")
    @JvmField
    public val ZOMBIE_HEAD: RegistryReference<ItemType> = of("zombie_head")
    @JvmField
    public val CREEPER_HEAD: RegistryReference<ItemType> = of("creeper_head")
    @JvmField
    public val DRAGON_HEAD: RegistryReference<ItemType> = of("dragon_head")
    @JvmField
    public val NETHER_STAR: RegistryReference<ItemType> = of("nether_star")
    @JvmField
    public val PUMPKIN_PIE: RegistryReference<ItemType> = of("pumpkin_pie")
    @JvmField
    public val FIREWORK_ROCKET: RegistryReference<ItemType> = of("firework_rocket")
    @JvmField
    public val FIREWORK_STAR: RegistryReference<ItemType> = of("firework_star")
    @JvmField
    public val ENCHANTED_BOOK: RegistryReference<ItemType> = of("enchanted_book")
    @JvmField
    public val NETHER_BRICK: RegistryReference<ItemType> = of("nether_brick")
    @JvmField
    public val PRISMARINE_SHARD: RegistryReference<ItemType> = of("prismarine_shard")
    @JvmField
    public val PRISMARINE_CRYSTALS: RegistryReference<ItemType> = of("prismarine_crystals")
    @JvmField
    public val RABBIT: RegistryReference<ItemType> = of("rabbit")
    @JvmField
    public val COOKED_RABBIT: RegistryReference<ItemType> = of("cooked_rabbit")
    @JvmField
    public val RABBIT_STEW: RegistryReference<ItemType> = of("rabbit_stew")
    @JvmField
    public val RABBIT_FOOT: RegistryReference<ItemType> = of("rabbit_foot")
    @JvmField
    public val RABBIT_HIDE: RegistryReference<ItemType> = of("rabbit_hide")
    @JvmField
    public val ARMOR_STAND: RegistryReference<ItemType> = of("armor_stand")
    @JvmField
    public val IRON_HORSE_ARMOR: RegistryReference<ItemType> = of("iron_horse_armor")
    @JvmField
    public val GOLDEN_HORSE_ARMOR: RegistryReference<ItemType> = of("golden_horse_armor")
    @JvmField
    public val DIAMOND_HORSE_ARMOR: RegistryReference<ItemType> = of("diamond_horse_armor")
    @JvmField
    public val LEATHER_HORSE_ARMOR: RegistryReference<ItemType> = of("leather_horse_armor")
    @JvmField
    public val LEAD: RegistryReference<ItemType> = of("lead")
    @JvmField
    public val NAME_TAG: RegistryReference<ItemType> = of("name_tag")
    @JvmField
    public val COMMAND_BLOCK_MINECART: RegistryReference<ItemType> = of("command_block_minecart")
    @JvmField
    public val MUTTON: RegistryReference<ItemType> = of("mutton")
    @JvmField
    public val COOKED_MUTTON: RegistryReference<ItemType> = of("cooked_mutton")
    @JvmField
    public val WHITE_BANNER: RegistryReference<ItemType> = of("white_banner")
    @JvmField
    public val ORANGE_BANNER: RegistryReference<ItemType> = of("orange_banner")
    @JvmField
    public val MAGENTA_BANNER: RegistryReference<ItemType> = of("magenta_banner")
    @JvmField
    public val LIGHT_BLUE_BANNER: RegistryReference<ItemType> = of("light_blue_banner")
    @JvmField
    public val YELLOW_BANNER: RegistryReference<ItemType> = of("yellow_banner")
    @JvmField
    public val LIME_BANNER: RegistryReference<ItemType> = of("lime_banner")
    @JvmField
    public val PINK_BANNER: RegistryReference<ItemType> = of("pink_banner")
    @JvmField
    public val GRAY_BANNER: RegistryReference<ItemType> = of("gray_banner")
    @JvmField
    public val LIGHT_GRAY_BANNER: RegistryReference<ItemType> = of("light_gray_banner")
    @JvmField
    public val CYAN_BANNER: RegistryReference<ItemType> = of("cyan_banner")
    @JvmField
    public val PURPLE_BANNER: RegistryReference<ItemType> = of("purple_banner")
    @JvmField
    public val BLUE_BANNER: RegistryReference<ItemType> = of("blue_banner")
    @JvmField
    public val BROWN_BANNER: RegistryReference<ItemType> = of("brown_banner")
    @JvmField
    public val GREEN_BANNER: RegistryReference<ItemType> = of("green_banner")
    @JvmField
    public val RED_BANNER: RegistryReference<ItemType> = of("red_banner")
    @JvmField
    public val BLACK_BANNER: RegistryReference<ItemType> = of("black_banner")
    @JvmField
    public val END_CRYSTAL: RegistryReference<ItemType> = of("end_crystal")
    @JvmField
    public val CHORUS_FRUIT: RegistryReference<ItemType> = of("chorus_fruit")
    @JvmField
    public val POPPED_CHORUS_FRUIT: RegistryReference<ItemType> = of("popped_chorus_fruit")
    @JvmField
    public val BEETROOT: RegistryReference<ItemType> = of("beetroot")
    @JvmField
    public val BEETROOT_SEEDS: RegistryReference<ItemType> = of("beetroot_seeds")
    @JvmField
    public val BEETROOT_SOUP: RegistryReference<ItemType> = of("beetroot_soup")
    @JvmField
    public val DRAGON_BREATH: RegistryReference<ItemType> = of("dragon_breath")
    @JvmField
    public val SPLASH_POTION: RegistryReference<ItemType> = of("splash_potion")
    @JvmField
    public val SPECTRAL_ARROW: RegistryReference<ItemType> = of("spectral_arrow")
    @JvmField
    public val TIPPED_ARROW: RegistryReference<ItemType> = of("tipped_arrow")
    @JvmField
    public val LINGERING_POTION: RegistryReference<ItemType> = of("lingering_potion")
    @JvmField
    public val SHIELD: RegistryReference<ItemType> = of("shield")
    @JvmField
    public val TOTEM_OF_UNDYING: RegistryReference<ItemType> = of("totem_of_undying")
    @JvmField
    public val SHULKER_SHELL: RegistryReference<ItemType> = of("shulker_shell")
    @JvmField
    public val IRON_NUGGET: RegistryReference<ItemType> = of("iron_nugget")
    @JvmField
    public val KNOWLEDGE_BOOK: RegistryReference<ItemType> = of("knowledge_book")
    @JvmField
    public val DEBUG_STICK: RegistryReference<ItemType> = of("debug_stick")
    @JvmField
    public val MUSIC_DISC_13: RegistryReference<ItemType> = of("music_disc_13")
    @JvmField
    public val MUSIC_DISC_CAT: RegistryReference<ItemType> = of("music_disc_cat")
    @JvmField
    public val MUSIC_DISC_BLOCKS: RegistryReference<ItemType> = of("music_disc_blocks")
    @JvmField
    public val MUSIC_DISC_CHIRP: RegistryReference<ItemType> = of("music_disc_chirp")
    @JvmField
    public val MUSIC_DISC_FAR: RegistryReference<ItemType> = of("music_disc_far")
    @JvmField
    public val MUSIC_DISC_MALL: RegistryReference<ItemType> = of("music_disc_mall")
    @JvmField
    public val MUSIC_DISC_MELLOHI: RegistryReference<ItemType> = of("music_disc_mellohi")
    @JvmField
    public val MUSIC_DISC_STAL: RegistryReference<ItemType> = of("music_disc_stal")
    @JvmField
    public val MUSIC_DISC_STRAD: RegistryReference<ItemType> = of("music_disc_strad")
    @JvmField
    public val MUSIC_DISC_WARD: RegistryReference<ItemType> = of("music_disc_ward")
    @JvmField
    public val MUSIC_DISC_11: RegistryReference<ItemType> = of("music_disc_11")
    @JvmField
    public val MUSIC_DISC_WAIT: RegistryReference<ItemType> = of("music_disc_wait")
    @JvmField
    public val MUSIC_DISC_OTHERSIDE: RegistryReference<ItemType> = of("music_disc_otherside")
    @JvmField
    public val MUSIC_DISC_5: RegistryReference<ItemType> = of("music_disc_5")
    @JvmField
    public val MUSIC_DISC_PIGSTEP: RegistryReference<ItemType> = of("music_disc_pigstep")
    @JvmField
    public val DISC_FRAGMENT_5: RegistryReference<ItemType> = of("disc_fragment_5")
    @JvmField
    public val TRIDENT: RegistryReference<ItemType> = of("trident")
    @JvmField
    public val PHANTOM_MEMBRANE: RegistryReference<ItemType> = of("phantom_membrane")
    @JvmField
    public val NAUTILUS_SHELL: RegistryReference<ItemType> = of("nautilus_shell")
    @JvmField
    public val HEART_OF_THE_SEA: RegistryReference<ItemType> = of("heart_of_the_sea")
    @JvmField
    public val CROSSBOW: RegistryReference<ItemType> = of("crossbow")
    @JvmField
    public val SUSPICIOUS_STEW: RegistryReference<ItemType> = of("suspicious_stew")
    @JvmField
    public val LOOM: RegistryReference<ItemType> = of("loom")
    @JvmField
    public val FLOWER_BANNER_PATTERN: RegistryReference<ItemType> = of("flower_banner_pattern")
    @JvmField
    public val CREEPER_BANNER_PATTERN: RegistryReference<ItemType> = of("creeper_banner_pattern")
    @JvmField
    public val SKULL_BANNER_PATTERN: RegistryReference<ItemType> = of("skull_banner_pattern")
    @JvmField
    public val MOJANG_BANNER_PATTERN: RegistryReference<ItemType> = of("mojang_banner_pattern")
    @JvmField
    public val GLOBE_BANNER_PATTERN: RegistryReference<ItemType> = of("globe_banner_pattern")
    @JvmField
    public val PIGLIN_BANNER_PATTERN: RegistryReference<ItemType> = of("piglin_banner_pattern")
    @JvmField
    public val GOAT_HORN: RegistryReference<ItemType> = of("goat_horn")
    @JvmField
    public val COMPOSTER: RegistryReference<ItemType> = of("composter")
    @JvmField
    public val BARREL: RegistryReference<ItemType> = of("barrel")
    @JvmField
    public val SMOKER: RegistryReference<ItemType> = of("smoker")
    @JvmField
    public val BLAST_FURNACE: RegistryReference<ItemType> = of("blast_furnace")
    @JvmField
    public val CARTOGRAPHY_TABLE: RegistryReference<ItemType> = of("cartography_table")
    @JvmField
    public val FLETCHING_TABLE: RegistryReference<ItemType> = of("fletching_table")
    @JvmField
    public val GRINDSTONE: RegistryReference<ItemType> = of("grindstone")
    @JvmField
    public val SMITHING_TABLE: RegistryReference<ItemType> = of("smithing_table")
    @JvmField
    public val STONECUTTER: RegistryReference<ItemType> = of("stonecutter")
    @JvmField
    public val BELL: RegistryReference<ItemType> = of("bell")
    @JvmField
    public val LANTERN: RegistryReference<ItemType> = of("lantern")
    @JvmField
    public val SOUL_LANTERN: RegistryReference<ItemType> = of("soul_lantern")
    @JvmField
    public val SWEET_BERRIES: RegistryReference<ItemType> = of("sweet_berries")
    @JvmField
    public val GLOW_BERRIES: RegistryReference<ItemType> = of("glow_berries")
    @JvmField
    public val CAMPFIRE: RegistryReference<ItemType> = of("campfire")
    @JvmField
    public val SOUL_CAMPFIRE: RegistryReference<ItemType> = of("soul_campfire")
    @JvmField
    public val SHROOMLIGHT: RegistryReference<ItemType> = of("shroomlight")
    @JvmField
    public val HONEYCOMB: RegistryReference<ItemType> = of("honeycomb")
    @JvmField
    public val BEE_NEST: RegistryReference<ItemType> = of("bee_nest")
    @JvmField
    public val BEEHIVE: RegistryReference<ItemType> = of("beehive")
    @JvmField
    public val HONEY_BOTTLE: RegistryReference<ItemType> = of("honey_bottle")
    @JvmField
    public val HONEYCOMB_BLOCK: RegistryReference<ItemType> = of("honeycomb_block")
    @JvmField
    public val LODESTONE: RegistryReference<ItemType> = of("lodestone")
    @JvmField
    public val CRYING_OBSIDIAN: RegistryReference<ItemType> = of("crying_obsidian")
    @JvmField
    public val BLACKSTONE: RegistryReference<ItemType> = of("blackstone")
    @JvmField
    public val BLACKSTONE_SLAB: RegistryReference<ItemType> = of("blackstone_slab")
    @JvmField
    public val BLACKSTONE_STAIRS: RegistryReference<ItemType> = of("blackstone_stairs")
    @JvmField
    public val GILDED_BLACKSTONE: RegistryReference<ItemType> = of("gilded_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE: RegistryReference<ItemType> = of("polished_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_SLAB: RegistryReference<ItemType> = of("polished_blackstone_slab")
    @JvmField
    public val POLISHED_BLACKSTONE_STAIRS: RegistryReference<ItemType> = of("polished_blackstone_stairs")
    @JvmField
    public val CHISELED_POLISHED_BLACKSTONE: RegistryReference<ItemType> = of("chiseled_polished_blackstone")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICKS: RegistryReference<ItemType> = of("polished_blackstone_bricks")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_SLAB: RegistryReference<ItemType> = of("polished_blackstone_brick_slab")
    @JvmField
    public val POLISHED_BLACKSTONE_BRICK_STAIRS: RegistryReference<ItemType> = of("polished_blackstone_brick_stairs")
    @JvmField
    public val CRACKED_POLISHED_BLACKSTONE_BRICKS: RegistryReference<ItemType> = of("cracked_polished_blackstone_bricks")
    @JvmField
    public val RESPAWN_ANCHOR: RegistryReference<ItemType> = of("respawn_anchor")
    @JvmField
    public val CANDLE: RegistryReference<ItemType> = of("candle")
    @JvmField
    public val WHITE_CANDLE: RegistryReference<ItemType> = of("white_candle")
    @JvmField
    public val ORANGE_CANDLE: RegistryReference<ItemType> = of("orange_candle")
    @JvmField
    public val MAGENTA_CANDLE: RegistryReference<ItemType> = of("magenta_candle")
    @JvmField
    public val LIGHT_BLUE_CANDLE: RegistryReference<ItemType> = of("light_blue_candle")
    @JvmField
    public val YELLOW_CANDLE: RegistryReference<ItemType> = of("yellow_candle")
    @JvmField
    public val LIME_CANDLE: RegistryReference<ItemType> = of("lime_candle")
    @JvmField
    public val PINK_CANDLE: RegistryReference<ItemType> = of("pink_candle")
    @JvmField
    public val GRAY_CANDLE: RegistryReference<ItemType> = of("gray_candle")
    @JvmField
    public val LIGHT_GRAY_CANDLE: RegistryReference<ItemType> = of("light_gray_candle")
    @JvmField
    public val CYAN_CANDLE: RegistryReference<ItemType> = of("cyan_candle")
    @JvmField
    public val PURPLE_CANDLE: RegistryReference<ItemType> = of("purple_candle")
    @JvmField
    public val BLUE_CANDLE: RegistryReference<ItemType> = of("blue_candle")
    @JvmField
    public val BROWN_CANDLE: RegistryReference<ItemType> = of("brown_candle")
    @JvmField
    public val GREEN_CANDLE: RegistryReference<ItemType> = of("green_candle")
    @JvmField
    public val RED_CANDLE: RegistryReference<ItemType> = of("red_candle")
    @JvmField
    public val BLACK_CANDLE: RegistryReference<ItemType> = of("black_candle")
    @JvmField
    public val SMALL_AMETHYST_BUD: RegistryReference<ItemType> = of("small_amethyst_bud")
    @JvmField
    public val MEDIUM_AMETHYST_BUD: RegistryReference<ItemType> = of("medium_amethyst_bud")
    @JvmField
    public val LARGE_AMETHYST_BUD: RegistryReference<ItemType> = of("large_amethyst_bud")
    @JvmField
    public val AMETHYST_CLUSTER: RegistryReference<ItemType> = of("amethyst_cluster")
    @JvmField
    public val POINTED_DRIPSTONE: RegistryReference<ItemType> = of("pointed_dripstone")
    @JvmField
    public val OCHRE_FROGLIGHT: RegistryReference<ItemType> = of("ochre_froglight")
    @JvmField
    public val VERDANT_FROGLIGHT: RegistryReference<ItemType> = of("verdant_froglight")
    @JvmField
    public val PEARLESCENT_FROGLIGHT: RegistryReference<ItemType> = of("pearlescent_froglight")
    @JvmField
    public val FROGSPAWN: RegistryReference<ItemType> = of("frogspawn")
    @JvmField
    public val ECHO_SHARD: RegistryReference<ItemType> = of("echo_shard")

    // @formatter:on
    @JvmStatic
    private fun of(name: String): RegistryReference<ItemType> = RegistryReference.of(Registries.ITEM, Key.key(name))
}

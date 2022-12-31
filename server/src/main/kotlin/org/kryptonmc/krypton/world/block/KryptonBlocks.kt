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
import org.kryptonmc.api.block.BlockSoundGroup
import org.kryptonmc.api.block.meta.BedPart
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.util.Direction
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.property.KryptonProperties
import org.kryptonmc.krypton.world.block.data.BlockSoundGroups
import org.kryptonmc.krypton.world.block.state.BlockBehaviour
import org.kryptonmc.krypton.world.block.state.BlockBehaviour.OffsetType
import org.kryptonmc.krypton.world.block.state.BlockBehaviour.Properties
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.type.CaveVines
import org.kryptonmc.krypton.world.material.MaterialColor
import org.kryptonmc.krypton.world.material.MaterialColors
import org.kryptonmc.krypton.world.material.Materials
import java.util.function.Function
import java.util.function.ToIntFunction

/*
 * The formatting for this class is very weird, but it's this way to reduce the amount of lines,
 * so analysis of it will finish this side of christmas.
 */
// TODO: Add the rest of these when we add the block implementations
@Suppress("CascadingCallWrapping")
@Catalogue(KryptonBlock::class)
object KryptonBlocks {

    private val NEVER_SPAWN = ValidSpawnPredicate { _, _, _, _ -> false }
    private val ALWAYS_SPAWN = ValidSpawnPredicate { _, _, _, _ -> false }
    private val OCELOT_OR_PARROT_SPAWN = ValidSpawnPredicate { _, _, _, type ->
        type === KryptonEntityTypes.OCELOT || type === KryptonEntityTypes.PARROT
    }
    private val NEVER = BlockBehaviour.StatePredicate { _, _, _ -> false }
    private val ALWAYS = BlockBehaviour.StatePredicate { _, _, _ -> true }

    @JvmField
    val AIR: KryptonBlock = register("air", AirBlock(Properties.of(Materials.AIR).noCollision().noLootTable().air()))
    @JvmField
    val STONE: KryptonBlock = register("stone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val GRANITE: KryptonBlock = register("granite",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.DIRT).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val POLISHED_GRANITE: KryptonBlock = register("polished_granite",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.DIRT).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val DIORITE: KryptonBlock = register("diorite",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.QUARTZ).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val POLISHED_DIORITE: KryptonBlock = register("polished_diorite",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.QUARTZ).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val ANDESITE: KryptonBlock = register("andesite",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val POLISHED_ANDESITE: KryptonBlock = register("polished_andesite",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val GRASS_BLOCK: KryptonBlock = register("grass_block",
        DummyBlock(Properties.of(Materials.GRASS).randomlyTicks().strength(0.6F).sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val DIRT: KryptonBlock = register("dirt",
        DummyBlock(Properties.of(Materials.DIRT, MaterialColors.DIRT).strength(0.5F).sounds(BlockSoundGroups.GRAVEL)))
    @JvmField
    val COARSE_DIRT: KryptonBlock = register("coarse_dirt",
        DummyBlock(Properties.of(Materials.DIRT, MaterialColors.DIRT).strength(0.5F).sounds(BlockSoundGroups.GRAVEL)))
    @JvmField
    val PODZOL: KryptonBlock = register("podzol",
        DummyBlock(Properties.of(Materials.DIRT, MaterialColors.PODZOL).strength(0.5F).sounds(BlockSoundGroups.GRAVEL)))
    @JvmField
    val COBBLESTONE: KryptonBlock = register("cobblestone", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val OAK_PLANKS: KryptonBlock = register("oak_planks",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.WOOD).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_PLANKS: KryptonBlock = register("spruce_planks",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_PLANKS: KryptonBlock = register("birch_planks",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.SAND).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_PLANKS: KryptonBlock = register("jungle_planks",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.DIRT).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_PLANKS: KryptonBlock = register("acacia_planks",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_ORANGE).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_PLANKS: KryptonBlock = register("dark_oak_planks",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_BROWN).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_PLANKS: KryptonBlock = register("mangrove_planks",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_RED).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val OAK_SAPLING: KryptonBlock = register("oak_sapling",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val SPRUCE_SAPLING: KryptonBlock = register("spruce_sapling",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val BIRCH_SAPLING: KryptonBlock = register("birch_sapling",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val JUNGLE_SAPLING: KryptonBlock = register("jungle_sapling",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val ACACIA_SAPLING: KryptonBlock = register("acacia_sapling",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val DARK_OAK_SAPLING: KryptonBlock = register("dark_oak_sapling",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val MANGROVE_PROPAGULE: KryptonBlock = register("mangrove_propagule", DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks()
        .instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val BEDROCK: KryptonBlock = register("bedrock",
        DummyBlock(Properties.of(Materials.STONE).strength(-1.0F, 3600000.0F).noLootTable().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val WATER: KryptonBlock = register("water", DummyBlock(Properties.of(Materials.WATER).noCollision().strength(100.0F).noLootTable()))
    @JvmField
    val LAVA: KryptonBlock = register("lava",
        DummyBlock(Properties.of(Materials.LAVA).noCollision().randomlyTicks().strength(100.0F).lightLevel { 15 }.noLootTable()))
    @JvmField
    val SAND: KryptonBlock = register("sand",
        DummyBlock(Properties.of(Materials.SAND, MaterialColors.SAND).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val RED_SAND: KryptonBlock = register("red_sand",
        DummyBlock(Properties.of(Materials.SAND, MaterialColors.COLOR_ORANGE).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val GRAVEL: KryptonBlock = register("gravel",
        DummyBlock(Properties.of(Materials.SAND, MaterialColors.STONE).strength(0.6F).sounds(BlockSoundGroups.GRAVEL)))
    @JvmField
    val GOLD_ORE: KryptonBlock = register("gold_ore", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.0F, 3.0F)))
    @JvmField
    val DEEPSLATE_GOLD_ORE: KryptonBlock = register("deepslate_gold_ore",
        DummyBlock(Properties.from(GOLD_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val IRON_ORE: KryptonBlock = register("iron_ore", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.0F, 3.0F)))
    @JvmField
    val DEEPSLATE_IRON_ORE: KryptonBlock = register("deepslate_iron_ore",
        DummyBlock(Properties.from(IRON_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val COAL_ORE: KryptonBlock = register("coal_ore", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.0F, 3.0F)))
    @JvmField
    val DEEPSLATE_COAL_ORE: KryptonBlock = register("deepslate_coal_ore",
        DummyBlock(Properties.from(COAL_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val NETHER_GOLD_ORE: KryptonBlock = register("nether_gold_ore", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(3.0F, 3.0F).sounds(BlockSoundGroups.NETHER_GOLD_ORE)))
    @JvmField
    val OAK_LOG: KryptonBlock = register("oak_log", log(MaterialColors.WOOD, MaterialColors.PODZOL))
    @JvmField
    val SPRUCE_LOG: KryptonBlock = register("spruce_log", log(MaterialColors.PODZOL, MaterialColors.COLOR_BROWN))
    @JvmField
    val BIRCH_LOG: KryptonBlock = register("birch_log", log(MaterialColors.SAND, MaterialColors.QUARTZ))
    @JvmField
    val JUNGLE_LOG: KryptonBlock = register("jungle_log", log(MaterialColors.DIRT, MaterialColors.PODZOL))
    @JvmField
    val ACACIA_LOG: KryptonBlock = register("acacia_log", log(MaterialColors.COLOR_ORANGE, MaterialColors.STONE))
    @JvmField
    val DARK_OAK_LOG: KryptonBlock = register("dark_oak_log", log(MaterialColors.COLOR_BROWN, MaterialColors.COLOR_BROWN))
    @JvmField
    val MANGROVE_LOG: KryptonBlock = register("mangrove_log", log(MaterialColors.COLOR_RED, MaterialColors.PODZOL))
    @JvmField
    val MANGROVE_ROOTS: KryptonBlock = register("mangrove_roots", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(0.7F)
        .randomlyTicks().sounds(BlockSoundGroups.MANGROVE_ROOTS).noOcclusion().isSuffocating(NEVER).noOcclusion()))
    @JvmField
    val MUDDY_MANGROVE_ROOTS: KryptonBlock = register("muddy_mangrove_roots",
        DummyBlock(Properties.of(Materials.DIRT, MaterialColors.PODZOL).strength(0.7F).sounds(BlockSoundGroups.MUDDY_MANGROVE_ROOTS)))
    @JvmField
    val STRIPPED_SPRUCE_LOG: KryptonBlock = register("stripped_spruce_log", log(MaterialColors.PODZOL, MaterialColors.PODZOL))
    @JvmField
    val STRIPPED_BIRCH_LOG: KryptonBlock = register("stripped_birch_log", log(MaterialColors.SAND, MaterialColors.SAND))
    @JvmField
    val STRIPPED_JUNGLE_LOG: KryptonBlock = register("stripped_jungle_log", log(MaterialColors.DIRT, MaterialColors.DIRT))
    @JvmField
    val STRIPPED_ACACIA_LOG: KryptonBlock = register("stripped_acacia_log", log(MaterialColors.COLOR_ORANGE, MaterialColors.COLOR_ORANGE))
    @JvmField
    val STRIPPED_DARK_OAK_LOG: KryptonBlock = register("stripped_dark_oak_log", log(MaterialColors.COLOR_BROWN, MaterialColors.COLOR_BROWN))
    @JvmField
    val STRIPPED_OAK_LOG: KryptonBlock = register("stripped_oak_log", log(MaterialColors.WOOD, MaterialColors.WOOD))
    @JvmField
    val STRIPPED_MANGROVE_LOG: KryptonBlock = register("stripped_mangrove_log", log(MaterialColors.COLOR_RED, MaterialColors.COLOR_RED))
    @JvmField
    val OAK_WOOD: KryptonBlock = register("oak_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.WOOD).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_WOOD: KryptonBlock = register("spruce_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_WOOD: KryptonBlock = register("birch_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.SAND).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_WOOD: KryptonBlock = register("jungle_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.DIRT).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_WOOD: KryptonBlock = register("acacia_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_GRAY).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_WOOD: KryptonBlock = register("dark_oak_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_BROWN).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_WOOD: KryptonBlock = register("mangrove_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_RED).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRIPPED_OAK_WOOD: KryptonBlock = register("stripped_oak_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.WOOD).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRIPPED_SPRUCE_WOOD: KryptonBlock = register("stripped_spruce_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRIPPED_BIRCH_WOOD: KryptonBlock = register("stripped_birch_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.SAND).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRIPPED_JUNGLE_WOOD: KryptonBlock = register("stripped_jungle_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.DIRT).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRIPPED_ACACIA_WOOD: KryptonBlock = register("stripped_acacia_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_ORANGE).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRIPPED_DARK_OAK_WOOD: KryptonBlock = register("stripped_dark_oak_wood",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_BROWN).strength(2.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRIPPED_MANGROVE_WOOD: KryptonBlock = register("stripped_mangrove_wood", log(MaterialColors.COLOR_RED, MaterialColors.COLOR_RED))
    @JvmField
    val OAK_LEAVES: KryptonBlock = register("oak_leaves", leaves(BlockSoundGroups.GRASS))
    @JvmField
    val SPRUCE_LEAVES: KryptonBlock = register("spruce_leaves", leaves(BlockSoundGroups.GRASS))
    @JvmField
    val BIRCH_LEAVES: KryptonBlock = register("birch_leaves", leaves(BlockSoundGroups.GRASS))
    @JvmField
    val JUNGLE_LEAVES: KryptonBlock = register("jungle_leaves", leaves(BlockSoundGroups.GRASS))
    @JvmField
    val ACACIA_LEAVES: KryptonBlock = register("acacia_leaves", leaves(BlockSoundGroups.GRASS))
    @JvmField
    val DARK_OAK_LEAVES: KryptonBlock = register("dark_oak_leaves", leaves(BlockSoundGroups.GRASS))
    @JvmField
    val MANGROVE_LEAVES: KryptonBlock = register("mangrove_leaves", DummyBlock(Properties.of(Materials.LEAVES).strength(0.2F).randomlyTicks()
        .sounds(BlockSoundGroups.GRASS).noOcclusion().isValidSpawn(OCELOT_OR_PARROT_SPAWN).isSuffocating(NEVER)))
    @JvmField
    val AZALEA_LEAVES: KryptonBlock = register("azalea_leaves", leaves(BlockSoundGroups.AZALEA_LEAVES))
    @JvmField
    val FLOWERING_AZALEA_LEAVES: KryptonBlock = register("flowering_azalea_leaves", leaves(BlockSoundGroups.AZALEA_LEAVES))
    @JvmField
    val SPONGE: KryptonBlock = register("sponge", DummyBlock(Properties.of(Materials.SPONGE).strength(0.6F).sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val WET_SPONGE: KryptonBlock = register("wet_sponge", DummyBlock(Properties.of(Materials.SPONGE).strength(0.6F).sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val GLASS: KryptonBlock = register("glass", DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()
        .isValidSpawn(NEVER_SPAWN).isRedstoneConductor(NEVER).isSuffocating(NEVER)))
    @JvmField
    val LAPIS_ORE: KryptonBlock = register("lapis_ore", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.0F, 3.0F)))
    @JvmField
    val DEEPSLATE_LAPIS_ORE: KryptonBlock = register("deepslate_lapis_ore",
        DummyBlock(Properties.from(LAPIS_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val LAPIS_BLOCK: KryptonBlock = register("lapis_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.LAPIS).requiresCorrectTool().strength(3.0F, 3.0F)))
    @JvmField
    val DISPENSER: KryptonBlock = register("dispenser", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.5F)))
    @JvmField
    val SANDSTONE: KryptonBlock = register("sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val CHISELED_SANDSTONE: KryptonBlock = register("chiseled_sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val CUT_SANDSTONE: KryptonBlock = register("cut_sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val NOTE_BLOCK: KryptonBlock = register("note_block", DummyBlock(Properties.of(Materials.WOOD).sounds(BlockSoundGroups.WOOD).strength(0.8F)))
    @JvmField
    val WHITE_BED: KryptonBlock = register("white_bed", bed(DyeColor.WHITE))
    @JvmField
    val ORANGE_BED: KryptonBlock = register("orange_bed", bed(DyeColor.ORANGE))
    @JvmField
    val MAGENTA_BED: KryptonBlock = register("magenta_bed", bed(DyeColor.MAGENTA))
    @JvmField
    val LIGHT_BLUE_BED: KryptonBlock = register("light_blue_bed", bed(DyeColor.LIGHT_BLUE))
    @JvmField
    val YELLOW_BED: KryptonBlock = register("yellow_bed", bed(DyeColor.YELLOW))
    @JvmField
    val LIME_BED: KryptonBlock = register("lime_bed", bed(DyeColor.LIME))
    @JvmField
    val PINK_BED: KryptonBlock = register("pink_bed", bed(DyeColor.PINK))
    @JvmField
    val GRAY_BED: KryptonBlock = register("gray_bed", bed(DyeColor.GRAY))
    @JvmField
    val LIGHT_GRAY_BED: KryptonBlock = register("light_gray_bed", bed(DyeColor.LIGHT_GRAY))
    @JvmField
    val CYAN_BED: KryptonBlock = register("cyan_bed", bed(DyeColor.CYAN))
    @JvmField
    val PURPLE_BED: KryptonBlock = register("purple_bed", bed(DyeColor.PURPLE))
    @JvmField
    val BLUE_BED: KryptonBlock = register("blue_bed", bed(DyeColor.BLUE))
    @JvmField
    val BROWN_BED: KryptonBlock = register("brown_bed", bed(DyeColor.BROWN))
    @JvmField
    val GREEN_BED: KryptonBlock = register("green_bed", bed(DyeColor.GREEN))
    @JvmField
    val RED_BED: KryptonBlock = register("red_bed", bed(DyeColor.RED))
    @JvmField
    val BLACK_BED: KryptonBlock = register("black_bed", bed(DyeColor.BLACK))
    @JvmField
    val POWERED_RAIL: KryptonBlock = register("powered_rail",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.7F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val DETECTOR_RAIL: KryptonBlock = register("detector_rail",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.7F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val STICKY_PISTON: KryptonBlock = register("sticky_piston", pistonBase(true))
    @JvmField
    val COBWEB: KryptonBlock = register("cobweb", DummyBlock(Properties.of(Materials.WEB).noCollision().requiresCorrectTool().strength(4.0F)))
    @JvmField
    val GRASS: KryptonBlock = register("grass",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XYZ)))
    @JvmField
    val FERN: KryptonBlock = register("fern",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XYZ)))
    @JvmField
    val DEAD_BUSH: KryptonBlock = register("dead_bush",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT, MaterialColors.WOOD).noCollision().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val SEAGRASS: KryptonBlock = register("seagrass",
        DummyBlock(Properties.of(Materials.REPLACEABLE_WATER_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val TALL_SEAGRASS: KryptonBlock = register("tall_seagrass", DummyBlock(Properties.of(Materials.REPLACEABLE_WATER_PLANT).noCollision()
        .instabreak().sounds(BlockSoundGroups.WET_GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val PISTON: KryptonBlock = register("piston", pistonBase(false))
    @JvmField
    val PISTON_HEAD: KryptonBlock = register("piston_head", DummyBlock(Properties.of(Materials.PISTON).strength(1.5F).noLootTable()))
    @JvmField
    val WHITE_WOOL: KryptonBlock = register("white_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.SNOW).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val ORANGE_WOOL: KryptonBlock = register("orange_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_ORANGE).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val MAGENTA_WOOL: KryptonBlock = register("magenta_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_MAGENTA).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val LIGHT_BLUE_WOOL: KryptonBlock = register("light_blue_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_LIGHT_BLUE).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val YELLOW_WOOL: KryptonBlock = register("yellow_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_YELLOW).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val LIME_WOOL: KryptonBlock = register("lime_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_LIGHT_GREEN).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val PINK_WOOL: KryptonBlock = register("pink_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_PINK).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val GRAY_WOOL: KryptonBlock = register("gray_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_GRAY).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val LIGHT_GRAY_WOOL: KryptonBlock = register("light_gray_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_LIGHT_GRAY).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val CYAN_WOOL: KryptonBlock = register("cyan_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_CYAN).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val PURPLE_WOOL: KryptonBlock = register("purple_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_PURPLE).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val BLUE_WOOL: KryptonBlock = register("blue_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_BLUE).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val BROWN_WOOL: KryptonBlock = register("brown_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_BROWN).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val GREEN_WOOL: KryptonBlock = register("green_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_GREEN).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val RED_WOOL: KryptonBlock = register("red_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_RED).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val BLACK_WOOL: KryptonBlock = register("black_wool",
        DummyBlock(Properties.of(Materials.WOOL, MaterialColors.COLOR_BLACK).strength(0.8F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val MOVING_PISTON: KryptonBlock = register("moving_piston", DummyBlock(Properties.of(Materials.PISTON).strength(-1.0F).dynamicShape()
        .noLootTable().noOcclusion().isRedstoneConductor(NEVER).isSuffocating(NEVER)))
    @JvmField
    val DANDELION: KryptonBlock = register("dandelion",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val POPPY: KryptonBlock = register("poppy",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val BLUE_ORCHID: KryptonBlock = register("blue_orchid",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val ALLIUM: KryptonBlock = register("allium",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val AZURE_BLUET: KryptonBlock = register("azure_bluet",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val RED_TULIP: KryptonBlock = register("red_tulip",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val ORANGE_TULIP: KryptonBlock = register("orange_tulip",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val WHITE_TULIP: KryptonBlock = register("white_tulip",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val PINK_TULIP: KryptonBlock = register("pink_tulip",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val OXEYE_DAISY: KryptonBlock = register("oxeye_daisy",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val CORNFLOWER: KryptonBlock = register("cornflower",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val WITHER_ROSE: KryptonBlock = register("wither_rose",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val LILY_OF_THE_VALLEY: KryptonBlock = register("lily_of_the_valley",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val BROWN_MUSHROOM: KryptonBlock = register("brown_mushroom", DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_BROWN).noCollision()
        .randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS).lightLevel { 1 }.hasPostProcess(ALWAYS)))
    @JvmField
    val RED_MUSHROOM: KryptonBlock = register("red_mushroom", DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_RED).noCollision()
        .randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS).hasPostProcess(ALWAYS)))
    @JvmField
    val GOLD_BLOCK: KryptonBlock = register("gold_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.GOLD).requiresCorrectTool().strength(3.0F, 6.0F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val IRON_BLOCK: KryptonBlock = register("iron_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.METAL).requiresCorrectTool().strength(5.0F, 6.0F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val BRICKS: KryptonBlock = register("bricks",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_RED).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val TNT: KryptonBlock = register("tnt", DummyBlock(Properties.of(Materials.EXPLOSIVE).instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val BOOKSHELF: KryptonBlock = register("bookshelf", DummyBlock(Properties.of(Materials.WOOD).strength(1.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MOSSY_COBBLESTONE: KryptonBlock = register("mossy_cobblestone",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val OBSIDIAN: KryptonBlock = register("obsidian",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK).requiresCorrectTool().strength(50.0F, 1200.0F)))
    @JvmField
    val TORCH: KryptonBlock = register("torch",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().instabreak().lightLevel { 14 }.sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WALL_TORCH: KryptonBlock = register("wall_torch",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().instabreak().lightLevel { 14 }.sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val FIRE: KryptonBlock = register("fire",
        DummyBlock(Properties.of(Materials.FIRE, MaterialColors.FIRE).noCollision().instabreak().lightLevel { 15 }.sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val SOUL_FIRE: KryptonBlock = register("soul_fire", DummyBlock(Properties.of(Materials.FIRE, MaterialColors.COLOR_LIGHT_BLUE).noCollision()
        .instabreak().lightLevel { 10 }.sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val SPAWNER: KryptonBlock = register("spawner",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(5.0F).sounds(BlockSoundGroups.METAL).noOcclusion()))
    @JvmField
    val OAK_STAIRS: KryptonBlock = register("oak_stairs", DummyBlock(Properties.from(OAK_PLANKS)))
    @JvmField
    val CHEST: KryptonBlock = register("chest", DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val REDSTONE_WIRE: KryptonBlock = register("redstone_wire", DummyBlock(Properties.of(Materials.DECORATION).noCollision().instabreak()))
    @JvmField
    val DIAMOND_ORE: KryptonBlock = register("diamond_ore", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.0F, 3.0F)))
    @JvmField
    val DEEPSLATE_DIAMOND_ORE: KryptonBlock = register("deepslate_diamond_ore",
        DummyBlock(Properties.from(DIAMOND_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val DIAMOND_BLOCK: KryptonBlock = register("diamond_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.DIAMOND).requiresCorrectTool().strength(5.0F, 6.0F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val CRAFTING_TABLE: KryptonBlock = register("crafting_table",
        DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WHEAT: KryptonBlock = register("wheat",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.CROP)))
    @JvmField
    val FARMLAND: KryptonBlock = register("farmland",
        DummyBlock(Properties.of(Materials.DIRT).randomlyTicks().strength(0.6F).sounds(BlockSoundGroups.GRAVEL).isSuffocating(ALWAYS)))
    @JvmField
    val FURNACE: KryptonBlock = register("furnace",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.5F).lightLevel(litBlockEmission(13))))
    @JvmField
    val OAK_SIGN: KryptonBlock = register("oak_sign",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_SIGN: KryptonBlock = register("spruce_sign",
        DummyBlock(Properties.of(Materials.WOOD, SPRUCE_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_SIGN: KryptonBlock = register("birch_sign",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.SAND).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_SIGN: KryptonBlock = register("acacia_sign",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_ORANGE).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_SIGN: KryptonBlock = register("jungle_sign",
        DummyBlock(Properties.of(Materials.WOOD, JUNGLE_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_SIGN: KryptonBlock = register("dark_oak_sign",
        DummyBlock(Properties.of(Materials.WOOD, DARK_OAK_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_SIGN: KryptonBlock = register("mangrove_sign",
        DummyBlock(Properties.of(Materials.WOOD, MANGROVE_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val OAK_DOOR: KryptonBlock = register("oak_door",
        DummyBlock(Properties.of(Materials.WOOD, OAK_PLANKS.defaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val LADDER: KryptonBlock = register("ladder",
        DummyBlock(Properties.of(Materials.DECORATION).strength(0.4F).sounds(BlockSoundGroups.LADDER).noOcclusion()))
    @JvmField
    val RAIL: KryptonBlock = register("rail",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.7F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val COBBLESTONE_STAIRS: KryptonBlock = register("cobblestone_stairs", DummyBlock(Properties.from(COBBLESTONE)))
    @JvmField
    val OAK_WALL_SIGN: KryptonBlock = register("oak_wall_sign",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_WALL_SIGN: KryptonBlock = register("spruce_wall_sign",
        DummyBlock(Properties.of(Materials.WOOD, SPRUCE_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_WALL_SIGN: KryptonBlock = register("birch_wall_sign",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.SAND).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_WALL_SIGN: KryptonBlock = register("acacia_wall_sign",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_ORANGE).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_WALL_SIGN: KryptonBlock = register("jungle_wall_sign",
        DummyBlock(Properties.of(Materials.WOOD, JUNGLE_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_WALL_SIGN: KryptonBlock = register("dark_oak_wall_sign",
        DummyBlock(Properties.of(Materials.WOOD, DARK_OAK_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_WALL_SIGN: KryptonBlock = register("mangrove_wall_sign",
        DummyBlock(Properties.of(Materials.WOOD, MANGROVE_LOG.defaultMaterialColor()).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LEVER: KryptonBlock = register("lever",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STONE_PRESSURE_PLATE: KryptonBlock = register("stone_pressure_plate",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().noCollision().strength(0.5F)))
    @JvmField
    val IRON_DOOR: KryptonBlock = register("iron_door", DummyBlock(Properties.of(Materials.METAL, MaterialColors.METAL).requiresCorrectTool()
        .strength(5.0F).sounds(BlockSoundGroups.METAL).noOcclusion()))
    @JvmField
    val OAK_PRESSURE_PLATE: KryptonBlock = register("oak_pressure_plate",
        DummyBlock(Properties.of(Materials.WOOD, OAK_PLANKS.defaultMaterialColor()).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_PRESSURE_PLATE: KryptonBlock = register("spruce_pressure_plate",
        DummyBlock(Properties.of(Materials.WOOD, SPRUCE_PLANKS.defaultMaterialColor()).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_PRESSURE_PLATE: KryptonBlock = register("birch_pressure_plate",
        DummyBlock(Properties.of(Materials.WOOD, BIRCH_PLANKS.defaultMaterialColor()).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_PRESSURE_PLATE: KryptonBlock = register("jungle_pressure_plate",
        DummyBlock(Properties.of(Materials.WOOD, JUNGLE_PLANKS.defaultMaterialColor()).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_PRESSURE_PLATE: KryptonBlock = register("acacia_pressure_plate",
        DummyBlock(Properties.of(Materials.WOOD, ACACIA_PLANKS.defaultMaterialColor()).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_PRESSURE_PLATE: KryptonBlock = register("dark_oak_pressure_plate",
        DummyBlock(Properties.of(Materials.WOOD, DARK_OAK_PLANKS.defaultMaterialColor()).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_PRESSURE_PLATE: KryptonBlock = register("mangrove_pressure_plate",
        DummyBlock(Properties.of(Materials.WOOD, MANGROVE_PLANKS.defaultMaterialColor()).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val REDSTONE_ORE: KryptonBlock = register("redstone_ore",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().randomlyTicks().lightLevel(litBlockEmission(9)).strength(3.0F, 3.0F)))
    @JvmField
    val DEEPSLATE_REDSTONE_ORE: KryptonBlock = register("deepslate_redstone_ore",
        DummyBlock(Properties.from(REDSTONE_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val REDSTONE_TORCH: KryptonBlock = register("redstone_torch",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().instabreak().lightLevel(litBlockEmission(7)).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val REDSTONE_WALL_TORCH: KryptonBlock = register("redstone_wall_torch",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().instabreak().lightLevel(litBlockEmission(7)).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STONE_BUTTON: KryptonBlock = register("stone_button", DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F)))
    @JvmField
    val SNOW: KryptonBlock = register("snow",
        DummyBlock(Properties.of(Materials.TOP_SNOW).randomlyTicks().strength(0.1F).requiresCorrectTool().sounds(BlockSoundGroups.SNOW)))
    @JvmField
    val ICE: KryptonBlock = register("ice", DummyBlock(Properties.of(Materials.ICE).friction(0.98F).randomlyTicks().strength(0.5F)
        .sounds(BlockSoundGroups.GLASS).noOcclusion().isValidSpawn { _, _, _, type -> type === KryptonEntityTypes.POLAR_BEAR }))
    @JvmField
    val SNOW_BLOCK: KryptonBlock = register("snow_block",
        DummyBlock(Properties.of(Materials.SNOW).requiresCorrectTool().strength(0.2F).sounds(BlockSoundGroups.SNOW)))
    @JvmField
    val CACTUS: KryptonBlock = register("cactus",
        DummyBlock(Properties.of(Materials.CACTUS).randomlyTicks().strength(0.4F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val CLAY: KryptonBlock = register("clay", DummyBlock(Properties.of(Materials.CLAY).strength(0.6F).sounds(BlockSoundGroups.GRAVEL)))
    @JvmField
    val SUGAR_CANE: KryptonBlock = register("sugar_cane",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val JUKEBOX: KryptonBlock = register("jukebox", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.DIRT).strength(2.0F, 6.0F)))
    @JvmField
    val OAK_FENCE: KryptonBlock = register("oak_fence",
        DummyBlock(Properties.of(Materials.WOOD, OAK_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val PUMPKIN: KryptonBlock = register("pumpkin",
        DummyBlock(Properties.of(Materials.VEGETABLE, MaterialColors.COLOR_ORANGE).strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val NETHERRACK: KryptonBlock = register("netherrack",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER).requiresCorrectTool().strength(0.4F).sounds(BlockSoundGroups.NETHERRACK)))
    @JvmField
    val SOUL_SAND: KryptonBlock = register("soul_sand", DummyBlock(Properties.of(Materials.SAND, MaterialColors.COLOR_BROWN).strength(0.5F)
        .speedFactor(0.4F).sounds(BlockSoundGroups.SOUL_SAND).isValidSpawn(ALWAYS_SPAWN).isRedstoneConductor(ALWAYS).isSuffocating(ALWAYS)))
    @JvmField
    val SOUL_SOIL: KryptonBlock = register("soul_soil",
        DummyBlock(Properties.of(Materials.DIRT, MaterialColors.COLOR_BROWN).strength(0.5F).sounds(BlockSoundGroups.SOUL_SOIL)))
    @JvmField
    val BASALT: KryptonBlock = register("basalt", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK).requiresCorrectTool()
        .strength(1.25F, 4.2F).sounds(BlockSoundGroups.BASALT)))
    @JvmField
    val POLISHED_BASALT: KryptonBlock = register("polished_basalt", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK)
        .requiresCorrectTool().strength(1.25F, 4.2F).sounds(BlockSoundGroups.BASALT)))
    @JvmField
    val SOUL_TORCH: KryptonBlock = register("soul_torch",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().instabreak().lightLevel { 10 }.sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SOUL_WALL_TORCH: KryptonBlock = register("soul_wall_torch",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().instabreak().lightLevel { 10 }.sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val GLOWSTONE: KryptonBlock = register("glowstone",
        DummyBlock(Properties.of(Materials.GLASS, MaterialColors.SAND).strength(0.3F).sounds(BlockSoundGroups.GLASS).lightLevel { 15 }))
    @JvmField
    val NETHER_PORTAL: KryptonBlock = register("nether_portal",
        DummyBlock(Properties.of(Materials.PORTAL).noCollision().randomlyTicks().strength(-1.0F).sounds(BlockSoundGroups.GLASS).lightLevel { 11 }))
    @JvmField
    val CARVED_PUMPKIN: KryptonBlock = register("carved_pumpkin", DummyBlock(Properties.of(Materials.VEGETABLE, MaterialColors.COLOR_ORANGE)
        .strength(1.0F).sounds(BlockSoundGroups.WOOD).isValidSpawn(ALWAYS_SPAWN)))
    @JvmField
    val JACK_O_LANTERN: KryptonBlock = register("jack_o_lantern", DummyBlock(Properties.of(Materials.VEGETABLE, MaterialColors.COLOR_ORANGE)
        .strength(1.0F).sounds(BlockSoundGroups.WOOD).lightLevel { 15 }.isValidSpawn(ALWAYS_SPAWN)))
    @JvmField
    val CAKE: KryptonBlock = register("cake", DummyBlock(Properties.of(Materials.CAKE).strength(0.5F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val REPEATER: KryptonBlock = register("repeater", DummyBlock(Properties.of(Materials.DECORATION).instabreak().sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WHITE_STAINED_GLASS: KryptonBlock = register("white_stained_glass", stainedGlass(DyeColor.WHITE))
    @JvmField
    val ORANGE_STAINED_GLASS: KryptonBlock = register("orange_stained_glass", stainedGlass(DyeColor.ORANGE))
    @JvmField
    val MAGENTA_STAINED_GLASS: KryptonBlock = register("magenta_stained_glass", stainedGlass(DyeColor.MAGENTA))
    @JvmField
    val LIGHT_BLUE_STAINED_GLASS: KryptonBlock = register("light_blue_stained_glass", stainedGlass(DyeColor.LIGHT_BLUE))
    @JvmField
    val YELLOW_STAINED_GLASS: KryptonBlock = register("yellow_stained_glass", stainedGlass(DyeColor.YELLOW))
    @JvmField
    val LIME_STAINED_GLASS: KryptonBlock = register("lime_stained_glass", stainedGlass(DyeColor.LIME))
    @JvmField
    val PINK_STAINED_GLASS: KryptonBlock = register("pink_stained_glass", stainedGlass(DyeColor.PINK))
    @JvmField
    val GRAY_STAINED_GLASS: KryptonBlock = register("gray_stained_glass", stainedGlass(DyeColor.GRAY))
    @JvmField
    val LIGHT_GRAY_STAINED_GLASS: KryptonBlock = register("light_gray_stained_glass", stainedGlass(DyeColor.LIGHT_GRAY))
    @JvmField
    val CYAN_STAINED_GLASS: KryptonBlock = register("cyan_stained_glass", stainedGlass(DyeColor.CYAN))
    @JvmField
    val PURPLE_STAINED_GLASS: KryptonBlock = register("purple_stained_glass", stainedGlass(DyeColor.PURPLE))
    @JvmField
    val BLUE_STAINED_GLASS: KryptonBlock = register("blue_stained_glass", stainedGlass(DyeColor.BLUE))
    @JvmField
    val BROWN_STAINED_GLASS: KryptonBlock = register("brown_stained_glass", stainedGlass(DyeColor.BROWN))
    @JvmField
    val GREEN_STAINED_GLASS: KryptonBlock = register("green_stained_glass", stainedGlass(DyeColor.GREEN))
    @JvmField
    val RED_STAINED_GLASS: KryptonBlock = register("red_stained_glass", stainedGlass(DyeColor.RED))
    @JvmField
    val BLACK_STAINED_GLASS: KryptonBlock = register("black_stained_glass", stainedGlass(DyeColor.BLACK))
    @JvmField
    val OAK_TRAPDOOR: KryptonBlock = register("oak_trapdoor", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.WOOD).strength(3.0F)
        .sounds(BlockSoundGroups.WOOD).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val SPRUCE_TRAPDOOR: KryptonBlock = register("spruce_trapdoor", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(3.0F)
        .sounds(BlockSoundGroups.WOOD).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val BIRCH_TRAPDOOR: KryptonBlock = register("birch_trapdoor", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.SAND).strength(3.0F)
        .sounds(BlockSoundGroups.WOOD).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val JUNGLE_TRAPDOOR: KryptonBlock = register("jungle_trapdoor", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.DIRT).strength(3.0F)
        .sounds(BlockSoundGroups.WOOD).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val ACACIA_TRAPDOOR: KryptonBlock = register("acacia_trapdoor", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_ORANGE)
        .strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val DARK_OAK_TRAPDOOR: KryptonBlock = register("dark_oak_trapdoor", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_BROWN)
        .strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val MANGROVE_TRAPDOOR: KryptonBlock = register("mangrove_trapdoor", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_RED)
        .strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val STONE_BRICKS: KryptonBlock = register("stone_bricks", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val MOSSY_STONE_BRICKS: KryptonBlock = register("mossy_stone_bricks",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val CRACKED_STONE_BRICKS: KryptonBlock = register("cracked_stone_bricks",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val CHISELED_STONE_BRICKS: KryptonBlock = register("chiseled_stone_bricks",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val PACKED_MUD: KryptonBlock = register("packed_mud", DummyBlock(Properties.from(DIRT).strength(1.0F, 3.0F).sounds(BlockSoundGroups.PACKED_MUD)))
    @JvmField
    val MUD_BRICKS: KryptonBlock = register("mud_bricks", DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_LIGHT_GRAY)
        .requiresCorrectTool().strength(1.5F, 3.0F).sounds(BlockSoundGroups.MUD_BRICKS)))
    @JvmField
    val INFESTED_STONE: KryptonBlock = register("infested_stone", DummyBlock(Properties.of(Materials.CLAY)))
    @JvmField
    val INFESTED_COBBLESTONE: KryptonBlock = register("infested_cobblestone", DummyBlock(Properties.of(Materials.CLAY)))
    @JvmField
    val INFESTED_STONE_BRICKS: KryptonBlock = register("infested_stone_bricks", DummyBlock(Properties.of(Materials.CLAY)))
    @JvmField
    val INFESTED_MOSSY_STONE_BRICKS: KryptonBlock = register("infested_mossy_stone_bricks", DummyBlock(Properties.of(Materials.CLAY)))
    @JvmField
    val INFESTED_CRACKED_STONE_BRICKS: KryptonBlock = register("infested_cracked_stone_bricks", DummyBlock(Properties.of(Materials.CLAY)))
    @JvmField
    val INFESTED_CHISELED_STONE_BRICKS: KryptonBlock = register("infested_chiseled_stone_bricks", DummyBlock(Properties.of(Materials.CLAY)))
    @JvmField
    val BROWN_MUSHROOM_BLOCK: KryptonBlock = register("brown_mushroom_block",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.DIRT).strength(0.2F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val RED_MUSHROOM_BLOCK: KryptonBlock = register("red_mushroom_block",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_RED).strength(0.2F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MUSHROOM_STEM: KryptonBlock = register("mushroom_stem",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.WOOL).strength(0.2F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val IRON_BARS: KryptonBlock = register("iron_bars", DummyBlock(Properties.of(Materials.METAL, MaterialColors.NONE).requiresCorrectTool()
        .strength(5.0F, 6.0F).sounds(BlockSoundGroups.METAL).noOcclusion()))
    @JvmField
    val CHAIN: KryptonBlock = register("chain", DummyBlock(Properties.of(Materials.METAL, MaterialColors.NONE).requiresCorrectTool()
        .strength(5.0F, 6.0F).sounds(BlockSoundGroups.CHAIN).noOcclusion()))
    @JvmField
    val GLASS_PANE: KryptonBlock = register("glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val MELON: KryptonBlock = register("melon",
        DummyBlock(Properties.of(Materials.VEGETABLE, MaterialColors.COLOR_LIGHT_GREEN).strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ATTACHED_PUMPKIN_STEM: KryptonBlock = register("attached_pumpkin_stem",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ATTACHED_MELON_STEM: KryptonBlock = register("attached_melon_stem",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val PUMPKIN_STEM: KryptonBlock = register("pumpkin_stem",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.HARD_CROP)))
    @JvmField
    val MELON_STEM: KryptonBlock = register("melon_stem",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.HARD_CROP)))
    @JvmField
    val VINE: KryptonBlock = register("vine",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().randomlyTicks().strength(0.2F).sounds(BlockSoundGroups.VINE)))
    @JvmField
    val GLOW_LICHEN: KryptonBlock = register("glow_lichen", DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT, MaterialColors.GLOW_LICHEN)
        .noCollision().strength(0.2F).sounds(BlockSoundGroups.GLOW_LICHEN)/* FIXME .lightLevel(GlowLichenBlock.emission(7))*/))
    @JvmField
    val OAK_FENCE_GATE: KryptonBlock = register("oak_fence_gate",
        DummyBlock(Properties.of(Materials.WOOD, OAK_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BRICK_STAIRS: KryptonBlock = register("brick_stairs", DummyBlock(Properties.from(BRICKS)))
    @JvmField
    val STONE_BRICK_STAIRS: KryptonBlock = register("stone_brick_stairs", DummyBlock(Properties.from(STONE_BRICKS)))
    @JvmField
    val MUD_BRICK_STAIRS: KryptonBlock = register("mud_brick_stairs", DummyBlock(Properties.from(MUD_BRICKS)))
    @JvmField
    val MYCELIUM: KryptonBlock = register("mycelium",
        DummyBlock(Properties.of(Materials.GRASS, MaterialColors.COLOR_PURPLE).randomlyTicks().strength(0.6F).sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val LILY_PAD: KryptonBlock = register("lily_pad",
        DummyBlock(Properties.of(Materials.PLANT).instabreak().sounds(BlockSoundGroups.LILY_PAD).noOcclusion()))
    @JvmField
    val NETHER_BRICKS: KryptonBlock = register("nether_bricks", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(2.0F, 6.0F).sounds(BlockSoundGroups.NETHER_BRICKS)))
    @JvmField
    val NETHER_BRICK_FENCE: KryptonBlock = register("nether_brick_fence", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(2.0F, 6.0F).sounds(BlockSoundGroups.NETHER_BRICKS)))
    @JvmField
    val NETHER_BRICK_STAIRS: KryptonBlock = register("nether_brick_stairs", DummyBlock(Properties.from(NETHER_BRICKS)))
    @JvmField
    val NETHER_WART: KryptonBlock = register("nether_wart",
        DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_RED).noCollision().randomlyTicks().sounds(BlockSoundGroups.NETHER_WART)))
    @JvmField
    val ENCHANTING_TABLE: KryptonBlock = register("enchanting_table",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_RED).requiresCorrectTool().lightLevel { 7 }.strength(5.0F, 1200.0F)))
    @JvmField
    val BREWING_STAND: KryptonBlock = register("brewing_stand",
        DummyBlock(Properties.of(Materials.METAL).requiresCorrectTool().strength(0.5F).lightLevel { 1 }.noOcclusion()))
    @JvmField
    val CAULDRON: KryptonBlock = register("cauldron",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.STONE).requiresCorrectTool().strength(2.0F).noOcclusion()))
    @JvmField
    val WATER_CAULDRON: KryptonBlock = register("water_cauldron", DummyBlock(Properties.from(CAULDRON)))
    @JvmField
    val LAVA_CAULDRON: KryptonBlock = register("lava_cauldron", DummyBlock(Properties.from(CAULDRON).lightLevel { 15 }))
    @JvmField
    val POWDER_SNOW_CAULDRON: KryptonBlock = register("powder_snow_cauldron", DummyBlock(Properties.from(CAULDRON)))
    @JvmField
    val END_PORTAL: KryptonBlock = register("end_portal", DummyBlock(Properties.of(Materials.PORTAL, MaterialColors.COLOR_BLACK).noCollision()
        .lightLevel { 15 }.strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val END_PORTAL_FRAME: KryptonBlock = register("end_portal_frame", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GREEN)
        .sounds(BlockSoundGroups.GLASS).lightLevel { 1 }.strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val END_STONE: KryptonBlock = register("end_stone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(3.0F, 9.0F)))
    @JvmField
    val DRAGON_EGG: KryptonBlock = register("dragon_egg",
        DummyBlock(Properties.of(Materials.EGG, MaterialColors.COLOR_BLACK).strength(3.0F, 9.0F).lightLevel { 1 }.noOcclusion()))
    @JvmField
    val REDSTONE_LAMP: KryptonBlock = register("redstone_lamp", DummyBlock(Properties.of(Materials.BUILDABLE_GLASS).lightLevel(litBlockEmission(15))
        .strength(0.3F).sounds(BlockSoundGroups.GLASS).isValidSpawn(ALWAYS_SPAWN)))
    @JvmField
    val COCOA: KryptonBlock = register("cocoa",
        DummyBlock(Properties.of(Materials.PLANT).randomlyTicks().strength(0.2F, 3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val SANDSTONE_STAIRS: KryptonBlock = register("sandstone_stairs", DummyBlock(Properties.from(SANDSTONE)))
    @JvmField
    val EMERALD_ORE: KryptonBlock = register("emerald_ore", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.0F, 3.0F)))
    @JvmField
    val DEEPSLATE_EMERALD_ORE: KryptonBlock = register("deepslate_emerald_ore",
        DummyBlock(Properties.from(EMERALD_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val ENDER_CHEST: KryptonBlock = register("ender_chest",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(22.5F, 600.0F).lightLevel { 7 }))
    @JvmField
    val TRIPWIRE_HOOK: KryptonBlock = register("tripwire_hook", DummyBlock(Properties.of(Materials.DECORATION).noCollision()))
    @JvmField
    val TRIPWIRE: KryptonBlock = register("tripwire", DummyBlock(Properties.of(Materials.DECORATION).noCollision()))
    @JvmField
    val EMERALD_BLOCK: KryptonBlock = register("emerald_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.EMERALD).requiresCorrectTool().strength(5.0F, 6.0F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val SPRUCE_STAIRS: KryptonBlock = register("spruce_stairs", DummyBlock(Properties.from(SPRUCE_PLANKS)))
    @JvmField
    val BIRCH_STAIRS: KryptonBlock = register("birch_stairs", DummyBlock(Properties.from(BIRCH_PLANKS)))
    @JvmField
    val JUNGLE_STAIRS: KryptonBlock = register("jungle_stairs", DummyBlock(Properties.from(JUNGLE_PLANKS)))
    @JvmField
    val COMMAND_BLOCK: KryptonBlock = register("command_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_BROWN).requiresCorrectTool().strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val BEACON: KryptonBlock = register("beacon",
        DummyBlock(Properties.of(Materials.GLASS, MaterialColors.DIAMOND).strength(3.0F).lightLevel { 15 }.noOcclusion().isRedstoneConductor(NEVER)))
    @JvmField
    val COBBLESTONE_WALL: KryptonBlock = register("cobblestone_wall", DummyBlock(Properties.from(COBBLESTONE)))
    @JvmField
    val MOSSY_COBBLESTONE_WALL: KryptonBlock = register("mossy_cobblestone_wall", DummyBlock(Properties.from(COBBLESTONE)))
    @JvmField
    val FLOWER_POT: KryptonBlock = register("flower_pot", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_OAK_SAPLING: KryptonBlock = register("potted_oak_sapling", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_SPRUCE_SAPLING: KryptonBlock = register("potted_spruce_sapling",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_BIRCH_SAPLING: KryptonBlock = register("potted_birch_sapling",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_JUNGLE_SAPLING: KryptonBlock = register("potted_jungle_sapling",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_ACACIA_SAPLING: KryptonBlock = register("potted_acacia_sapling",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_DARK_OAK_SAPLING: KryptonBlock = register("potted_dark_oak_sapling",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_MANGROVE_PROPAGULE: KryptonBlock = register("potted_mangrove_propagule",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_FERN: KryptonBlock = register("potted_fern", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_DANDELION: KryptonBlock = register("potted_dandelion", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_POPPY: KryptonBlock = register("potted_poppy", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_BLUE_ORCHID: KryptonBlock = register("potted_blue_orchid", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_ALLIUM: KryptonBlock = register("potted_allium", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_AZURE_BLUET: KryptonBlock = register("potted_azure_bluet", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_RED_TULIP: KryptonBlock = register("potted_red_tulip", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_ORANGE_TULIP: KryptonBlock = register("potted_orange_tulip",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_WHITE_TULIP: KryptonBlock = register("potted_white_tulip", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_PINK_TULIP: KryptonBlock = register("potted_pink_tulip", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_OXEYE_DAISY: KryptonBlock = register("potted_oxeye_daisy", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_CORNFLOWER: KryptonBlock = register("potted_cornflower", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_LILY_OF_THE_VALLEY: KryptonBlock = register("potted_lily_of_the_valley",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_WITHER_ROSE: KryptonBlock = register("potted_wither_rose", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_RED_MUSHROOM: KryptonBlock = register("potted_red_mushroom",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_BROWN_MUSHROOM: KryptonBlock = register("potted_brown_mushroom",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_DEAD_BUSH: KryptonBlock = register("potted_dead_bush", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_CACTUS: KryptonBlock = register("potted_cactus", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val CARROTS: KryptonBlock = register("carrots",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.CROP)))
    @JvmField
    val POTATOES: KryptonBlock = register("potatoes",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.CROP)))
    @JvmField
    val OAK_BUTTON: KryptonBlock = register("oak_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_BUTTON: KryptonBlock = register("spruce_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_BUTTON: KryptonBlock = register("birch_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_BUTTON: KryptonBlock = register("jungle_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_BUTTON: KryptonBlock = register("acacia_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_BUTTON: KryptonBlock = register("dark_oak_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_BUTTON: KryptonBlock = register("mangrove_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SKELETON_SKULL: KryptonBlock = register("skeleton_skull", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val SKELETON_WALL_SKULL: KryptonBlock = register("skeleton_wall_skull", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val WITHER_SKELETON_SKULL: KryptonBlock = register("wither_skeleton_skull", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val WITHER_SKELETON_WALL_SKULL: KryptonBlock = register("wither_skeleton_wall_skull",
        DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val ZOMBIE_HEAD: KryptonBlock = register("zombie_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val ZOMBIE_WALL_HEAD: KryptonBlock = register("zombie_wall_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val PLAYER_HEAD: KryptonBlock = register("player_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val PLAYER_WALL_HEAD: KryptonBlock = register("player_wall_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val CREEPER_HEAD: KryptonBlock = register("creeper_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val CREEPER_WALL_HEAD: KryptonBlock = register("creeper_wall_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val DRAGON_HEAD: KryptonBlock = register("dragon_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val DRAGON_WALL_HEAD: KryptonBlock = register("dragon_wall_head", DummyBlock(Properties.of(Materials.DECORATION).strength(1.0F)))
    @JvmField
    val ANVIL: KryptonBlock = register("anvil", DummyBlock(Properties.of(Materials.HEAVY_METAL, MaterialColors.METAL).requiresCorrectTool()
        .strength(5.0F, 1200.0F).sounds(BlockSoundGroups.ANVIL)))
    @JvmField
    val CHIPPED_ANVIL: KryptonBlock = register("chipped_anvil", DummyBlock(Properties.of(Materials.HEAVY_METAL, MaterialColors.METAL)
        .requiresCorrectTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroups.ANVIL)))
    @JvmField
    val DAMAGED_ANVIL: KryptonBlock = register("damaged_anvil", DummyBlock(Properties.of(Materials.HEAVY_METAL, MaterialColors.METAL)
        .requiresCorrectTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroups.ANVIL)))
    @JvmField
    val TRAPPED_CHEST: KryptonBlock = register("trapped_chest",
        DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LIGHT_WEIGHTED_PRESSURE_PLATE: KryptonBlock = register("light_weighted_pressure_plate",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.GOLD)
            .requiresCorrectTool()
            .noCollision()
            .strength(0.5F)
            .sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val HEAVY_WEIGHTED_PRESSURE_PLATE: KryptonBlock = register("heavy_weighted_pressure_plate",
        DummyBlock(Properties.of(Materials.METAL).requiresCorrectTool().noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val COMPARATOR: KryptonBlock = register("comparator", DummyBlock(Properties.of(Materials.DECORATION).instabreak().sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DAYLIGHT_DETECTOR: KryptonBlock = register("daylight_detector",
        DummyBlock(Properties.of(Materials.WOOD).strength(0.2F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val REDSTONE_BLOCK: KryptonBlock = register("redstone_block", DummyBlock(Properties.of(Materials.METAL, MaterialColors.FIRE)
        .requiresCorrectTool().strength(5.0F, 6.0F).sounds(BlockSoundGroups.METAL).isRedstoneConductor(NEVER)))
    @JvmField
    val NETHER_QUARTZ_ORE: KryptonBlock = register("nether_quartz_ore", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(3.0F, 3.0F).sounds(BlockSoundGroups.NETHER_ORE)))
    @JvmField
    val HOPPER: KryptonBlock = register("hopper", DummyBlock(Properties.of(Materials.METAL, MaterialColors.STONE).requiresCorrectTool()
        .strength(3.0F, 4.8F).sounds(BlockSoundGroups.METAL).noOcclusion()))
    @JvmField
    val QUARTZ_BLOCK: KryptonBlock = register("quartz_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.QUARTZ).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val CHISELED_QUARTZ_BLOCK: KryptonBlock = register("chiseled_quartz_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.QUARTZ).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val QUARTZ_PILLAR: KryptonBlock = register("quartz_pillar",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.QUARTZ).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val QUARTZ_STAIRS: KryptonBlock = register("quartz_stairs", DummyBlock(Properties.from(QUARTZ_BLOCK)))
    @JvmField
    val ACTIVATOR_RAIL: KryptonBlock = register("activator_rail",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.7F).sounds(BlockSoundGroups.METAL)))
    @JvmField
    val DROPPER: KryptonBlock = register("dropper", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.5F)))
    @JvmField
    val WHITE_TERRACOTTA: KryptonBlock = register("white_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_WHITE).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val ORANGE_TERRACOTTA: KryptonBlock = register("orange_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_ORANGE).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val MAGENTA_TERRACOTTA: KryptonBlock = register("magenta_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_MAGENTA).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val LIGHT_BLUE_TERRACOTTA: KryptonBlock = register("light_blue_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_LIGHT_BLUE).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val YELLOW_TERRACOTTA: KryptonBlock = register("yellow_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_YELLOW).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val LIME_TERRACOTTA: KryptonBlock = register("lime_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_LIGHT_GREEN).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val PINK_TERRACOTTA: KryptonBlock = register("pink_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_PINK).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val GRAY_TERRACOTTA: KryptonBlock = register("gray_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_GRAY).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val LIGHT_GRAY_TERRACOTTA: KryptonBlock = register("light_gray_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_LIGHT_GRAY).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val CYAN_TERRACOTTA: KryptonBlock = register("cyan_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_CYAN).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val PURPLE_TERRACOTTA: KryptonBlock = register("purple_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_PURPLE).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val BLUE_TERRACOTTA: KryptonBlock = register("blue_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_BLUE).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val BROWN_TERRACOTTA: KryptonBlock = register("brown_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_BROWN).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val GREEN_TERRACOTTA: KryptonBlock = register("green_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_GREEN).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val RED_TERRACOTTA: KryptonBlock = register("red_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_RED).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val BLACK_TERRACOTTA: KryptonBlock = register("black_terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_BLACK).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val WHITE_STAINED_GLASS_PANE: KryptonBlock = register("white_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val ORANGE_STAINED_GLASS_PANE: KryptonBlock = register("orange_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val MAGENTA_STAINED_GLASS_PANE: KryptonBlock = register("magenta_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val LIGHT_BLUE_STAINED_GLASS_PANE: KryptonBlock = register("light_blue_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val YELLOW_STAINED_GLASS_PANE: KryptonBlock = register("yellow_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val LIME_STAINED_GLASS_PANE: KryptonBlock = register("lime_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val PINK_STAINED_GLASS_PANE: KryptonBlock = register("pink_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val GRAY_STAINED_GLASS_PANE: KryptonBlock = register("gray_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val LIGHT_GRAY_STAINED_GLASS_PANE: KryptonBlock = register("light_gray_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val CYAN_STAINED_GLASS_PANE: KryptonBlock = register("cyan_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val PURPLE_STAINED_GLASS_PANE: KryptonBlock = register("purple_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val BLUE_STAINED_GLASS_PANE: KryptonBlock = register("blue_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val BROWN_STAINED_GLASS_PANE: KryptonBlock = register("brown_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val GREEN_STAINED_GLASS_PANE: KryptonBlock = register("green_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val RED_STAINED_GLASS_PANE: KryptonBlock = register("red_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val BLACK_STAINED_GLASS_PANE: KryptonBlock = register("black_stained_glass_pane",
        DummyBlock(Properties.of(Materials.GLASS).strength(0.3F).sounds(BlockSoundGroups.GLASS).noOcclusion()))
    @JvmField
    val ACACIA_STAIRS: KryptonBlock = register("acacia_stairs", DummyBlock(Properties.from(ACACIA_PLANKS)))
    @JvmField
    val DARK_OAK_STAIRS: KryptonBlock = register("dark_oak_stairs", DummyBlock(Properties.from(DARK_OAK_PLANKS)))
    @JvmField
    val MANGROVE_STAIRS: KryptonBlock = register("mangrove_stairs", DummyBlock(Properties.from(MANGROVE_PLANKS)))
    @JvmField
    val SLIME_BLOCK: KryptonBlock = register("slime_block",
        DummyBlock(Properties.of(Materials.CLAY, MaterialColors.GRASS).friction(0.8F).sounds(BlockSoundGroups.SLIME_BLOCK).noOcclusion()))
    @JvmField
    val BARRIER: KryptonBlock = register("barrier",
        DummyBlock(Properties.of(Materials.BARRIER).strength(-1.0F, 3600000.8F).noLootTable().noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val LIGHT: KryptonBlock = register("light",
        DummyBlock(Properties.of(Materials.AIR).strength(-1.0F, 3600000.8F).noLootTable().noOcclusion().lightLevel(LightBlock.LIGHT_EMISSION)))
    @JvmField
    val IRON_TRAPDOOR: KryptonBlock = register("iron_trapdoor", DummyBlock(Properties.of(Materials.METAL).requiresCorrectTool().strength(5.0F)
        .sounds(BlockSoundGroups.METAL).noOcclusion().isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val PRISMARINE: KryptonBlock = register("prismarine",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_CYAN).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val PRISMARINE_BRICKS: KryptonBlock = register("prismarine_bricks",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.DIAMOND).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val DARK_PRISMARINE: KryptonBlock = register("dark_prismarine",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.DIAMOND).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val PRISMARINE_STAIRS: KryptonBlock = register("prismarine_stairs", DummyBlock(Properties.from(PRISMARINE)))
    @JvmField
    val PRISMARINE_BRICK_STAIRS: KryptonBlock = register("prismarine_brick_stairs", DummyBlock(Properties.from(PRISMARINE_BRICKS)))
    @JvmField
    val DARK_PRISMARINE_STAIRS: KryptonBlock = register("dark_prismarine_stairs", DummyBlock(Properties.from(DARK_PRISMARINE)))
    @JvmField
    val PRISMARINE_SLAB: KryptonBlock = register("prismarine_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_CYAN).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val PRISMARINE_BRICK_SLAB: KryptonBlock = register("prismarine_brick_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.DIAMOND).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val DARK_PRISMARINE_SLAB: KryptonBlock = register("dark_prismarine_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.DIAMOND).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val SEA_LANTERN: KryptonBlock = register("sea_lantern",
        DummyBlock(Properties.of(Materials.GLASS, MaterialColors.QUARTZ).strength(0.3F).sounds(BlockSoundGroups.GLASS).lightLevel { 15 }))
    @JvmField
    val HAY_BLOCK: KryptonBlock = register("hay_block",
        DummyBlock(Properties.of(Materials.GRASS, MaterialColors.COLOR_YELLOW).strength(0.5F).sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val WHITE_CARPET: KryptonBlock = register("white_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.SNOW).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val ORANGE_CARPET: KryptonBlock = register("orange_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_ORANGE).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val MAGENTA_CARPET: KryptonBlock = register("magenta_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_MAGENTA).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val LIGHT_BLUE_CARPET: KryptonBlock = register("light_blue_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_LIGHT_BLUE).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val YELLOW_CARPET: KryptonBlock = register("yellow_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_YELLOW).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val LIME_CARPET: KryptonBlock = register("lime_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_LIGHT_GREEN).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val PINK_CARPET: KryptonBlock = register("pink_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_PINK).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val GRAY_CARPET: KryptonBlock = register("gray_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_GRAY).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val LIGHT_GRAY_CARPET: KryptonBlock = register("light_gray_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_LIGHT_GRAY).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val CYAN_CARPET: KryptonBlock = register("cyan_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_CYAN).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val PURPLE_CARPET: KryptonBlock = register("purple_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_PURPLE).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val BLUE_CARPET: KryptonBlock = register("blue_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_BLUE).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val BROWN_CARPET: KryptonBlock = register("brown_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_BROWN).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val GREEN_CARPET: KryptonBlock = register("green_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_GREEN).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val RED_CARPET: KryptonBlock = register("red_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_RED).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val BLACK_CARPET: KryptonBlock = register("black_carpet",
        DummyBlock(Properties.of(Materials.CLOTH_DECORATION, MaterialColors.COLOR_BLACK).strength(0.1F).sounds(BlockSoundGroups.WOOL)))
    @JvmField
    val TERRACOTTA: KryptonBlock = register("terracotta",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(1.25F, 4.2F)))
    @JvmField
    val COAL_BLOCK: KryptonBlock = register("coal_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK).requiresCorrectTool().strength(5.0F, 6.0F)))
    @JvmField
    val PACKED_ICE: KryptonBlock = register("packed_ice",
        DummyBlock(Properties.of(Materials.ICE_SOLID).friction(0.98F).strength(0.5F).sounds(BlockSoundGroups.GLASS)))
    @JvmField
    val SUNFLOWER: KryptonBlock = register("sunflower",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val LILAC: KryptonBlock = register("lilac",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val ROSE_BUSH: KryptonBlock = register("rose_bush",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val PEONY: KryptonBlock = register("peony",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val TALL_GRASS: KryptonBlock = register("tall_grass",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val LARGE_FERN: KryptonBlock = register("large_fern",
        DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.GRASS).offsetType(OffsetType.XZ)))
    @JvmField
    val WHITE_BANNER: KryptonBlock = register("white_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ORANGE_BANNER: KryptonBlock = register("orange_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MAGENTA_BANNER: KryptonBlock = register("magenta_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LIGHT_BLUE_BANNER: KryptonBlock = register("light_blue_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val YELLOW_BANNER: KryptonBlock = register("yellow_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LIME_BANNER: KryptonBlock = register("lime_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val PINK_BANNER: KryptonBlock = register("pink_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val GRAY_BANNER: KryptonBlock = register("gray_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LIGHT_GRAY_BANNER: KryptonBlock = register("light_gray_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CYAN_BANNER: KryptonBlock = register("cyan_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val PURPLE_BANNER: KryptonBlock = register("purple_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BLUE_BANNER: KryptonBlock = register("blue_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BROWN_BANNER: KryptonBlock = register("brown_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val GREEN_BANNER: KryptonBlock = register("green_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val RED_BANNER: KryptonBlock = register("red_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BLACK_BANNER: KryptonBlock = register("black_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WHITE_WALL_BANNER: KryptonBlock = register("white_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ORANGE_WALL_BANNER: KryptonBlock = register("orange_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MAGENTA_WALL_BANNER: KryptonBlock = register("magenta_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LIGHT_BLUE_WALL_BANNER: KryptonBlock = register("light_blue_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val YELLOW_WALL_BANNER: KryptonBlock = register("yellow_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LIME_WALL_BANNER: KryptonBlock = register("lime_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val PINK_WALL_BANNER: KryptonBlock = register("pink_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val GRAY_WALL_BANNER: KryptonBlock = register("gray_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val LIGHT_GRAY_WALL_BANNER: KryptonBlock = register("light_gray_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CYAN_WALL_BANNER: KryptonBlock = register("cyan_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val PURPLE_WALL_BANNER: KryptonBlock = register("purple_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BLUE_WALL_BANNER: KryptonBlock = register("blue_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BROWN_WALL_BANNER: KryptonBlock = register("brown_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val GREEN_WALL_BANNER: KryptonBlock = register("green_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val RED_WALL_BANNER: KryptonBlock = register("red_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BLACK_WALL_BANNER: KryptonBlock = register("black_wall_banner",
        DummyBlock(Properties.of(Materials.WOOD).noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val RED_SANDSTONE: KryptonBlock = register("red_sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val CHISELED_RED_SANDSTONE: KryptonBlock = register("chiseled_red_sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val CUT_RED_SANDSTONE: KryptonBlock = register("cut_red_sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(0.8F)))
    @JvmField
    val RED_SANDSTONE_STAIRS: KryptonBlock = register("red_sandstone_stairs", DummyBlock(Properties.from(RED_SANDSTONE)))
    @JvmField
    val OAK_SLAB: KryptonBlock = register("oak_slab",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.WOOD).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_SLAB: KryptonBlock = register("spruce_slab",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_SLAB: KryptonBlock = register("birch_slab",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.SAND).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_SLAB: KryptonBlock = register("jungle_slab",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.DIRT).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_SLAB: KryptonBlock = register("acacia_slab",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_ORANGE).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_SLAB: KryptonBlock = register("dark_oak_slab",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_BROWN).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_SLAB: KryptonBlock = register("mangrove_slab",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_RED).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STONE_SLAB: KryptonBlock = register("stone_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val SMOOTH_STONE_SLAB: KryptonBlock = register("smooth_stone_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val SANDSTONE_SLAB: KryptonBlock = register("sandstone_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val CUT_SANDSTONE_SLAB: KryptonBlock = register("cut_sandstone_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val PETRIFIED_OAK_SLAB: KryptonBlock = register("petrified_oak_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.WOOD).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val COBBLESTONE_SLAB: KryptonBlock = register("cobblestone_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val BRICK_SLAB: KryptonBlock = register("brick_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_RED).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val STONE_BRICK_SLAB: KryptonBlock = register("stone_brick_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val MUD_BRICK_SLAB: KryptonBlock = register("mud_brick_slab", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BROWN)
        .requiresCorrectTool().strength(1.5F, 3.0F).sounds(BlockSoundGroups.MUD_BRICKS)))
    @JvmField
    val NETHER_BRICK_SLAB: KryptonBlock = register("nether_brick_slab", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(2.0F, 6.0F).sounds(BlockSoundGroups.NETHER_BRICKS)))
    @JvmField
    val QUARTZ_SLAB: KryptonBlock = register("quartz_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.QUARTZ).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val RED_SANDSTONE_SLAB: KryptonBlock = register("red_sandstone_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val CUT_RED_SANDSTONE_SLAB: KryptonBlock = register("cut_red_sandstone_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val PURPUR_SLAB: KryptonBlock = register("purpur_slab",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_MAGENTA).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val SMOOTH_STONE: KryptonBlock = register("smooth_stone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.STONE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val SMOOTH_SANDSTONE: KryptonBlock = register("smooth_sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val SMOOTH_QUARTZ: KryptonBlock = register("smooth_quartz",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.QUARTZ).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val SMOOTH_RED_SANDSTONE: KryptonBlock = register("smooth_red_sandstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(2.0F, 6.0F)))
    @JvmField
    val SPRUCE_FENCE_GATE: KryptonBlock = register("spruce_fence_gate",
        DummyBlock(Properties.of(Materials.WOOD, SPRUCE_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_FENCE_GATE: KryptonBlock = register("birch_fence_gate",
        DummyBlock(Properties.of(Materials.WOOD, BIRCH_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_FENCE_GATE: KryptonBlock = register("jungle_fence_gate",
        DummyBlock(Properties.of(Materials.WOOD, JUNGLE_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_FENCE_GATE: KryptonBlock = register("acacia_fence_gate",
        DummyBlock(Properties.of(Materials.WOOD, ACACIA_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_FENCE_GATE: KryptonBlock = register("dark_oak_fence_gate",
        DummyBlock(Properties.of(Materials.WOOD, DARK_OAK_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_FENCE_GATE: KryptonBlock = register("mangrove_fence_gate",
        DummyBlock(Properties.of(Materials.WOOD, MANGROVE_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_FENCE: KryptonBlock = register("spruce_fence",
        DummyBlock(Properties.of(Materials.WOOD, SPRUCE_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BIRCH_FENCE: KryptonBlock = register("birch_fence",
        DummyBlock(Properties.of(Materials.WOOD, BIRCH_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val JUNGLE_FENCE: KryptonBlock = register("jungle_fence",
        DummyBlock(Properties.of(Materials.WOOD, JUNGLE_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val ACACIA_FENCE: KryptonBlock = register("acacia_fence",
        DummyBlock(Properties.of(Materials.WOOD, ACACIA_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val DARK_OAK_FENCE: KryptonBlock = register("dark_oak_fence",
        DummyBlock(Properties.of(Materials.WOOD, DARK_OAK_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val MANGROVE_FENCE: KryptonBlock = register("mangrove_fence",
        DummyBlock(Properties.of(Materials.WOOD, MANGROVE_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SPRUCE_DOOR: KryptonBlock = register("spruce_door",
        DummyBlock(Properties.of(Materials.WOOD, SPRUCE_PLANKS.defaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val BIRCH_DOOR: KryptonBlock = register("birch_door",
        DummyBlock(Properties.of(Materials.WOOD, BIRCH_PLANKS.defaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val JUNGLE_DOOR: KryptonBlock = register("jungle_door",
        DummyBlock(Properties.of(Materials.WOOD, JUNGLE_PLANKS.defaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val ACACIA_DOOR: KryptonBlock = register("acacia_door",
        DummyBlock(Properties.of(Materials.WOOD, ACACIA_PLANKS.defaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val DARK_OAK_DOOR: KryptonBlock = register("dark_oak_door",
        DummyBlock(Properties.of(Materials.WOOD, DARK_OAK_PLANKS.defaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val MANGROVE_DOOR: KryptonBlock = register("mangrove_door",
        DummyBlock(Properties.of(Materials.WOOD, MANGROVE_PLANKS.defaultMaterialColor()).strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val END_ROD: KryptonBlock = register("end_rod",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().lightLevel { 14 }.sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val CHORUS_PLANT: KryptonBlock = register("chorus_plant",
        DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_PURPLE).strength(0.4F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val CHORUS_FLOWER: KryptonBlock = register("chorus_flower", DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_PURPLE)
        .randomlyTicks().strength(0.4F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val PURPUR_BLOCK: KryptonBlock = register("purpur_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_MAGENTA).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val PURPUR_PILLAR: KryptonBlock = register("purpur_pillar",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_MAGENTA).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val PURPUR_STAIRS: KryptonBlock = register("purpur_stairs", DummyBlock(Properties.from(PURPUR_BLOCK)))
    @JvmField
    val END_STONE_BRICKS: KryptonBlock = register("end_stone_bricks",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(3.0F, 9.0F)))
    @JvmField
    val BEETROOTS: KryptonBlock = register("beetroots",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.CROP)))
    @JvmField
    val DIRT_PATH: KryptonBlock = register("dirt_path",
        DummyBlock(Properties.of(Materials.DIRT).strength(0.65F).sounds(BlockSoundGroups.GRASS).isSuffocating(ALWAYS)))
    @JvmField
    val END_GATEWAY: KryptonBlock = register("end_gateway", DummyBlock(Properties.of(Materials.PORTAL, MaterialColors.COLOR_BLACK).noCollision()
        .lightLevel { 15 }.strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val REPEATING_COMMAND_BLOCK: KryptonBlock = register("repeating_command_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_PURPLE).requiresCorrectTool().strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val CHAIN_COMMAND_BLOCK: KryptonBlock = register("chain_command_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_GREEN).requiresCorrectTool().strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val FROSTED_ICE: KryptonBlock = register("frosted_ice", DummyBlock(Properties.of(Materials.ICE).friction(0.98F).randomlyTicks().strength(0.5F)
        .sounds(BlockSoundGroups.GLASS).noOcclusion().isValidSpawn { _, _, _, type -> type === KryptonEntityTypes.POLAR_BEAR }))
    @JvmField
    val MAGMA_BLOCK: KryptonBlock = register("magma_block", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER).requiresCorrectTool()
        .lightLevel { 3 }.randomlyTicks().strength(0.5F).isValidSpawn { _, _, _, type -> type.isImmuneToFire }.hasPostProcess(ALWAYS)))
    @JvmField
    val NETHER_WART_BLOCK: KryptonBlock = register("nether_wart_block",
        DummyBlock(Properties.of(Materials.GRASS, MaterialColors.COLOR_RED).strength(1.0F).sounds(BlockSoundGroups.WART_BLOCK)))
    @JvmField
    val RED_NETHER_BRICKS: KryptonBlock = register("red_nether_bricks", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(2.0F, 6.0F).sounds(BlockSoundGroups.NETHER_BRICKS)))
    @JvmField
    val BONE_BLOCK: KryptonBlock = register("bone_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.SAND).requiresCorrectTool().strength(2.0F).sounds(BlockSoundGroups.BONE_BLOCK)))
    @JvmField
    val STRUCTURE_VOID: KryptonBlock = register("structure_void", DummyBlock(Properties.of(Materials.STRUCTURAL_AIR).noCollision().noLootTable()))
    @JvmField
    val OBSERVER: KryptonBlock = register("observer",
        DummyBlock(Properties.of(Materials.STONE).strength(3.0F).requiresCorrectTool().isRedstoneConductor(NEVER)))
    @JvmField
    val SHULKER_BOX: KryptonBlock = register("shulker_box", shulkerBox(null, Properties.of(Materials.SHULKER_SHELL)))
    @JvmField
    val WHITE_SHULKER_BOX: KryptonBlock = register("white_shulker_box",
        shulkerBox(DyeColor.WHITE, Properties.of(Materials.SHULKER_SHELL, MaterialColors.SNOW)))
    @JvmField
    val ORANGE_SHULKER_BOX: KryptonBlock = register("orange_shulker_box",
        shulkerBox(DyeColor.ORANGE, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_ORANGE)))
    @JvmField
    val MAGENTA_SHULKER_BOX: KryptonBlock = register("magenta_shulker_box",
        shulkerBox(DyeColor.MAGENTA, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_MAGENTA)))
    @JvmField
    val LIGHT_BLUE_SHULKER_BOX: KryptonBlock = register("light_blue_shulker_box",
        shulkerBox(DyeColor.LIGHT_BLUE, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_LIGHT_BLUE)))
    @JvmField
    val YELLOW_SHULKER_BOX: KryptonBlock = register("yellow_shulker_box",
        shulkerBox(DyeColor.YELLOW, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_YELLOW)))
    @JvmField
    val LIME_SHULKER_BOX: KryptonBlock = register("lime_shulker_box",
        shulkerBox(DyeColor.LIME, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_LIGHT_GREEN)))
    @JvmField
    val PINK_SHULKER_BOX: KryptonBlock = register("pink_shulker_box",
        shulkerBox(DyeColor.PINK, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_PINK)))
    @JvmField
    val GRAY_SHULKER_BOX: KryptonBlock = register("gray_shulker_box",
        shulkerBox(DyeColor.GRAY, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_GRAY)))
    @JvmField
    val LIGHT_GRAY_SHULKER_BOX: KryptonBlock = register("light_gray_shulker_box",
        shulkerBox(DyeColor.LIGHT_GRAY, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_LIGHT_GRAY)))
    @JvmField
    val CYAN_SHULKER_BOX: KryptonBlock = register("cyan_shulker_box",
        shulkerBox(DyeColor.CYAN, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_CYAN)))
    @JvmField
    val PURPLE_SHULKER_BOX: KryptonBlock = register("purple_shulker_box",
        shulkerBox(DyeColor.PURPLE, Properties.of(Materials.SHULKER_SHELL, MaterialColors.TERRACOTTA_PURPLE)))
    @JvmField
    val BLUE_SHULKER_BOX: KryptonBlock = register("blue_shulker_box",
        shulkerBox(DyeColor.BLUE, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_BLUE)))
    @JvmField
    val BROWN_SHULKER_BOX: KryptonBlock = register("brown_shulker_box",
        shulkerBox(DyeColor.BROWN, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_BROWN)))
    @JvmField
    val GREEN_SHULKER_BOX: KryptonBlock = register("green_shulker_box",
        shulkerBox(DyeColor.GREEN, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_GREEN)))
    @JvmField
    val RED_SHULKER_BOX: KryptonBlock = register("red_shulker_box",
        shulkerBox(DyeColor.RED, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_RED)))
    @JvmField
    val BLACK_SHULKER_BOX: KryptonBlock = register("black_shulker_box",
        shulkerBox(DyeColor.BLACK, Properties.of(Materials.SHULKER_SHELL, MaterialColors.COLOR_BLACK)))
    @JvmField
    val WHITE_GLAZED_TERRACOTTA: KryptonBlock = register("white_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.WHITE).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val ORANGE_GLAZED_TERRACOTTA: KryptonBlock = register("orange_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.ORANGE).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val MAGENTA_GLAZED_TERRACOTTA: KryptonBlock = register("magenta_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.MAGENTA).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val LIGHT_BLUE_GLAZED_TERRACOTTA: KryptonBlock = register("light_blue_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.LIGHT_BLUE).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val YELLOW_GLAZED_TERRACOTTA: KryptonBlock = register("yellow_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.YELLOW).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val LIME_GLAZED_TERRACOTTA: KryptonBlock = register("lime_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.LIME).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val PINK_GLAZED_TERRACOTTA: KryptonBlock = register("pink_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.PINK).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val GRAY_GLAZED_TERRACOTTA: KryptonBlock = register("gray_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.GRAY).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val LIGHT_GRAY_GLAZED_TERRACOTTA: KryptonBlock = register("light_gray_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.LIGHT_GRAY).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val CYAN_GLAZED_TERRACOTTA: KryptonBlock = register("cyan_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.CYAN).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val PURPLE_GLAZED_TERRACOTTA: KryptonBlock = register("purple_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.PURPLE).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val BLUE_GLAZED_TERRACOTTA: KryptonBlock = register("blue_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.BLUE).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val BROWN_GLAZED_TERRACOTTA: KryptonBlock = register("brown_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.BROWN).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val GREEN_GLAZED_TERRACOTTA: KryptonBlock = register("green_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.GREEN).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val RED_GLAZED_TERRACOTTA: KryptonBlock = register("red_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.RED).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val BLACK_GLAZED_TERRACOTTA: KryptonBlock = register("black_glazed_terracotta",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.BLACK).requiresCorrectTool().strength(1.4F)))
    @JvmField
    val WHITE_CONCRETE: KryptonBlock = register("white_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.WHITE).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val ORANGE_CONCRETE: KryptonBlock = register("orange_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.ORANGE).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val MAGENTA_CONCRETE: KryptonBlock = register("magenta_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.MAGENTA).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val LIGHT_BLUE_CONCRETE: KryptonBlock = register("light_blue_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.LIGHT_BLUE).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val YELLOW_CONCRETE: KryptonBlock = register("yellow_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.YELLOW).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val LIME_CONCRETE: KryptonBlock = register("lime_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.LIME).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val PINK_CONCRETE: KryptonBlock = register("pink_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.PINK).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val GRAY_CONCRETE: KryptonBlock = register("gray_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.GRAY).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val LIGHT_GRAY_CONCRETE: KryptonBlock = register("light_gray_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.LIGHT_GRAY).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val CYAN_CONCRETE: KryptonBlock = register("cyan_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.CYAN).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val PURPLE_CONCRETE: KryptonBlock = register("purple_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.PURPLE).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val BLUE_CONCRETE: KryptonBlock = register("blue_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.BLUE).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val BROWN_CONCRETE: KryptonBlock = register("brown_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.BROWN).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val GREEN_CONCRETE: KryptonBlock = register("green_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.GREEN).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val RED_CONCRETE: KryptonBlock = register("red_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.RED).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val BLACK_CONCRETE: KryptonBlock = register("black_concrete",
        DummyBlock(Properties.of(Materials.STONE, DyeColor.BLACK).requiresCorrectTool().strength(1.8F)))
    @JvmField
    val WHITE_CONCRETE_POWDER: KryptonBlock = register("white_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.WHITE).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val ORANGE_CONCRETE_POWDER: KryptonBlock = register("orange_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.ORANGE).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val MAGENTA_CONCRETE_POWDER: KryptonBlock = register("magenta_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.MAGENTA).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val LIGHT_BLUE_CONCRETE_POWDER: KryptonBlock = register("light_blue_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.LIGHT_BLUE).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val YELLOW_CONCRETE_POWDER: KryptonBlock = register("yellow_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.YELLOW).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val LIME_CONCRETE_POWDER: KryptonBlock = register("lime_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.LIME).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val PINK_CONCRETE_POWDER: KryptonBlock = register("pink_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.PINK).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val GRAY_CONCRETE_POWDER: KryptonBlock = register("gray_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.GRAY).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val LIGHT_GRAY_CONCRETE_POWDER: KryptonBlock = register("light_gray_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.LIGHT_GRAY).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val CYAN_CONCRETE_POWDER: KryptonBlock = register("cyan_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.CYAN).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val PURPLE_CONCRETE_POWDER: KryptonBlock = register("purple_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.PURPLE).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val BLUE_CONCRETE_POWDER: KryptonBlock = register("blue_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.BLUE).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val BROWN_CONCRETE_POWDER: KryptonBlock = register("brown_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.BROWN).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val GREEN_CONCRETE_POWDER: KryptonBlock = register("green_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.GREEN).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val RED_CONCRETE_POWDER: KryptonBlock = register("red_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.RED).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val BLACK_CONCRETE_POWDER: KryptonBlock = register("black_concrete_powder",
        DummyBlock(Properties.of(Materials.SAND, DyeColor.BLACK).strength(0.5F).sounds(BlockSoundGroups.SAND)))
    @JvmField
    val KELP: KryptonBlock = register("kelp",
        DummyBlock(Properties.of(Materials.WATER_PLANT).noCollision().randomlyTicks().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val KELP_PLANT: KryptonBlock = register("kelp_plant",
        DummyBlock(Properties.of(Materials.WATER_PLANT).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val DRIED_KELP_BLOCK: KryptonBlock = register("dried_kelp_block",
        DummyBlock(Properties.of(Materials.GRASS, MaterialColors.COLOR_GREEN).strength(0.5F, 2.5F).sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val TURTLE_EGG: KryptonBlock = register("turtle_egg",
        DummyBlock(Properties.of(Materials.EGG, MaterialColors.SAND).strength(0.5F).sounds(BlockSoundGroups.METAL).randomlyTicks().noOcclusion()))
    @JvmField
    val DEAD_TUBE_CORAL_BLOCK: KryptonBlock = register("dead_tube_coral_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val DEAD_BRAIN_CORAL_BLOCK: KryptonBlock = register("dead_brain_coral_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val DEAD_BUBBLE_CORAL_BLOCK: KryptonBlock = register("dead_bubble_coral_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val DEAD_FIRE_CORAL_BLOCK: KryptonBlock = register("dead_fire_coral_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val DEAD_HORN_CORAL_BLOCK: KryptonBlock = register("dead_horn_coral_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val TUBE_CORAL_BLOCK: KryptonBlock = register("tube_coral_block", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLUE)
        .requiresCorrectTool().strength(1.5F, 6.0F).sounds(BlockSoundGroups.CORAL_BLOCK)))
    @JvmField
    val BRAIN_CORAL_BLOCK: KryptonBlock = register("brain_coral_block", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_PINK)
        .requiresCorrectTool().strength(1.5F, 6.0F).sounds(BlockSoundGroups.CORAL_BLOCK)))
    @JvmField
    val BUBBLE_CORAL_BLOCK: KryptonBlock = register("bubble_coral_block", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_PURPLE)
        .requiresCorrectTool().strength(1.5F, 6.0F).sounds(BlockSoundGroups.CORAL_BLOCK)))
    @JvmField
    val FIRE_CORAL_BLOCK: KryptonBlock = register("fire_coral_block", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_RED)
        .requiresCorrectTool().strength(1.5F, 6.0F).sounds(BlockSoundGroups.CORAL_BLOCK)))
    @JvmField
    val HORN_CORAL_BLOCK: KryptonBlock = register("horn_coral_block", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_YELLOW)
        .requiresCorrectTool().strength(1.5F, 6.0F).sounds(BlockSoundGroups.CORAL_BLOCK)))
    @JvmField
    val DEAD_TUBE_CORAL: KryptonBlock = register("dead_tube_coral",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_BRAIN_CORAL: KryptonBlock = register("dead_brain_coral",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_BUBBLE_CORAL: KryptonBlock = register("dead_bubble_coral",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_FIRE_CORAL: KryptonBlock = register("dead_fire_coral",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_HORN_CORAL: KryptonBlock = register("dead_horn_coral",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val TUBE_CORAL: KryptonBlock = register("tube_coral",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_BLUE).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val BRAIN_CORAL: KryptonBlock = register("brain_coral",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_PINK).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val BUBBLE_CORAL: KryptonBlock = register("bubble_coral",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_PURPLE).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val FIRE_CORAL: KryptonBlock = register("fire_coral",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_RED).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val HORN_CORAL: KryptonBlock = register("horn_coral",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_YELLOW).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val DEAD_TUBE_CORAL_FAN: KryptonBlock = register("dead_tube_coral_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_BRAIN_CORAL_FAN: KryptonBlock = register("dead_brain_coral_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_BUBBLE_CORAL_FAN: KryptonBlock = register("dead_bubble_coral_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_FIRE_CORAL_FAN: KryptonBlock = register("dead_fire_coral_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_HORN_CORAL_FAN: KryptonBlock = register("dead_horn_coral_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val TUBE_CORAL_FAN: KryptonBlock = register("tube_coral_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_BLUE).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val BRAIN_CORAL_FAN: KryptonBlock = register("brain_coral_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_PINK).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val BUBBLE_CORAL_FAN: KryptonBlock = register("bubble_coral_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_PURPLE).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val FIRE_CORAL_FAN: KryptonBlock = register("fire_coral_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_RED).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val HORN_CORAL_FAN: KryptonBlock = register("horn_coral_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_YELLOW).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val DEAD_TUBE_CORAL_WALL_FAN: KryptonBlock = register("dead_tube_coral_wall_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_BRAIN_CORAL_WALL_FAN: KryptonBlock = register("dead_brain_coral_wall_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_BUBBLE_CORAL_WALL_FAN: KryptonBlock = register("dead_bubble_coral_wall_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_FIRE_CORAL_WALL_FAN: KryptonBlock = register("dead_fire_coral_wall_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val DEAD_HORN_CORAL_WALL_FAN: KryptonBlock = register("dead_horn_coral_wall_fan",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_GRAY).requiresCorrectTool().noCollision().instabreak()))
    @JvmField
    val TUBE_CORAL_WALL_FAN: KryptonBlock = register("tube_coral_wall_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_BLUE).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val BRAIN_CORAL_WALL_FAN: KryptonBlock = register("brain_coral_wall_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_PINK).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val BUBBLE_CORAL_WALL_FAN: KryptonBlock = register("bubble_coral_wall_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_PURPLE).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val FIRE_CORAL_WALL_FAN: KryptonBlock = register("fire_coral_wall_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_RED).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val HORN_CORAL_WALL_FAN: KryptonBlock = register("horn_coral_wall_fan",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_YELLOW).noCollision().instabreak().sounds(BlockSoundGroups.WET_GRASS)))
    @JvmField
    val SEA_PICKLE: KryptonBlock = register("sea_pickle",
        DummyBlock(Properties.of(Materials.WATER_PLANT, MaterialColors.COLOR_GREEN)
            .lightLevel { if (SeaPickleBlock.isDead(it)) 0 else 3 + 3 * it.requireProperty(SeaPickleBlock.PICKLES) }
            .sounds(BlockSoundGroups.SLIME_BLOCK)
            .noOcclusion()))
    @JvmField
    val BLUE_ICE: KryptonBlock = register("blue_ice",
        DummyBlock(Properties.of(Materials.ICE_SOLID).strength(2.8F).friction(0.989F).sounds(BlockSoundGroups.GLASS)))
    @JvmField
    val CONDUIT: KryptonBlock = register("conduit",
        DummyBlock(Properties.of(Materials.GLASS, MaterialColors.DIAMOND).strength(3.0F).lightLevel { 15 }.noOcclusion()))
    @JvmField
    val BAMBOO_SAPLING: KryptonBlock = register("bamboo_sapling", DummyBlock(Properties.of(Materials.BAMBOO_SAPLING).randomlyTicks().instabreak()
        .noCollision().strength(1.0F).sounds(BlockSoundGroups.BAMBOO_SAPLING).offsetType(OffsetType.XZ)))
    @JvmField
    val BAMBOO: KryptonBlock = register("bamboo", DummyBlock(Properties.of(Materials.BAMBOO, MaterialColors.PLANT).randomlyTicks().instabreak()
        .strength(1.0F).sounds(BlockSoundGroups.BAMBOO).noOcclusion().dynamicShape().offsetType(OffsetType.XZ)))
    @JvmField
    val POTTED_BAMBOO: KryptonBlock = register("potted_bamboo", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val VOID_AIR: KryptonBlock = register("void_air", AirBlock(Properties.of(Materials.AIR).noCollision().noLootTable().air()))
    @JvmField
    val CAVE_AIR: KryptonBlock = register("cave_air", AirBlock(Properties.of(Materials.AIR).noCollision().noLootTable().air()))
    @JvmField
    val BUBBLE_COLUMN: KryptonBlock = register("bubble_column", DummyBlock(Properties.of(Materials.BUBBLE_COLUMN).noCollision().noLootTable()))
    @JvmField
    val POLISHED_GRANITE_STAIRS: KryptonBlock = register("polished_granite_stairs", DummyBlock(Properties.from(POLISHED_GRANITE)))
    @JvmField
    val SMOOTH_RED_SANDSTONE_STAIRS: KryptonBlock = register("smooth_red_sandstone_stairs", DummyBlock(Properties.from(SMOOTH_RED_SANDSTONE)))
    @JvmField
    val MOSSY_STONE_BRICK_STAIRS: KryptonBlock = register("mossy_stone_brick_stairs", DummyBlock(Properties.from(MOSSY_STONE_BRICKS)))
    @JvmField
    val POLISHED_DIORITE_STAIRS: KryptonBlock = register("polished_diorite_stairs", DummyBlock(Properties.from(POLISHED_DIORITE)))
    @JvmField
    val MOSSY_COBBLESTONE_STAIRS: KryptonBlock = register("mossy_cobblestone_stairs", DummyBlock(Properties.from(MOSSY_COBBLESTONE)))
    @JvmField
    val END_STONE_BRICK_STAIRS: KryptonBlock = register("end_stone_brick_stairs", DummyBlock(Properties.from(END_STONE_BRICKS)))
    @JvmField
    val STONE_STAIRS: KryptonBlock = register("stone_stairs", DummyBlock(Properties.from(STONE)))
    @JvmField
    val SMOOTH_SANDSTONE_STAIRS: KryptonBlock = register("smooth_sandstone_stairs", DummyBlock(Properties.from(SMOOTH_SANDSTONE)))
    @JvmField
    val SMOOTH_QUARTZ_STAIRS: KryptonBlock = register("smooth_quartz_stairs", DummyBlock(Properties.from(SMOOTH_QUARTZ)))
    @JvmField
    val GRANITE_STAIRS: KryptonBlock = register("granite_stairs", DummyBlock(Properties.from(GRANITE)))
    @JvmField
    val ANDESITE_STAIRS: KryptonBlock = register("andesite_stairs", DummyBlock(Properties.from(ANDESITE)))
    @JvmField
    val RED_NETHER_BRICK_STAIRS: KryptonBlock = register("red_nether_brick_stairs", DummyBlock(Properties.from(RED_NETHER_BRICKS)))
    @JvmField
    val POLISHED_ANDESITE_STAIRS: KryptonBlock = register("polished_andesite_stairs", DummyBlock(Properties.from(POLISHED_ANDESITE)))
    @JvmField
    val DIORITE_STAIRS: KryptonBlock = register("diorite_stairs", DummyBlock(Properties.from(DIORITE)))
    @JvmField
    val POLISHED_GRANITE_SLAB: KryptonBlock = register("polished_granite_slab", DummyBlock(Properties.from(POLISHED_GRANITE)))
    @JvmField
    val SMOOTH_RED_SANDSTONE_SLAB: KryptonBlock = register("smooth_red_sandstone_slab", DummyBlock(Properties.from(SMOOTH_RED_SANDSTONE)))
    @JvmField
    val MOSSY_STONE_BRICK_SLAB: KryptonBlock = register("mossy_stone_brick_slab", DummyBlock(Properties.from(MOSSY_STONE_BRICKS)))
    @JvmField
    val POLISHED_DIORITE_SLAB: KryptonBlock = register("polished_diorite_slab", DummyBlock(Properties.from(POLISHED_DIORITE)))
    @JvmField
    val MOSSY_COBBLESTONE_SLAB: KryptonBlock = register("mossy_cobblestone_slab", DummyBlock(Properties.from(MOSSY_COBBLESTONE)))
    @JvmField
    val END_STONE_BRICK_SLAB: KryptonBlock = register("end_stone_brick_slab", DummyBlock(Properties.from(END_STONE_BRICKS)))
    @JvmField
    val SMOOTH_SANDSTONE_SLAB: KryptonBlock = register("smooth_sandstone_slab", DummyBlock(Properties.from(SMOOTH_SANDSTONE)))
    @JvmField
    val SMOOTH_QUARTZ_SLAB: KryptonBlock = register("smooth_quartz_slab", DummyBlock(Properties.from(SMOOTH_QUARTZ)))
    @JvmField
    val GRANITE_SLAB: KryptonBlock = register("granite_slab", DummyBlock(Properties.from(GRANITE)))
    @JvmField
    val ANDESITE_SLAB: KryptonBlock = register("andesite_slab", DummyBlock(Properties.from(ANDESITE)))
    @JvmField
    val RED_NETHER_BRICK_SLAB: KryptonBlock = register("red_nether_brick_slab", DummyBlock(Properties.from(RED_NETHER_BRICKS)))
    @JvmField
    val POLISHED_ANDESITE_SLAB: KryptonBlock = register("polished_andesite_slab", DummyBlock(Properties.from(POLISHED_ANDESITE)))
    @JvmField
    val DIORITE_SLAB: KryptonBlock = register("diorite_slab", DummyBlock(Properties.from(DIORITE)))
    @JvmField
    val BRICK_WALL: KryptonBlock = register("brick_wall", DummyBlock(Properties.from(BRICKS)))
    @JvmField
    val PRISMARINE_WALL: KryptonBlock = register("prismarine_wall", DummyBlock(Properties.from(PRISMARINE)))
    @JvmField
    val RED_SANDSTONE_WALL: KryptonBlock = register("red_sandstone_wall", DummyBlock(Properties.from(RED_SANDSTONE)))
    @JvmField
    val MOSSY_STONE_BRICK_WALL: KryptonBlock = register("mossy_stone_brick_wall", DummyBlock(Properties.from(MOSSY_STONE_BRICKS)))
    @JvmField
    val GRANITE_WALL: KryptonBlock = register("granite_wall", DummyBlock(Properties.from(GRANITE)))
    @JvmField
    val STONE_BRICK_WALL: KryptonBlock = register("stone_brick_wall", DummyBlock(Properties.from(STONE_BRICKS)))
    @JvmField
    val MUD_BRICK_WALL: KryptonBlock = register("mud_brick_wall", DummyBlock(Properties.from(MUD_BRICKS)))
    @JvmField
    val NETHER_BRICK_WALL: KryptonBlock = register("nether_brick_wall", DummyBlock(Properties.from(NETHER_BRICKS)))
    @JvmField
    val ANDESITE_WALL: KryptonBlock = register("andesite_wall", DummyBlock(Properties.from(ANDESITE)))
    @JvmField
    val RED_NETHER_BRICK_WALL: KryptonBlock = register("red_nether_brick_wall", DummyBlock(Properties.from(RED_NETHER_BRICKS)))
    @JvmField
    val SANDSTONE_WALL: KryptonBlock = register("sandstone_wall", DummyBlock(Properties.from(SANDSTONE)))
    @JvmField
    val END_STONE_BRICK_WALL: KryptonBlock = register("end_stone_brick_wall", DummyBlock(Properties.from(END_STONE_BRICKS)))
    @JvmField
    val DIORITE_WALL: KryptonBlock = register("diorite_wall", DummyBlock(Properties.from(DIORITE)))
    @JvmField
    val SCAFFOLDING: KryptonBlock = register("scaffolding",
        DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.SAND).noCollision().sounds(BlockSoundGroups.SCAFFOLDING).dynamicShape()))
    @JvmField
    val LOOM: KryptonBlock = register("loom", DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BARREL: KryptonBlock = register("barrel", DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SMOKER: KryptonBlock = register("smoker",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.5F).lightLevel(litBlockEmission(13))))
    @JvmField
    val BLAST_FURNACE: KryptonBlock = register("blast_furnace",
        DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.5F).lightLevel(litBlockEmission(13))))
    @JvmField
    val CARTOGRAPHY_TABLE: KryptonBlock = register("cartography_table",
        DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val FLETCHING_TABLE: KryptonBlock = register("fletching_table",
        DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val GRINDSTONE: KryptonBlock = register("grindstone", DummyBlock(Properties.of(Materials.HEAVY_METAL, MaterialColors.METAL).requiresCorrectTool()
        .strength(2.0F, 6.0F).sounds(BlockSoundGroups.STONE)))
    @JvmField
    val LECTERN: KryptonBlock = register("lectern", DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val SMITHING_TABLE: KryptonBlock = register("smithing_table",
        DummyBlock(Properties.of(Materials.WOOD).strength(2.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STONECUTTER: KryptonBlock = register("stonecutter", DummyBlock(Properties.of(Materials.STONE).requiresCorrectTool().strength(3.5F)))
    @JvmField
    val BELL: KryptonBlock = register("bell",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.GOLD).requiresCorrectTool().strength(5.0F).sounds(BlockSoundGroups.ANVIL)))
    @JvmField
    val LANTERN: KryptonBlock = register("lantern", DummyBlock(Properties.of(Materials.METAL).requiresCorrectTool().strength(3.5F)
        .sounds(BlockSoundGroups.LANTERN).lightLevel { 15 }.noOcclusion()))
    @JvmField
    val SOUL_LANTERN: KryptonBlock = register("soul_lantern", DummyBlock(Properties.of(Materials.METAL).requiresCorrectTool().strength(3.5F)
        .sounds(BlockSoundGroups.LANTERN).lightLevel { 10 }.noOcclusion()))
    @JvmField
    val CAMPFIRE: KryptonBlock = register("campfire", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(2.0F)
        .sounds(BlockSoundGroups.WOOD).lightLevel(litBlockEmission(15)).noOcclusion()))
    @JvmField
    val SOUL_CAMPFIRE: KryptonBlock = register("soul_campfire", DummyBlock(Properties.of(Materials.WOOD, MaterialColors.PODZOL).strength(2.0F)
        .sounds(BlockSoundGroups.WOOD).lightLevel(litBlockEmission(10)).noOcclusion()))
    @JvmField
    val SWEET_BERRY_BUSH: KryptonBlock = register("sweet_berry_bush",
        DummyBlock(Properties.of(Materials.PLANT).randomlyTicks().noCollision().sounds(BlockSoundGroups.SWEET_BERRY_BUSH)))
    @JvmField
    val WARPED_STEM: KryptonBlock = register("warped_stem", netherStem(MaterialColors.WARPED_STEM))
    @JvmField
    val STRIPPED_WARPED_STEM: KryptonBlock = register("stripped_warped_stem", netherStem(MaterialColors.WARPED_STEM))
    @JvmField
    val WARPED_HYPHAE: KryptonBlock = register("warped_hyphae",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, MaterialColors.WARPED_HYPHAE).strength(2.0F).sounds(BlockSoundGroups.STEM)))
    @JvmField
    val STRIPPED_WARPED_HYPHAE: KryptonBlock = register("stripped_warped_hyphae",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, MaterialColors.WARPED_HYPHAE).strength(2.0F).sounds(BlockSoundGroups.STEM)))
    @JvmField
    val WARPED_NYLIUM: KryptonBlock = register("warped_nylium", DummyBlock(Properties.of(Materials.STONE, MaterialColors.WARPED_NYLIUM)
        .requiresCorrectTool().strength(0.4F).sounds(BlockSoundGroups.NYLIUM).randomlyTicks()))
    @JvmField
    val WARPED_FUNGUS: KryptonBlock = register("warped_fungus",
        DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_CYAN).instabreak().noCollision().sounds(BlockSoundGroups.FUNGUS)))
    @JvmField
    val WARPED_WART_BLOCK: KryptonBlock = register("warped_wart_block",
        DummyBlock(Properties.of(Materials.GRASS, MaterialColors.WARPED_WART_BLOCK).strength(1.0F).sounds(BlockSoundGroups.WART_BLOCK)))
    @JvmField
    val WARPED_ROOTS: KryptonBlock = register("warped_roots",
        DummyBlock(Properties.of(Materials.REPLACEABLE_FIREPROOF_PLANT, MaterialColors.COLOR_CYAN)
            .noCollision()
            .instabreak()
            .sounds(BlockSoundGroups.ROOTS)
            .offsetType(OffsetType.XZ)))
    @JvmField
    val NETHER_SPROUTS: KryptonBlock = register("nether_sprouts",
        DummyBlock(Properties.of(Materials.REPLACEABLE_FIREPROOF_PLANT, MaterialColors.COLOR_CYAN)
            .noCollision()
            .instabreak()
            .sounds(BlockSoundGroups.NETHER_SPROUTS)
            .offsetType(OffsetType.XZ)))
    @JvmField
    val CRIMSON_STEM: KryptonBlock = register("crimson_stem", netherStem(MaterialColors.CRIMSON_STEM))
    @JvmField
    val STRIPPED_CRIMSON_STEM: KryptonBlock = register("stripped_crimson_stem", netherStem(MaterialColors.CRIMSON_STEM))
    @JvmField
    val CRIMSON_HYPHAE: KryptonBlock = register("crimson_hyphae",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, MaterialColors.CRIMSON_HYPHAE).strength(2.0F).sounds(BlockSoundGroups.STEM)))
    @JvmField
    val STRIPPED_CRIMSON_HYPHAE: KryptonBlock = register("stripped_crimson_hyphae",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, MaterialColors.CRIMSON_HYPHAE).strength(2.0F).sounds(BlockSoundGroups.STEM)))
    @JvmField
    val CRIMSON_NYLIUM: KryptonBlock = register("crimson_nylium", DummyBlock(Properties.of(Materials.STONE, MaterialColors.CRIMSON_NYLIUM)
        .requiresCorrectTool().strength(0.4F).sounds(BlockSoundGroups.NYLIUM).randomlyTicks()))
    @JvmField
    val CRIMSON_FUNGUS: KryptonBlock = register("crimson_fungus",
        DummyBlock(Properties.of(Materials.PLANT, MaterialColors.NETHER).instabreak().noCollision().sounds(BlockSoundGroups.FUNGUS)))
    @JvmField
    val SHROOMLIGHT: KryptonBlock = register("shroomlight",
        DummyBlock(Properties.of(Materials.GRASS, MaterialColors.COLOR_RED).strength(1.0F).sounds(BlockSoundGroups.SHROOMLIGHT).lightLevel { 15 }))
    @JvmField
    val WEEPING_VINES: KryptonBlock = register("weeping_vines", DummyBlock(Properties.of(Materials.PLANT, MaterialColors.NETHER).randomlyTicks()
        .noCollision().instabreak().sounds(BlockSoundGroups.WEEPING_VINES)))
    @JvmField
    val WEEPING_VINES_PLANT: KryptonBlock = register("weeping_vines_plant",
        DummyBlock(Properties.of(Materials.PLANT, MaterialColors.NETHER).noCollision().instabreak().sounds(BlockSoundGroups.WEEPING_VINES)))
    @JvmField
    val TWISTING_VINES: KryptonBlock = register("twisting_vines", DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_CYAN)
        .randomlyTicks().noCollision().instabreak().sounds(BlockSoundGroups.WEEPING_VINES)))
    @JvmField
    val TWISTING_VINES_PLANT: KryptonBlock = register("twisting_vines_plant",
        DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_CYAN).noCollision().instabreak().sounds(BlockSoundGroups.WEEPING_VINES)))
    @JvmField
    val CRIMSON_ROOTS: KryptonBlock = register("crimson_roots",
        DummyBlock(Properties.of(Materials.REPLACEABLE_FIREPROOF_PLANT, MaterialColors.NETHER)
            .noCollision()
            .instabreak()
            .sounds(BlockSoundGroups.ROOTS)
            .offsetType(OffsetType.XZ)))
    @JvmField
    val CRIMSON_PLANKS: KryptonBlock = register("crimson_planks",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, MaterialColors.CRIMSON_STEM).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_PLANKS: KryptonBlock = register("warped_planks",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, MaterialColors.WARPED_STEM).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CRIMSON_SLAB: KryptonBlock = register("crimson_slab",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_SLAB: KryptonBlock = register("warped_slab",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CRIMSON_PRESSURE_PLATE: KryptonBlock = register("crimson_pressure_plate",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor())
            .noCollision()
            .strength(0.5F)
            .sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_PRESSURE_PLATE: KryptonBlock = register("warped_pressure_plate",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor())
            .noCollision()
            .strength(0.5F)
            .sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CRIMSON_FENCE: KryptonBlock = register("crimson_fence",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_FENCE: KryptonBlock = register("warped_fence",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CRIMSON_TRAPDOOR: KryptonBlock = register("crimson_trapdoor",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor())
            .strength(3.0F)
            .sounds(BlockSoundGroups.WOOD)
            .noOcclusion()
            .isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val WARPED_TRAPDOOR: KryptonBlock = register("warped_trapdoor",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor())
            .strength(3.0F)
            .sounds(BlockSoundGroups.WOOD)
            .noOcclusion()
            .isValidSpawn(NEVER_SPAWN)))
    @JvmField
    val CRIMSON_FENCE_GATE: KryptonBlock = register("crimson_fence_gate",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_FENCE_GATE: KryptonBlock = register("warped_fence_gate",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor()).strength(2.0F, 3.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CRIMSON_STAIRS: KryptonBlock = register("crimson_stairs", DummyBlock(Properties.from(CRIMSON_PLANKS)))
    @JvmField
    val WARPED_STAIRS: KryptonBlock = register("warped_stairs", DummyBlock(Properties.from(WARPED_PLANKS)))
    @JvmField
    val CRIMSON_BUTTON: KryptonBlock = register("crimson_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_BUTTON: KryptonBlock = register("warped_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CRIMSON_DOOR: KryptonBlock = register("crimson_door", DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor())
        .strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val WARPED_DOOR: KryptonBlock = register("warped_door", DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor())
        .strength(3.0F).sounds(BlockSoundGroups.WOOD).noOcclusion()))
    @JvmField
    val CRIMSON_SIGN: KryptonBlock = register("crimson_sign", DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor())
        .noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_SIGN: KryptonBlock = register("warped_sign", DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor())
        .noCollision().strength(1.0F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val CRIMSON_WALL_SIGN: KryptonBlock = register("crimson_wall_sign",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, CRIMSON_PLANKS.defaultMaterialColor())
            .noCollision()
            .strength(1.0F)
            .sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val WARPED_WALL_SIGN: KryptonBlock = register("warped_wall_sign",
        DummyBlock(Properties.of(Materials.NETHER_WOOD, WARPED_PLANKS.defaultMaterialColor())
            .noCollision()
            .strength(1.0F)
            .sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val STRUCTURE_BLOCK: KryptonBlock = register("structure_block",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_LIGHT_GRAY).requiresCorrectTool().strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val JIGSAW: KryptonBlock = register("jigsaw",
        DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_LIGHT_GRAY).requiresCorrectTool().strength(-1.0F, 3600000.0F).noLootTable()))
    @JvmField
    val COMPOSTER: KryptonBlock = register("composter", DummyBlock(Properties.of(Materials.WOOD).strength(0.6F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val TARGET: KryptonBlock = register("target",
        DummyBlock(Properties.of(Materials.GRASS, MaterialColors.QUARTZ).strength(0.5F).sounds(BlockSoundGroups.GRASS)))
    @JvmField
    val BEE_NEST: KryptonBlock = register("bee_nest",
        DummyBlock(Properties.of(Materials.WOOD, MaterialColors.COLOR_YELLOW).strength(0.3F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val BEEHIVE: KryptonBlock = register("beehive", DummyBlock(Properties.of(Materials.WOOD).strength(0.6F).sounds(BlockSoundGroups.WOOD)))
    @JvmField
    val HONEY_BLOCK: KryptonBlock = register("honey_block", DummyBlock(Properties.of(Materials.CLAY, MaterialColors.COLOR_ORANGE).speedFactor(0.4F)
        .jumpFactor(0.5F).noOcclusion().sounds(BlockSoundGroups.HONEY_BLOCK)))
    @JvmField
    val HONEYCOMB_BLOCK: KryptonBlock = register("honeycomb_block",
        DummyBlock(Properties.of(Materials.CLAY, MaterialColors.COLOR_ORANGE).strength(0.6F).sounds(BlockSoundGroups.CORAL_BLOCK)))
    @JvmField
    val NETHERITE_BLOCK: KryptonBlock = register("netherite_block", DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_BLACK)
        .requiresCorrectTool().strength(50.0F, 1200.0F).sounds(BlockSoundGroups.NETHERITE_BLOCK)))
    @JvmField
    val ANCIENT_DEBRIS: KryptonBlock = register("ancient_debris", DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_BLACK)
        .requiresCorrectTool().strength(30.0F, 1200.0F).sounds(BlockSoundGroups.ANCIENT_DEBRIS)))
    @JvmField
    val CRYING_OBSIDIAN: KryptonBlock = register("crying_obsidian",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK).requiresCorrectTool().strength(50.0F, 1200.0F).lightLevel { 10 }))
    @JvmField
    val RESPAWN_ANCHOR: KryptonBlock = register("respawn_anchor", DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK)
        .requiresCorrectTool().strength(50.0F, 1200.0F).lightLevel { RespawnAnchorBlock.getScaledChargeLevel(it, 15) }))
    @JvmField
    val POTTED_CRIMSON_FUNGUS: KryptonBlock = register("potted_crimson_fungus",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_WARPED_FUNGUS: KryptonBlock = register("potted_warped_fungus",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_CRIMSON_ROOTS: KryptonBlock = register("potted_crimson_roots",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_WARPED_ROOTS: KryptonBlock = register("potted_warped_roots",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val LODESTONE: KryptonBlock = register("lodestone",
        DummyBlock(Properties.of(Materials.HEAVY_METAL).requiresCorrectTool().strength(3.5F).sounds(BlockSoundGroups.LODESTONE)))
    @JvmField
    val BLACKSTONE: KryptonBlock = register("blackstone",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK).requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val BLACKSTONE_STAIRS: KryptonBlock = register("blackstone_stairs", DummyBlock(Properties.from(BLACKSTONE)))
    @JvmField
    val BLACKSTONE_WALL: KryptonBlock = register("blackstone_wall", DummyBlock(Properties.from(BLACKSTONE)))
    @JvmField
    val BLACKSTONE_SLAB: KryptonBlock = register("blackstone_slab", DummyBlock(Properties.from(BLACKSTONE).strength(2.0F, 6.0F)))
    @JvmField
    val POLISHED_BLACKSTONE: KryptonBlock = register("polished_blackstone", DummyBlock(Properties.from(BLACKSTONE).strength(2.0F, 6.0F)))
    @JvmField
    val POLISHED_BLACKSTONE_BRICKS: KryptonBlock = register("polished_blackstone_bricks",
        DummyBlock(Properties.from(POLISHED_BLACKSTONE).strength(1.5F, 6.0F)))
    @JvmField
    val CRACKED_POLISHED_BLACKSTONE_BRICKS: KryptonBlock = register("cracked_polished_blackstone_bricks",
        DummyBlock(Properties.from(POLISHED_BLACKSTONE_BRICKS)))
    @JvmField
    val CHISELED_POLISHED_BLACKSTONE: KryptonBlock = register("chiseled_polished_blackstone",
        DummyBlock(Properties.from(POLISHED_BLACKSTONE).strength(1.5F, 6.0F)))
    @JvmField
    val POLISHED_BLACKSTONE_BRICK_SLAB: KryptonBlock = register("polished_blackstone_brick_slab",
        DummyBlock(Properties.from(POLISHED_BLACKSTONE_BRICKS).strength(2.0F, 6.0F)))
    @JvmField
    val POLISHED_BLACKSTONE_BRICK_STAIRS: KryptonBlock = register("polished_blackstone_brick_stairs",
        DummyBlock(Properties.from(POLISHED_BLACKSTONE_BRICKS)))
    @JvmField
    val POLISHED_BLACKSTONE_BRICK_WALL: KryptonBlock = register("polished_blackstone_brick_wall",
        DummyBlock(Properties.from(POLISHED_BLACKSTONE_BRICKS)))
    @JvmField
    val GILDED_BLACKSTONE: KryptonBlock = register("gilded_blackstone",
        DummyBlock(Properties.from(BLACKSTONE).sounds(BlockSoundGroups.GILDED_BLACKSTONE)))
    @JvmField
    val POLISHED_BLACKSTONE_STAIRS: KryptonBlock = register("polished_blackstone_stairs", DummyBlock(Properties.from(POLISHED_BLACKSTONE)))
    @JvmField
    val POLISHED_BLACKSTONE_SLAB: KryptonBlock = register("polished_blackstone_slab", DummyBlock(Properties.from(POLISHED_BLACKSTONE)))
    @JvmField
    val POLISHED_BLACKSTONE_PRESSURE_PLATE: KryptonBlock = register("polished_blackstone_pressure_plate",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_BLACK).requiresCorrectTool().noCollision().strength(0.5F)))
    @JvmField
    val POLISHED_BLACKSTONE_BUTTON: KryptonBlock = register("polished_blackstone_button",
        DummyBlock(Properties.of(Materials.DECORATION).noCollision().strength(0.5F)))
    @JvmField
    val POLISHED_BLACKSTONE_WALL: KryptonBlock = register("polished_blackstone_wall", DummyBlock(Properties.from(POLISHED_BLACKSTONE)))
    @JvmField
    val CHISELED_NETHER_BRICKS: KryptonBlock = register("chiseled_nether_bricks", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(2.0F, 6.0F).sounds(BlockSoundGroups.NETHER_BRICKS)))
    @JvmField
    val CRACKED_NETHER_BRICKS: KryptonBlock = register("cracked_nether_bricks", DummyBlock(Properties.of(Materials.STONE, MaterialColors.NETHER)
        .requiresCorrectTool().strength(2.0F, 6.0F).sounds(BlockSoundGroups.NETHER_BRICKS)))
    @JvmField
    val QUARTZ_BRICKS: KryptonBlock = register("quartz_bricks", DummyBlock(Properties.from(QUARTZ_BLOCK)))
    @JvmField
    val CANDLE: KryptonBlock = register("candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.SAND).noOcclusion().strength(0.1F)
        .sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val WHITE_CANDLE: KryptonBlock = register("white_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.WOOL).noOcclusion()
        .strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val ORANGE_CANDLE: KryptonBlock = register("orange_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_ORANGE)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val MAGENTA_CANDLE: KryptonBlock = register("magenta_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_MAGENTA)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val LIGHT_BLUE_CANDLE: KryptonBlock = register("light_blue_candle",
        DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_LIGHT_BLUE)
            .noOcclusion()
            .strength(0.1F)
            .sounds(BlockSoundGroups.CANDLE)
            .lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val YELLOW_CANDLE: KryptonBlock = register("yellow_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_YELLOW)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val LIME_CANDLE: KryptonBlock = register("lime_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_LIGHT_GREEN)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val PINK_CANDLE: KryptonBlock = register("pink_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_PINK).noOcclusion()
        .strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val GRAY_CANDLE: KryptonBlock = register("gray_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_GRAY).noOcclusion()
        .strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val LIGHT_GRAY_CANDLE: KryptonBlock = register("light_gray_candle",
        DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_LIGHT_GRAY)
            .noOcclusion()
            .strength(0.1F)
            .sounds(BlockSoundGroups.CANDLE)
            .lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val CYAN_CANDLE: KryptonBlock = register("cyan_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_CYAN).noOcclusion()
        .strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val PURPLE_CANDLE: KryptonBlock = register("purple_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_PURPLE)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val BLUE_CANDLE: KryptonBlock = register("blue_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_BLUE).noOcclusion()
        .strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val BROWN_CANDLE: KryptonBlock = register("brown_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_BROWN)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val GREEN_CANDLE: KryptonBlock = register("green_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_GREEN)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val RED_CANDLE: KryptonBlock = register("red_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_RED).noOcclusion()
        .strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val BLACK_CANDLE: KryptonBlock = register("black_candle", DummyBlock(Properties.of(Materials.DECORATION, MaterialColors.COLOR_BLACK)
        .noOcclusion().strength(0.1F).sounds(BlockSoundGroups.CANDLE).lightLevel(CandleBlock.LIGHT_EMISSION)))
    @JvmField
    val CANDLE_CAKE: KryptonBlock = register("candle_cake", DummyBlock(Properties.from(CAKE).lightLevel(litBlockEmission(3))))
    @JvmField
    val WHITE_CANDLE_CAKE: KryptonBlock = register("white_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val ORANGE_CANDLE_CAKE: KryptonBlock = register("orange_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val MAGENTA_CANDLE_CAKE: KryptonBlock = register("magenta_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val LIGHT_BLUE_CANDLE_CAKE: KryptonBlock = register("light_blue_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val YELLOW_CANDLE_CAKE: KryptonBlock = register("yellow_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val LIME_CANDLE_CAKE: KryptonBlock = register("lime_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val PINK_CANDLE_CAKE: KryptonBlock = register("pink_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val GRAY_CANDLE_CAKE: KryptonBlock = register("gray_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val LIGHT_GRAY_CANDLE_CAKE: KryptonBlock = register("light_gray_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val CYAN_CANDLE_CAKE: KryptonBlock = register("cyan_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val PURPLE_CANDLE_CAKE: KryptonBlock = register("purple_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val BLUE_CANDLE_CAKE: KryptonBlock = register("blue_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val BROWN_CANDLE_CAKE: KryptonBlock = register("brown_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val GREEN_CANDLE_CAKE: KryptonBlock = register("green_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val RED_CANDLE_CAKE: KryptonBlock = register("red_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val BLACK_CANDLE_CAKE: KryptonBlock = register("black_candle_cake", DummyBlock(Properties.from(CANDLE_CAKE)))
    @JvmField
    val AMETHYST_BLOCK: KryptonBlock = register("amethyst_block", DummyBlock(Properties.of(Materials.AMETHYST, MaterialColors.COLOR_PURPLE)
        .strength(1.5F).sounds(BlockSoundGroups.AMETHYST).requiresCorrectTool()))
    @JvmField
    val BUDDING_AMETHYST: KryptonBlock = register("budding_amethyst",
        DummyBlock(Properties.of(Materials.AMETHYST).randomlyTicks().strength(1.5F).sounds(BlockSoundGroups.AMETHYST).requiresCorrectTool()))
    @JvmField
    val AMETHYST_CLUSTER: KryptonBlock = register("amethyst_cluster", DummyBlock(Properties.of(Materials.AMETHYST).noOcclusion().randomlyTicks()
        .sounds(BlockSoundGroups.AMETHYST_CLUSTER).strength(1.5F).lightLevel { 5 }))
    @JvmField
    val LARGE_AMETHYST_BUD: KryptonBlock = register("large_amethyst_bud",
        DummyBlock(Properties.from(AMETHYST_CLUSTER).sounds(BlockSoundGroups.MEDIUM_AMETHYST_BUD).lightLevel { 4 }))
    @JvmField
    val MEDIUM_AMETHYST_BUD: KryptonBlock = register("medium_amethyst_bud",
        DummyBlock(Properties.from(AMETHYST_CLUSTER).sounds(BlockSoundGroups.LARGE_AMETHYST_BUD).lightLevel { 2 }))
    @JvmField
    val SMALL_AMETHYST_BUD: KryptonBlock = register("small_amethyst_bud",
        DummyBlock(Properties.from(AMETHYST_CLUSTER).sounds(BlockSoundGroups.SMALL_AMETHYST_BUD).lightLevel { 1 }))
    @JvmField
    val TUFF: KryptonBlock = register("tuff", DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_GRAY).sounds(BlockSoundGroups.TUFF)
        .requiresCorrectTool().strength(1.5F, 6.0F)))
    @JvmField
    val CALCITE: KryptonBlock = register("calcite", DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_WHITE)
        .sounds(BlockSoundGroups.CALCITE).requiresCorrectTool().strength(0.75F)))
    @JvmField
    val TINTED_GLASS: KryptonBlock = register("tinted_glass", DummyBlock(Properties.from(GLASS).color(MaterialColors.COLOR_GRAY).noOcclusion()
        .isValidSpawn(NEVER_SPAWN).isRedstoneConductor(NEVER).isSuffocating(NEVER)))
    @JvmField
    val POWDER_SNOW: KryptonBlock = register("powder_snow",
        DummyBlock(Properties.of(Materials.POWDER_SNOW).strength(0.25F).sounds(BlockSoundGroups.POWDER_SNOW).dynamicShape()))
    @JvmField
    val SCULK_SENSOR: KryptonBlock = register("sculk_sensor",
        DummyBlock(Properties.of(Materials.SCULK, MaterialColors.COLOR_CYAN).strength(1.5F).sounds(BlockSoundGroups.SCULK_SENSOR).lightLevel { 1 }))
    @JvmField
    val SCULK: KryptonBlock = register("sculk", DummyBlock(Properties.of(Materials.SCULK).strength(0.2F).sounds(BlockSoundGroups.SCULK)))
    @JvmField
    val SCULK_VEIN: KryptonBlock = register("sculk_vein",
        DummyBlock(Properties.of(Materials.SCULK).noCollision().strength(0.2F).sounds(BlockSoundGroups.SCULK_VEIN)))
    @JvmField
    val SCULK_CATALYST: KryptonBlock = register("sculk_catalyst",
        DummyBlock(Properties.of(Materials.SCULK).strength(3.0F, 3.0F).sounds(BlockSoundGroups.SCULK_CATALYST).lightLevel { 6 }))
    @JvmField
    val SCULK_SHRIEKER: KryptonBlock = register("sculk_shrieker",
        DummyBlock(Properties.of(Materials.SCULK, MaterialColors.COLOR_BLACK).strength(3.0F, 3.0F).sounds(BlockSoundGroups.SCULK_SHRIEKER)))
    @JvmField
    val OXIDIZED_COPPER: KryptonBlock = register("oxidized_copper", DummyBlock(Properties.of(Materials.METAL, MaterialColors.WARPED_NYLIUM)
        .requiresCorrectTool().strength(3.0F, 6.0F).sounds(BlockSoundGroups.COPPER)))
    @JvmField
    val WEATHERED_COPPER: KryptonBlock = register("weathered_copper", DummyBlock(Properties.of(Materials.METAL, MaterialColors.WARPED_STEM)
        .requiresCorrectTool().strength(3.0F, 6.0F).sounds(BlockSoundGroups.COPPER)))
    @JvmField
    val EXPOSED_COPPER: KryptonBlock = register("exposed_copper", DummyBlock(Properties.of(Materials.METAL, MaterialColors.TERRACOTTA_LIGHT_GRAY)
        .requiresCorrectTool().strength(3.0F, 6.0F).sounds(BlockSoundGroups.COPPER)))
    @JvmField
    val COPPER_BLOCK: KryptonBlock = register("copper_block", DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_ORANGE)
        .requiresCorrectTool().strength(3.0F, 6.0F).sounds(BlockSoundGroups.COPPER)))
    @JvmField
    val COPPER_ORE: KryptonBlock = register("copper_ore", DummyBlock(Properties.from(IRON_ORE)))
    @JvmField
    val DEEPSLATE_COPPER_ORE: KryptonBlock = register("deepslate_copper_ore",
        DummyBlock(Properties.from(COPPER_ORE).color(MaterialColors.DEEPSLATE).strength(4.5F, 3.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val OXIDIZED_CUT_COPPER: KryptonBlock = register("oxidized_cut_copper", DummyBlock(Properties.from(OXIDIZED_COPPER)))
    @JvmField
    val WEATHERED_CUT_COPPER: KryptonBlock = register("weathered_cut_copper", DummyBlock(Properties.from(WEATHERED_COPPER)))
    @JvmField
    val EXPOSED_CUT_COPPER: KryptonBlock = register("exposed_cut_copper", DummyBlock(Properties.from(EXPOSED_COPPER)))
    @JvmField
    val CUT_COPPER: KryptonBlock = register("cut_copper", DummyBlock(Properties.from(COPPER_BLOCK)))
    @JvmField
    val OXIDIZED_CUT_COPPER_STAIRS: KryptonBlock = register("oxidized_cut_copper_stairs", DummyBlock(Properties.from(OXIDIZED_CUT_COPPER)))
    @JvmField
    val WEATHERED_CUT_COPPER_STAIRS: KryptonBlock = register("weathered_cut_copper_stairs", DummyBlock(Properties.from(WEATHERED_COPPER)))
    @JvmField
    val EXPOSED_CUT_COPPER_STAIRS: KryptonBlock = register("exposed_cut_copper_stairs", DummyBlock(Properties.from(EXPOSED_COPPER)))
    @JvmField
    val CUT_COPPER_STAIRS: KryptonBlock = register("cut_copper_stairs", DummyBlock(Properties.from(COPPER_BLOCK)))
    @JvmField
    val OXIDIZED_CUT_COPPER_SLAB: KryptonBlock = register("oxidized_cut_copper_slab",
        DummyBlock(Properties.from(OXIDIZED_CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val WEATHERED_CUT_COPPER_SLAB: KryptonBlock = register("weathered_cut_copper_slab",
        DummyBlock(Properties.from(WEATHERED_CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val EXPOSED_CUT_COPPER_SLAB: KryptonBlock = register("exposed_cut_copper_slab",
        DummyBlock(Properties.from(EXPOSED_CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val CUT_COPPER_SLAB: KryptonBlock = register("cut_copper_slab", DummyBlock(Properties.from(CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val WAXED_COPPER_BLOCK: KryptonBlock = register("waxed_copper_block", DummyBlock(Properties.from(COPPER_BLOCK)))
    @JvmField
    val WAXED_WEATHERED_COPPER: KryptonBlock = register("waxed_weathered_copper", DummyBlock(Properties.from(WEATHERED_COPPER)))
    @JvmField
    val WAXED_EXPOSED_COPPER: KryptonBlock = register("waxed_exposed_copper", DummyBlock(Properties.from(EXPOSED_COPPER)))
    @JvmField
    val WAXED_OXIDIZED_COPPER: KryptonBlock = register("waxed_oxidized_copper", DummyBlock(Properties.from(OXIDIZED_COPPER)))
    @JvmField
    val WAXED_OXIDIZED_CUT_COPPER: KryptonBlock = register("waxed_oxidized_cut_copper", DummyBlock(Properties.from(OXIDIZED_COPPER)))
    @JvmField
    val WAXED_WEATHERED_CUT_COPPER: KryptonBlock = register("waxed_weathered_cut_copper", DummyBlock(Properties.from(WEATHERED_COPPER)))
    @JvmField
    val WAXED_EXPOSED_CUT_COPPER: KryptonBlock = register("waxed_exposed_cut_copper", DummyBlock(Properties.from(EXPOSED_COPPER)))
    @JvmField
    val WAXED_CUT_COPPER: KryptonBlock = register("waxed_cut_copper", DummyBlock(Properties.from(COPPER_BLOCK)))
    @JvmField
    val WAXED_OXIDIZED_CUT_COPPER_STAIRS: KryptonBlock = register("waxed_oxidized_cut_copper_stairs", DummyBlock(Properties.from(OXIDIZED_COPPER)))
    @JvmField
    val WAXED_WEATHERED_CUT_COPPER_STAIRS: KryptonBlock = register("waxed_weathered_cut_copper_stairs",
        DummyBlock(Properties.from(WEATHERED_COPPER)))
    @JvmField
    val WAXED_EXPOSED_CUT_COPPER_STAIRS: KryptonBlock = register("waxed_exposed_cut_copper_stairs", DummyBlock(Properties.from(EXPOSED_COPPER)))
    @JvmField
    val WAXED_CUT_COPPER_STAIRS: KryptonBlock = register("waxed_cut_copper_stairs", DummyBlock(Properties.from(COPPER_BLOCK)))
    @JvmField
    val WAXED_OXIDIZED_CUT_COPPER_SLAB: KryptonBlock = register("waxed_oxidized_cut_copper_slab",
        DummyBlock(Properties.from(WAXED_OXIDIZED_CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val WAXED_WEATHERED_CUT_COPPER_SLAB: KryptonBlock = register("waxed_weathered_cut_copper_slab",
        DummyBlock(Properties.from(WAXED_WEATHERED_CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val WAXED_EXPOSED_CUT_COPPER_SLAB: KryptonBlock = register("waxed_exposed_cut_copper_slab",
        DummyBlock(Properties.from(WAXED_EXPOSED_CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val WAXED_CUT_COPPER_SLAB: KryptonBlock = register("waxed_cut_copper_slab", DummyBlock(Properties.from(WAXED_CUT_COPPER).requiresCorrectTool()))
    @JvmField
    val LIGHTNING_ROD: KryptonBlock = register("lightning_rod", DummyBlock(Properties.of(Materials.METAL, MaterialColors.COLOR_ORANGE)
        .requiresCorrectTool().strength(3.0F, 6.0F).sounds(BlockSoundGroups.COPPER).noOcclusion()))
    @JvmField
    val POINTED_DRIPSTONE: KryptonBlock = register("pointed_dripstone", DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_BROWN)
        .noOcclusion().sounds(BlockSoundGroups.POINTED_DRIPSTONE).randomlyTicks().strength(1.5F, 3.0F).dynamicShape().offsetType(OffsetType.XZ)))
    @JvmField
    val DRIPSTONE_BLOCK: KryptonBlock = register("dripstone_block", DummyBlock(Properties.of(Materials.STONE, MaterialColors.TERRACOTTA_BROWN)
        .sounds(BlockSoundGroups.DRIPSTONE_BLOCK).requiresCorrectTool().strength(1.5F, 1.0F)))
    @JvmField
    val CAVE_VINES: KryptonBlock = register("cave_vines", DummyBlock(Properties.of(Materials.PLANT).randomlyTicks().noCollision()
        .lightLevel(CaveVines.emission(14)).instabreak().sounds(BlockSoundGroups.CAVE_VINES)))
    @JvmField
    val CAVE_VINES_PLANT: KryptonBlock = register("cave_vines_plant",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().lightLevel(CaveVines.emission(14)).instabreak().sounds(BlockSoundGroups.CAVE_VINES)))
    @JvmField
    val SPORE_BLOSSOM: KryptonBlock = register("spore_blossom",
        DummyBlock(Properties.of(Materials.PLANT).instabreak().noCollision().sounds(BlockSoundGroups.SPORE_BLOSSOM)))
    @JvmField
    val AZALEA: KryptonBlock = register("azalea",
        DummyBlock(Properties.of(Materials.PLANT).instabreak().sounds(BlockSoundGroups.AZALEA).noOcclusion()))
    @JvmField
    val FLOWERING_AZALEA: KryptonBlock = register("flowering_azalea",
        DummyBlock(Properties.of(Materials.PLANT).instabreak().sounds(BlockSoundGroups.FLOWERING_AZALEA).noOcclusion()))
    @JvmField
    val MOSS_CARPET: KryptonBlock = register("moss_carpet",
        DummyBlock(Properties.of(Materials.PLANT, MaterialColors.COLOR_GREEN).strength(0.1F).sounds(BlockSoundGroups.MOSS_CARPET)))
    @JvmField
    val MOSS_BLOCK: KryptonBlock = register("moss_block",
        DummyBlock(Properties.of(Materials.MOSS, MaterialColors.COLOR_GREEN).strength(0.1F).sounds(BlockSoundGroups.MOSS)))
    @JvmField
    val BIG_DRIPLEAF: KryptonBlock = register("big_dripleaf",
        DummyBlock(Properties.of(Materials.PLANT).strength(0.1F).sounds(BlockSoundGroups.BIG_DRIPLEAF)))
    @JvmField
    val BIG_DRIPLEAF_STEM: KryptonBlock = register("big_dripleaf_stem",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().strength(0.1F).sounds(BlockSoundGroups.BIG_DRIPLEAF)))
    @JvmField
    val SMALL_DRIPLEAF: KryptonBlock = register("small_dripleaf",
        DummyBlock(Properties.of(Materials.PLANT).noCollision().instabreak().sounds(BlockSoundGroups.SMALL_DRIPLEAF).offsetType(OffsetType.XYZ)))
    @JvmField
    val HANGING_ROOTS: KryptonBlock = register("hanging_roots", DummyBlock(Properties.of(Materials.REPLACEABLE_PLANT, MaterialColors.DIRT)
        .noCollision().instabreak().sounds(BlockSoundGroups.HANGING_ROOTS).offsetType(OffsetType.XZ)))
    @JvmField
    val ROOTED_DIRT: KryptonBlock = register("rooted_dirt",
        DummyBlock(Properties.of(Materials.DIRT, MaterialColors.DIRT).strength(0.5F).sounds(BlockSoundGroups.ROOTED_DIRT)))
    @JvmField
    val MUD: KryptonBlock = register("mud", DummyBlock(Properties.from(DIRT).color(MaterialColors.TERRACOTTA_CYAN).isValidSpawn(ALWAYS_SPAWN)
        .isRedstoneConductor(ALWAYS).isSuffocating(ALWAYS).sounds(BlockSoundGroups.MUD)))
    @JvmField
    val DEEPSLATE: KryptonBlock = register("deepslate", DummyBlock(Properties.of(Materials.STONE, MaterialColors.DEEPSLATE).requiresCorrectTool()
        .strength(3.0F, 6.0F).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val COBBLED_DEEPSLATE: KryptonBlock = register("cobbled_deepslate", DummyBlock(Properties.from(DEEPSLATE).strength(3.5F, 6.0F)))
    @JvmField
    val COBBLED_DEEPSLATE_STAIRS: KryptonBlock = register("cobbled_deepslate_stairs", DummyBlock(Properties.from(COBBLED_DEEPSLATE)))
    @JvmField
    val COBBLED_DEEPSLATE_SLAB: KryptonBlock = register("cobbled_deepslate_slab", DummyBlock(Properties.from(COBBLED_DEEPSLATE)))
    @JvmField
    val COBBLED_DEEPSLATE_WALL: KryptonBlock = register("cobbled_deepslate_wall", DummyBlock(Properties.from(COBBLED_DEEPSLATE)))
    @JvmField
    val POLISHED_DEEPSLATE: KryptonBlock = register("polished_deepslate",
        DummyBlock(Properties.from(COBBLED_DEEPSLATE).sounds(BlockSoundGroups.POLISHED_DEEPSLATE)))
    @JvmField
    val POLISHED_DEEPSLATE_STAIRS: KryptonBlock = register("polished_deepslate_stairs", DummyBlock(Properties.from(POLISHED_DEEPSLATE)))
    @JvmField
    val POLISHED_DEEPSLATE_SLAB: KryptonBlock = register("polished_deepslate_slab", DummyBlock(Properties.from(POLISHED_DEEPSLATE)))
    @JvmField
    val POLISHED_DEEPSLATE_WALL: KryptonBlock = register("polished_deepslate_wall", DummyBlock(Properties.from(POLISHED_DEEPSLATE)))
    @JvmField
    val DEEPSLATE_TILES: KryptonBlock = register("deepslate_tiles",
        DummyBlock(Properties.from(COBBLED_DEEPSLATE).sounds(BlockSoundGroups.DEEPSLATE_TILES)))
    @JvmField
    val DEEPSLATE_TILE_STAIRS: KryptonBlock = register("deepslate_tile_stairs", DummyBlock(Properties.from(DEEPSLATE_TILES)))
    @JvmField
    val DEEPSLATE_TILE_SLAB: KryptonBlock = register("deepslate_tile_slab", DummyBlock(Properties.from(DEEPSLATE_TILES)))
    @JvmField
    val DEEPSLATE_TILE_WALL: KryptonBlock = register("deepslate_tile_wall", DummyBlock(Properties.from(DEEPSLATE_TILES)))
    @JvmField
    val DEEPSLATE_BRICKS: KryptonBlock = register("deepslate_bricks",
        DummyBlock(Properties.from(COBBLED_DEEPSLATE).sounds(BlockSoundGroups.DEEPSLATE_BRICKS)))
    @JvmField
    val DEEPSLATE_BRICK_STAIRS: KryptonBlock = register("deepslate_brick_stairs", DummyBlock(Properties.from(DEEPSLATE_BRICKS)))
    @JvmField
    val DEEPSLATE_BRICK_SLAB: KryptonBlock = register("deepslate_brick_slab", DummyBlock(Properties.from(DEEPSLATE_BRICKS)))
    @JvmField
    val DEEPSLATE_BRICK_WALL: KryptonBlock = register("deepslate_brick_wall", DummyBlock(Properties.from(DEEPSLATE_BRICKS)))
    @JvmField
    val CHISELED_DEEPSLATE: KryptonBlock = register("chiseled_deepslate",
        DummyBlock(Properties.from(COBBLED_DEEPSLATE).sounds(BlockSoundGroups.DEEPSLATE_BRICKS)))
    @JvmField
    val CRACKED_DEEPSLATE_BRICKS: KryptonBlock = register("cracked_deepslate_bricks", DummyBlock(Properties.from(DEEPSLATE_BRICKS)))
    @JvmField
    val CRACKED_DEEPSLATE_TILES: KryptonBlock = register("cracked_deepslate_tiles", DummyBlock(Properties.from(DEEPSLATE_TILES)))
    @JvmField
    val INFESTED_DEEPSLATE: KryptonBlock = register("infested_deepslate",
        DummyBlock(Properties.of(Materials.CLAY, MaterialColors.DEEPSLATE).sounds(BlockSoundGroups.DEEPSLATE)))
    @JvmField
    val SMOOTH_BASALT: KryptonBlock = register("smooth_basalt", DummyBlock(Properties.from(BASALT)))
    @JvmField
    val RAW_IRON_BLOCK: KryptonBlock = register("raw_iron_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.RAW_IRON).requiresCorrectTool().strength(5.0F, 6.0F)))
    @JvmField
    val RAW_COPPER_BLOCK: KryptonBlock = register("raw_copper_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.COLOR_ORANGE).requiresCorrectTool().strength(5.0F, 6.0F)))
    @JvmField
    val RAW_GOLD_BLOCK: KryptonBlock = register("raw_gold_block",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.GOLD).requiresCorrectTool().strength(5.0F, 6.0F)))
    @JvmField
    val POTTED_AZALEA: KryptonBlock = register("potted_azalea_bush", DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val POTTED_FLOWERING_AZALEA: KryptonBlock = register("potted_flowering_azalea_bush",
        DummyBlock(Properties.of(Materials.DECORATION).instabreak().noOcclusion()))
    @JvmField
    val OCHRE_FROGLIGHT: KryptonBlock = register("ochre_froglight", DummyBlock(Properties.of(Materials.FROGLIGHT, MaterialColors.SAND)
        .strength(0.3F).lightLevel { 15 }.sounds(BlockSoundGroups.FROGLIGHT)))
    @JvmField
    val VERDANT_FROGLIGHT: KryptonBlock = register("verdant_froglight", DummyBlock(Properties.of(Materials.FROGLIGHT, MaterialColors.GLOW_LICHEN)
        .strength(0.3F).lightLevel { 15 }.sounds(BlockSoundGroups.FROGLIGHT)))
    @JvmField
    val PEARLESCENT_FROGLIGHT: KryptonBlock = register("pearlescent_froglight",
        DummyBlock(Properties.of(Materials.FROGLIGHT, MaterialColors.COLOR_PINK).strength(0.3F).lightLevel { 15 }.sounds(BlockSoundGroups.FROGLIGHT)))
    @JvmField
    val FROGSPAWN: KryptonBlock = register("frogspawn",
        DummyBlock(Properties.of(Materials.FROGSPAWN).instabreak().noOcclusion().noCollision().sounds(BlockSoundGroups.FROGSPAWN)))
    @JvmField
    val REINFORCED_DEEPSLATE: KryptonBlock = register("reinforced_deepslate",
        DummyBlock(Properties.of(Materials.STONE, MaterialColors.DEEPSLATE).sounds(BlockSoundGroups.DEEPSLATE).strength(55.0F, 1200.0F)))

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
    fun rebuildCaches() {
        KryptonBlock.STATES.forEach { it.initCache() }
    }

    @JvmStatic
    private fun litBlockEmission(lightValue: Int): ToIntFunction<KryptonBlockState> =
        ToIntFunction { if (it.requireProperty(KryptonProperties.LIT)) lightValue else 0 }

    @JvmStatic
    private fun bed(color: DyeColor): KryptonBlock =
        DummyBlock(Properties.of(Materials.WOOL, bedColorFunction(color)).sounds(BlockSoundGroups.WOOD).strength(0.2F).noOcclusion())

    @JvmStatic
    private fun bedColorFunction(color: DyeColor): ColorFunction = Function {
        if (it.requireProperty(KryptonProperties.BED_PART) == BedPart.FOOT) MaterialColor.fromDyeColor(color) else MaterialColors.WOOL
    }

    @JvmStatic
    private fun log(topColor: MaterialColor, barkColor: MaterialColor): KryptonBlock =
        DummyBlock(Properties.of(Materials.WOOD, logColorFunction(topColor, barkColor)).strength(2.0F).sounds(BlockSoundGroups.WOOD))

    @JvmStatic
    private fun logColorFunction(top: MaterialColor, bark: MaterialColor): ColorFunction = Function {
        if (it.requireProperty(KryptonProperties.AXIS) == Direction.Axis.Y) top else bark
    }

    @JvmStatic
    private fun netherStem(color: MaterialColor): KryptonBlock =
        DummyBlock(Properties.of(Materials.NETHER_WOOD, color).strength(2F).sounds(BlockSoundGroups.STEM))

    @JvmStatic
    private fun stainedGlass(color: DyeColor): KryptonBlock = DummyBlock(Properties.of(Materials.GLASS, color).strength(0.3F)
        .sounds(BlockSoundGroups.GLASS).noOcclusion().isValidSpawn(NEVER_SPAWN).isRedstoneConductor(NEVER).isSuffocating(NEVER))

    @JvmStatic
    private fun leaves(sounds: BlockSoundGroup): KryptonBlock = DummyBlock(Properties.of(Materials.LEAVES).strength(0.2F).randomlyTicks()
        .sounds(sounds).noOcclusion().isValidSpawn(OCELOT_OR_PARROT_SPAWN).isSuffocating(NEVER))

    @JvmStatic
    @Suppress("UnusedPrivateMember")
    private fun shulkerBox(color: DyeColor?, properties: Properties): KryptonBlock {
        val predicate = BlockBehaviour.StatePredicate { _, _, _ ->
            true
            /* FIXME
            val entity = world.getBlockEntity(pos)
            if (entity !is KryptonShulkerBox) true else entity.isClosed()
            */
        }
        return DummyBlock(properties.strength(2F).dynamicShape().noOcclusion().isSuffocating(predicate))
    }

    @JvmStatic
    @Suppress("UnusedPrivateMember")
    private fun pistonBase(sticky: Boolean): KryptonBlock {
        val predicate = BlockBehaviour.StatePredicate { state, _, _ -> !state.requireProperty(KryptonProperties.EXTENDED) }
        return DummyBlock(Properties.of(Materials.PISTON).strength(1.5F).isRedstoneConductor(NEVER).isSuffocating(predicate))
    }

    @JvmStatic
    private fun register(key: String, block: KryptonBlock): KryptonBlock = KryptonRegistries.register(KryptonRegistries.BLOCK, Key.key(key), block)
}

private typealias ColorFunction = Function<KryptonBlockState, MaterialColor>
private typealias ValidSpawnPredicate = BlockBehaviour.StateArgumentPredicate<KryptonEntityType<*>>
